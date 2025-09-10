/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
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
import za.gov.sars.common.ApplicationType;
import za.gov.sars.common.ClassDescription;
import za.gov.sars.common.DocumentType;
import za.gov.sars.common.EntityType;
import za.gov.sars.common.FeedbackType;
import za.gov.sars.common.NotificationType;
import za.gov.sars.common.Properties;
import za.gov.sars.common.Province;
import za.gov.sars.common.RepresentativeType;
import za.gov.sars.common.ResourceType;
import za.gov.sars.common.ResponseOption;
import za.gov.sars.common.RulingArea;
import za.gov.sars.common.RulingType;
import za.gov.sars.common.Status;
import za.gov.sars.common.CloseType;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.AtrApplicationQuestion;
import za.gov.sars.domain.DownloadRecord;
import za.gov.sars.domain.FeedBack;
import za.gov.sars.domain.JsonDocumentDto;
import za.gov.sars.domain.SupportingDocument;
import za.gov.sars.domain.User;
import za.gov.sars.domain.UserAtrApplication;
import za.gov.sars.service.AtrApplicationQuestionServiceLocal;
import za.gov.sars.service.AtrApplicationServiceLocal;
import za.gov.sars.service.DocumentAttachmentServiceLocal;
import za.gov.sars.service.DownloadRecordServiceLocal;
import za.gov.sars.service.EmailNotificationSenderServiceLocal;
import za.gov.sars.service.FeedBackServiceLocal;
import za.gov.sars.service.QuestionServiceLocal;
import za.gov.sars.service.SupportingDocumentServiceLocal;
import za.gov.sars.service.UserAtrApplicationServiceLocal;
import za.gov.sars.service.UserServiceLocal;

/**
 *
 * @author S2026064
 */
@ManagedBean
@ViewScoped
public class RulingPublishedBean extends BaseBean<ATRApplication> {

    @Autowired
    private AtrApplicationServiceLocal applicationService;

    @Autowired
    private AtrApplicationQuestionServiceLocal atrApplicationQuestionService;

    @Autowired
    private UserAtrApplicationServiceLocal userAtrApplicationService;
    @Autowired
    private SupportingDocumentServiceLocal supportingDocumentService;
    @Autowired
    private DocumentAttachmentServiceLocal documentAttachmentService;
    @Autowired
    private FeedBackServiceLocal feedBackService;
    @Autowired
    private DownloadRecordServiceLocal recordServiceLocal;
    @Autowired
    private UserServiceLocal userService;
    @Autowired
    private EmailNotificationSenderServiceLocal emailNotificationSenderService;

    @Autowired
    private QuestionServiceLocal questionService;

    private Slice<ATRApplication> atrApplications;
    private List<AtrApplicationQuestion> applicationQuestions = new ArrayList<>();
    private List<SelectItem> responsesList = new ArrayList<>();
    private List<RulingArea> rulingAreas = new ArrayList<>();
    private List<RulingType> rulingTypes = new ArrayList<>();
    private List<ResourceType> resourceTypes = new ArrayList<>();
    private List< ApplicationType> applicationTypes = new ArrayList<>();
    private List< EntityType> entityTypes = new ArrayList<>();
    private List<ClassDescription> descriptions = new ArrayList<>();
    private List<CloseType> saResidents = new ArrayList<>();
    private List<RepresentativeType> representativeTypes = new ArrayList<>();
    private List<Province> provinces = new ArrayList<>();
    private List<SupportingDocument> supportingDocuments = new ArrayList<>();
    private List<FeedBack> feedBacks = new ArrayList<>();
    private List<User> recipients = new ArrayList<>();
    private List<Status> statuses = new ArrayList<>();
    private UserAtrApplication userApplication;

    private User userDetail;
    private String naturalPerson;
    private StreamedContent downloadFile;
    private UploadedFile originalFile;
    private String destination = "./";

    private String comment;
    private String withdrawalReason;
    private Integer tabIndex = 0;
    private boolean letterConditions;

