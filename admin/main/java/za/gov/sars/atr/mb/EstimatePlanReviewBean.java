package za.gov.sars.atr.mb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import za.gov.sars.common.ActivityCategory;
import za.gov.sars.common.ApplicationType;
import za.gov.sars.common.ChargeCategory;
import za.gov.sars.common.ClassDescription;
import za.gov.sars.common.EntityType;
import za.gov.sars.common.EstimatePlanStatus;
import za.gov.sars.common.NotificationType;
import za.gov.sars.common.Province;
import za.gov.sars.common.RepresentativeType;
import za.gov.sars.common.ResourceType;
import za.gov.sars.common.ResponseOption;
import za.gov.sars.common.RulingType;
import za.gov.sars.common.Status;
import za.gov.sars.common.CloseType;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.Activity;
import za.gov.sars.domain.AtrApplicationQuestion;
import za.gov.sars.domain.ChargeOutRateCategory;
import za.gov.sars.domain.EstimatePlan;
import za.gov.sars.domain.EstimateRecord;
import za.gov.sars.domain.FeedBack;
import za.gov.sars.domain.User;
import za.gov.sars.domain.UserApplicationActivity;
import za.gov.sars.domain.UserAtrApplication;
import za.gov.sars.domain.service.ActivityServiceLocal;
import za.gov.sars.dto.EstimationPlanRevisionDTO;
import za.gov.sars.service.AtrApplicationQuestionServiceLocal;
import za.gov.sars.service.AtrApplicationServiceLocal;
import za.gov.sars.service.ChargeOutRateCategoryServiceLocal;
import za.gov.sars.service.EmailNotificationSenderServiceLocal;
import za.gov.sars.service.EstimatePlanServiceLocal;
import za.gov.sars.service.FeedBackServiceLocal;
import za.gov.sars.service.UserApplicationActivityServiceLocal;
import za.gov.sars.service.UserAtrApplicationServiceLocal;
import za.gov.sars.service.UserServiceLocal;

@ManagedBean
@ViewScoped
public class EstimatePlanReviewBean extends BaseBean<ATRApplication> {

    @Autowired
    private AtrApplicationServiceLocal atrApplicationService;
    @Autowired
    private UserAtrApplicationServiceLocal userAtrApplicationService;
    @Autowired
    private UserServiceLocal userService;
    @Autowired
    private ChargeOutRateCategoryServiceLocal chargeOutRateCategoryService;
    @Autowired
    private ActivityServiceLocal activityService;
    @Autowired
    private UserApplicationActivityServiceLocal userApplicationActivityService;
    @Autowired
    private FeedBackServiceLocal feedBackService;
    @Autowired
    private AtrApplicationQuestionServiceLocal atrApplicationQuestionService;
    @Autowired
    private EstimatePlanServiceLocal estimatePlanService;
    @Autowired
    private EmailNotificationSenderServiceLocal emailNotificationSenderService;
    private List<EstimatePlan> estimatePlans = new ArrayList<>();
    private List<ChargeOutRateCategory> chargeOutRateCategories;
    private List<Province> provinces = new ArrayList<>();
    private List<RulingType> rulingTypes = new ArrayList<>();
    private List<SelectItem> responsesList = new ArrayList<>();
    private List<AtrApplicationQuestion> applicationQuestions = new ArrayList<>();
    private List<ClassDescription> descriptions = new ArrayList<>();
    private List<CloseType> saResidents = new ArrayList<>();
    private List<RepresentativeType> representativeTypes = new ArrayList<>();
    private List<ResourceType> resourceTypes = new ArrayList<>();
    private Slice<ATRApplication> atrApplications;

    private List<ChargeCategory> chargeCategories = new ArrayList<>();
    private List<UserApplicationActivity> userApplicationActivities = new ArrayList<>();
    private List< ApplicationType> applicationTypes = new ArrayList<>();
    private List<Activity> activities = new ArrayList<>();
    private List< EntityType> entityTypes = new ArrayList<>();

    private User userDetail;
    private User allocatedManager;
    private UserAtrApplication userApplication;
    private User allocatedResource;
    private String selectedComplexityLevel;
    private ChargeOutRateCategory category;
    private EstimatePlan selectedPlan;
    private EstimatePlan selectedRevision;
    private Long selectedRevisionNumber;
    private String naturalPerson;
    private boolean isUrgentOrExtraordinary;
    private List<Status> statuses = new ArrayList<>();
    private Integer tabIndex = 0;

