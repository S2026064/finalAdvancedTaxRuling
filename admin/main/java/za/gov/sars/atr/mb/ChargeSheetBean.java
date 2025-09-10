/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.atr.mb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import za.gov.sars.common.ActivityCategory;
import za.gov.sars.common.ActivityType;
import za.gov.sars.common.FeedbackType;
import za.gov.sars.common.ResourceType;
import za.gov.sars.common.Status;
import za.gov.sars.common.TimesheetStatus;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.Activity;
import za.gov.sars.domain.AtrApplicationQuestion;
import za.gov.sars.domain.ChargeSheet;
import za.gov.sars.domain.ChargeSheetEntry;
import za.gov.sars.domain.EstimatePlan;
import za.gov.sars.domain.EstimateRecord;
import za.gov.sars.domain.FeedBack;
import za.gov.sars.domain.TimeSheetActivity;
import za.gov.sars.domain.User;
import za.gov.sars.domain.UserAtrApplication;
import za.gov.sars.domain.service.ActivityServiceLocal;
import za.gov.sars.service.AtrApplicationQuestionServiceLocal;
import za.gov.sars.service.AtrApplicationServiceLocal;
import za.gov.sars.service.ChargeSheetServiceLocal;
import za.gov.sars.service.FeedBackServiceLocal;
import za.gov.sars.service.TimeSheetActivityServiceLocal;
import za.gov.sars.service.UserAtrApplicationServiceLocal;

/**
 *
 * @author S2030702
 */
@ManagedBean
@ViewScoped
public class ChargeSheetBean extends BaseBean<ChargeSheet> {

    @Autowired
    private ChargeSheetServiceLocal chargeSheetService;

    @Autowired
    private TimeSheetActivityServiceLocal timesheetActivityService;
    @Autowired
    private FeedBackServiceLocal feedBackService;

    @Autowired
    private AtrApplicationServiceLocal atrApplicationService;

    @Autowired
    private UserAtrApplicationServiceLocal userAtrApplicationService;

    @Autowired
    private ActivityServiceLocal activityService;

    @Autowired
    private AtrApplicationQuestionServiceLocal atrApplicationQuestionService;

    private List<EstimateRecord> estimateRecords = new ArrayList<>();
    private List<UserAtrApplication> userAtrApplications = new ArrayList<>();
    private Map<ActivityCategory, List<Activity>> activities = new HashMap<>();
    private List<AtrApplicationQuestion> applicationQuestions = new ArrayList<>();
    private List<ChargeSheetEntry> chargeSheetEntries = new ArrayList<>();
    private List<ResourceType> resourceTypes = new ArrayList<>();
    private Map<ActivityCategory, List<ChargeSheetEntry>> entriesByCategory = new HashMap<>();
    private Slice<TimeSheetActivity> timeSheetActivities;

    private ATRApplication selectedATRApplication;
    private EstimatePlan estimatePlan;
    private Integer tabIndex = 0;
    private String naturalPerson;
    private User userDetail;
    private String comment;

    double estimateTotalHours = 0.0;
    double estimateTotalAmount = 0.0;
    double timesheetTotalHours = 0.0;
    double timesheetTotalAmount = 0.0;

    @PostConstruct
    public void init() {
        reset().setList(true);
        resourceTypes.add(ResourceType.MANAGER);
        resourceTypes.add(ResourceType.PRIMARY_RESOURCE);
        userAtrApplications = userAtrApplicationService.findByUserAndAtrApplication_StatusAndResourceTypeIn(getActiveUser().getUser(),Status.CHARGESHEET_IN_PROGRESS, resourceTypes);
        activities = activityService.findByActivityType(ActivityType.BILLABLE).stream()
                .collect(Collectors.groupingBy(
                        Activity::getActivityCategory,
                        TreeMap::new,
                        Collectors.toList()));
    }

