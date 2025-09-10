package za.gov.sars.atr.mb;

import za.gov.sars.domain.service.ActivityServiceLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import za.gov.sars.common.ActivityType;
import za.gov.sars.common.ResourceType;
import za.gov.sars.common.Status;
import za.gov.sars.domain.*;
import za.gov.sars.service.*;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.*;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@ManagedBean
@ViewScoped
public class AssignResourceBean extends BaseBean<ATRApplication> {

    private static final Logger log = LoggerFactory.getLogger(AssignResourceBean.class);

    @Autowired
    private AtrApplicationServiceLocal atrApplicationService;

    @Autowired
    private UserAtrApplicationServiceLocal userAtrApplicationService;

    @Autowired
    private ActivityServiceLocal activityService;

    @Autowired
    private UserApplicationActivityServiceLocal userApplicationActivityService;

    private List<ATRApplication> allocatedAtrApplications;
     private List<ResourceType> resourceTypes = new ArrayList<>();
    private ATRApplication selectedApplication;

    private List<UserAtrApplication> assignedUsers;
    private List<Activity> allActivities;
    private Map<String, Boolean> assignmentMatrix = new HashMap<>();

    
    private boolean assignmentView = false;
    private boolean loading = false;
    
    // New fields for enhanced functionality
    private Map<Long, ResourceType> userResourceTypeMap = new HashMap<>();
    private List<Activity> billableActivities = new ArrayList<>();
    private List<Activity> nonBillableActivities = new ArrayList<>();
    private Map<String, Boolean> autoAssignedMatrix = new HashMap<>();

    @PostConstruct
    public void init() {
                reset().setList(true);

        try {
           
            Pageable pageable = PageRequest.of(0, 100);
                //This will take most status because you can assign at any stage
            Page<ATRApplication> pageResult = atrApplicationService.findByStatusAndAllocatedManagerOrAssignedUser( Arrays.asList(Status.ASSIGN_ACTIVITIES,Status.DRAFT_RULING_ACCEPTED), getActiveUser().getUser(), pageable );

            allocatedAtrApplications = pageResult.getContent();
        } catch (Exception e) {
            log.error("Error initializing AssignResourceBean", e);
            addErrorMessage("Error loading allocated applications.");
        }
    }

    public void startAssignment(ATRApplication atrApplication) {
        if (atrApplication == null) {
            addErrorMessage("Invalid application selected.");
            return;
        }

        try {
            this.selectedApplication = atrApplication;
            
            // Load assigned users
            resourceTypes.add(ResourceType.MANAGER);
            resourceTypes.add(ResourceType.PRIMARY_RESOURCE);
            resourceTypes.add(ResourceType.SECONDARY_RESOURCE);
            this.assignedUsers = userAtrApplicationService.findByAtrApplicationAndResourceTypeIn(atrApplication, resourceTypes);
            if (assignedUsers == null) {
                assignedUsers = new ArrayList<>();
            }
            
            // Build user resource type mapping
            buildUserResourceTypeMap();
            
            // Load all activities and categorize them
            loadAndCategorizeActivities();
            
            // Initialize the assignment matrix with auto-assignment logic
            initializeMatrixWithAutoAssignment();
            
            // Switch views
            reset().setList(false);
            assignmentView = true;
            
            addInformationMessage("Assignment view loaded successfully with auto-assignments applied.");
            
        } catch (Exception e) {
            log.error("Error starting assignment for application ID: " + 
                     (atrApplication.getId() != null ? atrApplication.getId() : "null"), e);
            addErrorMessage("Error loading assignment data: " + e.getMessage());
            
            // Reset state on error
            returnToList();
        }
    }

    private void buildUserResourceTypeMap() {
        userResourceTypeMap.clear();
        
        for (UserAtrApplication userAssignment : assignedUsers) {
            if (userAssignment.getUser() != null && userAssignment.getUser().getId() != null) {
                userResourceTypeMap.put(userAssignment.getUser().getId(), userAssignment.getResourceType());
            }
        }
        
        log.info("Built user resource type map with {} entries", userResourceTypeMap.size());
    }

