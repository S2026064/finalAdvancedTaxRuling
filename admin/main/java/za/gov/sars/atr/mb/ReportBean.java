/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.atr.mb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.RevisionType;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import za.gov.sars.atr.mb.util.ChargePerResourceHelper;
import za.gov.sars.atr.mb.util.EstimateVarienceHelper;
import za.gov.sars.atr.mb.util.OpenApplicationHelper;
import za.gov.sars.atr.mb.util.ProgressTrackingDaysHelper;
import za.gov.sars.atr.mb.util.StatusChangeDateHelper;
import za.gov.sars.atr.mb.util.StuffHoursHelper;
import za.gov.sars.atr.mb.util.TimeSheetHelper;
import za.gov.sars.common.ActivityType;
import za.gov.sars.common.ChargeCategory;
import za.gov.sars.common.DateUtil;
import za.gov.sars.common.EstimatePlanStatus;
import za.gov.sars.common.ReportType;
import za.gov.sars.common.ResourceType;
import za.gov.sars.common.RulingType;
import za.gov.sars.common.Status;
import za.gov.sars.common.TaxAct;
import za.gov.sars.common.TimesheetStatus;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.ActSection;
import za.gov.sars.domain.Activity;
import za.gov.sars.domain.ChargeOutRateCategory;
import za.gov.sars.domain.EstimatePlan;
import za.gov.sars.domain.RulingActSection;
import za.gov.sars.domain.TimeSheetActivity;
import za.gov.sars.domain.User;
import za.gov.sars.domain.UserApplicationActivity;
import za.gov.sars.domain.UserAtrApplication;
import za.gov.sars.domain.service.ActivityServiceLocal;
import za.gov.sars.dto.AtrApplicationRevisionDTO;
import za.gov.sars.dto.ProductivitySummaryReportDTO;
import za.gov.sars.service.AtrApplicationServiceLocal;
import za.gov.sars.service.ChargeOutRateCategoryServiceLocal;
import za.gov.sars.service.ReportService;
import za.gov.sars.service.TimeSheetActivityServiceLocal;
import za.gov.sars.service.UserApplicationActivityServiceLocal;
import za.gov.sars.service.UserAtrApplicationServiceLocal;
import za.gov.sars.service.UserServiceLocal;

/**
 *
 * @author S2028398
 */
@ManagedBean
@ViewScoped
public class ReportBean extends BaseBean<ATRApplication> {

    @Autowired
    private ReportService reportService;
    @Autowired
    private UserServiceLocal userService;
    @Autowired
    private ChargeOutRateCategoryServiceLocal chargeOutRateCategoryService;
    @Autowired
    private AtrApplicationServiceLocal atrApplicationService;
    @Autowired
    private UserAtrApplicationServiceLocal userAppService;
    @Autowired
    private UserApplicationActivityServiceLocal applicationActivityService;
    @Autowired
    private TimeSheetActivityServiceLocal timeSheetActivityService;
    @Autowired
    private ActivityServiceLocal activityService;

    private List<Status> statuses = new ArrayList<>();
    private List<TimeSheetHelper> timeSheets = new ArrayList<>();
    private List<ChargePerResourceHelper> chargePerResourceHelpers = new ArrayList<>();
    private List<StuffHoursHelper> stuffHoursHelpers = new ArrayList<>();
    private List<OpenApplicationHelper> openApplications = new ArrayList<>();
    private List<StatusChangeDateHelper> statusChangeDateHelpers = new ArrayList<>();
    private List<ReportType> reportTypes = new ArrayList<>();
    private List<UserAtrApplication> userAtrApplications = new ArrayList<>();
    private List<UserApplicationActivity> userApplicationActivitys = new ArrayList<>();
    private List<UserApplicationActivity> resources = new ArrayList<>();
    private List<ATRApplication> atrApplications = new ArrayList<>();
    private List<ATRApplication> applications = new ArrayList<>();
    private List<RulingActSection> rulingSections = new ArrayList<>();
    private List<Activity> userActivities = new ArrayList<>();
    private List<ActSection> sections = new ArrayList<>();
    private Map<ATRApplication, List<User>> managersMap = new HashMap<>();
    private Map<ATRApplication, List<User>> primaryResourcesMap = new HashMap<>();
    private List<AtrApplicationRevisionDTO> revisions;
    private List<ProductivitySummaryReportDTO> productivitySummaryReport;

    private ATRApplication selectedApplication;
    private List<RulingType> rulingTypes = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private List<ProgressTrackingDaysHelper> trackingDaysHelpers = new ArrayList<>();
    private List<TimesheetStatus> timesheetStatuses = new ArrayList<>();
    private List<EstimateVarienceHelper> estimateVarienceHelpers = new ArrayList<>();
    private Map<Long, Double> staffHoursMap = new HashMap<>();
    private List<TimeSheetActivity> timeSheetActivities = new ArrayList<>();

    private Date startDate;
    private Date endDate;
    private ReportType reportType;
    private User manager;
    private User primaryResource;

    private RulingType rullingType;
    private boolean rulingActive = Boolean.FALSE;
    private boolean userActive = Boolean.FALSE;
    private boolean activity = Boolean.FALSE;
    private double totalHours;
    private Activity selectedActivity;

