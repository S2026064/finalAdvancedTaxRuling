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
import za.gov.sars.common.EstimatePlanType;
import za.gov.sars.common.RulingType;
import za.gov.sars.common.Status;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.EstimatePlan;
import za.gov.sars.domain.User;

/**
 *
 * @author S2026064
 */
@Repository
public interface AtrApplicationRepository extends JpaRepository<ATRApplication, Long>, JpaSpecificationExecutor<ATRApplication> {

    //This Query is not neccesary the framework provides for it by default
    @Query("SELECT e FROM ATRApplication e WHERE e.caseNum=:caseNum")
    ATRApplication searchByCaseNum(@Param("caseNum") long caseNum);

    ATRApplication findByCaseNum(long caseNum);

    //ATRApplication findByEstimatePlan(EstimatePlan estimatePlan);

    @Query("SELECT a FROM ATRApplication a "
            + "JOIN a.userAtrApplications au "
            + "JOIN au.user u "
            + "WHERE u.sid =:sid")
    Page<ATRApplication> findBySid(@Param("sid") String sid, Pageable pageable);

    @Query("SELECT a FROM ATRApplication a "
            + "JOIN a.userAtrApplications au "
            + "JOIN au.user u "
            + "WHERE u.sid =:sid")
    List<ATRApplication> findBySid(@Param("sid") String sid);

    @Query("SELECT a FROM ATRApplication a "
            + "JOIN a.userAtrApplications au "
            + "JOIN au.user u "
            + "WHERE u.idNumber =:idNumber OR u.passPortNumber=:passPortNumber")
    Page<ATRApplication> findByIdNumberOrPassPortNumber(@Param("idNumber") String idNumber, @Param("passPortNumber") String passPortNumber, Pageable pageable);

    @Query("SELECT a FROM ATRApplication a "
            + "JOIN a.userAtrApplications au "
            + "JOIN au.user u "
            + "WHERE (u.idNumber =:idNumber OR u.passPortNumber=:passPortNumber) AND a.status IN (:status)")
    Page<ATRApplication> findByIdNumberOrPassPortNumberAndStatus(@Param("idNumber") String idNumber, @Param("passPortNumber") String passPortNumber, @Param("status") List<Status> statuses, Pageable pageable);

    Page<ATRApplication> findByApplicantName(String applicantName, Pageable pageable);

    @Query(value = "SELECT TOP (1) * FROM atr_application  ORDER BY [case_number]  DESC", nativeQuery = true)
    ATRApplication findLastInsertedRecord();

    @Query("SELECT DISTINCT a FROM ATRApplication a " + "JOIN a.userAtrApplications ua " + "JOIN ua.user u " + "WHERE u.sid = :sid AND a.status IN (:status)")
    Page<ATRApplication> findBySidAndStatus(@Param("sid") String sid, @Param("status") List<Status> statuses, Pageable pageable);

    //This Query is not neccesary the framework provides for it by default
    @Query("SELECT a FROM ATRApplication a WHERE a.status IN :statuses ORDER BY a.createdDate DESC")
    Page<ATRApplication> findByStatusIn(@Param("statuses") List<Status> statuses, Pageable pageable);

    Page<ATRApplication> findByStatusNotInOrderByCreatedDateDesc(List<Status> statuses, Pageable pageable);

    Page<ATRApplication> findByStatusInOrderByCreatedDateDesc(List<Status> statuses, Pageable pageable);

    //This Query is not neccesary the framework provides for it by default
    @Query("SELECT a FROM ATRApplication a WHERE a.status = :status ORDER BY a.createdDate DESC")
    Page<ATRApplication> findByStatus(@Param("status") Status status, Pageable pageable);

    Page<ATRApplication> findByStatusOrderByCreatedDateDesc(Status status, Pageable pageable);

    @Query("SELECT DISTINCT a FROM ATRApplication a "
            + "LEFT JOIN a.userAtrApplications ua "
            + "WHERE a.status IN :statuses "
            + "AND ua.user = :user "
            + "ORDER BY a.createdDate DESC")
    Page<ATRApplication> searchByStatusInAndUserAtrApplications_User(
            @Param("statuses") List<Status> statuses,
            @Param("user") User user,
            Pageable pageable);

    Page<ATRApplication> findByStatusInAndUserAtrApplications_User(List<Status> statuses, User user, Pageable pageable);

    Page<ATRApplication> findByStatusNotInAndUserAtrApplications_User(List<Status> statuses, User user, Pageable pageable);

    Page<ATRApplication> findByUserAtrApplications_User(User user, Pageable pageable);

    //This Query is not neccesary the framework provides for it by default
    @Query("SELECT a FROM ATRApplication a ORDER BY a.createdDate DESC")
    Page<ATRApplication> findAllOrdered(Pageable pageable);

    Page<ATRApplication> findAllByOrderByCreatedDateDesc(Pageable pageable);

    @Query("SELECT a FROM ATRApplication a WHERE a.status IN (:statuses) AND (CONVERT(date, a.createdDate) >=:startDate AND CONVERT(date, a.createdDate)<=:endDate)")
    List<ATRApplication> findByStatusIn(@Param("statuses") List<Status> statuses, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT a FROM ATRApplication a WHERE CONVERT(date, a.createdDate) >=:startDate AND CONVERT(date, a.createdDate)<=:endDate")
    List<ATRApplication> findByDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    @Query("SELECT a FROM ATRApplication a WHERE a.rulingType =:rulingType AND CONVERT(date, a.createdDate) >=:startDate AND CONVERT(date, a.createdDate)<=:endDate")
    List<ATRApplication> findByRulingTypeAndDate(@Param("rulingType") RulingType rulingType,@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    /// This Quiries are used for extracting reports
    
     @Query("SELECT e FROM ATRApplication e WHERE e.status IN :statuses AND (CONVERT(date, e.createdDate) >=:startDate AND CONVERT(date, e.createdDate)<=:endDate)")
    List<ATRApplication> findByStatusesAndDate(@Param("statuses") List<Status> statuses,@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT a FROM ATRApplication a WHERE a.rulingType=:rulingType AND a.status IN (:statuses) AND (CONVERT(date, a.createdDate) >=:startDate AND CONVERT(date, a.createdDate)<=:endDate)")
    List<ATRApplication> findByRulingTypeAndStatusIn(@Param("rulingType") RulingType rulingType, @Param("statuses") List<Status> statuses, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
}
