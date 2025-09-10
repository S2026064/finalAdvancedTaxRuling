/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.service;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import za.gov.sars.common.TimesheetStatus;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.Activity;
import za.gov.sars.domain.TimeSheetActivity;
import za.gov.sars.domain.User;

/**
 *
 * @author S2030702
 */
public interface TimeSheetActivityServiceLocal {

    TimeSheetActivity findById(Long id);

    TimeSheetActivity update(TimeSheetActivity entity);

    TimeSheetActivity deleteById(Long id);

    Page<TimeSheetActivity> findByAtrApplicationAndTimesheetStatus(ATRApplication atrApplication, TimesheetStatus timesheetStatus, Pageable pageable);
    
    Page<TimeSheetActivity> findByAtrApplication(ATRApplication atrApplication, Pageable pageable);

    List<TimeSheetActivity> findByTimesheetStatusesAndDate(List<TimesheetStatus> timesheetStatus, Date createdDateStart, Date createdDateEndDate);

    List<TimeSheetActivity> findByAtrApplicationAndTimesheetStatusAndDate(ATRApplication atrApplication, TimesheetStatus timesheetStatus, Date createdDateStart, Date createdDateEndDate);
    
    List<TimeSheetActivity> findByAtrApplicationAndDate(ATRApplication atrApplication,  Date createdDateStart, Date createdDateEndDate);

    List<TimeSheetActivity> findByTimesheetStatusAndDate(TimesheetStatus timesheetStatus, Date createdDateStart, Date createdDateEndDate);

    List<TimeSheetActivity> findByResourceAndTimesheetStatus(User resource, TimesheetStatus timesheetStatus);

    List<TimeSheetActivity> findByDate(Date createdDateStart, Date createdDateEndDate);

    List<TimeSheetActivity> findByUserAndActivityAndTimesheetStatusAndDate(User resource, Activity activity, TimesheetStatus timesheetStatus, Date createdDateStart, Date createdDateEndDate);

    List<TimeSheetActivity> findByUserAndTimesheetStatusAndDate(User resource, TimesheetStatus timesheetStatus, Date createdDateStart, Date createdDateEndDate);

}