    @PostConstruct
    public void init() {
        reset().setList(true);
        reportTypes = Arrays.asList(ReportType.values());
        atrApplications = atrApplicationService.findAll();
    }

    public void viewReport() {
        switch (reportType) {
            case CHARGE_PER_INVOICE:
                setPanelTitleName(ReportType.CHARGE_PER_INVOICE.toString());
                List<UserAtrApplication> allUserApps = userAppService.findUsersByAtrApplicationId(selectedApplication);

                Map<Long, UserAtrApplication> uniqueResourcesMap = allUserApps.stream()
                        .filter(user -> !user.getResourceType().equals(ResourceType.APPLICANT))
                        .collect(Collectors.toMap(
                                user -> user.getUser().getId(),
                                Function.identity(),
                                (existing, replacement) -> existing));
                userAtrApplications = new ArrayList<>(uniqueResourcesMap.values());
                reset().setChargePerInvoice(true);
                break;
            default:
        }

        if (this.startDate == null) {
            addWarningMessage("Please select the report start date range");
            return;
        }

        if (this.endDate == null) {
            addWarningMessage("Please select the report end date range");
            return;
        }

        if (this.endDate.before(this.startDate)) {
            addWarningMessage("The end date cannot be before the starting date please correct the date range selection");
            return;
        }

        //update the time to midnight for reporting
        Calendar endDateCalendar = Calendar.getInstance();
        endDateCalendar.setTime(endDate);
        endDateCalendar.set(Calendar.HOUR_OF_DAY, 23);
        endDateCalendar.set(Calendar.MINUTE, 59);
        endDateCalendar.set(Calendar.SECOND, 59);
        endDateCalendar.set(Calendar.MILLISECOND, 59);
        Date endDateMidnight = endDateCalendar.getTime();
        switch (reportType) {
            case TIME_SHEET:
                setPanelTitleName(ReportType.TIME_SHEET.toString());
                timeSheets.clear();
                for (TimeSheetActivity timeSheetActivity : timeSheetActivityService.findByDate(startDate, endDateMidnight)) {
                    if (timeSheetActivity != null) {
                        TimeSheetHelper timeSheetHelper = new TimeSheetHelper();
                        timeSheetHelper.setRefNumber(Long.toString(timeSheetActivity.getAtrApplication().getCaseNum()));
                        if (StringUtils.isNotEmpty(timeSheetActivity.getAtrApplication().getApplicantName())) {
                            timeSheetHelper.setCompanyName(timeSheetActivity.getAtrApplication().getApplicantName());
                        }
                        timeSheetHelper.setDate(timeSheetActivity.getCapturedDate());
                        timeSheetHelper.setHours(timeSheetActivity.getNumberOfHours());
                        User resource = userService.findBySid(timeSheetActivity.getCreatedBy());
                        if (resource != null) {
                            timeSheetHelper.setResource(resource.getFullName());
                        }
                        timeSheetHelper.setActivity(timeSheetActivity.getActivity());
                        timeSheetHelper.setDescription(timeSheetActivity.getDescription());
                        timeSheetHelper.setActivityStatus(timeSheetActivity.getTimesheetStatus().toString());
                        timeSheetHelper.setActivitytype(timeSheetActivity.getActivity().getActivityType().toString());
                        timeSheets.add(timeSheetHelper);
                    }
                }
                reset().setTimesheetReport(true);
                break;
            case STUFF_HOURS_ACTIVITY:
                setPanelTitleName(ReportType.STUFF_HOURS_ACTIVITY.toString());
                stuffHoursHelpers.clear();
                for (TimeSheetActivity timeSheetActivity : timeSheetActivityService.findByUserAndActivityAndTimesheetStatusAndDate(manager, selectedActivity, TimesheetStatus.CLOSED, startDate, endDateMidnight)) {
                    if (timeSheetActivity != null) {
                        StuffHoursHelper stuffHoursHelper = new StuffHoursHelper();
                        User stuff = userService.findBySid(timeSheetActivity.getCreatedBy());
                        stuffHoursHelper.setStuff(stuff.getFullName());
                        stuffHoursHelper.setHours(timeSheetActivity.getNumberOfHours());
                        stuffHoursHelper.setActivity(timeSheetActivity.getActivity());
                        totalHours += timeSheetActivity.getNumberOfHours();
                        stuffHoursHelpers.add(stuffHoursHelper);
                    }
                }
                reset().setHoursActivity(true);
                break;
            case CHARGE_PER_RESOURCE:
                setPanelTitleName(ReportType.CHARGE_PER_RESOURCE.toString());
                chargePerResourceHelpers.clear();
                for (TimeSheetActivity timeSheetActivity : timeSheetActivityService.findByUserAndTimesheetStatusAndDate(manager, TimesheetStatus.CLOSED, startDate, endDateMidnight)) {
                    if (getCurrentEstimatePlan(timeSheetActivity.getAtrApplication()) != null && getCurrentEstimatePlan(timeSheetActivity.getAtrApplication()).getStatus().equals(EstimatePlanStatus.ESTIMATE_PUBLISHED)) {
                        ChargeOutRateCategory chargeOutRateCategory = getCurrentEstimatePlan(timeSheetActivity.getAtrApplication()).getChargeOutRateCategory();
                        ChargePerResourceHelper chargePerResourceHelper = new ChargePerResourceHelper();
                        chargePerResourceHelper.setApplication(timeSheetActivity.getAtrApplication());
                        chargePerResourceHelper.setActivity(timeSheetActivity.getActivity());
                        chargePerResourceHelper.setActivityStatus(timeSheetActivity.getActivity().getActivityType().toString());
                        if (timeSheetActivity.getActivity().getActivityType().equals(ActivityType.BILLABLE) && chargeOutRateCategory.getChargeCategory().equals(ChargeCategory.URGENT_APPLICATION)) {
                            ChargeOutRateCategory urgentApplication = chargeOutRateCategoryService.findByChargeCategory(ChargeCategory.URGENT_APPLICATION);
                            chargePerResourceHelper.setTotalHours(timeSheetActivity.getNumberOfHours() * urgentApplication.getHourlyRate());
                        } else if (timeSheetActivity.getActivity().getActivityType().equals(ActivityType.BILLABLE) && !chargeOutRateCategory.getChargeCategory().equals(ChargeCategory.URGENT_APPLICATION)) {
                            for (ChargeOutRateCategory notUrgentApplication : chargeOutRateCategoryService.findByChargeCategoryNot(ChargeCategory.URGENT_APPLICATION)) {
                                chargePerResourceHelper.setTotalHours(timeSheetActivity.getNumberOfHours() * notUrgentApplication.getHourlyRate());
                                break;
                            }
                        } else {
                            chargePerResourceHelper.setTotalHours(timeSheetActivity.getNumberOfHours() * 0);
                        }
                        chargePerResourceHelpers.add(chargePerResourceHelper);
                    }
                }
                reset().setChargeReport(true);
                break;
            case OPEN_APPLICATION:
                setPanelTitleName(ReportType.OPEN_APPLICATION.toString());
                openApplications.clear();
                statuses.add(Status.APPLICATION_REJECTED);
                statuses.add(Status.NEW);
                statuses.add(Status.DRAFT_RULING_ACCEPTED);
                statuses.add(Status.SAVED);
                statuses.add(Status.APPLICATION_REJECTED);
                statuses.add(Status.DRAFT_RULING_REQUIRES_AUTHORISATION);
                statuses.add(Status.DRAFT_RULING_FEEDBACK_RECIEVED);
                statuses.add(Status.DRAFT_RULING_PUBLISHED);
                statuses.add(Status.DRAFT_RULING_REJECTED);
                statuses.add(Status.REQUEST_WITHDRAWAL_DRAFT_RULING);
                statuses.add(Status.ESTIMATE_ACCEPTED_PAYMENT_UNCONFIRMED);
                statuses.add(Status.ESTIMATE_ACCEPTED_PAYMENT_CONFIRMED);
                statuses.add(Status.ESTIMATE_ACCEPTED_PENDING_DOCUMENTATION);
                statuses.add(Status.ESTIMATE_EXPIRED);
                statuses.add(Status.ESTIMATE_INPROGRESS);
                statuses.add(Status.ESTIMATE_PUBLISHED);
                statuses.add(Status.ESTIMATE_REJECTED);
                statuses.add(Status.ESTIMATION_REQUIRE_APPROVAL);
                statuses.add(Status.MANAGER_ESTIMATION_REJECTED);
                statuses.add(Status.ESTIMATE_REQUIRES_AUTHORISATION);
                statuses.add(Status.REQUEST_WITHDRAWAL_ESTIMATE);
                statuses.add(Status.SANITISED_RULING_ACCEPTED);
                statuses.add(Status.CASE_INPROGRESS);
                statuses.add(Status.RULING_REQUIRES_AUTHORISATION);
                statuses.add(Status.REVISED_ESTIMATE_PUBLISHED);
                statuses.add(Status.RULING_PUBLISHED_PENDING_OUTSTANDING_AMOUNT);
                statuses.add(Status.SANITISED_RULING_REQUIRES_AUTHORISATION);
                statuses.add(Status.SANITISED_RULING_FEEDBACK_RECIEVED);
                statuses.add(Status.SANITISED_RULING_PUBLISHED);
                statuses.add(Status.SANITISED_RULING_REJECTED);
                statuses.add(Status.REQUEST_WITHDRAWAL_SANITISED_RULING);
                statuses.add(Status.SUBMITTED_PAYMENT_UNCONFIRMED);
                statuses.add(Status.SUBMITTED_AND_PAID);
                statuses.add(Status.CASE_WITHDRAWN_PENDING_DOCUMENTATION);
                statuses.add(Status.CASE_WITHDRAWN);
                statuses.add(Status.PENDING_AUTHORISATION);
                statuses.add(Status.READY_FOR_ALLOCATION);
                statuses.add(Status.ALLOCATED_TO_MANAGER);
                statuses.add(Status.ASSIGN_ACTIVITIES);
                for (ATRApplication aTRApplication : atrApplicationService.findByRulingTypeAndStatusIn(rullingType, statuses, startDate, endDateMidnight)) {
                    OpenApplicationHelper openApplicationHelper = new OpenApplicationHelper();
                    openApplicationHelper.setATRApplication(aTRApplication);
                    openApplications.add(openApplicationHelper);
                }
                reset().setUpdate(true);
                break;
            case REJECTED_WITHDRAWN_APPLICATION:
                setPanelTitleName(ReportType.REJECTED_WITHDRAWN_APPLICATION.toString());
                openApplications.clear();
                statuses.add(Status.APPLICATION_REJECTED);
                statuses.add(Status.DRAFT_RULING_ACCEPTED);
                statuses.add(Status.APPLICATION_REJECTED);
                statuses.add(Status.REQUEST_WITHDRAWAL_DRAFT_RULING);
                statuses.add(Status.SANITISED_RULING_REJECTED);
                statuses.add(Status.REQUEST_WITHDRAWAL_SANITISED_RULING);
                statuses.add(Status.CASE_WITHDRAWN_PENDING_DOCUMENTATION);
                statuses.add(Status.CASE_WITHDRAWN);
                for (ATRApplication aTRApplication : atrApplicationService.findByRulingTypeAndStatusIn(rullingType, statuses, startDate, endDateMidnight)) {
                    OpenApplicationHelper openApplicationHelper = new OpenApplicationHelper();
                    openApplicationHelper.setATRApplication(aTRApplication);
                    openApplications.add(openApplicationHelper);
                }
                reset().setSearch(true);
                break;
            case STATUS_CHANGE_DATES:
                setPanelTitleName(ReportType.STATUS_CHANGE_DATES.toString());
                statusChangeDateHelpers.clear();
                for (ATRApplication aTRApplication : atrApplicationService.findByDate(startDate, endDateMidnight)) {
                    revisions = atrApplicationService.findRevisionsByApplicationId(aTRApplication.getId());
                    for (AtrApplicationRevisionDTO artApplicationRevisionDTO : revisions) {
                        if (artApplicationRevisionDTO.getRevisionType().equals(RevisionType.MOD)) {
                            StatusChangeDateHelper statusChangeDateHelper = new StatusChangeDateHelper();
                            ATRApplication searchApplication = atrApplicationService.findById(artApplicationRevisionDTO.getApplicationId());
                            statusChangeDateHelper.setATRApplication(searchApplication);
//                            boolean exist = statusChangeDateHelpers.stream().anyMatch(record -> record.getATRApplication().getCaseNum() == searchApplication.getCaseNum());
//                            if (!exist) {
//                                statusChangeDateHelpers.add(statusChangeDateHelper);
//                            }
                        }
                    }
                }
                reset().setStatusChangeDate(true);
                break;
            case KNOWLEDGE_MANAGEMENT:
                getApplicationsByStatus(statuses, startDate, endDate);
                setPanelTitleName(ReportType.KNOWLEDGE_MANAGEMENT.toString());
                reset().setKnowledgeManagement(true);
                break;
            case RULING_VALIDITY:
                getApplicationsByStatus(statuses, startDate, endDate);
                setPanelTitleName(ReportType.RULING_VALIDITY.toString());
                reset().setRulingValidity(true);
                break;
            case PRODUCTIVITY_SUMMARY:
                productivitySummaryReport = reportService.getProductivitySummaryReport(startDate, endDate);
                setPanelTitleName(ReportType.PRODUCTIVITY_SUMMARY.toString());
                reset().setProductivitySummary(true);
                break;
            case STAFF_HOURS_PER_ATR:
                setPanelTitleName(ReportType.STAFF_HOURS_PER_ATR.toString());
                userAtrApplications = getApplicationsByDate(selectedApplication, startDate, endDate);
                staffHoursMap = timeSheetActivityService.findByAtrApplicationAndTimesheetStatusAndDate(selectedApplication, TimesheetStatus.SUBMITTED, startDate, endDate)
                        .stream()
                        .collect(Collectors.groupingBy(
                                activity -> activity.getResource().getId(),
                                Collectors.summingDouble(activity -> activity.getNumberOfHours())
                        ));

                for (UserAtrApplication userAtrApplication : userAtrApplications) {
                    staffHoursMap.putIfAbsent(userAtrApplication.getUser().getId(), 0.0);
                }
                totalHours = staffHoursMap.values()
                        .stream()
                        .mapToDouble(Double::doubleValue)
                        .sum();

                reset().setStaffHoursPerApp(true);
                break;
            case DUE_DATE_VARIANCE:
                userAtrApplications = findAllApps(startDate, endDate);
                setPanelTitleName(ReportType.DUE_DATE_VARIANCE.toString());
                reset().setDueDateVariance(true);
                break;
            case ESTIMATE_ACTUAL_COMPARISON_BY_RESOURCE_TYPE:
                setPanelTitleName(ReportType.ESTIMATE_ACTUAL_COMPARISON_BY_RESOURCE_TYPE.toString());
                timeSheetActivities = timeSheetActivityService.findByAtrApplicationAndDate(selectedApplication, startDate, endDate);
                userActivities = getActivitiesForApplication(selectedApplication, startDate, endDate);
                reset().setEstimateComparison(true);
                break;
            case ESTIMATION_DEVIATION:
                setPanelTitleName(ReportType.ESTIMATION_DEVIATION.toString());
                userAtrApplications = findAllApps(startDate, endDate);
                reset().setEstimationDeviation(true);
                break;
            case APPLICATION_ASSIGNED_BY_RESOURCE:
                setPanelTitleName(ReportType.APPLICATION_ASSIGNED_BY_RESOURCE.toString());
                userAtrApplications = findAllApps(startDate, endDate);
                reset().setApplicationAssignedResource(true);
                break;
            case REPRESENTATIVE_DETAILS:
                setPanelTitleName(ReportType.REPRESENTATIVE_DETAILS.toString());
                statusChangeDateHelpers.clear();
                for (ATRApplication aTRApplication : atrApplicationService.findByDate(startDate, endDateMidnight)) {
                    StatusChangeDateHelper statusChangeDateHelper = new StatusChangeDateHelper();
                    statusChangeDateHelper.setATRApplication(aTRApplication);
                    statusChangeDateHelpers.add(statusChangeDateHelper);
                }
                reset().setRepresentativeDetails(true);
                break;
            case PROGRESS_TRACKING_DAYS:
                setPanelTitleName(ReportType.PROGRESS_TRACKING_DAYS.toString());
                trackingDaysHelpers.clear();
                for (ATRApplication aTRApplication : atrApplicationService.findByRulingTypeAndDate(rullingType, startDate, endDateMidnight)) {
                    Pageable pageable = Pageable.unpaged();
                    int days = 0;
                    for (TimeSheetActivity timeSheetActivity : timeSheetActivityService.findByAtrApplicationAndTimesheetStatus(aTRApplication, TimesheetStatus.CLOSED, pageable)) {
                        ProgressTrackingDaysHelper progressTrackingDaysHelper = new ProgressTrackingDaysHelper();
                        progressTrackingDaysHelper.setTimeSheetActivity(timeSheetActivity);
                        boolean applicationExist = trackingDaysHelpers.stream().anyMatch(record -> record.getATRApplication().getCaseNum() == aTRApplication.getCaseNum());
                        if (applicationExist) {
                            for (ProgressTrackingDaysHelper daysHelper : trackingDaysHelpers) {
                                boolean exist = trackingDaysHelpers.stream().anyMatch(timeSheetDate -> DateUtil.convertStringToDate(timeSheetDate.getTimeSheetActivity().getCreatedDate()).equals(DateUtil.convertStringToDate(timeSheetActivity.getCreatedDate())));
                                if (!exist) {
                                    daysHelper.setNumDays(daysHelper.getNumDays() + 1);
                                }
                            }
                        } else {
                            days += 1;
                            progressTrackingDaysHelper.setATRApplication(aTRApplication);
                            progressTrackingDaysHelper.setNumDays(days);
                            trackingDaysHelpers.add(progressTrackingDaysHelper);
                        }
                    }
                }
                reset().setProgressTrackingDays(true);
                break;
            case ESTIMATION_VARIANCE:
                setPanelTitleName(ReportType.ESTIMATION_VARIANCE.toString());
                for (TimeSheetActivity timeSheetActivity : timeSheetActivityService.findByTimesheetStatusAndDate(TimesheetStatus.CLOSED, startDate, endDateMidnight)) {
                    if (getCurrentEstimatePlan(timeSheetActivity.getAtrApplication()) != null && getCurrentEstimatePlan(timeSheetActivity.getAtrApplication()).getStatus().equals(EstimatePlanStatus.ESTIMATE_PUBLISHED)) {
                        double estimateTotal = getCurrentEstimatePlan(timeSheetActivity.getAtrApplication()).getGrandTotal();
                        double actualTotal = 0.0;
                        EstimateVarienceHelper estimateVarienceHelper = new EstimateVarienceHelper();
                        estimateVarienceHelper.setApplication(timeSheetActivity.getAtrApplication());
                        estimateVarienceHelper.setActualTotal(actualTotal);
                        estimateVarienceHelper.setEstimatedTotal(estimateTotal);
                        estimateVarienceHelper.setTotalInvoiced(actualTotal - estimateTotal);
                        estimateVarienceHelpers.add(estimateVarienceHelper);
                    }
                }
                reset().setEstimationVarienece(true);
                break;
            default:
                reset().setList(true);
        }
    }

