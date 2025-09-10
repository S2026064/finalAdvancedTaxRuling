/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.atr.mb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import za.gov.sars.atr.util.UniqueReferenceGenerator;
import za.gov.sars.common.AddressType;
import za.gov.sars.common.ApplicationType;
import za.gov.sars.common.ClassDescription;
import za.gov.sars.common.EntityType;
import za.gov.sars.common.NotificationType;
import za.gov.sars.common.Province;
import za.gov.sars.common.QuestionType;
import za.gov.sars.common.RepresentativeType;
import za.gov.sars.common.ResourceType;
import za.gov.sars.common.ResponseOption;
import za.gov.sars.common.RulingArea;
import za.gov.sars.common.RulingType;
import za.gov.sars.common.Status;
import za.gov.sars.common.YesNo;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.Address;
import za.gov.sars.domain.AtrApplicationQuestion;
import za.gov.sars.domain.Question;
import za.gov.sars.domain.TaxType;
import za.gov.sars.domain.User;
import za.gov.sars.domain.UserAtrApplication;
import za.gov.sars.service.AtrApplicationQuestionServiceLocal;
import za.gov.sars.service.AtrApplicationServiceLocal;
import za.gov.sars.service.EmailNotificationSenderServiceLocal;
import za.gov.sars.service.QuestionServiceLocal;
import za.gov.sars.service.UserAtrApplicationServiceLocal;
import za.gov.sars.service.UserServiceLocal;

/**
 *
 * @author S2026064
 */
@ManagedBean
@ViewScoped
public class ApplicationBean extends BaseBean<ATRApplication> {

    @Autowired
    private AtrApplicationServiceLocal applicationService;

    @Autowired
    private AtrApplicationQuestionServiceLocal atrApplicationQuestionService;

    @Autowired
    private UserAtrApplicationServiceLocal userAtrApplicationService;

    @Autowired
    private UserServiceLocal userService;

    @Autowired
    private QuestionServiceLocal questionService;
    
    @Autowired
    private EmailNotificationSenderServiceLocal emailNotificationSenderService;

    private List<RulingArea> rulingAreas = new ArrayList<>();
    private List<RulingType> rulingTypes = new ArrayList<>();
    private List< ApplicationType> applicationTypes = new ArrayList<>();
    private List< EntityType> entityTypes = new ArrayList<>();
    private List<AtrApplicationQuestion> applicationQuestions = new ArrayList<>();
    private List<ClassDescription> descriptions = new ArrayList<>();
    private List<YesNo> saResidents = new ArrayList<>();
    private List<RepresentativeType> representativeTypes = new ArrayList<>();
    private List<Province> provinces = new ArrayList<>();
    private List<SelectItem> responsesList = new ArrayList<>();

    private Slice<ATRApplication> atrApplications;

    private User userDetail;
    private Integer tabIndex = 0;
    private String naturalPerson;

    @PostConstruct
    public void init() {
        reset().setList(true);
        rulingTypes.addAll(Arrays.asList(RulingType.values()));
        applicationTypes.addAll(Arrays.asList(ApplicationType.values()));
        entityTypes.addAll(Arrays.asList(EntityType.values()));
        provinces.addAll(Arrays.asList(Province.values()));
        saResidents.addAll(Arrays.asList(YesNo.values()));
        userDetail = userService.findByIdNumberAndPassPortNumber(getActiveUser().getUser().getIdNumber(), getActiveUser().getUser().getPassPortNumber());
        descriptions.addAll(Arrays.asList(ClassDescription.values()));
        representativeTypes.addAll(Arrays.asList(RepresentativeType.values()));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdDate").descending());
        atrApplications = userAtrApplicationService.findAppByUserResourceType(getActiveUser().getUser(), ResourceType.APPLICANT, pageable);
        addCollections(atrApplications.toList());

        Arrays.asList(ResponseOption.values()).stream()
                .forEach(responseOption -> {
                    responsesList.add(new SelectItem(responseOption, responseOption.toString()));
                });

    }

