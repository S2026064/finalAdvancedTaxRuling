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
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DualListModel;
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
import za.gov.sars.common.TaxAct;
import za.gov.sars.common.ValidityRulingType;
import za.gov.sars.common.CloseType;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.ActSection;
import za.gov.sars.domain.AtrApplicationQuestion;
import za.gov.sars.domain.DownloadRecord;
import za.gov.sars.domain.FeedBack;
import za.gov.sars.domain.JsonDocumentDto;
import za.gov.sars.domain.RulingActSection;
import za.gov.sars.domain.SupportingDocument;
import za.gov.sars.domain.User;
import za.gov.sars.domain.UserAtrApplication;
import za.gov.sars.domain.ValidityDetails;
import za.gov.sars.service.ActSectionServiceLocal;
import za.gov.sars.service.AtrApplicationQuestionServiceLocal;
import za.gov.sars.service.AtrApplicationServiceLocal;
import za.gov.sars.service.DocumentAttachmentServiceLocal;
import za.gov.sars.service.DownloadRecordServiceLocal;
import za.gov.sars.service.EmailNotificationSenderServiceLocal;
import za.gov.sars.service.FeedBackServiceLocal;
import za.gov.sars.service.RulingActSectionServiceLocal;
import za.gov.sars.service.SupportingDocumentServiceLocal;
import za.gov.sars.service.UserAtrApplicationServiceLocal;
import za.gov.sars.service.UserServiceLocal;

/**
 *
 * @author S2026064
 */
@ManagedBean
@ViewScoped
public class RulingBean extends BaseBean<ATRApplication> {

    @Autowired
    private AtrApplicationServiceLocal applicationService;
    @Autowired
    private AtrApplicationQuestionServiceLocal atrApplicationQuestionService;
    @Autowired
    private UserAtrApplicationServiceLocal userAtrApplicationService;
    @Autowired
    private UserServiceLocal userService;
    @Autowired
    private SupportingDocumentServiceLocal supportingDocumentService;
    @Autowired
    private DocumentAttachmentServiceLocal documentAttachmentService;
    @Autowired
    private DownloadRecordServiceLocal recordServiceLocal;
    @Autowired
    private FeedBackServiceLocal feedBackService;
    @Autowired
    private ActSectionServiceLocal actSectionService;
    @Autowired
    private RulingActSectionServiceLocal rulingActSectionService;

    @Autowired
    private EmailNotificationSenderServiceLocal emailNotificationSenderService;

    private Slice<ATRApplication> atrApplications;
    private List<AtrApplicationQuestion> applicationQuestions = new ArrayList<>();
    private List<SelectItem> responsesList = new ArrayList<>();
    private List<RulingArea> rulingAreas = new ArrayList<>();
    private List<RulingType> rulingTypes = new ArrayList<>();
    private List< ApplicationType> applicationTypes = new ArrayList<>();
    private List< EntityType> entityTypes = new ArrayList<>();
    private List<ClassDescription> descriptions = new ArrayList<>();
    private List<CloseType> saResidents = new ArrayList<>();
    private List<RepresentativeType> representativeTypes = new ArrayList<>();
    private List<Province> provinces = new ArrayList<>();
    private List<SupportingDocument> supportingDocuments = new ArrayList<>();
    private List<JsonDocumentDto> JsonDocumentDtos = new ArrayList<>();
    private List<FeedBack> feedBacks = new ArrayList<>();
    private List<UserAtrApplication> userAtrApplications = new ArrayList<>();
    private List<Status> statuses = new ArrayList<>();
    private UserAtrApplication userApplication;
    private User userDetail;
    private String naturalPerson;
    private List<User> recipients = new ArrayList<>();
    private List<ResourceType> resourceTypes = new ArrayList<>();
    private DualListModel<ActSection> actSectionsPickList;
    private List<RulingActSection> rulingActSections = new ArrayList<>();
    private List<TaxAct> taxActList;
    private List<ValidityRulingType> validityRulingTypes = new ArrayList<>();
    private TaxAct selectedTaxAct;
    
    

    private UploadedFile originalFile;
    private StreamedContent downloadFile;
    private String destination = "./";
    private Integer tabIndex = 0;
    private String comment;