    private void getApplicationsByStatus(List<Status> statuses, Date startDate, Date endDate) {
        statuses.add(Status.RULING_REQUIRES_AUTHORISATION);
        statuses.add(Status.RULING_PUBLISHED_PENDING_OUTSTANDING_AMOUNT);
        statuses.add(Status.CASE_FINALISED);

        List<UserAtrApplication> userApps = userAppService.findByStatusesAndDate(statuses, startDate, endDate);
        Map<Long, UserAtrApplication> uniqueMap = userApps.stream()
                .collect(Collectors.toMap(
                        u -> u.getAtrApplication().getId(),
                        Function.identity(),
                        (existing, replacement) -> existing));
        userAtrApplications = new ArrayList<>(uniqueMap.values());

        for (UserAtrApplication user : userApps) {
            if (user.getResourceType().equals(ResourceType.MANAGER)) {
                manager = user.getUser();
            }

        }
    }

    private List<UserAtrApplication> getApplicationsByDate(ATRApplication selectedApplication, Date startDate, Date endDate) {
        List<UserAtrApplication> allUserApps = userAppService.findByAtrApplicationAndDate(selectedApplication, startDate, endDate);

        Map<Long, UserAtrApplication> uniqueResourcesMap = allUserApps.stream()
                .filter(user -> user.getResourceType() != null && !user.getResourceType().equals(ResourceType.APPLICANT))
                .collect(Collectors.toMap(
                        user -> user.getUser().getId(),
                        Function.identity(),
                        (existing, replacement) -> existing
                ));
        for (UserAtrApplication user : allUserApps) {
            if (user.getResourceType().equals(ResourceType.MANAGER)) {
                manager = user.getUser();
            }
        }

        return new ArrayList<>(uniqueResourcesMap.values());
    }