    public void addOrUpdate(ATRApplication application) {

        reset().setAdd(true);
        setTabIndex(0);
        if (application != null) {
            for (AtrApplicationQuestion applicationQuestion : atrApplicationQuestionService.findByAtrApplication(application)) {
                applicationQuestions.add(applicationQuestion);
            }

            if (getActiveUser().getIdNumber() != null) {
                application.setUpdatedBy(getActiveUser().getIdNumber());
                application.setUpdatedDate(new Date());

            } else {
                application.setUpdatedBy(getActiveUser().getUser().getPassPortNumber());
                application.setUpdatedDate(new Date());
            }
        } else {
            application = new ATRApplication();
            application.setStatus(Status.NEW);

            TaxType taxType = new TaxType();
            taxType.setCreatedBy(getActiveUser().getFullName());
            taxType.setCreatedDate(new Date());
            application.setTaxType(taxType);

            Address address = new Address();
            address.setCreatedBy(getActiveUser().getFullName());
            address.setCreatedDate(new Date());
            address.setAddressType(AddressType.BUSINESS);
            application.setAddress(address);

            if (getActiveUser().getIdNumber() != null) {
                application.setCreatedBy(getActiveUser().getIdNumber());
                application.setCreatedDate(new Date());

            } else {
                application.setCreatedBy(getActiveUser().getUser().getPassPortNumber());
                application.setCreatedDate(new Date());
            }

            for (Question question : questionService.listAll()) {
                AtrApplicationQuestion applicationQuestion = new AtrApplicationQuestion();
                applicationQuestion.setCreatedBy(getActiveUser().getFullName());
                applicationQuestion.setCreatedDate(new Date());
                applicationQuestion.setQuestion(question);

                applicationQuestions.add(applicationQuestion);
            }
            addCollection(application);
        }
        addEntity(application);
    }

    public void save(ATRApplication application) {
        application.setStatus(Status.SAVED);
        if (application.getId() != null) {
            applicationService.update(application);
            for (AtrApplicationQuestion response : applicationQuestions) {
                response.setUpdatedBy(getActiveUser().getFullName());
                response.setUpdatedDate(new Date());
                atrApplicationQuestionService.update(response);
            }
        } else {
            UniqueReferenceGenerator referenceNumber = new UniqueReferenceGenerator();
            application.setCaseNum(referenceNumber.generateNumber());
            applicationService.save(application);
            UserAtrApplication userApp = new UserAtrApplication();
            userApp.setCreatedBy(getActiveUser().getFullName());
            userApp.setCreatedDate(new Date());
            userApp.setUser(getActiveUser().getUser());
            userApp.setAtrApplication(application);
            userApp.setResourceType(ResourceType.APPLICANT);
            userAtrApplicationService.save(userApp);
            for (AtrApplicationQuestion response : applicationQuestions) {
                response.setAtrApplication(application);
                atrApplicationQuestionService.save(response);

            }

        }
        addInformationMessage("Application saved successfully");
        setTabIndex(0);
        applicationQuestions.clear();
        reset().setList(true);

    }