    @PostConstruct
    public void init() {
        reset().setList(true);

        Pageable pageable = PageRequest.of(0, 20);
        resourceTypes.add(ResourceType.MANAGER);
        resourceTypes.add(ResourceType.PRIMARY_RESOURCE);
        resourceTypes.add(ResourceType.APPLICANT);
        validityRulingTypes.addAll(Arrays.asList(ValidityRulingType.values()));
        rulingTypes.addAll(Arrays.asList(RulingType.values()));
        applicationTypes.addAll(Arrays.asList(ApplicationType.values()));
        entityTypes.addAll(Arrays.asList(EntityType.values()));
        provinces.addAll(Arrays.asList(Province.values()));
        saResidents.addAll(Arrays.asList(CloseType.values()));
        descriptions.addAll(Arrays.asList(ClassDescription.values()));
        representativeTypes.addAll(Arrays.asList(RepresentativeType.values()));
        taxActList = Arrays.asList(TaxAct.values());

        List<ActSection> sourceSections = new ArrayList<>();
        List<ActSection> targetSections = new ArrayList<>();
        actSectionsPickList = new DualListModel<>(sourceSections, targetSections);

        if (getActiveUser().getUser().getUserRole().getDescription().equalsIgnoreCase("Resource")) {
            statuses.add(Status.ESTIMATE_ACCEPTED_PENDING_DOCUMENTATION);
            statuses.add(Status.DRAFT_RULING_ACCEPTED);
            statuses.add(Status.SANITISED_RULING_ACCEPTED);
            statuses.add(Status.DRAFT_RULING_PUBLISHED);
            statuses.add(Status.SANITISED_RULING_PUBLISHED);
            statuses.add(Status.DRAFT_RULING_REJECTED);
            statuses.add(Status.SANITISED_RULING_REJECTED);
            statuses.add(Status.RULING_REJECTED);
            atrApplications = userAtrApplicationService.findAppByUserResourceTypeAndAppStatusIn(getActiveUser().getUser(), ResourceType.PRIMARY_RESOURCE, statuses, pageable);
//            userAtrApplicationService.findAtrApplicationsByUserId(getActiveUser().getUser()).stream().filter(userAtrApplication -> (userAtrApplication.getResourceType().equals(ResourceType.PRIMARY_RESOURCE))).map(userAtrApplication -> userAtrApplication.getAtrApplication()).filter(aTRApplication -> (statuses.contains(aTRApplication.getStatus()))).forEachOrdered(aTRApplication -> {
//                atrApplications.and(aTRApplication);
//            });

        } else if (getActiveUser().getUser().getUserRole().getDescription().equalsIgnoreCase("Manager")) {
            statuses.add(Status.DRAFT_RULING_REQUIRES_AUTHORISATION);
            statuses.add(Status.REQUEST_WITHDRAWAL_DRAFT_RULING);
            statuses.add(Status.REQUEST_WITHDRAWAL_SANITISED_RULING);
            statuses.add(Status.SANITISED_RULING_REQUIRES_AUTHORISATION);
            atrApplications = userAtrApplicationService.findAppByUserResourceTypeAndAppStatusIn(getActiveUser().getUser(), ResourceType.MANAGER, statuses, pageable);

        } else {
            statuses.add(Status.RULING_REQUIRES_AUTHORISATION);
            atrApplications = applicationService.findByStatusInOrderByCreatedDateDesc(statuses, pageable);
        }
        Arrays.asList(ResponseOption.values()).stream()
                .forEach(responseOption -> {
                    responsesList.add(new SelectItem(responseOption, responseOption.toString()));
                });
    }