    private List<UserAtrApplication> findAllApps(Date startDate, Date endDate) {
        List<UserAtrApplication> allUserApps = userAppService.findByDate(startDate, endDate);
        Map<Long, UserAtrApplication> uniqueResourcesMap = allUserApps.stream()
                .filter(user -> user.getResourceType() != null && !user.getResourceType().equals(ResourceType.APPLICANT))
                .collect(Collectors.toMap(
                        user -> user.getAtrApplication().getId(),
                        Function.identity(),
                        (existing, replacement) -> existing
                ));

        for (UserAtrApplication userApp : allUserApps) {
            ATRApplication app = userApp.getAtrApplication();
            User user = userApp.getUser();
            if (userApp.getResourceType() == ResourceType.MANAGER) {
                managersMap.computeIfAbsent(app, manager -> new ArrayList<>()).add(user);
            } else if (userApp.getResourceType() == ResourceType.PRIMARY_RESOURCE) {
                primaryResourcesMap.computeIfAbsent(app, resource -> new ArrayList<>()).add(user);
            }
        }

        return new ArrayList<>(uniqueResourcesMap.values());
    }

    public String getPrimaryResources(UserAtrApplication userApp) {
        ATRApplication app = userApp.getAtrApplication();
        List<User> primaryResources = primaryResourcesMap.get(app);
        if (primaryResources == null) {
            return "";
        }
        return primaryResources.stream()
                .map(User::getFullName)
                .distinct()
                .collect(Collectors.joining(", "));
    }