    @PostConstruct

    public void init() {
        reset().setList(true);
        saResidents.addAll(Arrays.asList(CloseType.values()));
        descriptions.addAll(Arrays.asList(ClassDescription.values()));
        representativeTypes.addAll(Arrays.asList(RepresentativeType.values()));
        provinces.addAll(Arrays.asList(Province.values()));
        applicationTypes.addAll(Arrays.asList(ApplicationType.values()));
        rulingTypes.addAll(Arrays.asList(RulingType.values()));
        resourceTypes.addAll(Arrays.asList(ResourceType.values()));
        statuses.add(Status.ESTIMATE_REQUIRES_AUTHORISATION);
        Arrays.asList(ResponseOption.values()).stream()
                .forEach(responseOption -> {
                    responsesList.add(new SelectItem(responseOption, responseOption.toString()));
                });
        loadUserApplications();

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

    public void loadUserApplications() {
        Pageable pageable = PageRequest.of(0, 10);
        atrApplications = atrApplicationService.findByStatusAndAllocatedManagerOrAssignedUser(statuses, getActiveUser().getUser(), pageable);

        if (atrApplications != null && !atrApplications.isEmpty()) {
            addCollections(atrApplications.toList());
        }
    }

    public void cancel() {
        reset().setList(true);
    }

    public void reviewEstimatePlan(ATRApplication atrApplication) {
        try {
            reset().setAdd(true);
            applicationQuestions = atrApplicationQuestionService.findByAtrApplication(atrApplication);
            userApplication = userAtrApplicationService.findUsersByAtrApplicationIdAndResourceType(atrApplication, ResourceType.APPLICANT);
            userDetail = userApplication.getUser();
            applicationQuestions.stream().filter(response -> (response.getQuestion().getDescription().equalsIgnoreCase("16. Is the Applicant a SMME (Small, Medium and Micro Enterprise)?"))).forEachOrdered(response -> {
                naturalPerson = response.getResponseOption().toString();
            });

            List<FeedBack> unreadFeedbacks = feedBackService.findUnreadByApplicant(atrApplication);

            for (FeedBack feedback : unreadFeedbacks) {
                feedback.setUnreadByApplicant(false);
                feedBackService.save(feedback);
            }

        } catch (Exception e) {
            addErrorMessage("Error loading application for review: " + e.getMessage());
            //logger.error("Error in reviewEstimatePlan", e);
        }
        addEntity(atrApplication);
    }

    public void review(ATRApplication atrApplication) {
        try {
            if (getCurrentEstimatePlan(atrApplication).getStatus().equals(EstimatePlanStatus.ESTIMATE_PUBLISHED)) {
                atrApplication.setStatus(Status.ESTIMATE_PUBLISHED);
                ATRApplication savedApplication = atrApplicationService.update(atrApplication);
                removeEntity(atrApplication);
                addFreshEntityAndSynchView(savedApplication);

                addInformationMessage("Estimate plan approved successfully");
                userAtrApplicationService.findByAtrApplication(savedApplication).stream().filter(app -> (app.getResourceType().equals(ResourceType.APPLICANT))).forEachOrdered(app -> {
                    emailNotificationSenderService.sendEmailNotification(NotificationType.ESTIMATION_INPROGRESS, savedApplication);
                });
            } else if (getCurrentEstimatePlan(atrApplication).getStatus().equals(EstimatePlanStatus.ESTIMATE_REJECTED)) {
                atrApplication.setStatus(Status.MANAGER_ESTIMATION_REJECTED);
                ATRApplication savedApplication = atrApplicationService.update(atrApplication);
                removeEntity(atrApplication);
                addFreshEntityAndSynchView(savedApplication);
                addInformationMessage("Estimate plan rejected successfully");
                userAtrApplicationService.findByAtrApplication(savedApplication).stream().filter(app -> (app.getResourceType().equals(ResourceType.PRIMARY_RESOURCE))).forEachOrdered(app -> {
                    emailNotificationSenderService.sendEmailNotification(NotificationType.MANAGER_ESTIMATION_REJECTED, savedApplication);
                });
            }

            reset().setList(true);
        } catch (Exception e) {
            addErrorMessage("Error approving estimate plan: " + e.getMessage());
        }
    }

    public void addOrUpdateEstimatePlan(EstimatePlan estimatePlan, String targetPage) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("estimateKey", estimatePlan);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("applicationKey", getEntity().getId());
        openDialogBox(targetPage, null);
    }

