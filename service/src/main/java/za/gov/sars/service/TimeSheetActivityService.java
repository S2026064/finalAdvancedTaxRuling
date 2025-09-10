/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.service;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.gov.sars.common.TimesheetStatus;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.Activity;
import za.gov.sars.domain.TimeSheetActivity;
import za.gov.sars.domain.User;
import za.gov.sars.persistence.TimeSheetActivityRepository;

/**
 *
 * @author S2028398
 */
@Service
@Transactional
public class TimeSheetActivityService implements TimeSheetActivityServiceLocal {

    @Autowired
    private TimeSheetActivityRepository timeSheetActivityRepository;

    @Override
    public TimeSheetActivity findById(Long id) {
        return timeSheetActivityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                                "The requested id [" + id
                                + "] does not exist."));
    }

    @Override
    public TimeSheetActivity deleteById(Long id) {
        TimeSheetActivity timeSheetActivity = findById(id);
        if (timeSheetActivity != null) {
            timeSheetActivityRepository.delete(timeSheetActivity);
        }
        return timeSheetActivity;
    }

    @Override
    public Page<TimeSheetActivity> findByAtrApplicationAndTimesheetStatus(ATRApplication atrApplication, TimesheetStatus timesheetStatus, Pageable pageable) {
        return timeSheetActivityRepository.findByAtrApplicationAndTimesheetStatus(atrApplication, timesheetStatus, pageable);
    }

    @Override
    public List<TimeSheetActivity> findByTimesheetStatusesAndDate(List<TimesheetStatus> timesheetStatus, Date createdDateStart, Date createdDateEndDate) {
        return timeSheetActivityRepository.findByTimesheetStatusesAndDate(timesheetStatus, createdDateStart, createdDateEndDate);
    }

    @Override
    public List<TimeSheetActivity> findByAtrApplicationAndTimesheetStatusAndDate(ATRApplication atrApplication, TimesheetStatus timesheetStatus, Date createdDateStart, Date createdDateEndDate) {
        return timeSheetActivityRepository.findByAtrApplicationAndTimesheetStatusAndDate(atrApplication, timesheetStatus, createdDateStart, createdDateEndDate);
    }

    @Override
    public TimeSheetActivity update(TimeSheetActivity entity) {
        return timeSheetActivityRepository.save(entity);
    }

    @Override
    public List<TimeSheetActivity> findByTimesheetStatusAndDate(TimesheetStatus timesheetStatus, Date createdDateStart, Date createdDateEndDate) {
        return timeSheetActivityRepository.findByTimesheetStatusAndDate(timesheetStatus, createdDateStart, createdDateEndDate);

    }

    @Override
    public List<TimeSheetActivity> findByResourceAndTimesheetStatus(User resource, TimesheetStatus timesheetStatus) {
        return timeSheetActivityRepository.findByResourceAndTimesheetStatus(resource, timesheetStatus);
    }

    @Override
    public List<TimeSheetActivity> findByDate(Date createdDateStart, Date createdDateEndDate) {
        return timeSheetActivityRepository.findByDate(createdDateStart, createdDateEndDate);
    }

    @Override
    public List<TimeSheetActivity> findByUserAndActivityAndTimesheetStatusAndDate(User resource, Activity activity, TimesheetStatus timesheetStatus, Date createdDateStart, Date createdDateEndDate) {
        return timeSheetActivityRepository.findByUserAndActivityAndTimesheetStatusAndDate(resource, activity, timesheetStatus, createdDateStart, createdDateEndDate);
    }

    @Override
    public List<TimeSheetActivity> findByUserAndTimesheetStatusAndDate(User resource, TimesheetStatus timesheetStatus, Date createdDateStart, Date createdDateEndDate) {
    return timeSheetActivityRepository.findByUserAndTimesheetStatusAndDate(resource, timesheetStatus, createdDateStart, createdDateEndDate);
    }

    @Override
    public Page<TimeSheetActivity> findByAtrApplication(ATRApplication atrApplication, Pageable pageable) {
        return timeSheetActivityRepository.findByAtrApplication(atrApplication, pageable);
    }

    @Override
    public List<TimeSheetActivity> findByAtrApplicationAndDate(ATRApplication atrApplication, Date createdDateStart, Date createdDateEndDate) {
        return timeSheetActivityRepository.findByAtrApplicationAndDate(atrApplication, createdDateStart, createdDateEndDate);
    }

}