    public void createChargesheet(UserAtrApplication userAtrApplication) {
        reset().setAdd(true);
        setTabIndex(0);
        Pageable pageable = PageRequest.of(0, 20);
        selectedATRApplication = userAtrApplication.getAtrApplication();
        timeSheetActivities = timesheetActivityService.findByAtrApplicationAndTimesheetStatus(selectedATRApplication, TimesheetStatus.CLOSED, pageable);
        if (selectedATRApplication.getChargeSheet() == null) {
            estimatePlan = getCurrentEstimatePlan(selectedATRApplication);
            ChargeSheet chargeSheet = new ChargeSheet();
            chargeSheet.setCreatedBy(getActiveUser().getSid());
            chargeSheet.setCreatedDate(new Date());
            chargeSheet.setTravelExp(estimatePlan.getTravelExp());
            chargeSheet.setGrandTotal(estimatePlan.getGrandTotal());
            chargeSheet.setExternalResource(estimatePlan.getExternalResource());
            selectedATRApplication.setChargeSheet(chargeSheet);

            estimateRecords = estimatePlan.getEstimateRecords();
            for (List<Activity> filteredActivities : activities.values()) {

                for (Activity activity : filteredActivities) {
                    for (EstimateRecord estimate : estimateRecords) {
                        if (estimate.getDescription().equalsIgnoreCase(activity.getDescription())
                                && estimate.getActivityType() == activity.getActivityType()) {

                            estimateTotalHours = estimate.getTotalHours();
                            estimateTotalAmount = estimate.getTotalAmount();
                        }
                    }

                    for (TimeSheetActivity timeSheetActivity : timeSheetActivities) {
                        if (timeSheetActivity.getActivity().getDescription().equalsIgnoreCase(activity.getDescription())
                                && timeSheetActivity.getActivity().getActivityType() == activity.getActivityType()) {

                            timesheetTotalHours = timeSheetActivity.getNumberOfHours();
                            // We need to look into this again
                            timesheetTotalAmount = timeSheetActivity.getNumberOfHours() * estimatePlan.getChargeOutRateCategory().getHourlyRate();
                        }
                    }

                    ChargeSheetEntry entry = new ChargeSheetEntry();
                    entry.setActivity(activity);
                    entry.setChargeSheet(chargeSheet);
                    entry.setEstimatedHours(estimateTotalHours);
                    entry.setEstimatedAmount(estimateTotalAmount);
                    entry.setBalance(estimateTotalAmount);
                    entry.setInvoiceToDate(0);
                    entry.setThisPeriodHours(timesheetTotalHours);
                    entry.setThisPeriodAmount(timesheetTotalAmount);
                    entry.setInvoiceTotal(timesheetTotalAmount);

                    chargeSheetEntries.add(entry);
                }
            }

            Map<ActivityCategory, List<ChargeSheetEntry>> entriesByCategory = chargeSheetEntries.stream()
                    .collect(Collectors.groupingBy(
                            e -> e.getActivity().getActivityCategory(),
                            TreeMap::new,
                            Collectors.toList()
                    ));

            this.setEntriesByCategory(entriesByCategory);
            chargeSheet.setChargeSheetEntrys(chargeSheetEntries);

            for (UserAtrApplication userApplication : userAtrApplications) {
                if (userApplication.getResourceType().equals(ResourceType.MANAGER)) {
                    userDetail = userApplication.getUser();
                }
            }
            addEntity(chargeSheet);
//            selectedATRApplication = userAtrApplication.getAtrApplication();
        } else {

            ChargeSheet existingChargeSheet = userAtrApplication.getAtrApplication().getChargeSheet();
            List<ChargeSheetEntry> existingEntries = existingChargeSheet.getChargeSheetEntrys();

            Map<ActivityCategory, List<ChargeSheetEntry>> entriesByCategory = existingEntries.stream()
                    .collect(Collectors.groupingBy(
                            e -> e.getActivity().getActivityCategory(),
                            TreeMap::new,
                            Collectors.toList()
                    ));

            this.setEntriesByCategory(entriesByCategory);

//            selectedATRApplication = userAtrApplication.getAtrApplication();
            addEntity(existingChargeSheet);
        }

    }

    public void save(ChargeSheet chargeSheet) {

    }

    public void submit(ChargeSheet chargeSheet) {
        chargeSheet.setStatus(Status.CHARGESHEET_SUBMITTED);
        if (chargeSheet.getId() != null) {
            chargeSheetService.update(chargeSheet);
        } else {
            chargeSheetService.save(chargeSheet);
        }

        atrApplicationService.update(selectedATRApplication);
        addInformationMessage("Charge sheet successfully submitted");
        reset().setList(true);

    }