    public void handleUpdateEstimatePlanReturnListener(SelectEvent event) {
        EstimatePlan estimatePlan = (EstimatePlan) event.getObject();
        Integer index = getEntity().getEstimationPlans().indexOf(estimatePlan);
        getEntity().getEstimationPlans().set(index, estimatePlan);
    }

    public Slice<ATRApplication> getAtrApplications() {
        return atrApplications;
    }

    public User getAllocatedManager() {
        return allocatedManager;
    }

    public void setAllocatedManager(User allocatedManager) {
        this.allocatedManager = allocatedManager;
    }

    public User getAllocatedResource() {
        return allocatedResource;
    }

    public void setAllocatedResource(User allocatedResource) {
        this.allocatedResource = allocatedResource;
    }

    public String getSelectedComplexityLevel() {
        return selectedComplexityLevel;
    }

    public void setSelectedComplexityLevel(String selectedComplexityLevel) {
        this.selectedComplexityLevel = selectedComplexityLevel;
    }

    public ChargeOutRateCategory getCategory() {
        return category;
    }

    public void setCategory(ChargeOutRateCategory category) {
        this.category = category;
    }

    public Integer getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(Integer tabIndex) {
        this.tabIndex = tabIndex;
    }

    public List<EstimatePlan> getEstimatePlans() {
        return estimatePlans;
    }

    public void setEstimatePlans(List<EstimatePlan> estimatePlans) {
        this.estimatePlans = estimatePlans;
    }

    public EstimatePlan getSelectedPlan() {
        return selectedPlan;
    }

    public void setSelectedPlan(EstimatePlan selectedPlan) {
        this.selectedPlan = selectedPlan;

    }

    public void setSelectedRevision(EstimatePlan selectedRevision) {
        this.selectedRevision = selectedRevision;
    }

    public Long getSelectedRevisionNumber() {
        return selectedRevisionNumber;
    }

    public void setSelectedRevisionNumber(Long selectedRevisionNumber) {
        this.selectedRevisionNumber = selectedRevisionNumber;
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }

    public List<UserApplicationActivity> getUserApplicationActivities() {
        return userApplicationActivities;
    }

    public void setUserApplicationActivities(List<UserApplicationActivity> userApplicationActivities) {
        this.userApplicationActivities = userApplicationActivities;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<ChargeCategory> getChargeCategories() {
        return chargeCategories;
    }

    public void setChargeCategories(List<ChargeCategory> chargeCategories) {
        this.chargeCategories = chargeCategories;
    }

    public List<ChargeOutRateCategory> getChargeOutRateCategories() {
        return chargeOutRateCategories;
    }

    public void setChargeOutRateCategories(List<ChargeOutRateCategory> chargeOutRateCategories) {
        this.chargeOutRateCategories = chargeOutRateCategories;
    }

    public List<AtrApplicationQuestion> getApplicationQuestions() {
        return applicationQuestions;
    }

    public void setApplicationQuestions(List<AtrApplicationQuestion> applicationQuestions) {
        this.applicationQuestions = applicationQuestions;
    }

    public List<ResourceType> getResourceTypes() {
        return resourceTypes;
    }

    public void setResourceTypes(List<ResourceType> resourceTypes) {
        this.resourceTypes = resourceTypes;
    }

    public User getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(User userDetail) {
        this.userDetail = userDetail;
    }

    public UserAtrApplication getUserApplication() {
        return userApplication;
    }

    public void setUserApplication(UserAtrApplication userApplication) {
        this.userApplication = userApplication;
    }

    public String getNaturalPerson() {
        return naturalPerson;
    }

    public void setNaturalPerson(String naturalPerson) {
        this.naturalPerson = naturalPerson;
    }

    public List<SelectItem> getResponsesList() {
        return responsesList;
    }

    public void setResponsesList(List<SelectItem> responsesList) {
        this.responsesList = responsesList;
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

    public List<Province> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<Province> provinces) {
        this.provinces = provinces;
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

    public boolean isIsUrgentOrExtraordinary() {
        return isUrgentOrExtraordinary;
    }

    public void setIsUrgentOrExtraordinary(boolean isUrgentOrExtraordinary) {
        this.isUrgentOrExtraordinary = isUrgentOrExtraordinary;
    }

    public List<EntityType> getEntityTypes() {
        return entityTypes;
    }

    public void setEntityTypes(List<EntityType> entityTypes) {
        this.entityTypes = entityTypes;
    }

}
