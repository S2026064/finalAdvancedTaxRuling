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
import javax.faces.model.SelectItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import za.gov.sars.common.ActivityCategory;
import za.gov.sars.common.ApplicationType;
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
import za.gov.sars.common.YesNo;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.AtrApplicationQuestion;
import za.gov.sars.domain.ChargeOutRateCategory;
import za.gov.sars.domain.EstimatePlan;
import za.gov.sars.domain.EstimateRecord;
import za.gov.sars.domain.FeedBack;
import za.gov.sars.domain.User;
import za.gov.sars.domain.UserAtrApplication;
import za.gov.sars.domain.service.ActivityServiceLocal;
import za.gov.sars.dto.EstimationPlanRevisionDTO;
import za.gov.sars.service.AtrApplicationQuestionServiceLocal;
import za.gov.sars.service.AtrApplicationServiceLocal;
import za.gov.sars.service.ChargeOutRateCategoryServiceLocal;
import za.gov.sars.service.EmailNotificationSenderServiceLocal;
import za.gov.sars.service.FeedBackServiceLocal;
import za.gov.sars.service.UserApplicationActivityServiceLocal;
import za.gov.sars.service.UserAtrApplicationServiceLocal;
import za.gov.sars.service.UserServiceLocal;

@ManagedBean
@ViewScoped
public class EstimationPublishedBean extends BaseBean<ATRApplication> {

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
    private EmailNotificationSenderServiceLocal emailNotificationSenderService;

    private List<EstimatePlan> estimatePlans = new ArrayList<>();
    private List<ResourceType> resourceTypes = new ArrayList<>();
    private Slice<ATRApplication> atrApplications;
    private List<ChargeOutRateCategory> categories;
    private List<Province> provinces = new ArrayList<>();
    private List<RulingType> rulingTypes = new ArrayList<>();
    private List<SelectItem> responsesList = new ArrayList<>();
    private List<AtrApplicationQuestion> applicationQuestions = new ArrayList<>();
    private List<ClassDescription> descriptions = new ArrayList<>();
    private List<YesNo> saResidents = new ArrayList<>();
    private List<RepresentativeType> representativeTypes = new ArrayList<>();
    private List< ApplicationType> applicationTypes = new ArrayList<>();
    private List< EntityType> entityTypes = new ArrayList<>();

    private User allocatedManager;
    private User allocatedResource;
    private String selectedComplexityLevel;
    private ChargeOutRateCategory category;
    private List<EstimationPlanRevisionDTO> revisions;
    private EstimatePlan selectedPlan;
    private EstimatePlan selectedRevision;
    private User userDetail;
    private UserAtrApplication userApplication;
    private Long selectedRevisionNumber;
    private double subtotal = 0.0;
    private double travelExpense = 0.0;
    private double externalResource = 0.0;
    private double grandTotal = 0.0;
    private double depositAmount = 0.0;
    private List<Status> statuses = new ArrayList<>();
    private Integer tabIndex = 0;
    private String naturalPerson;
    private boolean letterConditions;
    private EstimatePlan currentPlan;

    @PostConstruct
    public void init() {
        reset().setList(true);

        resourceTypes.addAll(Arrays.asList(ResourceType.values()));
        saResidents.addAll(Arrays.asList(YesNo.values()));
        descriptions.addAll(Arrays.asList(ClassDescription.values()));
        representativeTypes.addAll(Arrays.asList(RepresentativeType.values()));
        provinces.addAll(Arrays.asList(Province.values()));
        applicationTypes.addAll(Arrays.asList(ApplicationType.values()));
        rulingTypes.addAll(Arrays.asList(RulingType.values()));
        statuses.add(Status.ESTIMATE_PUBLISHED);
        categories = chargeOutRateCategoryService.findAll();
        Arrays.asList(ResponseOption.values()).stream()
                .forEach(responseOption -> {
                    responsesList.add(new SelectItem(responseOption, responseOption.toString()));
                });
        loadUserApplications();

    }

    public Map<ActivityCategory, List<EstimateRecord>> getItemsGroupedByCategory(EstimatePlan plan) {
        return plan.getEstimateRecords().stream()
                .collect(Collectors.groupingBy(
                        EstimateRecord::getActivityCategory,
                        TreeMap::new, // Sorts by category enum order
                        Collectors.toList()
                ));
    }