    @PostConstruct
    public void init() {
        reset().setList(true);

        Pageable pageable = PageRequest.of(0, 10);
        resourceTypes.add(ResourceType.MANAGER);
        resourceTypes.add(ResourceType.PRIMARY_RESOURCE);
        rulingTypes.addAll(Arrays.asList(RulingType.values()));
        applicationTypes.addAll(Arrays.asList(ApplicationType.values()));
        entityTypes.addAll(Arrays.asList(EntityType.values()));
        provinces.addAll(Arrays.asList(Province.values()));
        saResidents.addAll(Arrays.asList(CloseType.values()));
        descriptions.addAll(Arrays.asList(ClassDescription.values()));
        statuses.add(Status.DRAFT_RULING_PUBLISHED);
        statuses.add(Status.RULING_PUBLISHED_PENDING_OUTSTANDING_AMOUNT);
        statuses.add(Status.SANITISED_RULING_PUBLISHED);
        statuses.add(Status.REQUEST_WITHDRAWAL_DRAFT_RULING);
        statuses.add(Status.REQUEST_WITHDRAWAL_SANITISED_RULING);
        representativeTypes.addAll(Arrays.asList(RepresentativeType.values()));
//        atrApplications = applicationService.findByIdNumberOrPassPortNumberAndStatus(getActiveUser().getUser().getIdNumber(), getActiveUser().getUser().getPassPortNumber(), statuses, pageable);
        atrApplications = userAtrApplicationService.findAppByUserResourceTypeAndAppStatusIn(getActiveUser().getUser(), ResourceType.APPLICANT, statuses, pageable);
        Arrays.asList(ResponseOption.values()).stream()
                .forEach(responseOption -> {
                    responsesList.add(new SelectItem(responseOption, responseOption.toString()));
                });
        supportingDocuments = supportingDocumentService.findByDocumentType(DocumentType.FEEDBACK);
    }

    public void review(ATRApplication application) {
        reset().setAdd(true);
        setTabIndex(3);
        applicationQuestions = atrApplicationQuestionService.findByAtrApplication(application);
//        userDetail = userAtrApplicationService.findByAtrApplicationAndResourceType(application, ResourceType.APPLICANT);
        userApplication = userAtrApplicationService.findUsersByAtrApplicationIdAndResourceType(application, ResourceType.APPLICANT);
        userDetail = userApplication.getUser();

        applicationQuestions.stream().filter(response -> (response.getQuestion().getDescription().equalsIgnoreCase("16. Is the Applicant a SMME (Small, Medium and Micro Enterprise)?"))).forEachOrdered(response -> {
            naturalPerson = response.getResponseOption().toString();
        });

        List<FeedBack> unreadFeedbacks = feedBackService.findUnreadByApplicant(application);

        for (FeedBack feedback : unreadFeedbacks) {
            feedback.setUnreadByApplicant(false);
            feedBackService.save(feedback);
        }

        addEntity(application);
    }

    public void accept(ATRApplication application) {
        application.setUpdatedBy(getActiveUser().getSid());
        application.setUpdatedDate(new Date());
        if (application.getStatus().equals(Status.DRAFT_RULING_PUBLISHED)) {
            application.setStatus(Status.DRAFT_RULING_ACCEPTED);
        } else {
            application.setStatus(Status.SANITISED_RULING_ACCEPTED);
        }
        applicationService.update(application);
        List<String> roleDescriptions = Arrays.asList("Senior Manager");
        List<User> officeMnager = userService.findByUserRole(roleDescriptions);
        recipients.addAll(officeMnager);
        for (UserAtrApplication atrApp : userAtrApplicationService.findByAtrApplicationAndResourceTypeIn(application, resourceTypes)) {
            User user = atrApp.getUser();
            if (user != null && !recipients.contains(user)) {
                recipients.add(user);
            }
        }
        if (application.getStatus().equals(Status.DRAFT_RULING_ACCEPTED)) {
//            emailNotificationSenderService.sendEmailNotification(NotificationType.DRAFT_RULING_ACCEPTED, Long.toString(application.getCaseNum()), application.getApplicantName(), application.getUpdatedDate(), recipients);
            emailNotificationSenderService.sendEmailNotification(NotificationType.DRAFT_RULING_ACCEPTED, application);

        } else {
            emailNotificationSenderService.sendEmailNotification(NotificationType.SANITISED_RULING_ACCEPTED, application);
        }
        addInformationMessage("A ruling is successfully accepted");
        reset().setList(true);
    }

