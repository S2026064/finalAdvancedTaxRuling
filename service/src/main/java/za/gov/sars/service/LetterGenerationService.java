
package za.gov.sars.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.gov.sars.atr.gen.GeneratePdfResponse;
import za.gov.sars.atr.gen.dto.res.PDFDocumentGenerationManagementResponse;
import za.gov.sars.atr.soapclient.PdfWebServiceClient;
import za.gov.sars.domain.LetterDTO;


/**
 *
 * @author S2028398
 */
@Service
public class LetterGenerationService implements LetterGenerationServiceLocal {

    @Autowired
    private PdfWebServiceClient pdfWebServiceClient;

    private EnhancedLetterGenerationService enhancedLetterGenerationService = new EnhancedLetterGenerationService();

    private static final String CODE = "W1RITl+MiZA3JDPhUOYDtA==";

    /**
     *
     * @param letterDTO
     * @return
     */
    @Override
    public PDFDocumentGenerationManagementResponse generateLetterDocument(LetterDTO letterDTO) {

        try {

            String file = StringUtils.toEncodedString(enhancedLetterGenerationService.constructMsg(letterDTO).getBytes(), StandardCharsets.UTF_8);
            GeneratePdfResponse response = pdfWebServiceClient.getPdfDocument(file, CODE, "http://tempuri.org/GeneratePdf");
            String encodedReponse = StringUtils.toEncodedString(response.getGeneratePdfResult().getBytes(), StandardCharsets.UTF_8);
            StringReader reader = new StringReader(encodedReponse);
            JAXBContext objectContext = JAXBContext.newInstance(PDFDocumentGenerationManagementResponse.class);
            Unmarshaller unmarshaller = objectContext.createUnmarshaller();
            return (PDFDocumentGenerationManagementResponse) unmarshaller.unmarshal(reader);

        } catch (JAXBException ex) {
            Logger.getLogger(LetterGenerationService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LetterGenerationService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(LetterGenerationService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void downloadGeneratedDocument(String base64String) {
            try ( BufferedWriter writer = new BufferedWriter(new FileWriter("./document.txt"))) {
//                    writer.flush();
                    writer.write(base64String);
                } catch (IOException ex) {
            Logger.getLogger(LetterGenerationService.class.getName()).log(Level.SEVERE, null, ex);
        }
//        try {
//            Base64 decoder = new Base64();
//            byte[] decodedBytes = decoder.(base64String);
//
//            File file = new File("responseLetter.pdf");
//            file.createNewFile();
//            FileOutputStream fop = new FileOutputStream(file);
//
//            fop.write(decodedBytes);
//            fop.flush();
//            fop.close();
//        } catch (IOException ex) {
//            Logger.getLogger(LetterGenerationService.class.getName()).log(Level.SEVERE, null, ex);
//        try {
//            Base64 decoder = new Base64();
//            byte[] decodedBytes = decoder.(base64String);
//
//            File file = new File("responseLetter.pdf");
//            file.createNewFile();
//            FileOutputStream fop = new FileOutputStream(file);
//
//            fop.write(decodedBytes);
//            fop.flush();
//            fop.close();
//        } catch (IOException ex) {
//            Logger.getLogger(LetterGenerationService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


// public void downloadGeneratedDocument(String base64String)  {
//    try {
//        URL link = new URL("http://localhost:8080/admin/research.xhtml");
//        BufferedInputStream in = null;
//        FileOutputStream fout = null;
//        try {
//            in = new BufferedInputStream(link.openStream());
//            fout = new FileOutputStream("response.pdf");
//              byte[] decodedBytes = Base64.getDecoder().decode(base64String);
//            //final byte data[] = new byte[1024];
//            int count;
//            while ((count = in.read(decodedBytes, 0, 1024)) != -1) {
//                fout.write(decodedBytes, 0, count);
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(LetterGenerationService.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            if (in != null) {
//                try {
//                    in.close();
//                } catch (IOException ex) {
//                    Logger.getLogger(LetterGenerationService.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//            if (fout != null) {
//                try {
//                    fout.close();
//                } catch (IOException ex) {
//                    Logger.getLogger(LetterGenerationService.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }
//    }      catch (MalformedURLException ex) {
//            Logger.getLogger(LetterGenerationService.class.getName()).log(Level.SEVERE, null, ex);
//        }
// }
// }