    public void submit(ATRApplication application) {

    //    application.setStatus(Status.SUBMITTED_PAYMENT_UNCONFIRMED);
         application.setStatus(Status.READY_FOR_ALLOCATION);
        if (application.getId() != null) {
            applicationService.update(application);
            for (AtrApplicationQuestion response : applicationQuestions) {
                if ((response.getResponseOption() == null) && !response.getQuestion().getQuestionType().equals(QuestionType.PRE_SCREENING)) {
                    atrApplicationQuestionService.deleteById(response.getId());
                }
            }
        } else {
            UniqueReferenceGenerator referenceNumber = new UniqueReferenceGenerator();
            application.setCaseNum(referenceNumber.generateNumber());
            applicationService.save(application);
            UserAtrApplication userApp = new UserAtrApplication();
            userApp.setCreatedBy(getActiveUser().getFullName());
            userApp.setCreatedDate(new Date());
            userApp.setUser(getActiveUser().getUser());
            userApp.setAtrApplication(application);
            userApp.setResourceType(ResourceType.APPLICANT);
            userAtrApplicationService.save(userApp);
            for (AtrApplicationQuestion response : applicationQuestions) {
                if (response.getQuestion().getQuestionType().equals(QuestionType.PRE_SCREENING)) {
                    response.setAtrApplication(application);
                    atrApplicationQuestionService.save(response);
                }
                if (!(response.getResponseOption() == null) && !response.getQuestion().getQuestionType().equals(QuestionType.PRE_SCREENING)) {
                    response.setAtrApplication(application);
                    atrApplicationQuestionService.save(response);
                }
            }

        }
        emailNotificationSenderService.sendEmailNotification(NotificationType.NEW_APPLICATION_RECEIVED, application);
        addInformationMessage("application submitted successfuly");
        reset().setList(true);
        applicationQuestions.clear();
    }

    public void delete(ATRApplication application) {
        atrApplicationQuestionService.findByAtrApplication(application).stream()
                .filter(applicationQuestion -> (applicationQuestion.getId() != null))
                .forEachOrdered(applicationQuestion -> {
                    atrApplicationQuestionService.deleteById(applicationQuestion.getId());
                });
        userAtrApplicationService.findByAtrApplication(application).stream()
                .filter(userApp -> (userApp.getId() != null))
                .forEachOrdered(userApp -> {
                    userAtrApplicationService.deleteById(userApp.getId());
                });
        applicationService.deleteById(application.getId());
        remove(application);
        addInformationMessage("Application with reference number:  ", Long.toString(application.getCaseNum()), ",  successfully deleted");
        reset().setList(true);

    }

    public void cancel(ATRApplication application) {

        if (application.getId() != null) {
            reset().setList(true);
        } else {
            remove(application);
            reset().setList(true);
        }
        applicationQuestions.clear();
        setTabIndex(0);
    }

    public void next(Integer index) {
        if (!getEntity().isTermsAndConditions()) {
            addWarningMessage("Please accept Terms and Conditions before you continue.");
            return;
        }
        applicationQuestions.stream().filter(response -> (response.getQuestion().getDescription().equalsIgnoreCase("16. Is the Applicant a SMME (Small, Medium and Micro Enterprise)?"))).forEachOrdered(response -> {
            naturalPerson = response.getResponseOption().toString();
        });
        setTabIndex(index);
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

    public void back(Integer index) {
        setTabIndex(index);
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

    public List<RepresentativeType> getRepresentativeTypes() {
        return representativeTypes;
    }

    public void setRepresentativeTypes(List<RepresentativeType> representativeTypes) {
        this.representativeTypes = representativeTypes;
    }

    public List<ClassDescription> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<ClassDescription> descriptions) {
        this.descriptions = descriptions;
    }

    public List<EntityType> getEntityTypes() {
        return entityTypes;
    }

    public void setEntityTypes(List<EntityType> entityTypes) {
        this.entityTypes = entityTypes;
    }

    public User getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(User userDetail) {
        this.userDetail = userDetail;
    }

    public Integer getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(Integer tabIndex) {
        this.tabIndex = tabIndex;
    }

    public boolean isSubQuestion(String question) {
        if (question == null || question.trim().isEmpty()) {
            return false;
        }

        char firstChar = question.trim().charAt(0);
        return Character.isLetter(firstChar);
    }

    public String getNaturalPerson() {
        return naturalPerson;
    }

    public void setNaturalPerson(String naturalPerson) {
        this.naturalPerson = naturalPerson;
    }

    public List<YesNo> getSaResidents() {
        return saResidents;
    }

    public void setSaResidents(List<YesNo> saResidents) {
        this.saResidents = saResidents;
    }

    public List<Province> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<Province> provinces) {
        this.provinces = provinces;
    }

}