    public void review(ATRApplication application) {
        reset().setAdd(true);
        setTabIndex(4);

        List<FeedBack> unreadFeedbacks = feedBackService.findUnreadByApplicant(application);

        for (FeedBack feedback : unreadFeedbacks) {
            feedback.setUnreadByApplicant(false);
            feedBackService.save(feedback);
        }
        if(application.getStatus().equals(Status.SANITISED_RULING_ACCEPTED) || application.getStatus().equals(Status.RULING_REJECTED)){
            if(application.getValidityDetail() != null){
             ValidityDetails details = application.getValidityDetail();
             details.setUpdatedBy(getActiveUser().getSid());
             details.setUpdatedDate(new Date());
            
            }else{
             ValidityDetails details = new ValidityDetails();
             details.setCreatedBy(getActiveUser().getSid());
             details.setCreatedDate(new Date());
             application.setValidityDetail(details);
            }
            if(!application.getRulingActSections().isEmpty()){
           selectedTaxAct  = application.getRulingActSections().stream()
                   .findFirst().get().getActSection().getTaxAct();
            List<ActSection> targetSections = application.getRulingActSections()
                .stream()
                .map(actSection -> actSection.getActSection())
                .collect(Collectors.toList());

        List<ActSection> allSections = actSectionService.findByTaxAct(selectedTaxAct);
        List<ActSection> sourceSections = allSections.stream()
                .filter(persisted -> !targetSections.contains(persisted))
                .collect(Collectors.toList());

        actSectionsPickList = new DualListModel<>(sourceSections, targetSections);
            }
        }
         if(application.getStatus().equals(Status.RULING_REQUIRES_AUTHORISATION)){
                selectedTaxAct  = application.getRulingActSections().stream()
                   .findFirst().get().getActSection().getTaxAct();
            }
         
          applicationQuestions = atrApplicationQuestionService.findByAtrApplication(application);
        userApplication = userAtrApplicationService.findUsersByAtrApplicationIdAndResourceType(application, ResourceType.APPLICANT);
        userDetail = userApplication.getUser();
        applicationQuestions.stream().filter(response -> (response.getQuestion().getDescription().equalsIgnoreCase("16. Is the Applicant a SMME (Small, Medium and Micro Enterprise)?"))).forEachOrdered(response -> {
            naturalPerson = response.getResponseOption().toString();
        });

        addEntity(application);
    }

    public void submit(ATRApplication application) {
       
        
        String responseObjectId = null;
        List<SupportingDocument> uploadFailedDocuments = new ArrayList<>();
        List<SupportingDocument> documents = application.getSupportingDocuments();
        if (!documents.isEmpty()) {
            SupportingDocument lastDocument = documents.get(documents.size() - 1);
            if (lastDocument.getId() == null) {
                switch (application.getStatus()) {
                    case ESTIMATE_ACCEPTED_PENDING_DOCUMENTATION:
                    case DRAFT_RULING_REJECTED:
                        application.setStatus(Status.DRAFT_RULING_REQUIRES_AUTHORISATION);
                        break;
                    case DRAFT_RULING_ACCEPTED:
                    case SANITISED_RULING_REJECTED:
                        application.setStatus(Status.SANITISED_RULING_REQUIRES_AUTHORISATION);
                        break;
                    default:
                        application.setStatus(Status.RULING_REQUIRES_AUTHORISATION);
           List<ActSection> selectedSections = actSectionsPickList.getTarget();
          List<RulingActSection> rulingActSections = new ArrayList<>();
        for (ActSection section : selectedSections) {
            RulingActSection rulingSection = new RulingActSection();
            rulingSection.setCreatedBy(getActiveUser().getSid());
            rulingSection.setCreatedDate(new Date());
            rulingSection.setActSection(section);
            rulingSection.setAtrApplication(application);
            rulingActSections.add(rulingSection);
        }
        if (application.getRulingActSections() != null) {
            application.getRulingActSections().clear();
        }
         application.setRulingActSections(rulingActSections);
//       for (RulingActSection rulingSection : rulingActSections) {
//
//            rulingActSectionService.save(rulingSection);
//        } 
                        break;
                }
            } else {
                switch (application.getStatus()) {
                    case ESTIMATE_ACCEPTED_PENDING_DOCUMENTATION:
                    case DRAFT_RULING_REJECTED:
                        addErrorMessage("Please upload draft ruling document");
                        return;
                    case DRAFT_RULING_ACCEPTED:
                    case SANITISED_RULING_REJECTED:
                        addErrorMessage("Please upload sanitised ruling document");
                        return;
                    default:
                        addErrorMessage("Please upload final ruling document");
                        return;
                        
                }
            }
            Iterator<SupportingDocument> iterator = documents.iterator();
            while (iterator.hasNext()) {
                SupportingDocument document = iterator.next();
                if (document.getId() == null) {
                    JsonDocumentDto jsonDocumentDto = uploadDocumentsToServer(document);
                    JSONObject myResponse = new JSONObject(documentAttachmentService.uploadDocument(jsonDocumentDto));
                    responseObjectId = myResponse.getString("objectId");
                    document.setCode(responseObjectId);
                    supportingDocumentService.save(document);
                    if (document.getCode() == null) {
                        uploadFailedDocuments.add(document);
                        iterator.remove();
                    }
                }
            }
        } else {
            addErrorMessage("Please Upload draft ruling document");
            return;
        }

        application.setUpdatedBy(getActiveUser().getSid());
        application.setUpdatedDate(new Date());
        applicationService.update(application);
        List<String> roleDescriptions = Arrays.asList("Administrator", "Senior Manager");
        List<User> admins = userService.findByUserRole(roleDescriptions);
        //
        recipients.addAll(admins);
        for (UserAtrApplication atrApp : userAtrApplicationService.findByAtrApplicationAndResourceTypeIn(application, resourceTypes)) {
            User user = atrApp.getUser();
            if (user != null && !recipients.contains(user)) {
                recipients.add(user);
            }
        }
        switch (application.getStatus()) {
            case DRAFT_RULING_REQUIRES_AUTHORISATION:
                emailNotificationSenderService.sendEmailNotification(NotificationType.DRAFT_RULING_REQUIRE_APPROVAL, application);
                break;
            case SANITISED_RULING_REQUIRES_AUTHORISATION:
                emailNotificationSenderService.sendEmailNotification(NotificationType.SANITISED_RULING_REQUIRES_APPROVAL, application);
                break;
            default:
                emailNotificationSenderService.sendEmailNotification(NotificationType.AUTHORISE_RULING, application);
                break;
        }
        addInformationMessage("A ruling is successfully submitted");
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
        feedback.setCreatedBy("Admin");
        feedback.setAtrApplication(atrApplication);
        feedBackService.save(feedback);
        feedBacks.add(feedback);
        atrApplication.setUpdatedBy(getActiveUser().getFullName());
        atrApplication.setUpdatedDate(new Date());
        applicationService.update(atrApplication);
        addInformationMessage("Feedback successfully submitted");
        comment = null;
        reset().setFeedbackPanel(true);
    }

