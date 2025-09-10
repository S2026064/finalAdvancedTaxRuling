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
import za.gov.sars.common.RulingType;
import za.gov.sars.common.Status;
import za.gov.sars.common.ResourceType;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.User;
import za.gov.sars.domain.UserAtrApplication;
import za.gov.sars.service.AtrApplicationServiceLocal;
import za.gov.sars.service.UserAtrApplicationServiceLocal;
import za.gov.sars.service.UserServiceLocal;
import org.primefaces.model.DualListModel;

@ManagedBean
@ViewScoped
public class AdminDashBean extends BaseBean<ATRApplication> {

    @Autowired
    private AtrApplicationServiceLocal atrApplicationService;

    @Autowired
    private UserAtrApplicationServiceLocal userAtrApplicationService;

    @Autowired
    private UserServiceLocal userService;

    private List<Status> statuses = new ArrayList<>();
    private List<RulingType> rulingTypes = new ArrayList<>();
    private List<SelectItem> availableManagers = new ArrayList<>();
    private List<SelectItem> availablePrimaryResources = new ArrayList<>();
    private List<User> availableAdditionalResources = new ArrayList<>();

    private ATRApplication selectedApplication;
    private User selectedManager;
    private User selectedPrimaryResource;
    private DualListModel<User> additionalResourcesPickList;

    private Slice<ATRApplication> atrApplications;
    private String currentUserRole;

    @PostConstruct
    public void init() {
        reset().setList(true);
        loadUserRole();
        loadApplicationsBasedOnRole();
        initializeDropdowns();
        loadAvailableUsersForAllocation();
    }

    private void loadUserRole() {
        currentUserRole = getActiveUser().getUser().getUserRole().getDescription().toLowerCase();
    }

    private void loadApplicationsBasedOnRole() {
        Pageable pageable = PageRequest.of(0, 10);

        switch (currentUserRole) {
            case "administrator":
                atrApplications = atrApplicationService.findByStatusIn(
                        Arrays.asList(Status.SUBMITTED_PAYMENT_UNCONFIRMED, Status.SUBMITTED_AND_PAID, Status.READY_FOR_ALLOCATION),
                        pageable);
                break;
            case "manager":
                atrApplications = atrApplicationService.findByStatusAndAllocatedManagerOrAssignedUser(
                        Arrays.asList(Status.ALLOCATED_TO_MANAGER, Status.CASE_INPROGRESS, Status.ESTIMATE_INPROGRESS),
                        getActiveUser().getUser(), pageable);
                break;
            default:
                atrApplications = atrApplicationService.findByStatus(Status.PENDING_AUTHORISATION, pageable);
                break;
        }

        if (atrApplications != null) {
            addCollections(atrApplications.toList());
        }
    }

    private void loadAvailableUsersForAllocation() {
        availableManagers.clear();
        availablePrimaryResources.clear();
        availableAdditionalResources.clear();

        if ("administrator".equals(currentUserRole)) {
            List<User> managers = userService.findUserByUserRoleDescription("Manager");
            if (managers != null) {
                managers.forEach(manager
                        -> availableManagers.add(new SelectItem(manager, manager.getFullName())));
            }

            List<User> allUsers = new ArrayList<>();
            List<User> admins = userService.findUserByUserRoleDescription("Administrator");
            List<User> managerList = userService.findUserByUserRoleDescription("Manager");

            if (admins != null) {
                allUsers.addAll(admins);
            }
            if (managerList != null) {
                allUsers.addAll(managerList);
            }

            allUsers.forEach(user
                    -> availablePrimaryResources.add(new SelectItem(user,
                            user.getFullName() + " (" + user.getUserRole().getDescription() + ")")));

            availableAdditionalResources.addAll(allUsers);
        } else if ("manager".equals(currentUserRole)) {
            List<User> allUsers = new ArrayList<>();
            List<User> admins = userService.findUserByUserRoleDescription("Administrator");
            List<User> managers = userService.findUserByUserRoleDescription("Manager");

            if (admins != null) {
                allUsers.addAll(admins);
            }
            if (managers != null) {
                allUsers.addAll(managers);
            }

            allUsers.stream()
                    .filter(user -> !user.equals(getActiveUser().getUser()))
                    .forEach(user
                            -> availablePrimaryResources.add(new SelectItem(user,
                            user.getFullName() + " (" + user.getUserRole().getDescription() + ")")));

            availableAdditionalResources.addAll(allUsers);
            availableAdditionalResources.removeIf(user -> user.equals(getActiveUser().getUser()));
        }

        List<User> sourceUsers = new ArrayList<>(availableAdditionalResources);
        additionalResourcesPickList = new DualListModel<>(sourceUsers, new ArrayList<>());
    }

    private void initializeDropdowns() {
        statuses.addAll(Arrays.asList(Status.values()));
        rulingTypes.addAll(Arrays.asList(RulingType.values()));
    }

    public void viewApplicationDetails(ATRApplication application) {
        this.selectedApplication = application;
        clearSelections();
        loadAvailableUsersForAllocation();
        reset().setAdd(true);
    }

