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
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.springframework.beans.factory.annotation.Autowired;
import za.gov.sars.common.CloseType;
import za.gov.sars.common.Status;
import za.gov.sars.common.TimesheetStatus;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.TimeSheetActivity;
import za.gov.sars.domain.Timesheet;
import za.gov.sars.service.AtrApplicationServiceLocal;
import za.gov.sars.service.TimeSheetActivityServiceLocal;
import za.gov.sars.service.TimesheetServiceLocal;

/**
 *
 * @author S2028398
 */
@ManagedBean
@ViewScoped
public class ReviewTimeSheetBean extends BaseBean<Timesheet> {

    @Autowired
    private TimesheetServiceLocal timesheetService;
    @Autowired
    private AtrApplicationServiceLocal atrApplicationService;
    @Autowired
    private TimeSheetActivityServiceLocal timeSheetActivityService;

    private List<CloseType> closeTypes = new ArrayList<>();
    private List<ATRApplication> atrApplications = new ArrayList<>();

    private CloseType closeType;
    private ATRApplication selectedATRApplication;
    private Date startDate;
    private Date endDate;
    private boolean applicationActive = Boolean.FALSE;
    private boolean periodActive = Boolean.FALSE;

    @PostConstruct
    public void init() {
        reset().setList(true);
        closeTypes.addAll(Arrays.asList(CloseType.values()));

    }

    public void review(Timesheet timesheet) {
        addEntity(timesheet);
        reset().setAdd(true);
    }

    public void approve(Timesheet timesheet) {
        timesheet.setUpdatedBy(getActiveUser().getSid());
        timesheet.setUpdatedDate(new Date());
        timesheet.setTimesheetStatus(TimesheetStatus.CLOSED);
        timesheetService.update(timesheet);
        addInformationMessage("Time Sheet was successfully approved.");
        synchronize(timesheet);
        reset().setList(true);
    }

    public void cancel() {
        reset().setList(true);
    }

    public void closeTypeListner() {
        if (closeType.equals(CloseType.INDIVIDUAL)) {
            applicationActive = Boolean.TRUE;
            periodActive = Boolean.FALSE;
            atrApplications = atrApplications = atrApplicationService.findAll();
        } else {
            applicationActive = Boolean.FALSE;
            periodActive = Boolean.TRUE;
        }
    }

    public void closeTimesheets() {
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

        if (closeType.equals(CloseType.ALL)) {
            List<TimeSheetActivity> allTimesheet = timeSheetActivityService.findByTimesheetStatusAndDate(TimesheetStatus.SUBMITTED, startDate, endDateMidnight);
            if (allTimesheet.size() > 0) {
                for (TimeSheetActivity timesheet : allTimesheet) {
                    timesheet.setUpdatedBy(getActiveUser().getSid());
                    timesheet.setUpdatedDate(new Date());
                    timesheet.setTimesheetStatus(TimesheetStatus.CLOSED);
                    timeSheetActivityService.update(timesheet);
                    if (!timesheet.getAtrApplication().getStatus().equals(Status.CHARGESHEET_IN_PROGRESS)) {
                        timesheet.getAtrApplication().setStatus(Status.CHARGESHEET_IN_PROGRESS);
                        atrApplicationService.update(timesheet.getAtrApplication());
                    }
                }
                addInformationMessage("Time sheets successfully closed");
            } else {
                addWarningMessage("No Time sheets found");
            }
        } else {
            List<TimeSheetActivity> allTimesheetByApplication = timeSheetActivityService.findByAtrApplicationAndTimesheetStatusAndDate(selectedATRApplication, TimesheetStatus.SUBMITTED, startDate, endDateMidnight);
            if (allTimesheetByApplication.size() > 0) {
                for (TimeSheetActivity timesheet : allTimesheetByApplication) {
                    timesheet.setUpdatedBy(getActiveUser().getSid());
                    timesheet.setUpdatedDate(new Date());
                    timesheet.setTimesheetStatus(TimesheetStatus.CLOSED);
                    timeSheetActivityService.update(timesheet);
                }
                selectedATRApplication.setStatus(Status.CHARGESHEET_IN_PROGRESS);
                atrApplicationService.update(selectedATRApplication);
                addInformationMessage("Time sheets successfully closed");
            } else {
                addWarningMessage("No Time sheets found");
            }
        }
        reset().setList(true);
    }

    public List<Timesheet> getTimesheets() {
        return this.getCollections();
    }

    public List<CloseType> getCloseTypes() {
        return closeTypes;
    }

    public void setCloseTypes(List<CloseType> closeTypes) {
        this.closeTypes = closeTypes;
    }

    public CloseType getCloseType() {
        return closeType;
    }

    public void setCloseType(CloseType closeType) {
        this.closeType = closeType;
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

    public ATRApplication getSelectedATRApplication() {
        return selectedATRApplication;
    }

    public void setSelectedATRApplication(ATRApplication selectedATRApplication) {
        this.selectedATRApplication = selectedATRApplication;
    }

    public boolean isApplicationActive() {
        return applicationActive;
    }

    public void setApplicationActive(boolean applicationActive) {
        this.applicationActive = applicationActive;
    }

    public boolean isPeriodActive() {
        return periodActive;
    }

    public void setPeriodActive(boolean periodActive) {
        this.periodActive = periodActive;
    }

    public List<ATRApplication> getAtrApplications() {
        return atrApplications;
    }

    public void setAtrApplications(List<ATRApplication> atrApplications) {
        this.atrApplications = atrApplications;
    }

}