    public void addFeedback(ATRApplication atrApplication) {
        reset().setFeedbackPanel(true);
        feedBacks = feedBackService.findByAtrApplication(atrApplication);

    }

    public void renderWithrawalRjectPanel(ATRApplication application) {
        reset().setRejectWithdrawal(true);
    }

    public void rejectWithdrawal(ATRApplication application) {
        if (application.getStatus().equals(Status.REQUEST_WITHDRAWAL_DRAFT_RULING)) {
            application.setStatus(Status.DRAFT_RULING_PUBLISHED);
        } else {
            application.setStatus(Status.SANITISED_RULING_PUBLISHED);
        }
        FeedBack feedback = new FeedBack();
        feedback.setCreatedBy(getActiveUser().getFullName());
        feedback.setCreatedDate(new Date());
        feedback.setFeedbackType(FeedbackType.REQUEST_DECLINED);
        feedback.setDescription(comment);
        feedback.setUnreadByAdmin(true);
        feedback.setCreatedBy("Admin");
        feedback.setAtrApplication(application);
        feedBackService.save(feedback);
        application.setUpdatedBy(getActiveUser().getFullName());
        application.setUpdatedDate(new Date());
        applicationService.update(application);
        addInformationMessage("Withdrawal of case ", Long.toString(application.getCaseNum()), " was successfully rejected");
        reset().setList(true);
    }

    public void renderWithrawalPanel(ATRApplication application) {
        reset().setAcceptWithdrawal(true);
    }

    public void acceptWithdrawal(ATRApplication application) {
        FeedBack feedback = new FeedBack();
        feedback.setCreatedBy(getActiveUser().getFullName());
        feedback.setCreatedDate(new Date());
        feedback.setFeedbackType(FeedbackType.REQUEST_APPROVED);
        feedback.setDescription(comment);
        feedback.setUnreadByAdmin(true);
        feedback.setCreatedBy("Admin");
        feedback.setAtrApplication(application);
        feedBackService.save(feedback);
        application.setUpdatedBy(getActiveUser().getFullName());
        application.setUpdatedDate(new Date());
        application.setStatus(Status.CASE_WITHDRAWN);
        applicationService.update(application);
        addInformationMessage("Withdrawal of case ", Long.toString(application.getCaseNum()), " was successfully accepted");
        reset().setList(true);
    }

    public Long getUnreadCount(ATRApplication app) {
        return feedBackService.countUnreadByApplicant(app);
    }

