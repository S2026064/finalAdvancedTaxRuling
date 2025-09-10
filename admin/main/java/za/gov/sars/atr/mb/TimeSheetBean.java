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
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import za.gov.sars.common.DateUtil;
import za.gov.sars.common.TimesheetStatus;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.Activity;
import za.gov.sars.domain.TimeSheetActivity;
import za.gov.sars.domain.Timesheet;
import za.gov.sars.domain.UserApplicationActivity;
import za.gov.sars.service.AtrApplicationServiceLocal;
import za.gov.sars.service.TimeSheetActivityServiceLocal;
import za.gov.sars.service.TimesheetServiceLocal;
import za.gov.sars.service.UserApplicationActivityServiceLocal;

/**
 *
 * @author S2028398
 */
@ManagedBean
@ViewScoped
public class TimeSheetBean extends BaseBean<Timesheet> {

    @Autowired
    private TimesheetServiceLocal timesheetService;
    @Autowired
    private UserApplicationActivityServiceLocal userApplicationActivityService;
    @Autowired
    private AtrApplicationServiceLocal applicationService;
    @Autowired
    private TimeSheetActivityServiceLocal timeSheetActivityService;

    private List<UserApplicationActivity> userAtrApplicationActivities = new ArrayList<>();

    private List<TimesheetStatus> timesheetStatuses = new ArrayList<>();
    private List<ATRApplication> atrApplications = new ArrayList<>();
    private ATRApplication selectedAtrApplications;
    private List<Activity> activities = new ArrayList<>();
    private Page<Timesheet> slices;

    private Date minDate;
    private Date maxDate;
    private Calendar calendar = Calendar.getInstance();
    private boolean timeActivity = Boolean.FALSE;

    @PostConstruct
    public void init() {
        reset().setAdd(true);
        userAtrApplicationActivities = userApplicationActivityService.findAtrApplicationsByUserId(getActiveUser().getUser().getId());
        ATRApplication thisATRApplications;
        for (UserApplicationActivity userApplicationActivity : userAtrApplicationActivities) {
            thisATRApplications = userApplicationActivity.getAtrApplication();
            ATRApplication aTRApplication = applicationService.findByCaseNum(thisATRApplications.getCaseNum());
            //pending confirmation
//           !(aTRApplication.getStatus().equals(Status.CASE_WITHDRAWN) || aTRApplication.getStatus().equals(Status.SUBMITTED_AND_PAID))
            if (!atrApplications.contains(aTRApplication)) {
                atrApplications.add(thisATRApplications);
            }
        }
        timesheetStatuses.addAll(Arrays.asList(TimesheetStatus.values()));
        addOrUpdate(timesheetService.findByResource(getActiveUser().getUser()));
    }

    public void addOrUpdate(Timesheet timesheet) {
        reset().setAdd(true);
        if (timesheet != null) {
            timesheet.setUpdatedBy(getActiveUser().getSid());
            timesheet.setUpdatedDate(new Date());
            if (!timesheet.getTimeSheetActivities().isEmpty()) {
                List<TimeSheetActivity> timeSheetActivities = timesheet.getTimeSheetActivities().stream().filter(activity -> !(TimesheetStatus.CLOSED).equals(activity.getTimesheetStatus())).collect(Collectors.toList());
                timesheet.addTimeSheetActivities(timeSheetActivities);
            }
        } else {
            timesheet = new Timesheet();
            timesheet.setCreatedBy(getActiveUser().getSid());
            timesheet.setCreatedDate(new Date());
            timesheet.setResource(getActiveUser().getUser());

            addCollection(timesheet);
        }
        addEntity(timesheet);
    }

    public void addTimeSheetActivity() {
        TimeSheetActivity timeSheetActivity = new TimeSheetActivity();
        if (timeSheetActivity.getId() == null) {
            timeSheetActivity = new TimeSheetActivity();
            timeSheetActivity.setCreatedBy(getActiveUser().getSid());
            timeSheetActivity.setCreatedDate(new Date());
            timeSheetActivity.setCapturedDate(getEntity().getStartDate());
        } else {
            timeSheetActivity.setUpdatedBy(getActiveUser().getSid());
            timeSheetActivity.setUpdatedDate(new Date());
        }
        getEntity().addTimeSheetActivity(timeSheetActivity);
    }