    public void allocateApplication() {
        try {
            if ("administrator".equals(currentUserRole) && selectedManager == null) {
                addErrorMessage("Please select a manager to allocate to.");
                return;
            }

            if (selectedPrimaryResource == null && (additionalResourcesPickList.getTarget() == null || additionalResourcesPickList.getTarget().isEmpty())) {
                addErrorMessage("Please select at least a primary resource or additional resources.");
                return;
            }

            if (selectedApplication.getUserAtrApplications() == null) {
                selectedApplication.setUserAtrApplications(new ArrayList<>());
            }

            if ("administrator".equals(currentUserRole)) {
                UserAtrApplication managerAllocation = buildUserAtrApplication(selectedManager, ResourceType.MANAGER);
                userAtrApplicationService.save(managerAllocation);
                selectedApplication.getUserAtrApplications().add(managerAllocation);
                selectedApplication.setStatus(Status.ALLOCATED_TO_MANAGER);
                addInformationMessage("Application allocated to Manager: " + selectedManager.getFullName());
            }

            if (selectedPrimaryResource != null) {
                UserAtrApplication primaryAllocation = buildUserAtrApplication(selectedPrimaryResource, ResourceType.PRIMARY_RESOURCE);
                userAtrApplicationService.save(primaryAllocation);
                selectedApplication.getUserAtrApplications().add(primaryAllocation);
                addInformationMessage("Primary Resource assigned: " + selectedPrimaryResource.getFullName());
            }

            List<User> additionalUsers = additionalResourcesPickList.getTarget();
            if (additionalUsers != null && !additionalUsers.isEmpty()) {
                for (User additionalUser : additionalUsers) {
                    UserAtrApplication additionalAllocation = buildUserAtrApplication(additionalUser, ResourceType.SECONDARY_RESOURCE);
                    userAtrApplicationService.save(additionalAllocation);
                    selectedApplication.getUserAtrApplications().add(additionalAllocation);
                }
                addInformationMessage("Assigned " + additionalUsers.size() + " additional resource(s).");
            }

            selectedApplication.setUpdatedBy(getActiveUser().getUser().getFullName());
            selectedApplication.setUpdatedDate(new Date());
            atrApplicationService.save(selectedApplication);
            init();

        } catch (Exception e) {
            addErrorMessage("Error allocating application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private UserAtrApplication buildUserAtrApplication(User user, ResourceType resourceType) {
        UserAtrApplication allocation = new UserAtrApplication();
        allocation.setUser(user);
        allocation.setAtrApplication(selectedApplication);
        allocation.setResourceType(resourceType);
        allocation.setCreatedBy(getActiveUser().getUser().getFullName());
        allocation.setCreatedDate(new Date());
        return allocation;
    }

    public void cancel() {
        clearSelections();
        reset().setList(true);
    }

    private void clearSelections() {
        selectedManager = null;
        selectedPrimaryResource = null;
        if (additionalResourcesPickList != null) {
            additionalResourcesPickList.getTarget().clear();
        }
    }

    public boolean isAllocationPhase() {
        return selectedApplication != null
                && (Status.SUBMITTED_AND_PAID.equals(selectedApplication.getStatus())
                || Status.READY_FOR_ALLOCATION.equals(selectedApplication.getStatus())
                || Status.ALLOCATED_TO_MANAGER.equals(selectedApplication.getStatus()));
    }

    public boolean canAllocate() {
        return ("administrator".equals(currentUserRole) || "manager".equals(currentUserRole))
                && isAllocationPhase();
    }

    public String getAllocatedToDisplay() {
        if (selectedApplication == null || selectedApplication.getUserAtrApplications() == null
                || selectedApplication.getUserAtrApplications().isEmpty()) {
            return "Not allocated";
        }

        UserAtrApplication managerAllocation = selectedApplication.getUserAtrApplications().stream()
                .filter(userApp -> ResourceType.MANAGER.equals(userApp.getResourceType()))
                .findFirst()
                .orElse(null);

        if (managerAllocation != null) {
            return managerAllocation.getUser().getFullName() + " (Manager)";
        }

        UserAtrApplication firstAllocation = selectedApplication.getUserAtrApplications().get(0);
        return firstAllocation.getUser().getFullName() + " ("
                + getResourceTypeDisplayName(firstAllocation.getResourceType()) + ")";
    }

    public String getAllocationButtonText() {
        switch (currentUserRole) {
            case "administrator":
                return "Allocate to Manager";
            case "manager":
                return "Assign Primary & Additional Resources";
            default:
                return "Allocate";
        }
    }

    public String getCurrentUserRoleDisplay() {
        switch (currentUserRole) {
            case "administrator":
                return "Administrator";
            case "manager":
                return "Manager";
            default:
                return currentUserRole;
        }
    }

    public String getResourceTypeDisplayName(ResourceType resourceType) {
        return resourceType != null ? resourceType.toString() : "";
    }

    // Getters and Setters
    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }

    public List<RulingType> getRulingTypes() {
        return rulingTypes;
    }

    public void setRulingTypes(List<RulingType> rulingTypes) {
        this.rulingTypes = rulingTypes;
    }

    public ATRApplication getSelectedApplication() {
        return selectedApplication;
    }

    public void setSelectedApplication(ATRApplication selectedApplication) {
        this.selectedApplication = selectedApplication;
    }

    public Slice<ATRApplication> getAtrApplications() {
        return atrApplications;
    }

    public void setAtrApplications(Slice<ATRApplication> atrApplications) {
        this.atrApplications = atrApplications;
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

    public List<User> getAvailableAdditionalResources() {
        return availableAdditionalResources;
    }

    public void setAvailableAdditionalResources(List<User> availableAdditionalResources) {
        this.availableAdditionalResources = availableAdditionalResources;
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

    public boolean getCanAllocate() {
        return canAllocate();
    }

}
