package za.gov.sars.persistence;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.gov.sars.common.TimesheetStatus;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.Activity;
import za.gov.sars.domain.TimeSheetActivity;
import za.gov.sars.domain.User;
import za.gov.sars.dto.ProductivitySummaryReportDTO;

/**
 *
 * @author S2026064
 */
@Repository
public interface TimeSheetActivityRepository extends JpaRepository<TimeSheetActivity, Long>, JpaSpecificationExecutor<TimeSheetActivity> {

    List<TimeSheetActivity> findByResourceAndTimesheetStatus(User resource, TimesheetStatus timesheetStatus);

    Page<TimeSheetActivity> findByAtrApplicationAndTimesheetStatus(ATRApplication atrApplication, TimesheetStatus timesheetStatus, Pageable pageable);

    Page<TimeSheetActivity> findByAtrApplication(ATRApplication atrApplication, Pageable pageable);

    @Query("SELECT e FROM TimeSheetActivity e WHERE e.timesheetStatus IN (:timesheetStatus) AND (CONVERT(date, e.capturedDate) >=:createdDateStart AND CONVERT(date, e.capturedDate)<=:createdDateEndDate)")
    List<TimeSheetActivity> findByTimesheetStatusesAndDate(@Param("timesheetStatus") List<TimesheetStatus> timesheetStatus, @Param("createdDateStart") Date createdDateStart, @Param("createdDateEndDate") Date createdDateEndDate);

    @Query("SELECT e FROM TimeSheetActivity e WHERE e.atrApplication=:atrApplication AND e.timesheetStatus=:timesheetStatus AND (CONVERT(date, e.capturedDate) >=:createdDateStart AND CONVERT(date, e.capturedDate)<=:createdDateEndDate)")
    List<TimeSheetActivity> findByAtrApplicationAndTimesheetStatusAndDate(@Param("atrApplication") ATRApplication atrApplication, @Param("timesheetStatus") TimesheetStatus timesheetStatus, @Param("createdDateStart") Date createdDateStart, @Param("createdDateEndDate") Date createdDateEndDate);
    
    @Query("SELECT e FROM TimeSheetActivity e WHERE e.atrApplication=:atrApplication AND (CONVERT(date, e.capturedDate) >=:createdDateStart AND CONVERT(date, e.capturedDate)<=:createdDateEndDate)")
    List<TimeSheetActivity> findByAtrApplicationAndDate(@Param("atrApplication") ATRApplication atrApplication, @Param("createdDateStart") Date createdDateStart, @Param("createdDateEndDate") Date createdDateEndDate);

    @Query("SELECT e FROM TimeSheetActivity e WHERE e.timesheetStatus=:timesheetStatus AND (CONVERT(date, e.capturedDate) >=:createdDateStart AND CONVERT(date, e.capturedDate)<=:createdDateEndDate)")
    List<TimeSheetActivity> findByTimesheetStatusAndDate(@Param("timesheetStatus") TimesheetStatus timesheetStatus, @Param("createdDateStart") Date createdDateStart, @Param("createdDateEndDate") Date createdDateEndDate);

    @Query("SELECT e FROM TimeSheetActivity e WHERE CONVERT(date, e.capturedDate) >=:createdDateStart AND CONVERT(date, e.capturedDate)<=:createdDateEndDate")
    List<TimeSheetActivity> findByDate(@Param("createdDateStart") Date createdDateStart, @Param("createdDateEndDate") Date createdDateEndDate);

    @Query("SELECT e FROM TimeSheetActivity e WHERE e.resource=:resource AND e.activity=:activity AND e.timesheetStatus=:timesheetStatus AND (CONVERT(date, e.capturedDate) >=:createdDateStart AND CONVERT(date, e.capturedDate)<=:createdDateEndDate)")
    List<TimeSheetActivity> findByUserAndActivityAndTimesheetStatusAndDate(@Param("resource") User resource, @Param("activity") Activity activity, @Param("timesheetStatus") TimesheetStatus timesheetStatus, @Param("createdDateStart") Date createdDateStart, @Param("createdDateEndDate") Date createdDateEndDate);

    @Query("SELECT e FROM TimeSheetActivity e WHERE e.resource=:resource AND e.timesheetStatus=:timesheetStatus AND (CONVERT(date, e.capturedDate) >=:createdDateStart AND CONVERT(date, e.capturedDate)<=:createdDateEndDate)")
    List<TimeSheetActivity> findByUserAndTimesheetStatusAndDate(@Param("resource") User resource, @Param("timesheetStatus") TimesheetStatus timesheetStatus, @Param("createdDateStart") Date createdDateStart, @Param("createdDateEndDate") Date createdDateEndDate);

    @Query("SELECT new za.gov.sars.dto.ProductivitySummaryReportDTO("
            + "u.fullName, "
            + "SUM(CASE WHEN a.activityType = za.gov.sars.common.ActivityType.BILLABLE THEN t.numberOfHours ELSE 0.0 END), "
            + "SUM(CASE WHEN a.activityType IN (za.gov.sars.common.ActivityType.NON_BILLABLE, za.gov.sars.common.ActivityType.GENERAL) THEN t.numberOfHours ELSE 0.0 END), "
            + "SUM(t.numberOfHours), "
            + "0.0, 0.0, 0.0) "
            + "FROM TimeSheetActivity t "
            + "JOIN t.resource u "
            + "JOIN t.activity a "
            + "WHERE t.createdDate BETWEEN :startDate AND :endDate "
            + "GROUP BY u.id, u.fullName")
    List<ProductivitySummaryReportDTO> fetchUserProductivityReport(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