    public void removeTimeSheetActivity(TimeSheetActivity timeSheetActivity) {
        if (timeSheetActivity.getId() != null) {
            getEntity().getTimeSheetActivities().remove(timeSheetActivity);
            addEntity(timesheetService.update(getEntity()));
            timeSheetActivityService.deleteById(timeSheetActivity.getId());
            addEntity(timesheetService.update(getEntity()));
        } else {
            if (getEntity().getTimeSheetActivities().contains(timeSheetActivity)) {
                getEntity().removeTimeSheetActivity(timeSheetActivity);
            }
        }
        addInformationMessage("Record has been successfully removed ");
    }

    public void onAtrApplicationChange(SelectEvent event) {
        activities.clear();
        ATRApplication selectedAtrApplication = (ATRApplication) event.getObject();
        userAtrApplicationActivities = userApplicationActivityService.findActivitiesByUserIdAndAtrApplicationId(getActiveUser().getUser().getId(), selectedAtrApplication.getId());
        Activity activity;
        for (UserApplicationActivity userApplicationActivity : userAtrApplicationActivities) {
            activity = userApplicationActivity.getActivity();
            activities.add(activity);
        }

    }

    public void onTimseheetUpdate(Timesheet timesheet) {
        for (TimeSheetActivity timeSheetActivity : timesheet.getTimeSheetActivities()) {
            timeSheetActivity.setActivity(userApplicationActivityService.findByAtrApplicationAndActivity(timeSheetActivity.getAtrApplication(), timeSheetActivity.getActivity()).getActivity());
        }
    }

    public void save(Timesheet timesheet) {
        if (timesheet.getId() != null) {
            for (TimeSheetActivity timeSheetActivity : timesheet.getTimeSheetActivities()) {
                if (timeSheetActivity.getTimesheetStatus() != null) {
                    if (!timeSheetActivity.getTimesheetStatus().equals(TimesheetStatus.SUBMITTED)) {
                        timeSheetActivity.setStartDate(getEntity().getStartDate());
                        timeSheetActivity.setEndDate(getEntity().getEndDate());
                        timeSheetActivity.setTimesheetStatus(TimesheetStatus.SAVED);
                        timeSheetActivity.setTimesheet(timesheet);
                        timeSheetActivity.setResource(getActiveUser().getUser());
                    }
                } else {
                    timeSheetActivity.setStartDate(getEntity().getStartDate());
                    timeSheetActivity.setEndDate(getEntity().getEndDate());
                    timeSheetActivity.setTimesheetStatus(TimesheetStatus.SAVED);
                    timeSheetActivity.setTimesheet(timesheet);
                    timeSheetActivity.setResource(getActiveUser().getUser());
                }
            }
            timesheetService.update(timesheet);
            addInformationMessage("Time Sheet was successfully updated.");
        } else {
            for (TimeSheetActivity timeSheetActivity : timesheet.getTimeSheetActivities()) {
                timeSheetActivity.setStartDate(getEntity().getStartDate());
                timeSheetActivity.setEndDate(getEntity().getEndDate());
                timeSheetActivity.setTimesheetStatus(TimesheetStatus.SAVED);
                timeSheetActivity.setTimesheet(timesheet);
                timeSheetActivity.setResource(getActiveUser().getUser());
            }
            timesheet.setTimesheetStatus(TimesheetStatus.SAVED);
            timesheetService.save(timesheet);
            addInformationMessage("Time Sheet was successfully created.");
        }
        reset().setAdd(true);
    }