    public void feedback(ATRApplication atrApplication) {
        String responseObjectId = null;
        List<SupportingDocument> uploadFailedDocuments = new ArrayList<>();
        Iterator<SupportingDocument> iterator = atrApplication.getSupportingDocuments().iterator();
        while (iterator.hasNext()) {
            SupportingDocument supportingDocument = iterator.next();
            if (supportingDocument.getId() == null) {
                JsonDocumentDto jsonDocumentDto = uploadDocumentsToServer(supportingDocument);
                JSONObject myResponse = new JSONObject(documentAttachmentService.uploadDocument(jsonDocumentDto));
                responseObjectId = myResponse.getString("objectId");
                supportingDocument.setCode(responseObjectId);
                supportingDocumentService.save(supportingDocument);
                if (supportingDocument.getCode() == null) {
                    uploadFailedDocuments.add(supportingDocument);
                    iterator.remove();

                }
            }
        }

        FeedBack feedback = new FeedBack();
        feedback.setCreatedBy(getActiveUser().getFullName());
        feedback.setCreatedDate(new Date());
        feedback.setFeedbackType(FeedbackType.FEEDBACK);
        feedback.setDescription(comment);
        feedback.setUnreadByAdmin(true);
        feedback.setCreatedBy("Applicant");
        feedback.setAtrApplication(atrApplication);
        feedBackService.save(feedback);
        feedBacks.add(feedback);
        atrApplication.setUpdatedBy(getActiveUser().getFullName());
        atrApplication.setUpdatedDate(new Date());
        applicationService.update(atrApplication);

        List<String> roleDescriptions = Arrays.asList("System Administrator", "Senior Manager");
        List<User> officeMnager = userService.findByUserRole(roleDescriptions);

        for (UserAtrApplication atrApp : userAtrApplicationService.findByAtrApplicationAndResourceTypeIn(atrApplication, resourceTypes)) {
            User user = atrApp.getUser();
            if (user != null && !recipients.contains(user)) {
                recipients.add(user);
            }
        }
        if (atrApplication.getStatus().equals(Status.DRAFT_RULING_PUBLISHED)) {
            if (!recipients.isEmpty()) {
                emailNotificationSenderService.sendEmailNotification(NotificationType.DRAFT_RULING_FEEDBACK_RECEIVED, atrApplication);
            }
            emailNotificationSenderService.sendEmailNotification(NotificationType.DRAFT_RULING_FEEDBACK_RECEIVED, atrApplication);
        } else {
            if (!recipients.isEmpty()) {
                emailNotificationSenderService.sendEmailNotification(NotificationType.SANITISED_RULING_FEEDBACK, atrApplication);
            }
            emailNotificationSenderService.sendEmailNotification(NotificationType.SANITISED_RULING_FEEDBACK, atrApplication);

        }
        addInformationMessage("Feedback successfully submitted");
        comment = null;
        reset().setFeedbackPanel(true);
    }

    public void addFeedback(ATRApplication atrApplication) {
        reset().setFeedbackPanel(true);
        feedBacks = feedBackService.findByAtrApplication(atrApplication);

    }

    public Long getUnreadCount(ATRApplication app) {
        return feedBackService.countUnreadByApplicant(app);
    }

    public void withdraw(ATRApplication atrApplication) {

        FeedBack feedback = new FeedBack();
        feedback.setCreatedBy(getActiveUser().getFullName());
        feedback.setCreatedDate(new Date());
        feedback.setFeedbackType(FeedbackType.REQUEST_WITHDRAWAL);
        feedback.setDescription(withdrawalReason);
        feedback.setCreatedBy("Applicant");
        //  feedback.setUnreadByAdmin(true);
        feedback.setAtrApplication(atrApplication);
        feedBackService.save(feedback);
        atrApplication.setUpdatedBy(getActiveUser().getFullName());
        atrApplication.setUpdatedDate(new Date());

        if (atrApplication.getStatus().equals(Status.DRAFT_RULING_PUBLISHED)) {
            atrApplication.setStatus(Status.REQUEST_WITHDRAWAL_DRAFT_RULING);
        } else {
            atrApplication.setStatus(Status.REQUEST_WITHDRAWAL_SANITISED_RULING);
        }

        applicationService.update(atrApplication);

        List<String> roleDescriptions = Arrays.asList("System Administrator", "Senior Manager");
        List<User> admins = userService.findByUserRole(roleDescriptions);
        recipients.addAll(admins);

        for (UserAtrApplication atrApp : userAtrApplicationService.findByAtrApplicationAndResourceTypeIn(atrApplication, resourceTypes)) {
            User user = atrApp.getUser();
            if (user != null && !recipients.contains(user)) {
                recipients.add(user);
            }
        }
        switch (atrApplication.getStatus()) {
            case REQUEST_WITHDRAWAL_DRAFT_RULING:
                emailNotificationSenderService.sendEmailNotification(NotificationType.APPLICATION_DRAFT_RULING_WITHDRAWN, atrApplication);
                break;
            default:
                emailNotificationSenderService.sendEmailNotification(NotificationType.APPLICATION_SANITISED_RULING_WITHDRAWN, atrApplication);
                break;
        }

        addInformationMessage("Withdrawal request for [" + atrApplication.getCaseNum() + "] is submitted successfully!");
        reset().setAdd(true);
        //  reset().setList(true);
    }