    public void approve(ATRApplication application) {
        application.setUpdatedBy(getActiveUser().getSid());
        application.setUpdatedDate(new Date());
        switch (application.getStatus()) {
            case DRAFT_RULING_REQUIRES_AUTHORISATION:
                application.setStatus(Status.DRAFT_RULING_PUBLISHED);
                break;
            case SANITISED_RULING_REQUIRES_AUTHORISATION:
                application.setStatus(Status.SANITISED_RULING_PUBLISHED);
                break;
            default:
                application.setStatus(Status.RULING_PUBLISHED_PENDING_OUTSTANDING_AMOUNT);
                break;
        }
        applicationService.update(application);
        addInformationMessage("A ruling is successfully approved");
        reset().setList(true);
//        List<String> roleDescriptions = Arrays.asList("System Administrator");
//        List<User> admins = userService.findByUserRole(roleDescriptions);
//        recipients.addAll(admins);
//        for (UserAtrApplication atrApp : userAtrApplicationService.findByAtrApplicationAndResourceTypeIn(application, resourceTypes)) {
//            User user = atrApp.getUser();
//            if (user != null && !recipients.contains(user)) {
//                recipients.add(user);
//            }
//        }
        switch (application.getStatus()) {
            case DRAFT_RULING_PUBLISHED:
                emailNotificationSenderService.sendEmailNotification(NotificationType.DRAFT_RULING_PUBLISHED, application);
//                emailNotificationSenderService.sendEmailNotification(NotificationType.DRAFT_RULING_PUBLISHED, Long.toString(application.getCaseNum()), application.getApplicantName(), application.getUpdatedDate(), recipients);
//                });
                break;
            case SANITISED_RULING_PUBLISHED:
//                userAtrApplicationService.findByAtrApplication(application).stream().filter(app -> (app.getResourceType().equals(ResourceType.PRIMARY_RESOURCE))).forEachOrdered(app -> {
                emailNotificationSenderService.sendEmailNotification(NotificationType.SANITISED_RULING_PUBLISHED, application);
//                });
                break;
            default:
//                userAtrApplicationService.findByAtrApplication(application).stream().filter(app -> (app.getResourceType().equals(ResourceType.PRIMARY_RESOURCE))).forEachOrdered(app -> {
                emailNotificationSenderService.sendEmailNotification(NotificationType.RULING_PUBLISHED_PUBLICLY, application);
//                });
                break;
        }
    }

    public void rejectRuling(ATRApplication application) {
        switch (application.getStatus()) {
            case DRAFT_RULING_REQUIRES_AUTHORISATION:
                application.setStatus(Status.DRAFT_RULING_REJECTED);
                break;
            case SANITISED_RULING_REQUIRES_AUTHORISATION:
                application.setStatus(Status.SANITISED_RULING_REJECTED);
                break;
            default:
                application.setStatus(Status.RULING_REJECTED);
                break;
        }
        FeedBack feedback = new FeedBack();
        feedback.setCreatedBy(getActiveUser().getSid());
        feedback.setCreatedDate(new Date());
        feedback.setDescription(comment);
        feedback.setFeedbackType(FeedbackType.RULING_REJECTED);
        feedback.setAtrApplication(application);
        feedBackService.save(feedback);
        applicationService.update(application);
        addInformationMessage("A ruling was successfuly rejected");
        reset().setAdd(true);
        reset().setList(true);
    }