    public void submit(Timesheet timesheet) {
        if (timesheet.getEndDate().before(timesheet.getStartDate())) {
            addWarningMessage("End date cannot be before start date !");
            return;
        }

        for (TimeSheetActivity timeSheetActivity : timesheet.getTimeSheetActivities()) {
            if (timeSheetActivity.getTimesheetStatus() != null) {
                if (timeSheetActivity.getTimesheetStatus().equals(TimesheetStatus.SAVED)) {
                    timeSheetActivity.setStartDate(getEntity().getStartDate());
                    timeSheetActivity.setEndDate(getEntity().getEndDate());
                    timeSheetActivity.setTimesheetStatus(TimesheetStatus.SUBMITTED);
                    timeSheetActivity.setTimesheet(timesheet);
                    timeSheetActivity.setResource(getActiveUser().getUser());
                }
            } else {
                timeSheetActivity.setStartDate(getEntity().getStartDate());
                timeSheetActivity.setEndDate(getEntity().getEndDate());
                timeSheetActivity.setTimesheetStatus(TimesheetStatus.SUBMITTED);
                timeSheetActivity.setTimesheet(timesheet);
                timeSheetActivity.setResource(getActiveUser().getUser());
            }
        }
        timesheet.setTimesheetStatus(TimesheetStatus.SUBMITTED);
        timesheetService.update(timesheet);
        addInformationMessage("Time Sheet was successfully submitted.");
        reset().setAdd(true);
    }

    public void cancel(Timesheet timesheet) {
        if (timesheet.getId() == null && getTimesheets().contains(timesheet)) {
            remove(timesheet);
        }
        reset().setAdd(true);
    }

    public void delete(TimeSheetActivity timeSheetActivity) {
        getEntity().getTimeSheetActivities().remove(timeSheetActivity);
        addEntity(timesheetService.update(getEntity()));
        timeSheetActivityService.deleteById(timeSheetActivity.getId());
        addInformationMessage("Time Sheet Activity was successfully deleted");
        reset().setList(true);
    }

    public void addComment(TimeSheetActivity timeSheetActivity) {
        timeSheetActivity.setDescriptionActive(true);
        timeSheetActivity.setDescription(timeSheetActivity.getDescription());
    }

    public void removeComment(TimeSheetActivity timeSheetActivity) {
        timeSheetActivity.setDescriptionActive(false);
        timeSheetActivity.setDescription(null);
    }

    public Date getMinDate() {
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        if (calendar.getTime().after(new Date())) {
            calendar.add(Calendar.WEEK_OF_YEAR, -1);
        }
        minDate = calendar.getTime();
        return minDate;
    }

    public Date getMaxDate() {
        if (calendar.getTime().before(new Date()) || calendar.getTime().equals(new Date())) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }
        maxDate = calendar.getTime();
        return maxDate;
    }

    public String getStartDate() {
        if (minDate == null) {
            return "";
        }
        return DateUtil.convertStringToDate(minDate);
    }

    public List<Timesheet> getTimesheets() {
        return this.getCollections();
    }

    public List<TimesheetStatus> getTimesheetStatuses() {
        return timesheetStatuses;
    }

    public void setTimesheetStatuses(List<TimesheetStatus> timesheetStatuses) {
        this.timesheetStatuses = timesheetStatuses;
    }

    public List<ATRApplication> getAtrApplications() {
        return atrApplications;
    }

    public void setAtrApplications(List<ATRApplication> atrApplications) {
        this.atrApplications = atrApplications;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public ATRApplication getSelectedAtrApplications() {
        return selectedAtrApplications;
    }

    public void setSelectedAtrApplications(ATRApplication selectedAtrApplications) {
        this.selectedAtrApplications = selectedAtrApplications;
    }

    public List<UserApplicationActivity> getUserAtrApplicationActivities() {
        return userAtrApplicationActivities;
    }

    public void setUserAtrApplicationActivities(List<UserApplicationActivity> userAtrApplicationActivities) {
        this.userAtrApplicationActivities = userAtrApplicationActivities;
    }

    public Page<Timesheet> getSlices() {
        return slices;
    }

    public void setSlices(Page<Timesheet> slices) {
        this.slices = slices;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public boolean isTimeActivity() {
        return timeActivity;
    }

    public void setTimeActivity(boolean timeActivity) {
        this.timeActivity = timeActivity;
    }

}
