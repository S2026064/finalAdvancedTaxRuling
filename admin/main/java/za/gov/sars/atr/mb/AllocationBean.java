package za.gov.sars.atr.mb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import za.gov.sars.common.ApplicationType;
import za.gov.sars.common.ClassDescription;
import za.gov.sars.common.CloseType;
import za.gov.sars.common.EntityType;
import za.gov.sars.common.NotificationType;
import za.gov.sars.common.Province;
import za.gov.sars.common.RepresentativeType;
import za.gov.sars.common.ResourceType;
import za.gov.sars.common.ResponseOption;
import za.gov.sars.common.RulingType;
import za.gov.sars.common.Status;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.AtrApplicationQuestion;
import za.gov.sars.domain.User;
import za.gov.sars.domain.UserAtrApplication;
import za.gov.sars.service.AtrApplicationQuestionServiceLocal;
import za.gov.sars.service.AtrApplicationServiceLocal;
import za.gov.sars.service.EmailNotificationSenderServiceLocal;
import za.gov.sars.service.UserAtrApplicationServiceLocal;
import za.gov.sars.service.UserServiceLocal;

@ManagedBean
@ViewScoped
public class AllocationBean extends BaseBean<ATRApplication> {

    @Autowired
    private AtrApplicationServiceLocal atrApplicationService;
    @Autowired
    private UserAtrApplicationServiceLocal userAtrApplicationService;
    @Autowired
    private AtrApplicationQuestionServiceLocal atrApplicationQuestionService;
    @Autowired
    private UserServiceLocal userService;
    @Autowired
    private EmailNotificationSenderServiceLocal emailNotificationSenderService;

    // Core fields
    private Slice<ATRApplication> atrApplications;
    private ATRApplication selectedApplication;
    private User selectedManager;
    private User selectedPrimaryResource;
    private DualListModel<User> additionalResourcesPickList;
    private String currentUserRole;
    private Integer tabIndex = 0;

    // UI Lists
    private List<SelectItem> availableManagers = new ArrayList<>();
    private List<SelectItem> availablePrimaryResources = new ArrayList<>();

    // Application data
    private List<AtrApplicationQuestion> applicationQuestions = new ArrayList<>();
    private UserAtrApplication userAtrApplication;
    private User userDetail;
    private String naturalPerson;

    // Dropdown data - initialized once
    private final List<RulingType> rulingTypes = Arrays.asList(RulingType.values());
    private final List<ApplicationType> applicationTypes = Arrays.asList(ApplicationType.values());
    private final List<EntityType> entityTypes = Arrays.asList(EntityType.values());
    private final List<Province> provinces = Arrays.asList(Province.values());
    private final List<CloseType> saResidents = Arrays.asList(CloseType.values());
    private final List<ClassDescription> descriptions = Arrays.asList(ClassDescription.values());
    private final List<RepresentativeType> representativeTypes = Arrays.asList(RepresentativeType.values());
    private final List<SelectItem> responsesList = Arrays.stream(ResponseOption.values())
            .map(option -> new SelectItem(option, option.toString()))
            .collect(Collectors.toList());

    @PostConstruct
    public void init() {
        reset().setList(true);
        currentUserRole = loadCurrentUserRole();
        loadApplications();
    }

    private String loadCurrentUserRole() {
        return getActiveUser() != null && getActiveUser().getUser() != null
                && getActiveUser().getUser().getUserRole() != null
                ? getActiveUser().getUser().getUserRole().getDescription().toLowerCase()
                : "";
    }

    private void loadApplications() {
        Pageable pageable = PageRequest.of(0, 20);
        //loading this status will also need more status because we can add or change uses at any stage

        try {
            if ("administrator".equals(currentUserRole)) {
                atrApplications = atrApplicationService.findByStatusNotIn(Arrays.asList(Status.RULING_PUBLISHED_PENDING_OUTSTANDING_AMOUNT), pageable);
            }
            if ("manager".equals(currentUserRole)) {
                atrApplications = atrApplicationService.findByStatusNotInAndUserAtrApplication(Arrays.asList(Status.READY_FOR_ALLOCATION, Status.RULING_PUBLISHED_PENDING_OUTSTANDING_AMOUNT), getActiveUser().getUser(), pageable);
            }

            if (atrApplications != null) {
                addCollections(atrApplications.toList());
            }
        } catch (Exception e) {
            addErrorMessage("Error loading applications: " + e.getMessage());
        }
    }