    public void submitWithdrawal() {

    }

    public void fileDownload(SupportingDocument documentation) throws IOException {
        DownloadRecord record = new DownloadRecord();
        record.setCaseNumber(getEntity().getCaseNum());
        record.setDownloader(getActiveUser().getSid());
        record.setDownloadDate(new Date());
        record.setDescription(documentation.getDescription());
        recordServiceLocal.save(record);
        String objectId = documentation.getCode();
        documentAttachmentService.download(objectId);
        FileInputStream fis = new FileInputStream("./document.txt");
        String stringTooLong = IOUtils.toString(fis, "UTF-8");
        String b64 = stringTooLong;
        byte[] decoder = Base64.getDecoder().decode(b64);
        downloadFile = DefaultStreamedContent.builder().name(documentation.getDescription()).contentType("application/pdf").stream(() -> new ByteArrayInputStream(decoder)).build();
    }

    public void reInstate(ATRApplication application) {
        if (application.getStatus().equals(Status.REQUEST_WITHDRAWAL_DRAFT_RULING)) {
            application.setStatus(Status.DRAFT_RULING_PUBLISHED);
        } else {
            application.setStatus(Status.SANITISED_RULING_PUBLISHED);
        }
        applicationService.update(application);

        List<String> roleDescriptions = Arrays.asList("System Administrator", "Senior Manager");
        List<User> admins = userService.findByUserRole(roleDescriptions);
        recipients.addAll(admins);

        for (UserAtrApplication atrApp : userAtrApplicationService.findByAtrApplicationAndResourceTypeIn(application, resourceTypes)) {
            User user = atrApp.getUser();
            if (user != null && !recipients.contains(user)) {
                recipients.add(user);
            }
        }
        
        emailNotificationSenderService.sendEmailNotification(NotificationType.APPLICATION_REINSTATED, application);

        addInformationMessage(Long.toString(application.getCaseNum()), " has been reinstated successfully");
        reset().setAdd(true);
    }

    public void cancel(ATRApplication application) {
        reset().setList(true);
    }

    public void cancelFeedback(ATRApplication application) {
        reset().setAdd(true);
    }