    private void loadAndCategorizeActivities() {
        // Load all activities
        this.allActivities = activityService.findAll();
        if (allActivities == null) {
            allActivities = new ArrayList<>();
        }

        // Categorize activities by type
        billableActivities.clear();
        nonBillableActivities.clear();

        for (Activity activity : allActivities) {
            if (activity.getActivityType() == ActivityType.NON_BILLABLE) {
                nonBillableActivities.add(activity);
            } else if (activity.getActivityType() == ActivityType.BILLABLE || 
                      activity.getActivityType() == ActivityType.GENERAL) {
                billableActivities.add(activity);
            }
        }

        log.info("Loaded {} total activities: {} billable/general, {} non-billable", 
                allActivities.size(), billableActivities.size(), nonBillableActivities.size());
    }

    private void initializeMatrixWithAutoAssignment() {
        assignmentMatrix.clear();
        autoAssignedMatrix.clear();

        if (assignedUsers == null || assignedUsers.isEmpty() || allActivities == null || allActivities.isEmpty()) {
            log.warn("No users or activities found for assignment matrix");
            return;
        }

        List<User> users = assignedUsers.stream()
                .map(UserAtrApplication::getUser)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (users.isEmpty()) {
            log.warn("No valid users found for assignment matrix");
            return;
        }

        // Load existing assignments from database
        List<UserApplicationActivity> existingAssignments = 
                userApplicationActivityService.findByUsersAndApplication(users, selectedApplication);

        // Initialize matrix with existing assignments and apply auto-assignment logic
        for (UserAtrApplication userAssignment : assignedUsers) {
            User user = userAssignment.getUser();
            if (user == null || user.getId() == null) {
                log.warn("Skipping user assignment with null user or ID");
                continue;
            }

            Long userId = user.getId();
            ResourceType resourceType = userAssignment.getResourceType();
            
            for (Activity activity : allActivities) {
                if (activity == null || activity.getId() == null) {
                    log.warn("Skipping activity with null ID");
                    continue;
                }

                String key = generateMatrixKey(userId, activity.getId());
                
                // Check if there's an existing assignment from database
                boolean hasExistingAssignment = existingAssignments.stream()
                        .anyMatch(uaa -> uaa.getUser() != null 
                                && uaa.getUser().getId() != null
                                && uaa.getActivity() != null
                                && uaa.getActivity().getId() != null
                                && uaa.getUser().getId().equals(userId)
                                && uaa.getActivity().getId().equals(activity.getId()));

                boolean shouldAutoAssign = shouldAutoAssignActivity(resourceType, activity);
                
                // Mark as auto-assigned if it should be auto-assigned
                autoAssignedMatrix.put(key, shouldAutoAssign);
                
                // Use existing assignment or auto-assignment result
                assignmentMatrix.put(key, hasExistingAssignment || shouldAutoAssign);
            }
        }

        log.info("Initialized assignment matrix with {} entries and {} auto-assigned entries", 
                assignmentMatrix.size(), autoAssignedMatrix.size());
    }

    private boolean shouldAutoAssignActivity(ResourceType resourceType, Activity activity) {
        if (resourceType == null || activity == null) {
            return false;
        }

        switch (resourceType) {
            case PRIMARY_RESOURCE:
                // Primary resources get ALL activities assigned
                return true;
                
            case SECONDARY_RESOURCE:
                // Secondary resources get only NON_BILLABLE activities assigned
                return activity.getActivityType() == ActivityType.NON_BILLABLE;
                
            case MANAGER:
            case APPLICANT:
            default:
                // Managers and applicants don't get auto-assigned activities
                return false;
        }
    }