    public void viewApplicationDetails(ATRApplication application) {
        if (application == null) {
            addErrorMessage("Application not found or invalid.");
            return;
        }

        this.selectedApplication = application;
        loadExistingAllocations();
        loadAvailableUsers();
        loadApplicationData();

        reset().setAdd(true);
        setTabIndex(4);
        addEntity(application);
    }

    private void loadExistingAllocations() {
        // Reset single selections
        selectedManager = null;
        selectedPrimaryResource = null;

        // Load existing single allocations (manager and primary resource)
        userAtrApplicationService.findByAtrApplication(selectedApplication)
                .forEach(allocation -> {
                    if (ResourceType.MANAGER.equals(allocation.getResourceType())) {
                        selectedManager = allocation.getUser(); // Single manager
                    } else if (ResourceType.PRIMARY_RESOURCE.equals(allocation.getResourceType())) {
                        selectedPrimaryResource = allocation.getUser(); // Single primary resource
                    }
                    // Secondary resources will be loaded in setupAdditionalResourcesPickList()
                });
    }

    private void loadAvailableUsers() {
        List<User> allUsers = getAllUsers();
        availableManagers.clear();
        availablePrimaryResources.clear();

        if ("administrator".equals(currentUserRole)) {
            setupAdminDropdowns(allUsers);
        } else if ("manager".equals(currentUserRole)) {
            setupManagerDropdowns(allUsers);
        }

        setupAdditionalResourcesPickList(allUsers);
    }