    public void review(UserAtrApplication userAtrApplication) {
        reset().setAdd(true);
        selectedATRApplication = userAtrApplication.getAtrApplication();
        ChargeSheet existingChargeSheet = selectedATRApplication.getChargeSheet();
        List<ChargeSheetEntry> existingEntries = existingChargeSheet.getChargeSheetEntrys();

        Map<ActivityCategory, List<ChargeSheetEntry>> entriesByCategory = existingEntries.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getActivity().getActivityCategory(),
                        TreeMap::new,
                        Collectors.toList()
                ));

        this.setEntriesByCategory(entriesByCategory);

//        estimatePlan = selectedATRApplication.;
        addEntity(existingChargeSheet);
    }

    public void rework(ChargeSheet chargeSheet) {

        //     ChargeSheet existingChargeSheet = chargeSheetService.findByIdWithEntries(chargeSheet.getId());
        //   existingChargeSheet.getChargeSheetEntrys().size();
        chargeSheet.setStatus(Status.CHARGESHEET_SEND_FOR_REWORK);
        chargeSheetService.update(chargeSheet);

        FeedBack feedback = new FeedBack();
        feedback.setCreatedBy(getActiveUser().getSid());
        feedback.setCreatedDate(new Date());
        feedback.setDescription(comment);
        feedback.setFeedbackType(FeedbackType.REWORK);
        feedback.setAtrApplication(selectedATRApplication);
        feedBackService.save(feedback);

        atrApplicationService.update(selectedATRApplication);
        addInformationMessage("Charge sheet sent for rework successfully submitted");
        reset().setList(true);
    }
    
    public void approve(ChargeSheet chargeSheet){
        chargeSheet.setStatus(Status.CHARGESHEET_APPROVED);
        chargeSheetService.update(chargeSheet);
        
        selectedATRApplication.setStatus(Status.CASE_FINALISED);
        atrApplicationService.update(selectedATRApplication);
        addInformationMessage("Charge sheet approved and applicant invoiced.");
        reset().setList(true);
    }

   

    public List<EstimateRecord> getEstimateRecords() {
        return estimateRecords;
    }

    public void setEstimateRecords(List<EstimateRecord> estimateRecords) {
        this.estimateRecords = estimateRecords;
    }

    public List<UserAtrApplication> getUserAtrApplications() {
        return userAtrApplications;
    }

    public void setUserAtrApplications(List<UserAtrApplication> userAtrApplications) {
        this.userAtrApplications = userAtrApplications;
    }

    public ATRApplication getSelectedATRApplication() {
        return selectedATRApplication;
    }

    public void setSelectedATRApplication(ATRApplication selectedATRApplication) {
        this.selectedATRApplication = selectedATRApplication;
    }

    public Integer getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(Integer tabIndex) {
        this.tabIndex = tabIndex;
    }

    public Map<ActivityCategory, List<Activity>> getActivities() {
        return activities;
    }

    public void setActivities(Map<ActivityCategory, List<Activity>> activities) {
        this.activities = activities;
    }

    public String getNaturalPerson() {
        return naturalPerson;
    }

    public void setNaturalPerson(String naturalPerson) {
        this.naturalPerson = naturalPerson;
    }

    public User getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(User userDetail) {
        this.userDetail = userDetail;
    }

    public List<ChargeSheetEntry> getChargeSheetEntries() {
        return chargeSheetEntries;
    }

    public void setChargeSheetEntries(List<ChargeSheetEntry> chargeSheetEntries) {
        this.chargeSheetEntries = chargeSheetEntries;
    }

    public Map<ActivityCategory, List<ChargeSheetEntry>> getEntriesByCategory() {
        return entriesByCategory;
    }

    public void setEntriesByCategory(Map<ActivityCategory, List<ChargeSheetEntry>> entriesByCategory) {
        this.entriesByCategory = entriesByCategory;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public EstimatePlan getEstimatePlan() {
        return estimatePlan;
    }

    public void setEstimatePlan(EstimatePlan estimatePlan) {
        this.estimatePlan = estimatePlan;
    }

}
