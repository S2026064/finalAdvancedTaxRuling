/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.atr.mb;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import za.gov.sars.common.DocumentType;
import za.gov.sars.common.NotificationType;
import za.gov.sars.common.Properties;
import za.gov.sars.common.ResourceType;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.JsonDocumentDto;
import za.gov.sars.domain.SupportingDocument;
import za.gov.sars.domain.UserAtrApplication;
import za.gov.sars.service.AtrApplicationServiceLocal;
import za.gov.sars.service.DocumentAttachmentServiceLocal;
import za.gov.sars.service.EmailNotificationSenderServiceLocal;
import za.gov.sars.service.SupportingDocumentServiceLocal;
import za.gov.sars.service.UserAtrApplicationServiceLocal;

/**
 *
 * @author S2028398
 */
@ManagedBean
@ViewScoped
public class AtrApplicationDocumentUpload extends BaseBean<ATRApplication> {

    @Autowired
    private AtrApplicationServiceLocal applicationService;
    @Autowired
    private SupportingDocumentServiceLocal supportingDocumentService;
    @Autowired
    private DocumentAttachmentServiceLocal documentAttachmentService;
    @Autowired
    private EmailNotificationSenderServiceLocal emailNotificationService;
    @Autowired
    private UserAtrApplicationServiceLocal userAtrApplicationService;

    private UploadedFile originalFile;
    private static final String DESTINATION = "./";
    private StreamedContent downloadFile;

    private Slice<ATRApplication> atrApplications;

    @PostConstruct
    public void init() {
        reset().setList(true);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdDate").descending());
        atrApplications = applicationService.findByIdNumberOrPassPortNumber(getActiveUser().getUser().getIdNumber(), getActiveUser().getUser().getPassPortNumber(), pageable);
        addCollections(atrApplications.toList());
    }

    public void update(ATRApplication aTRApplication) {
        reset().setAdd(true);
        aTRApplication.setUpdatedBy(getActiveUser().getSid());
        aTRApplication.setUpdatedDate(new Date());
        addEntity(aTRApplication);
    }

    public void save(ATRApplication aTRApplication) {
        for (SupportingDocument supportingDocument : aTRApplication.getSupportingDocuments()) {
            if (supportingDocument.getId() == null) {
                JsonDocumentDto jsonDocumentDto = uploadDocumentsToServer(supportingDocument);
                JSONObject myResponse;
                myResponse = new JSONObject(documentAttachmentService.uploadDocument(jsonDocumentDto));
                String objectId = myResponse.getString("objectId");
                supportingDocument.setCode(objectId);
            }
        }
        applicationService.update(aTRApplication);
        UserAtrApplication userAtrApplication = userAtrApplicationService.findByAtrApplicationAndResourceType(aTRApplication, ResourceType.MANAGER);
        if (userAtrApplication != null) {
            emailNotificationService.sendEmailNotification(NotificationType.APPLICATION_DOCUMENTS_UPLOADED, aTRApplication);
        }
        if (aTRApplication.getSupportingDocuments().size() > 1) {
            addInformationMessage("Application documents successfully submitted");
        } else {
            addInformationMessage("Application document successfully submitted");
        }
        reset().setList(true);
    }

    public void cancel() {
        reset().setList(true);
    }

    public void handleFileUpload(FileUploadEvent event) {
        this.originalFile = null;
        UploadedFile file = event.getFile();
        if (file != null && file.getContent() != null && file.getContent().length > 0 && file.getFileName() != null) {
            this.originalFile = file;
            try {
                copyFile(event.getFile().getFileName(), file.getInputStream(), file.getSize());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void copyFile(String fileName, InputStream inputStream, Long fileSize) {
        try {
            StringBuilder builder = new StringBuilder(DESTINATION);
            builder.append(fileName);
            try (OutputStream outputStream = new FileOutputStream(new File(builder.toString()))) {
                int read = 0;
                byte[] bytes = new byte[1024];
                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                inputStream.close();
                outputStream.flush();
            }
            addAttachmentItem(DESTINATION, fileName, fileSize);
        } catch (IOException e) {
            this.addInformationMessage(e.getMessage());
        }
    }

    public void addAttachmentItem(String destination, String fileName, Long fileSize) {
        SupportingDocument supportingDocument = new SupportingDocument();
        supportingDocument.setCreatedBy(getActiveUser().getSid());
        supportingDocument.setDocumentType(DocumentType.OTHER);
        supportingDocument.setCreatedDate(new Date());
        supportingDocument.setDescription(fileName);
        supportingDocument.setDocumentSize(fileSize.doubleValue());
        supportingDocument.setATRApplication(getEntity());
        getEntity().addSupportingDocument(supportingDocument);
    }

    public JsonDocumentDto uploadDocumentsToServer(SupportingDocument supportingDocument) {
        String base64File = "";
        JsonDocumentDto jsonDocumentDto = new JsonDocumentDto();
        List<Properties> propertyList = new ArrayList<>();
        Properties properties = new Properties();
        properties.setPropertyName(supportingDocument.getDescription());
        propertyList.add(properties);

        StringBuilder builder = new StringBuilder(DESTINATION);
        builder.append(supportingDocument.getDescription());
        File newFile = new File(builder.toString());

        try (FileInputStream imageInFile = new FileInputStream(newFile)) {
            byte fileData[] = new byte[(int) newFile.length()];
            imageInFile.read(fileData);
            base64File = Base64.getEncoder().encodeToString(fileData);
        } catch (FileNotFoundException e) {
            this.addInformationMessage("File not found" + e);
        } catch (IOException ioe) {
            this.addInformationMessage("Exception while reading the file " + ioe);
        }
        jsonDocumentDto.setObjectType("sars_pca_docs");
        jsonDocumentDto.setObjectName(supportingDocument.getDescription());
        jsonDocumentDto.setContentType(FilenameUtils.getExtension(supportingDocument.getDescription()));
        jsonDocumentDto.setAuthor(getActiveUser().getSid());
        jsonDocumentDto.setProperties(propertyList);
        jsonDocumentDto.setContent(base64File);
        return jsonDocumentDto;
    }

    public void removeAttach(SupportingDocument supportingDocument) {
        if (supportingDocument.getId() != null) {
            getEntity().removeSupportingDocument(supportingDocument);
            supportingDocumentService.deleteById(supportingDocument.getId());
        } else {
            getEntity().removeSupportingDocument(supportingDocument);
        }

        addInformationMessage("Document successfuly deleted");
    }

    public void fileDownload(SupportingDocument supportingDocument) throws IOException {
        String objectId = supportingDocument.getCode();
        documentAttachmentService.download(objectId);
        FileInputStream fis = new FileInputStream("./document.txt");
        String stringTooLong = IOUtils.toString(fis, "UTF-8");
        String b64 = stringTooLong;
        byte[] decoder = Base64.getDecoder().decode(b64);
        downloadFile = DefaultStreamedContent.builder().name(supportingDocument.getDescription()).contentType("application/pdf").stream(() -> new ByteArrayInputStream(decoder)).build();
    }

    public List<ATRApplication> getAtrApplications() {
        return this.getCollections();
    }

    public StreamedContent getDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(StreamedContent downloadFile) {
        this.downloadFile = downloadFile;
    }

}