    public String getManagers(UserAtrApplication userApp) {
        ATRApplication app = userApp.getAtrApplication();
        List<User> managers = managersMap.get(app);
        if (managers == null) {
            return "";
        }
        return managers.stream()
                .map(User::getFullName)
                .distinct()
                .collect(Collectors.joining(", "));
    }

    private List<Activity> getUserActivities(List<UserApplicationActivity> resources) {
        return resources.stream()
                .map(UserApplicationActivity::getActivity)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Activity> getActivitiesForApplication(ATRApplication selectedApplication, Date startDate, Date endDate) {
        resources = applicationActivityService.findByAtrApplicationAndDate(selectedApplication, startDate, endDate);
        return getUserActivities(resources);
    }

    public String getUsersForActivity(Activity activity, List<UserApplicationActivity> allActivities) {
        Map<User, Double> userActualHoursMap = timeSheetActivities.stream()
                .filter(act -> act.getActivity().equals(activity))
                .collect(Collectors.groupingBy(
                        sheetActivity -> sheetActivity.getResource(),
                        Collectors.summingDouble(sheetActivity -> sheetActivity.getNumberOfHours())
                ));

        return "<b>Total</b>" + (userActualHoursMap.isEmpty() ? "" : "<br/>") + userActualHoursMap.keySet().stream()
                .map(act -> act.getFullName())
                .distinct()
                .collect(Collectors.joining("<br/>"));
    }

    public String getUserActualHours(Activity activity) {

        double totalActualHours = timeSheetActivities.stream()
                .filter(act -> act.getActivity().equals(activity))
                .mapToDouble(act -> act.getNumberOfHours())
                .sum();

        Map<User, Double> userActualHoursMap = timeSheetActivities.stream()
                .filter(act -> act.getActivity().equals(activity))
                .collect(Collectors.groupingBy(
                        sheetActivity -> sheetActivity.getResource(),
                        Collectors.summingDouble(sheetActivity -> sheetActivity.getNumberOfHours())
                ));

        if (userActualHoursMap.isEmpty()) {
            return "<b>0.00</b>";
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<b>");
        stringBuilder.append(totalActualHours);
        stringBuilder.append("</b>").append(userActualHoursMap.isEmpty() ? "" : "<br/>");

        Iterator<Map.Entry<User, Double>> it = userActualHoursMap.entrySet().iterator();
        while (it.hasNext()) {
            stringBuilder.append(it.next().getValue());
            if (it.hasNext()) {
                stringBuilder.append("<br/>");
            }
        }
        return stringBuilder.toString();
    }

    public String repeatBreaks(double value, Activity activity) {
        StringBuilder sb = new StringBuilder();
        sb.append("<b>");
        sb.append(value);
        sb.append("</b>");
        int count = timeSheetActivities.stream()
                .filter(act -> act.getActivity().equals(activity))
                .collect(Collectors.groupingBy(
                        sheetActivity -> sheetActivity.getResource(),
                        Collectors.counting())
                ).size();
        
        while(count-- >= 0) {
            sb.append("<br/>");
        }

//        Map<User, Double> userActualHoursMap = timeSheetActivities.stream()
//                .filter(act -> act.getActivity().equals(activity))
//                .collect(Collectors.groupingBy(
//                        sheetActivity -> sheetActivity.getResource(),
//                        Collectors.summingDouble(sheetActivity -> sheetActivity.getNumberOfHours())
//                ));
//        if (!userActualHoursMap.isEmpty()) {
//            userActualHoursMap.keySet()
//                    .stream()
//                    .forEach(key -> sb.append("<br/>"));
//            sb.append("<br/>");
//        }
        return sb.toString();
    }

    public String getEstimateHours(EstimatePlan estimatePlan, Activity activity) {

        double hours = estimatePlan.getEstimateRecords().stream()
                .filter(record -> record.getDescription().equals(activity.getDescription()))
                .collect(Collectors.summingDouble(record -> record.getConsultantHours() + record.getManagerHours()));

        return repeatBreaks(hours, activity);
    }

    public String getEstimateAmount(EstimatePlan estimatePlan, Activity activity) {

        double hours = estimatePlan.getEstimateRecords().stream()
                .filter(record -> record.getDescription().equals(activity.getDescription()))
                .filter(record -> record.getActivityType().equals(ActivityType.BILLABLE))
                .collect(Collectors.summingDouble(record -> (record.getConsultantHours() + record.getManagerHours()) * record.getHourlyRate()));

        return repeatBreaks(hours, activity);
    }

    public String getActSectionsDescription(UserAtrApplication application) {
        return application.getAtrApplication().getRulingActSections().stream()
                .map(sec -> sec.getActSection().getDescription())
                .collect(Collectors.joining("<br/>"));
    }

    public String getActSectionsTypes(UserAtrApplication application) {
        TaxAct taxAct = application.getAtrApplication().getRulingActSections().get(0).getActSection().getTaxAct();
        String ActPrefix = "";
        switch (taxAct) {
            case INCOME_TAX_ACT:
                ActPrefix = "ITA - s ";
                break;
            case SECURITIES_TRANSFER_TAX_ACT:
                ActPrefix = "STT - s ";
                break;
            case TRANSFER_DUTY_ACT:
                ActPrefix = "TDA - s ";
                break;
            case VAT_TAX_ACT:
                ActPrefix = "VAT - s ";
                break;
        }
        return ActPrefix + application.getAtrApplication().getRulingActSections().stream()
                .map(sec -> sec.getActSection().getType())
                .collect(Collectors.joining("; "));
    }

    public void reportListener() {
        if (reportType.equals(ReportType.PROGRESS_TRACKING_DAYS) || reportType.equals(ReportType.OPEN_APPLICATION) || reportType.equals(ReportType.REJECTED_WITHDRAWN_APPLICATION)) {
            rulingTypes.clear();
            rulingActive = Boolean.TRUE;
            userActive = Boolean.FALSE;
            activity = Boolean.FALSE;
            rulingTypes.addAll(Arrays.asList(RulingType.values()));
        }
        if (reportType.equals(ReportType.STUFF_HOURS_ACTIVITY) || reportType.equals(ReportType.CHARGE_PER_RESOURCE)) {
            users.clear();
            userActivities.clear();
            if (reportType.equals(ReportType.CHARGE_PER_RESOURCE)) {
                userActive = Boolean.TRUE;
                activity = Boolean.FALSE;
                rulingActive = Boolean.FALSE;
            } else {
                userActive = Boolean.TRUE;
                activity = Boolean.TRUE;
                rulingActive = Boolean.FALSE;
            }
            users = userService.findAll();
            userActivities = activityService.findAll();
        }
    }

    public String getActSectionsDescription(ATRApplication application) {
        return application.getRulingActSections().stream()
                .map(sec -> sec.getActSection().getDescription())
                .collect(Collectors.joining("<br/>"));
    }

    public ProductivitySummaryReportDTO getTotalsRow() {
        return productivitySummaryReport.isEmpty() ? null : productivitySummaryReport.get(productivitySummaryReport.size() - 1);
    }

    public String htmlToText(String value) {
        if (value == null) {
            return "";
        }
        // Convert <br> to Excel-style newlines
        String withBreaks = value.replaceAll("(?i)<br\\s*/?>", "_");
        // Strip all other HTML
        return Jsoup.parse(withBreaks).text().replaceAll("_", "\r\n");
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<TimeSheetHelper> getTimeSheets() {
        return timeSheets;
    }

    public void setTimeSheets(List<TimeSheetHelper> timeSheets) {
        this.timeSheets = timeSheets;
    }

    public List<ReportType> getReportTypes() {
        return reportTypes;
    }

    public void setReportTypes(List<ReportType> reportTypes) {
        this.reportTypes = reportTypes;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public List<StuffHoursHelper> getStuffHoursHelpers() {
        return stuffHoursHelpers;
    }

    public void setStuffHoursHelpers(List<StuffHoursHelper> stuffHoursHelpers) {
        this.stuffHoursHelpers = stuffHoursHelpers;
    }

    public List<OpenApplicationHelper> getOpenApplications() {
        return openApplications;
    }

    public void setOpenApplications(List<OpenApplicationHelper> openApplications) {
        this.openApplications = openApplications;
    }

    public List<ChargePerResourceHelper> getChargePerResourceHelpers() {
        return chargePerResourceHelpers;
    }

    public void setChargePerResourceHelpers(List<ChargePerResourceHelper> chargePerResourceHelpers) {
        this.chargePerResourceHelpers = chargePerResourceHelpers;
    }

    public List<StatusChangeDateHelper> getStatusChangeDateHelpers() {
        return statusChangeDateHelpers;
    }

    public void setStatusChangeDateHelpers(List<StatusChangeDateHelper> statusChangeDateHelpers) {
        this.statusChangeDateHelpers = statusChangeDateHelpers;
    }

    public List<RulingType> getRulingTypes() {
        return rulingTypes;
    }

    public void setRulingTypes(List<RulingType> rulingTypes) {
        this.rulingTypes = rulingTypes;
    }

    public RulingType getRullingType() {
        return rullingType;
    }

    public void setRullingType(RulingType rullingType) {
        this.rullingType = rullingType;
    }

    public boolean isRulingActive() {
        return rulingActive;
    }

    public void setRulingActive(boolean rulingActive) {
        this.rulingActive = rulingActive;
    }

    public List<ProgressTrackingDaysHelper> getTrackingDaysHelpers() {
        return trackingDaysHelpers;
    }

    public void setTrackingDaysHelpers(List<ProgressTrackingDaysHelper> trackingDaysHelpers) {
        this.trackingDaysHelpers = trackingDaysHelpers;
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }

    public List<RulingActSection> getRulingSections() {
        return rulingSections;
    }

    public void setRulingSections(List<RulingActSection> rulingSections) {
        this.rulingSections = rulingSections;
    }

    public List<ActSection> getSections() {
        return sections;
    }

    public void setSections(List<ActSection> sections) {
        this.sections = sections;
    }

    public List<TimesheetStatus> getTimesheetStatuses() {
        return timesheetStatuses;
    }

    public void setTimesheetStatuses(List<TimesheetStatus> timesheetStatuses) {
        this.timesheetStatuses = timesheetStatuses;
    }

    public List<EstimateVarienceHelper> getEstimateVarienceHelpers() {
        return estimateVarienceHelpers;
    }

    public void setEstimateVarienceHelpers(List<EstimateVarienceHelper> estimateVarienceHelpers) {
        this.estimateVarienceHelpers = estimateVarienceHelpers;
    }

    public List<UserAtrApplication> getUserAtrApplications() {
        return userAtrApplications;
    }

    public void setUserAtrApplications(List<UserAtrApplication> userAtrApplications) {
        this.userAtrApplications = userAtrApplications;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public List<ATRApplication> getAtrApplications() {
        return atrApplications;
    }

    public void setAtrApplications(List<ATRApplication> atrApplications) {
        this.atrApplications = atrApplications;
    }

    public ATRApplication getSelectedApplication() {
        return selectedApplication;
    }

    public void setSelectedApplication(ATRApplication selectedApplication) {
        this.selectedApplication = selectedApplication;
    }

    public List<UserApplicationActivity> getUserApplicationActivitys() {
        return userApplicationActivitys;
    }

    public void setUserApplicationActivitys(List<UserApplicationActivity> userApplicationActivitys) {
        this.userApplicationActivitys = userApplicationActivitys;
    }

    public List<Activity> getUserActivities() {
        return userActivities;
    }

    public void setUserActivities(List<Activity> userActivities) {
        this.userActivities = userActivities;
    }

    public List<UserApplicationActivity> getResources() {
        return resources;
    }

    public void setResources(List<UserApplicationActivity> resources) {
        this.resources = resources;
    }

    public User getPrimaryResource() {
        return primaryResource;
    }

    public void setPrimaryResource(User primaryResource) {
        this.primaryResource = primaryResource;
    }

    public Map<ATRApplication, List<User>> getManagersMap() {
        return managersMap;
    }

    public void setManagersMap(Map<ATRApplication, List<User>> managersMap) {
        this.managersMap = managersMap;
    }

    public Map<ATRApplication, List<User>> getPrimaryResourcesMap() {
        return primaryResourcesMap;
    }

    public void setPrimaryResourcesMap(Map<ATRApplication, List<User>> primaryResourcesMap) {
        this.primaryResourcesMap = primaryResourcesMap;
    }

    public double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(double totalHours) {
        this.totalHours = totalHours;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public boolean isUserActive() {
        return userActive;
    }

    public void setUserActive(boolean userActive) {
        this.userActive = userActive;
    }

    public Activity getSelectedActivity() {
        return selectedActivity;
    }

    public void setSelectedActivity(Activity selectedActivity) {
        this.selectedActivity = selectedActivity;
    }

    public boolean isActivity() {
        return activity;
    }

    public void setActivity(boolean activity) {
        this.activity = activity;
    }

    public Map<Long, Double> getStaffHoursMap() {
        return staffHoursMap;
    }

    public void setStaffHoursMap(Map<Long, Double> staffHoursMap) {
        this.staffHoursMap = staffHoursMap;
    }

    public List<ProductivitySummaryReportDTO> getProductivitySummaryReport() {
        return productivitySummaryReport;
    }

    public void setProductivitySummaryReport(List<ProductivitySummaryReportDTO> productivitySummaryReport) {
        this.productivitySummaryReport = productivitySummaryReport;
    }

    public List<TimeSheetActivity> getTimeSheetActivities() {
        return timeSheetActivities;
    }

    public void setTimeSheetActivities(List<TimeSheetActivity> timeSheetActivities) {
        this.timeSheetActivities = timeSheetActivities;
    }

}