    private List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        try {
            Stream.of("Administrator", "Manager", "Resource")
                    .forEach(role -> {
                        List<User> users = userService.findUserByUserRoleDescription(role);
                        if (users != null) {
                            allUsers.addAll(users);
                        }
                    });
        } catch (Exception e) {
            addErrorMessage("Error loading users: " + e.getMessage());
        }
        return allUsers;
    }

    private void setupAdminDropdowns(List<User> allUsers) {
        // Managers
        allUsers.stream()
                .filter(user -> "Manager".equalsIgnoreCase(user.getUserRole().getDescription()))
                .forEach(user -> availableManagers.add(new SelectItem(user, user.getFullName())));

        // Primary resources (exclude selected manager)
        allUsers.stream()
                .filter(user -> selectedManager == null || !user.equals(selectedManager))
                .forEach(user -> availablePrimaryResources.add(new SelectItem(user,
                user.getFullName() + " (" + user.getUserRole().getDescription() + ")")));
    }

    private void setupManagerDropdowns(List<User> allUsers) {
        User currentUser = getActiveUser().getUser();
        allUsers.stream()
                .filter(user -> !user.equals(currentUser))
                .forEach(user -> availablePrimaryResources.add(new SelectItem(user,
                user.getFullName() + " (" + user.getUserRole().getDescription() + ")")));
    }

    private void setupAdditionalResourcesPickList(List<User> allUsers) {
        // Available users (exclude the single manager and single primary resource)
        List<User> sourceUsers = allUsers.stream()
                .filter(user -> !user.equals(selectedManager) && !user.equals(selectedPrimaryResource))
                .collect(Collectors.toList());

        // Currently selected secondary resources (list)
        List<User> targetUsers = userAtrApplicationService.findByAtrApplication(selectedApplication).stream()
                .filter(allocation -> ResourceType.SECONDARY_RESOURCE.equals(allocation.getResourceType()))
                .map(UserAtrApplication::getUser)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Remove already selected secondary resources from available list
        sourceUsers.removeAll(targetUsers);
        additionalResourcesPickList = new DualListModel<>(sourceUsers, targetUsers);
    }

    private void loadApplicationData() {
        applicationQuestions = atrApplicationQuestionService.findByAtrApplication(selectedApplication);
        if (applicationQuestions == null) {
            applicationQuestions = new ArrayList<>();
        }

        userAtrApplication = userAtrApplicationService.findByAtrApplicationAndResourceType(
                selectedApplication, ResourceType.APPLICANT);
        userDetail = userAtrApplication != null ? userAtrApplication.getUser() : null;

        // Load natural person info
        naturalPerson = applicationQuestions.stream()
                .filter(response -> response.getQuestion() != null
                && "16. Is the Applicant a SMME (Small, Medium and Micro Enterprise)?"
                        .equalsIgnoreCase(response.getQuestion().getDescription()))
                .findFirst()
                .map(response -> response.getResponseOption() != null ? response.getResponseOption().toString() : null)
                .orElse(null);
    }

    public void onManagerSelection() {
        loadAvailableUsers();
    }

    public void onPrimaryResourceSelection() {
        if (additionalResourcesPickList != null) {
            setupAdditionalResourcesPickList(getAllUsers());
        }
    }

    public void allocateApplication() {
        if (!validateAllocation()) {
            return;
        }

        if ("manager".equals(currentUserRole)) {
            selectedManager = getActiveUser().getUser();
        }

        ensureCollectionInitialized();
        processAllocations();
        updateApplicationStatus();
        saveApplication();
          sendNotifications();

        addInformationMessage("Allocation updated successfully");
        init();
    }

    private boolean validateAllocation() {
        if ("administrator".equals(currentUserRole) && selectedManager == null) {
            addErrorMessage("Please select a manager. Manager selection is required.");
            return false;
        }
        return true;
    }

    private void ensureCollectionInitialized() {
        if (selectedApplication.getUserAtrApplications() == null) {
            selectedApplication.setUserAtrApplications(new ArrayList<>());
        }
    }

    private void processAllocations() {
        if ("administrator".equals(currentUserRole)) {
            // Process single manager allocation
            processResourceAllocation(selectedManager, ResourceType.MANAGER);
            // Process single primary resource allocation
            processResourceAllocation(selectedPrimaryResource, ResourceType.PRIMARY_RESOURCE);
        } else if ("manager".equals(currentUserRole)) {
            // Process single primary resource allocation
            processResourceAllocation(selectedPrimaryResource, ResourceType.PRIMARY_RESOURCE);
        }

        // Process list of secondary resources
        processAdditionalResources();
    }

    private void processResourceAllocation(User user, ResourceType resourceType) {
        if (user != null) {
            saveOrUpdateAllocation(user, resourceType);
        } else {
            clearAllocation(resourceType);
        }
    }

    private void saveOrUpdateAllocation(User user, ResourceType resourceType) {
        UserAtrApplication allocation = userAtrApplicationService
                .findByAtrApplicationAndResourceType(selectedApplication, resourceType);

        if (allocation != null) {
            allocation.setUser(user);
            allocation.setUpdatedBy(getActiveUser().getUser().getFullName());
            allocation.setUpdatedDate(new Date());
        } else {
            allocation = createNewAllocation(user, resourceType);
        }

        userAtrApplicationService.save(allocation);
        updateApplicationCollection(allocation, resourceType);
    }

    private UserAtrApplication createNewAllocation(User user, ResourceType resourceType) {
        UserAtrApplication allocation = new UserAtrApplication();
        allocation.setUser(user);
        allocation.setAtrApplication(selectedApplication);
        allocation.setResourceType(resourceType);
        allocation.setCreatedBy(getActiveUser().getUser().getFullName());
        allocation.setCreatedDate(new Date());
        return allocation;
    }

    private void updateApplicationCollection(UserAtrApplication allocation, ResourceType resourceType) {
        selectedApplication.getUserAtrApplications().removeIf(
                existing -> resourceType.equals(existing.getResourceType()));
        selectedApplication.getUserAtrApplications().add(allocation);
    }

    private void clearAllocation(ResourceType resourceType) {
        UserAtrApplication existing = userAtrApplicationService
                .findByAtrApplicationAndResourceType(selectedApplication, resourceType);
        if (existing != null && existing.getId() != null) {
            selectedApplication.getUserAtrApplications().removeIf(
                    allocation -> resourceType.equals(allocation.getResourceType()));
            userAtrApplicationService.deleteById(existing.getId());
        }
    }

    private void processAdditionalResources() {
        // Get list of selected secondary resources from pick list
        List<User> selectedSecondaryResources = additionalResourcesPickList != null
                ? additionalResourcesPickList.getTarget() : new ArrayList<>();

        // Remove unselected secondary resource allocations
        userAtrApplicationService.findByAtrApplication(selectedApplication).stream()
                .filter(allocation -> ResourceType.SECONDARY_RESOURCE.equals(allocation.getResourceType()))
                .filter(allocation -> !shouldKeepSecondaryAllocation(allocation, selectedSecondaryResources))
                .forEach(allocation -> userAtrApplicationService.deleteById(allocation.getId()));

        // Add new secondary resource allocations
        selectedSecondaryResources.stream()
                .filter(user -> !isUserAlreadyAllocatedAsPrimaryOrManager(user))
                .filter(user -> !isUserAlreadyAllocatedAsSecondary(user))
                .forEach(user -> userAtrApplicationService.save(createNewAllocation(user, ResourceType.SECONDARY_RESOURCE)));
    }

    private boolean shouldKeepSecondaryAllocation(UserAtrApplication allocation, List<User> selectedSecondaryUsers) {
        return allocation.getUser() != null && selectedSecondaryUsers.stream()
                .anyMatch(user -> user.getId().equals(allocation.getUser().getId()));
    }

    private boolean isUserAlreadyAllocatedAsPrimaryOrManager(User user) {
        return user.equals(selectedManager) || user.equals(selectedPrimaryResource);
    }

    private boolean isUserAlreadyAllocatedAsSecondary(User user) {
        return userAtrApplicationService.findByAtrApplication(selectedApplication).stream()
                .filter(allocation -> ResourceType.SECONDARY_RESOURCE.equals(allocation.getResourceType()))
                .anyMatch(allocation -> allocation.getUser() != null && allocation.getUser().getId().equals(user.getId()));
    }

    private void updateApplicationStatus() {
        if ("administrator".equals(currentUserRole) && selectedManager != null
                && selectedApplication.getStatus() == Status.READY_FOR_ALLOCATION) {
            selectedApplication.setStatus(Status.ALLOCATED_TO_MANAGER);
        } else if ("manager".equals(currentUserRole)
                && selectedApplication.getStatus() == Status.ALLOCATED_TO_MANAGER) {
            selectedApplication.setStatus(Status.ASSIGN_ACTIVITIES);
        }
    }

    private void saveApplication() {
        selectedApplication.setUpdatedBy(getActiveUser().getUser().getFullName());
        selectedApplication.setUpdatedDate(new Date());
        atrApplicationService.save(selectedApplication);
    }

    private void sendNotifications() {
        // Notify single manager (if allocated by administrator)
        if ("administrator".equalsIgnoreCase(currentUserRole) && selectedManager != null) {
            emailNotificationSenderService.sendEmailNotification(NotificationType.APPLICATION_ALLOCATED_MANAGER,
                    selectedApplication);
        }

        // Notify single primary resource (if allocated)
        if (selectedPrimaryResource != null) {
            emailNotificationSenderService.sendEmailNotification(NotificationType.APPLICATION_ALLOCATED_RESOURCE,
                    selectedApplication);
        }

        // Notify list of secondary resources (if any)
//        if (additionalResourcesPickList != null && additionalResourcesPickList.getTarget() != null) {
//            additionalResourcesPickList.getTarget().forEach(secondaryResource
//                    -> emailNotificationSenderService.sendEmailNotification(NotificationType.APPLICATION_ALLOCATED_MANAGER,
//                            Long.toString(selectedApplication.getCaseNum()),
//                            selectedApplication.getApplicantName(),
//                            selectedApplication.getUpdatedDate(),
//                            secondaryResource
//                    )
//            );
//        }
    }

    public void cancel() {
        selectedManager = null;
        selectedPrimaryResource = null;
        if (additionalResourcesPickList != null) {
            additionalResourcesPickList = new DualListModel<>(new ArrayList<>(), new ArrayList<>());
        }
        reset().setList(true);
    }

    public boolean isSubQuestion(String question) {
        return question != null && !question.trim().isEmpty()
                && Character.isLetter(question.trim().charAt(0));
    }

    public Boolean companyQuestionSelectedYes(String description) {
        return applicationQuestions.stream()
                .filter(res -> res.getQuestion().getDescription().equalsIgnoreCase(description))
                .findFirst()
                .map(question -> question.getResponseOption() != null
                && question.getResponseOption().equals(ResponseOption.YES))
                .orElse(false);
    }

    // Getters and Setters - Essential ones only
    public Slice<ATRApplication> getAtrApplications() {
        return atrApplications;
    }

    public void setAtrApplications(Slice<ATRApplication> atrApplications) {
        this.atrApplications = atrApplications;
    }

    public ATRApplication getSelectedApplication() {
        return selectedApplication;
    }

    public void setSelectedApplication(ATRApplication selectedApplication) {
        this.selectedApplication = selectedApplication;
    }

    public String getCurrentUserRole() {
        return currentUserRole;
    }

    public void setCurrentUserRole(String currentUserRole) {
        this.currentUserRole = currentUserRole;
    }

    public List<SelectItem> getAvailableManagers() {
        return availableManagers;
    }

    public void setAvailableManagers(List<SelectItem> availableManagers) {
        this.availableManagers = availableManagers;
    }

    public List<SelectItem> getAvailablePrimaryResources() {
        return availablePrimaryResources;
    }

    public void setAvailablePrimaryResources(List<SelectItem> availablePrimaryResources) {
        this.availablePrimaryResources = availablePrimaryResources;
    }

    public User getSelectedManager() {
        return selectedManager;
    }

    public void setSelectedManager(User selectedManager) {
        this.selectedManager = selectedManager;
    }

    public User getSelectedPrimaryResource() {
        return selectedPrimaryResource;
    }

    public void setSelectedPrimaryResource(User selectedPrimaryResource) {
        this.selectedPrimaryResource = selectedPrimaryResource;
    }

    public DualListModel<User> getAdditionalResourcesPickList() {
        return additionalResourcesPickList;
    }

    public void setAdditionalResourcesPickList(DualListModel<User> additionalResourcesPickList) {
        this.additionalResourcesPickList = additionalResourcesPickList;
    }

    public Integer getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(Integer tabIndex) {
        this.tabIndex = tabIndex;
    }

    public List<AtrApplicationQuestion> getApplicationQuestions() {
        return applicationQuestions;
    }

    public void setApplicationQuestions(List<AtrApplicationQuestion> applicationQuestions) {
        this.applicationQuestions = applicationQuestions;
    }

    public UserAtrApplication getUserAtrApplication() {
        return userAtrApplication;
    }

    public void setUserAtrApplication(UserAtrApplication userAtrApplication) {
        this.userAtrApplication = userAtrApplication;
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

    // Dropdown getters (no setters needed for final fields)
    public List<RulingType> getRulingTypes() {
        return rulingTypes;
    }

    public List<ApplicationType> getApplicationTypes() {
        return applicationTypes;
    }

    public List<EntityType> getEntityTypes() {
        return entityTypes;
    }

    public List<Province> getProvinces() {
        return provinces;
    }

    public List<CloseType> getSaResidents() {
        return saResidents;
    }

    public List<ClassDescription> getDescriptions() {
        return descriptions;
    }

    public List<RepresentativeType> getRepresentativeTypes() {
        return representativeTypes;
    }

    public List<SelectItem> getResponsesList() {
        return responsesList;
    }
}
