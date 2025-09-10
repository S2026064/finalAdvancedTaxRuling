/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import za.gov.sars.domain.LetterDTO;
import za.gov.sars.atr.gen.RequestOperationType;
import za.gov.sars.atr.gen.dto.req.GeneratePDFDocument;
import za.gov.sars.atr.gen.dto.req.PDFDocumentGenerationManagementRequest;
import za.gov.sars.service.mail.LDAPService;
import za.gov.sars.service.mail.MailService;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.springframework.stereotype.Service;

import za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems;
import za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.Footer;
import za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.Header;
import za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.Header.AddressDetails;
import za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.Header.AddressDetails.Item;
import za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.Header.OtherItems;
import za.gov.sars.atr.gen.dto.req.PDFDocumentGenerationManagementRequest.Details;
import za.gov.sars.atr.gen.dto.req.PDFDocumentGenerationManagementRequest.Details.Detail;

/**
 *
 * @author S2028398
 */
@Service
public class EnhancedLetterGenerationService {

    private MailService mailService = new MailService();
    private LDAPService lDAPService = new LDAPService();


    

    public String constructMsg(LetterDTO letter) throws IOException, ParserConfigurationException {
        try {

            PDFDocumentGenerationManagementRequest request = new PDFDocumentGenerationManagementRequest();

            request.setRequestOperation(RequestOperationType.GENERATE_LETTER.toString());

            Details details = new Details();
            Detail detail = new Detail();
            GeneratePDFDocument generatePDFDocument = new GeneratePDFDocument();

            generatePDFDocument.setHeader(createHeader());
            generatePDFDocument.setContentItems(createContentItems(letter));

            generatePDFDocument.setFooter(createFooter());

            detail.setGeneratePDFDocument(generatePDFDocument);
            details.setDetail(detail);
            request.setDetails(details);

            JacksonXmlModule xmlModule = new JacksonXmlModule();
            xmlModule.setDefaultUseWrapper(false);
            ObjectMapper objectMapper = new XmlMapper(xmlModule);
            String xml = objectMapper.writeValueAsString(request);
            XmlMapper xmlMapper = new XmlMapper();
            request = objectMapper.readValue(xml, PDFDocumentGenerationManagementRequest.class);
            request.setXmlnsXsi("http://www.w3.org/2001/XMLSchema-instance");
            request.setXmlnsXsd("http://www.w3.org/2001/XMLSchema");
            request.setXmlns("http://www.sars.gov.za/enterpriseMessagingModel/PDFDocumentGenerationManagement/xml/schemas/version/1.0");
            request.getDetails().getDetail().getGeneratePDFDocument().setXmlns("http://www.sars.gov.za/enterpriseMessagingModel/GeneratePDFDocument/xml/schemas/");

            JAXBContext context = JAXBContext.newInstance(PDFDocumentGenerationManagementRequest.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE); // To format XML
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(request, stringWriter);

            return stringWriter.toString();
        } catch (JAXBException ex) {
            Logger.getLogger(EnhancedLetterGenerationService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(EnhancedLetterGenerationService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private Header createHeader() {

        Header header = new Header();
        AddressDetails addressDetails = new AddressDetails();

        Item clientNameItem = new Item();
        clientNameItem.setName("Address Line 1");
        clientNameItem.setValue("South African Revenue Service");
        addressDetails.getItem().add(clientNameItem);

        Item clientAddressLine1Item = new Item();
        clientAddressLine1Item.setName("Address line 2");
        clientAddressLine1Item.setValue("Pretoria Head Office");
        addressDetails.getItem().add(clientAddressLine1Item);

        Item clientAddressLine2Item = new Item();
        clientAddressLine2Item.setName("Address line 3");
        clientAddressLine2Item.setValue("299 Bronkhorst Street");
        addressDetails.getItem().add(clientAddressLine2Item);

        Item clientAddressLine3Item = new Item();
        clientAddressLine3Item.setName("Address line 4");
        clientAddressLine3Item.setValue("Nieuw Muckleneuk, 0181");
        addressDetails.getItem().add(clientAddressLine3Item);

        Item clientPostalCodeItem = new Item();
        clientPostalCodeItem.setName("Address line 5");
        clientPostalCodeItem.setValue("Private Bag X923, Pretoria, 0001");
        addressDetails.getItem().add(clientPostalCodeItem);

        Item telephone = new Item();
        telephone.setName("Address line 6");
        telephone.setValue("Telephone: (012) 422 4000");
        addressDetails.getItem().add(telephone);

        header.setAddressDetails(addressDetails);

        OtherItems otherItems = new OtherItems();
        za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.Header.OtherItems.Item departmentItem = new za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.Header.OtherItems.Item();
        departmentItem.setName("Department");
        departmentItem.setValue("CUSTOMS AND EXCISE");
        otherItems.getItem().add(departmentItem);

        za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.Header.OtherItems.Item letterNameItem = new za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.Header.OtherItems.Item();
        letterNameItem.setName("Name");
        letterNameItem.setValue("Tariff Determination");
        otherItems.getItem().add(letterNameItem);
        header.setOtherItems(otherItems);

        return header;

    }

    private ContentItems createContentItems(LetterDTO letter) throws ParserConfigurationException, JAXBException, TransformerException {
        ContentItems contentItems = new ContentItems();
        za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item districtItem = new za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item();
        districtItem.setName("District");
        districtItem.setValue(letter.getDistrict());
        contentItems.getItem().add(districtItem);

        za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item caseNumberItem = new za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item();
        caseNumberItem.setName("CaseNumber");
        caseNumberItem.setValue(letter.getCaseNumber());
        contentItems.getItem().add(caseNumberItem);

        za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item capturedDateItem = new za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item();
        capturedDateItem.setName("CapturedDate");
        capturedDateItem.setValue(letter.getCapturedDate());
        contentItems.getItem().add(capturedDateItem);

        if (letter.getAgent() != null) {
            za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item agentItem = new za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item();
            agentItem.setName("Agent");
            agentItem.setValue(letter.getAgent());
           
            contentItems.getItem().add(agentItem);
        } else {
            za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item agentItem = new za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item();
            agentItem.setName("Agent");
            agentItem.setValue("N/A");
            contentItems.getItem().add(agentItem);
        }

        za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item importerItem = new za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item();
        importerItem.setName("Importer");
        importerItem.setValue(letter.getImporter());
        contentItems.getItem().add(importerItem);

        za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item billOfEntryItem = new za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item();
        billOfEntryItem.setName("BillOfEntry");
        billOfEntryItem.setValue(letter.getBillOfEntry());
        contentItems.getItem().add(billOfEntryItem);

        za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item billOfEntryDateItem = new za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item();
        billOfEntryDateItem.setName("BillOfEntryDate");
        billOfEntryDateItem.setValue(letter.getBillOfEntryDate());
        contentItems.getItem().add(billOfEntryDateItem);

        za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item capturerItem = new za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item();
        capturerItem.setName("Capturer");
        capturerItem.setValue(letter.getCapturer());
        contentItems.getItem().add(capturerItem);

        za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item statusItem = new za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item();
        statusItem.setName("Status");
        statusItem.setValue(letter.getStatus());
        contentItems.getItem().add(statusItem);

        za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item statusDateItem = new za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item();
        statusDateItem.setName("StatusDate");
        statusDateItem.setValue(letter.getStatusDate());
        contentItems.getItem().add(statusDateItem);

        if (letter.getAmmendmentDate() != null) {
            za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item ammendmentDateItem = new za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item();
            ammendmentDateItem.setName("AmendmentDate");
            ammendmentDateItem.setValue(letter.getAmmendmentDate());
            contentItems.getItem().add(ammendmentDateItem);
        } else {
            za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item ammendmentDateItem = new za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item();
            ammendmentDateItem.setName("AmendmentDate");
            ammendmentDateItem.setValue("N/A");
            contentItems.getItem().add(ammendmentDateItem);
        }

        za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item tariffSpecialistItem = new za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item();
        tariffSpecialistItem.setName("TariffSpecialist");
        tariffSpecialistItem.setValue(letter.getTariffSpecialist());
        contentItems.getItem().add(tariffSpecialistItem);

        za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item lawAndAnalysisItem = new za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item();
        lawAndAnalysisItem.setName("LawAndAnalysis");
        lawAndAnalysisItem.setValue((letter.getLawAndAnalysis()));
        contentItems.getItem().add(lawAndAnalysisItem);

        za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item tariffCodeItem = new za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item();
        tariffCodeItem.setName("TariffCode");
        tariffCodeItem.setValue(letter.getTariffCode());
        contentItems.getItem().add(tariffCodeItem);

        za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item determinationItem = new za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item();
        determinationItem.setName("Determination");
        determinationItem.setValue(letter.getDetermination());
        contentItems.getItem().add(determinationItem);

        za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item descriptionItem = new za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.ContentItems.Item();
        descriptionItem.setName("Description");
        descriptionItem.setValue(letter.getDescription());
        contentItems.getItem().add(descriptionItem);

        return contentItems;
    }

    private Footer createFooter() {
        Footer footer = new Footer();
        za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.Footer.Item footerItem = new za.gov.sars.atr.gen.dto.req.GeneratePDFDocument.Footer.Item();
        footerItem.setName("LETTER_ID");
        footerItem.setValue("TDNLetter");

        footer.setItem(footerItem);
        return footer;
    }
}