    public void resetToAutoAssignments() {
        if (selectedApplication == null || assignedUsers == null || allActivities == null) {
            addErrorMessage("Cannot reset assignments - missing data.");
            return;
        }

        try {
            int autoAssignedCount = 0;
            autoAssignedMatrix.clear();
            
            for (UserAtrApplication userAssignment : assignedUsers) {
                User user = userAssignment.getUser();
                if (user == null || user.getId() == null) {
                    continue;
                }

                Long userId = user.getId();
                ResourceType resourceType = userAssignment.getResourceType();
                
                for (Activity activity : allActivities) {
                    if (activity == null || activity.getId() == null) {
                        continue;
                    }

                    String key = generateMatrixKey(userId, activity.getId());
                    boolean shouldAssign = shouldAutoAssignActivity(resourceType, activity);
                    
                    // Mark as auto-assigned and set the assignment
                    autoAssignedMatrix.put(key, shouldAssign);
                    assignmentMatrix.put(key, shouldAssign);
                    
                    if (shouldAssign) {
                        autoAssignedCount++;
                    }
                }
            }

            addInformationMessage("Reset to auto-assignments completed. " + autoAssignedCount + " activities auto-assigned.");
            
        } catch (Exception e) {
            log.error("Error resetting to auto-assignments", e);
            addErrorMessage("Error resetting assignments: " + e.getMessage());
        }
    }

    public void saveAssignments() {
        if (selectedApplication == null) {
            addErrorMessage("No application selected.");
            return;
        }

        try {
            List<User> users = assignedUsers.stream()
                    .map(UserAtrApplication::getUser)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            // Delete existing assignments
            userApplicationActivityService.deleteByUsersAndApplication(users, selectedApplication);

            // Create new assignments
            List<UserApplicationActivity> newAssignments = new ArrayList<>();
            User currentUser = getActiveUser().getUser();
            Date currentDate = new Date();

            for (UserAtrApplication userAssignment : assignedUsers) {
                User user = userAssignment.getUser();
                if (user == null || user.getId() == null) {
                    continue;
                }

                Long userId = user.getId();
                for (Activity activity : allActivities) {
                    if (activity == null || activity.getId() == null) {
                        continue;
                    }

                    String key = generateMatrixKey(userId, activity.getId());
                    if (assignmentMatrix.getOrDefault(key, false)) {
                        UserApplicationActivity assignment = new UserApplicationActivity();
                        assignment.setAtrApplication(selectedApplication);
                        assignment.setUser(user);
                        assignment.setActivity(activity);
                        assignment.setCreatedBy(currentUser.getFullName());
                        assignment.setCreatedDate(currentDate);
                        newAssignments.add(assignment);
                    }
                }
            }

            // Save new assignments
            if (!newAssignments.isEmpty()) {
                selectedApplication.setStatus(Status.ESTIMATE_INPROGRESS);
                atrApplicationService.update(selectedApplication);
                userApplicationActivityService.saveAll(newAssignments);
                addInformationMessage("Successfully assigned " + newAssignments.size() + " activities to users.");
            } else {
                addInformationMessage("No activities were assigned.");
            }

            returnToList();

        } catch (DataAccessException e) {
            log.error("Database error while saving assignments for application: " + selectedApplication.getId(), e);
            addErrorMessage("Database error occurred while saving assignments. Please try again.");
        } catch (Exception e) {
            log.error("Error saving assignments for application: " + selectedApplication.getId(), e);
            addErrorMessage("Failed to save assignments: " + e.getMessage());
        }
      //  selectedApplication.setStatus(Status.ESTIMATE_INPROGRESS);
    }

    public void returnToList() {
        selectedApplication = null;
        assignedUsers = null;
        allActivities = null;
        assignmentMatrix.clear();
        autoAssignedMatrix.clear();
        userResourceTypeMap.clear();
        billableActivities.clear();
        nonBillableActivities.clear();
        assignmentView = false;
        reset().setList(true);
        loading = false;
    }