    public void loadUserApplications() {
        Pageable pageable = PageRequest.of(0, 10);
        atrApplications = userAtrApplicationService.findAppByUserResourceTypeAndAppStatusIn(getActiveUser().getUser(), ResourceType.APPLICANT, statuses, pageable);
        if (atrApplications != null && !atrApplications.isEmpty()) {
            addCollections(atrApplications.toList());
        }
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
            currentPlan = getCurrentEstimatePlan(atrApplication);

//            this.selectedApplication = atrApplication1;
//            this.entity = atrApplication1;
            if (getCurrentEstimatePlan(atrApplication).getChargeOutRateCategory() != null) {
                this.category = getCurrentEstimatePlan(atrApplication).getChargeOutRateCategory();
            }

        } catch (Exception e) {
            addErrorMessage("Error loading application for review: " + e.getMessage());
            //logger.error("Error in reviewEstimatePlan", e);
        }
        addEntity(atrApplication);
    }

    public void accept(ATRApplication atrApplication) {

        if (!getCurrentEstimatePlan(atrApplication).isLetterConditions()) {
            addErrorMessage("Please read and sign engagement Letter");
            return;
        }
        atrApplication.setStatus(Status.ESTIMATE_ACCEPTED_PENDING_DOCUMENTATION);
        getCurrentEstimatePlan(atrApplication).setStatus(EstimatePlanStatus.ESTIMATE_ACCEPTED);

        getCurrentEstimatePlan(atrApplication).setUpdatedBy(getActiveUser().getSid());
        getCurrentEstimatePlan(atrApplication).setUpdatedDate(new Date());

        atrApplicationService.update(atrApplication);
        addInformationMessage("Estimate plan accepted successfully");
        userAtrApplicationService.findByAtrApplication(atrApplication).stream().filter(app -> (app.getResourceType().equals(ResourceType.APPLICANT))).forEachOrdered(app -> {
            emailNotificationSenderService.sendEmailNotification(NotificationType.ESTIMATION_ACCEPTED_PENDING_DOCUMENTATION, atrApplication);
        });
        reset().setList(true);

    }

    public void reject(ATRApplication atrApplication) {
        try {

            if (atrApplication.getStatus() != Status.ESTIMATE_PUBLISHED) {
                addErrorMessage("Application is not in a state that can be rejected");
                return;
            }

            if (getCurrentEstimatePlan(atrApplication) == null) {
                addErrorMessage("No estimate plan found for rejection");
                return;
            }

            atrApplication.setStatus(Status.ESTIMATE_REJECTED);
            getCurrentEstimatePlan(atrApplication).setStatus(EstimatePlanStatus.ESTIMATE_NOT_ACCEPTED);

            getCurrentEstimatePlan(atrApplication).setUpdatedBy(getActiveUser().getIdNumber());
            getCurrentEstimatePlan(atrApplication).setUpdatedDate(new Date());

            addInformationMessage("Estimate plan rejected successfully");
            userAtrApplicationService.findByAtrApplication(atrApplication).stream().filter(app -> (app.getResourceType().equals(ResourceType.APPLICANT))).forEachOrdered(app -> {
                emailNotificationSenderService.sendEmailNotification(NotificationType.ESTIMATION_REJECTED, atrApplication);
            });
            reset().setList(true);
        } catch (Exception e) {
            addErrorMessage("Error rejecting estimate plan: " + e.getMessage());
        }
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

    public void cancel() {
        reset().setList(true);
    }

    public List<ResourceType> getResourceTypes() {
        return resourceTypes;
    }

    public void setResourceTypes(List<ResourceType> resourceTypes) {
        this.resourceTypes = resourceTypes;
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

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTravelExpense() {
        return travelExpense;
    }

    public void setTravelExpense(double travelExpense) {
        this.travelExpense = travelExpense;
    }

    public double getExternalResource() {
        return externalResource;
    }

    public void setExternalResource(double externalResource) {
        this.externalResource = externalResource;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public double getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(double depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getSelectedComplexityLevel() {
        return selectedComplexityLevel;
    }

    public void setSelectedComplexityLevel(String selectedComplexityLevel) {
        this.selectedComplexityLevel = selectedComplexityLevel;
    }

    public List<ChargeOutRateCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<ChargeOutRateCategory> categories) {
        this.categories = categories;
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

    public List<EstimatePlan> getCurrentEstimatePlans() {
        return estimatePlans;
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

    public List<Province> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<Province> provinces) {
        this.provinces = provinces;
    }

    public List<RulingType> getRulingTypes() {
        return rulingTypes;
    }

    public void setRulingTypes(List<RulingType> rulingTypes) {
        this.rulingTypes = rulingTypes;
    }

    public List<SelectItem> getResponsesList() {
        return responsesList;
    }

    public void setResponsesList(List<SelectItem> responsesList) {
        this.responsesList = responsesList;
    }

    public List<AtrApplicationQuestion> getApplicationQuestions() {
        return applicationQuestions;
    }

    public void setApplicationQuestions(List<AtrApplicationQuestion> applicationQuestions) {
        this.applicationQuestions = applicationQuestions;
    }

    public List<ClassDescription> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(List<ClassDescription> descriptions) {
        this.descriptions = descriptions;
    }

    public List<YesNo> getSaResidents() {
        return saResidents;
    }

    public void setSaResidents(List<YesNo> saResidents) {
        this.saResidents = saResidents;
    }

    public List<RepresentativeType> getRepresentativeTypes() {
        return representativeTypes;
    }

    public void setRepresentativeTypes(List<RepresentativeType> representativeTypes) {
        this.representativeTypes = representativeTypes;
    }

    public List<EstimationPlanRevisionDTO> getRevisions() {
        return revisions;
    }

    public void setRevisions(List<EstimationPlanRevisionDTO> revisions) {
        this.revisions = revisions;
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

    public List<ApplicationType> getApplicationTypes() {
        return applicationTypes;
    }

    public void setApplicationTypes(List<ApplicationType> applicationTypes) {
        this.applicationTypes = applicationTypes;
    }

    public boolean isLetterConditions() {
        return letterConditions;
    }

    public void setLetterConditions(boolean letterConditions) {
        this.letterConditions = letterConditions;
    }

    public List<EntityType> getEntityTypes() {
        return entityTypes;
    }

    public void setEntityTypes(List<EntityType> entityTypes) {
        this.entityTypes = entityTypes;
    }

    public EstimatePlan getCurrentPlan() {
        return currentPlan;
    }

    public void setCurrentPlan(EstimatePlan currentPlan) {
        this.currentPlan = currentPlan;
    }

    public List<EstimatePlan> getEstimatePlans() {
        return estimatePlans;
    }

    public void setEstimatePlans(List<EstimatePlan> estimatePlans) {
        this.estimatePlans = estimatePlans;
    }

}