    public void handleFileUpload(FileUploadEvent event) {
        
        this.originalFile = null;

        UploadedFile file = event.getFile();
        if (file != null && file.getContent() != null && file.getContent().length > 0 && file.getFileName() != null) {
            this.originalFile = file;
            FacesMessage msg = new FacesMessage("Successful", this.originalFile.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            try {
                copyFile(event.getFile().getFileName(), file.getInputStream(), file.getSize());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        reset().setAdd(true);

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
        switch (getEntity().getStatus()) {
            case ESTIMATE_ACCEPTED_PENDING_DOCUMENTATION:
            case DRAFT_RULING_REJECTED:
                supportingDocument.setDocumentType(DocumentType.DRAFT_RULING);
                break;
            case DRAFT_RULING_ACCEPTED:
            case SANITISED_RULING_REJECTED:
                supportingDocument.setDocumentType(DocumentType.SANITISED_RULING);
                break;
            default:
                supportingDocument.setDocumentType(DocumentType.FINAL_RULING);
                break;
        }

        supportingDocument.setCreatedDate(new Date());
        supportingDocument.setDocumentSize(fileSize.doubleValue());
        supportingDocument.setDescription(fileName);
        supportingDocument.setATRApplication(getEntity());
        getEntity().addSupportingDocument(supportingDocument);
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

    public void recordDownload(SupportingDocument documentation) throws IOException {
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

    public void deleteDocument(SupportingDocument supportingDocument) {
        synchroniseDocument(getEntity(), supportingDocument);
        if (supportingDocument.getId() != null) {
            addEntity(applicationService.update(getEntity()));
        }
        addCollection(getEntity());
        addInformationMessage("Document successfully deleted");
    }

    private void synchroniseDocument(ATRApplication application, SupportingDocument supportingDocument) {
        application.removeSupportingDocument(supportingDocument);
        getCollections().remove(application);
    }
    
    public void onActSectionTransfer() {
        List<ActSection> tempList = actSectionsPickList.getTarget();
        System.out.println("====" + tempList.toString());
    }

    public void onActSelection() {
        // Load all sections for the selected Act
        List<ActSection> sourceSections = actSectionService.findByTaxAct(selectedTaxAct);

        // Keep only those already selected sections that belong to this Act
        List<ActSection> filteredTargetSections = actSectionsPickList.getTarget()
                .stream()
                .filter(section -> section.getTaxAct().equals(selectedTaxAct))
                .collect(Collectors.toList());

        // Remove already-selected ones from the source to avoid duplication
        sourceSections.removeAll(filteredTargetSections);

        // Update existing DualListModel
        actSectionsPickList.setSource(sourceSections);
        actSectionsPickList.setTarget(filteredTargetSections);

    }

    public void cancel(ATRApplication application) {
        reset().setList(true);
    }

    public void cancelFeedback(ATRApplication application) {
        reset().setAdd(true);
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

    public User getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(User userDetail) {
        this.userDetail = userDetail;
    }

    public String getNaturalPerson() {
        return naturalPerson;
    }

    public void setNaturalPerson(String naturalPerson) {
        this.naturalPerson = naturalPerson;
    }

    public UploadedFile getOriginalFile() {
        return originalFile;
    }

    public void setOriginalFile(UploadedFile originalFile) {
        this.originalFile = originalFile;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public List<SupportingDocument> getSupportingDocuments() {
        return supportingDocuments;
    }

    public void setSupportingDocuments(List<SupportingDocument> supportingDocuments) {
        this.supportingDocuments = supportingDocuments;
    }

    public List<JsonDocumentDto> getJsonDocumentDtos() {
        return JsonDocumentDtos;
    }

    public void setJsonDocumentDtos(List<JsonDocumentDto> JsonDocumentDtos) {
        this.JsonDocumentDtos = JsonDocumentDtos;
    }

    public StreamedContent getDownloadFile() {
        return downloadFile;
    }

    public void setDownloadFile(StreamedContent downloadFile) {
        this.downloadFile = downloadFile;
    }

    public Integer getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(Integer tabIndex) {
        this.tabIndex = tabIndex;
    }

    public List<UserAtrApplication> getUserAtrApplications() {
        return userAtrApplications;
    }

    public void setUserAtrApplications(List<UserAtrApplication> userAtrApplications) {
        this.userAtrApplications = userAtrApplications;
    }

    public List<FeedBack> getFeedBacks() {
        return feedBacks;
    }

    public void setFeedBacks(List<FeedBack> feedBacks) {
        this.feedBacks = feedBacks;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public DualListModel<ActSection> getActSectionsPickList() {
        return actSectionsPickList;
    }

    public void setActSectionsPickList(DualListModel<ActSection> actSectionsPickList) {
        this.actSectionsPickList = actSectionsPickList;
    }

    public List<TaxAct> getTaxActList() {
        return taxActList;
    }

    public void setTaxActList(List<TaxAct> taxActList) {
        this.taxActList = taxActList;
    }

    public TaxAct getSelectedTaxAct() {
        return selectedTaxAct;
    }

    public void setSelectedTaxAct(TaxAct selectedTaxAct) {
        this.selectedTaxAct = selectedTaxAct;
    }

    public List<ValidityRulingType> getValidityRulingTypes() {
        return validityRulingTypes;
    }

    public void setValidityRulingTypes(List<ValidityRulingType> validityRulingTypes) {
        this.validityRulingTypes = validityRulingTypes;
    }

}