    // Helper methods for XHTML
    public String generateMatrixKey(Long userId, Long activityId) {
        if (userId == null || activityId == null) {
            return "";
        }
        return userId + "-" + activityId;
    }

    public boolean isUserAssignedToActivity(Long userId, Long activityId) {
        String key = generateMatrixKey(userId, activityId);
        return assignmentMatrix.getOrDefault(key, false);
    }

    public void setUserActivityAssignment(Long userId, Long activityId, boolean assigned) {
        String key = generateMatrixKey(userId, activityId);
        assignmentMatrix.put(key, assigned);
    }

    // Enhanced getter methods for XHTML EL expressions
    public boolean getHasAssignedUsers() {
        return assignedUsers != null && !assignedUsers.isEmpty();
    }

    public boolean getHasActivities() {
        return allActivities != null && !allActivities.isEmpty();
    }

    public int getTotalPossibleAssignments() {
        if (!getHasAssignedUsers() || !getHasActivities()) {
            return 0;
        }
        return assignedUsers.size() * allActivities.size();
    }

    public int getCurrentAssignments() {
        return (int) assignmentMatrix.values().stream()
                .mapToInt(assigned -> assigned ? 1 : 0)
                .sum();
    }

    public int getBillableAssignmentsCount() {
        if (!getHasAssignedUsers() || billableActivities.isEmpty()) {
            return 0;
        }
        
        return (int) assignmentMatrix.entrySet().stream()
                .filter(entry -> entry.getValue()) // Only assigned activities
                .filter(entry -> {
                    String[] parts = entry.getKey().split("-");
                    if (parts.length == 2) {
                        try {
                            Long activityId = Long.parseLong(parts[1]);
                            return billableActivities.stream()
                                    .anyMatch(activity -> activity.getId().equals(activityId));
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    }
                    return false;
                })
                .count();
    }

    public int getNonBillableAssignmentsCount() {
        if (!getHasAssignedUsers() || nonBillableActivities.isEmpty()) {
            return 0;
        }
        
        return (int) assignmentMatrix.entrySet().stream()
                .filter(entry -> entry.getValue()) // Only assigned activities
                .filter(entry -> {
                    String[] parts = entry.getKey().split("-");
                    if (parts.length == 2) {
                        try {
                            Long activityId = Long.parseLong(parts[1]);
                            return nonBillableActivities.stream()
                                    .anyMatch(activity -> activity.getId().equals(activityId));
                        } catch (NumberFormatException e) {
                            return false;
                        }
                    }
                    return false;
                })
                .count();
    }

    public String getUserResourceTypeDisplay(Long userId) {
        ResourceType resourceType = userResourceTypeMap.get(userId);
        if (resourceType != null) {
            switch (resourceType) {
                case PRIMARY_RESOURCE:
                    return "Primary";
                case SECONDARY_RESOURCE:
                    return "Secondary";
                case MANAGER:
                    return "Manager";
                case APPLICANT:
                    return "Applicant";
                default:
                    return resourceType.toString();
            }
        }
        return "Unknown";
    }

    public String getActivityTypeDisplay(Activity activity) {
        if (activity != null && activity.getActivityType() != null) {
            return activity.getActivityType().toString();
        }
        return "Unknown";
    }

    public String getActivityCssClass(Activity activity) {
        if (activity != null && activity.getActivityType() != null) {
            switch (activity.getActivityType()) {
                case BILLABLE:
                    return "billable-activity";
                case NON_BILLABLE:
                    return "non-billable-activity";
                case GENERAL:
                    return "general-activity";
                default:
                    return "";
            }
        }
        return "";
    }

    // Select/Unselect all activities for a specific user
    public void selectAllActivitiesForUser(Long userId) {
        if (userId == null || allActivities == null) {
            return;
        }
        
        try {
            for (Activity activity : allActivities) {
                if (activity != null && activity.getId() != null) {
                    String key = generateMatrixKey(userId, activity.getId());
                    // Only modify if not auto-assigned
                    if (!autoAssignedMatrix.getOrDefault(key, false)) {
                        assignmentMatrix.put(key, true);
                    }
                }
            }
            addInformationMessage("All non-auto-assigned activities selected for user.");
        } catch (Exception e) {
            log.error("Error selecting all activities for user: " + userId, e);
            addErrorMessage("Error selecting activities: " + e.getMessage());
        }
    }
    
    public void unselectAllActivitiesForUser(Long userId) {
        if (userId == null || allActivities == null) {
            return;
        }
        
        try {
            for (Activity activity : allActivities) {
                if (activity != null && activity.getId() != null) {
                    String key = generateMatrixKey(userId, activity.getId());
                    // Only modify if not auto-assigned
                    if (!autoAssignedMatrix.getOrDefault(key, false)) {
                        assignmentMatrix.put(key, false);
                    }
                }
            }
            addInformationMessage("All non-auto-assigned activities unselected for user.");
        } catch (Exception e) {
            log.error("Error unselecting all activities for user: " + userId, e);
            addErrorMessage("Error unselecting activities: " + e.getMessage());
        }
    }

    // Check if a specific assignment is auto-assigned (read-only)
    public boolean isAutoAssigned(Long userId, Long activityId) {
        String key = generateMatrixKey(userId, activityId);
        return autoAssignedMatrix.getOrDefault(key, false);
    }

    // Utility methods (can be called as methods in EL)
    public boolean hasAssignedUsers() {
        return getHasAssignedUsers();
    }

    public boolean hasActivities() {
        return getHasActivities();
    }

    public List<ATRApplication> getAllocatedAtrApplications() {
        return allocatedAtrApplications;
    }

    public void setAllocatedAtrApplications(List<ATRApplication> allocatedAtrApplications) {
        this.allocatedAtrApplications = allocatedAtrApplications;
    }

    public ATRApplication getSelectedApplication() {
        return selectedApplication;
    }

    public void setSelectedApplication(ATRApplication selectedApplication) {
        this.selectedApplication = selectedApplication;
    }

    public List<UserAtrApplication> getAssignedUsers() {
        return assignedUsers;
    }

    public void setAssignedUsers(List<UserAtrApplication> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }

    public List<Activity> getAllActivities() {
        return allActivities;
    }

    public void setAllActivities(List<Activity> allActivities) {
        this.allActivities = allActivities;
    }

    public Map<String, Boolean> getAssignmentMatrix() {
        return assignmentMatrix;
    }

    public void setAssignmentMatrix(Map<String, Boolean> assignmentMatrix) {
        this.assignmentMatrix = assignmentMatrix;
    }

    public boolean isAssignmentView() {
        return assignmentView;
    }

    public void setAssignmentView(boolean assignmentView) {
        this.assignmentView = assignmentView;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public Map<Long, ResourceType> getUserResourceTypeMap() {
        return userResourceTypeMap;
    }

    public void setUserResourceTypeMap(Map<Long, ResourceType> userResourceTypeMap) {
        this.userResourceTypeMap = userResourceTypeMap;
    }

    public List<Activity> getBillableActivities() {
        return billableActivities;
    }

    public void setBillableActivities(List<Activity> billableActivities) {
        this.billableActivities = billableActivities;
    }

    public List<Activity> getNonBillableActivities() {
        return nonBillableActivities;
    }

    public void setNonBillableActivities(List<Activity> nonBillableActivities) {
        this.nonBillableActivities = nonBillableActivities;
    }

    public Map<String, Boolean> getAutoAssignedMatrix() {
        return autoAssignedMatrix;
    }

    public void setAutoAssignedMatrix(Map<String, Boolean> autoAssignedMatrix) {
        this.autoAssignedMatrix = autoAssignedMatrix;
    }
    
    
}