    public void handleFileUpload(FileUploadEvent event) {

        this.originalFile = null;

        UploadedFile file = event.getFile();
        if (file != null && file.getContent() != null && file.getContent().length > 0 && file.getFileName() != null) {
            this.originalFile = file;
//            FacesMessage msg = new FacesMessage("Successful", this.originalFile.getFileName() + " is uploaded.");
//            FacesContext.getCurrentInstance().addMessage(null, msg);

            try {
                copyFile(event.getFile().getFileName(), file.getInputStream(), file.getSize());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        reset().setAdd(true);

    }

    public void copyFile(String fileName, InputStream inputStream, Long fileSize) {
        try {
            StringBuilder builder = new StringBuilder(destination);
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
            attachSupportingDocument(destination, fileName, fileSize);
        } catch (IOException e) {
            this.addInformationMessage(e.getMessage());
        }
    }

    public void attachSupportingDocument(String destination, String fileName, Long fileSize) {
        SupportingDocument supportingDocument = new SupportingDocument();
        supportingDocument.setCode("");
        supportingDocument.setCreatedBy(getActiveUser().getSid());
        //an if statement to be written to control document type based on the app status
        supportingDocument.setDocumentType(DocumentType.FEEDBACK);
        supportingDocument.setCreatedDate(new Date());
        supportingDocument.setDocumentSize(fileSize.doubleValue());
        supportingDocument.setDescription(fileName);
        getEntity().addSupportingDocument(supportingDocument);
        supportingDocuments.add(supportingDocument);
        addEntity(getEntity());
    }

    public JsonDocumentDto uploadDocumentsToServer(SupportingDocument supportingDocument) {
        String base64File = "";
        JsonDocumentDto jsonDocumentDto = new JsonDocumentDto();
        List<Properties> propertyList = new ArrayList<>();
        Properties properties = new Properties();
        properties.setPropertyName(supportingDocument.getDescription());
        propertyList.add(properties);

        StringBuilder builder = new StringBuilder(destination);
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

    public boolean isSubQuestion(String question) {
        if (question == null || question.trim().isEmpty()) {
            return false;
        }

        char firstChar = question.trim().charAt(0);
        return Character.isLetter(firstChar);
    }

    public Boolean companyQuestionSelectedYes(String description) {
        AtrApplicationQuestion foundQuestion = findQuestionByDescription(description);

        if (isResponseOptionNotNull(foundQuestion)) {
            return foundQuestion.getResponseOption().equals(ResponseOption.YES);
        }
        return false;
    }

    private boolean isResponseOptionNotNull(AtrApplicationQuestion question) {
        return question != null && question.getResponseOption() != null;
    }

    private AtrApplicationQuestion findQuestionByDescription(String description) {
        return this.applicationQuestions.stream()
                .filter(res -> res.getQuestion().getDescription().equalsIgnoreCase(description))
                .findFirst()
                .orElse(null);
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }

    public Slice<ATRApplication> getAtrApplications() {
        return atrApplications;
    }

    public void setAtrApplications(Slice<ATRApplication> atrApplications) {
        this.atrApplications = atrApplications;
    }

    public List<AtrApplicationQuestion> getApplicationQuestions() {
        return applicationQuestions;
    }

    public void setApplicationQuestions(List<AtrApplicationQuestion> applicationQuestions) {
        this.applicationQuestions = applicationQuestions;
    }

    public List<SelectItem> getResponsesList() {
        return responsesList;
    }

    public void setResponsesList(List<SelectItem> responsesList) {
        this.responsesList = responsesList;
    }

    public AtrApplicationServiceLocal getApplicationService() {
        return applicationService;
    }

    public void setApplicationService(AtrApplicationServiceLocal applicationService) {
        this.applicationService = applicationService;
    }

    public List<RulingArea> getRulingAreas() {
        return rulingAreas;
    }

    public void setRulingAreas(List<RulingArea> rulingAreas) {
        this.rulingAreas = rulingAreas;
    }

    public List<RulingType> getRulingTypes() {
        return rulingTypes;
    }

    public void setRulingTypes(List<RulingType> rulingTypes) {
        this.rulingTypes = rulingTypes;
    }

    public List<ApplicationType> getApplicationTypes() {
        return applicationTypes;
    }

    public void setApplicationTypes(List<ApplicationType> applicationTypes) {
        this.applicationTypes = applicationTypes;
    }

    public List<EntityType> getEntityTypes() {
        return entityTypes;
    }

    public void setEntityTypes(List<EntityType> entityTypes) {
        this.entityTypes = entityTypes;
    }

    public List<ClassDescription> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<ClassDescription> descriptions) {
        this.descriptions = descriptions;
    }

    public List<CloseType> getSaResidents() {
        return saResidents;
    }

    public void setSaResidents(List<CloseType> saResidents) {
        this.saResidents = saResidents;
    }

    public List<RepresentativeType> getRepresentativeTypes() {
        return representativeTypes;
    }

    public void setRepresentativeTypes(List<RepresentativeType> representativeTypes) {
        this.representativeTypes = representativeTypes;
    }

    public List<Province> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<Province> provinces) {
        this.provinces = provinces;
    }

    public String getNaturalPerson() {
        return naturalPerson;
    }

    public void setNaturalPerson(String naturalPerson) {
        this.naturalPerson = naturalPerson;
    }

    public StreamedContent getDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(StreamedContent downloadFile) {
        this.downloadFile = downloadFile;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public UploadedFile getOriginalFile() {
        return originalFile;
    }

    public void setOriginalFile(UploadedFile originalFile) {
        this.originalFile = originalFile;
    }

    public List<SupportingDocument> getSupportingDocuments() {
        return supportingDocuments;
    }

    public void setSupportingDocuments(List<SupportingDocument> supportingDocuments) {
        this.supportingDocuments = supportingDocuments;
    }

    public Integer getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(Integer tabIndex) {
        this.tabIndex = tabIndex;
    }

    public String getWithdrawalReason() {
        return withdrawalReason;
    }

    public void setWithdrawalReason(String withdrawalReason) {
        this.withdrawalReason = withdrawalReason;
    }

    public List<FeedBack> getFeedBacks() {
        return feedBacks;
    }

    public void setFeedBacks(List<FeedBack> feedBacks) {
        this.feedBacks = feedBacks;
    }

    public User getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(User userDetail) {
        this.userDetail = userDetail;
    }

    public boolean isLetterConditions() {
        return letterConditions;
    }

    public void setLetterConditions(boolean letterConditions) {
        this.letterConditions = letterConditions;
    }

}
