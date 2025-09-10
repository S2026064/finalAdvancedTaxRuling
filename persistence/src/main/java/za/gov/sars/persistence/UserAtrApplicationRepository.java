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
import za.gov.sars.common.ResourceType;
import za.gov.sars.common.Status;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.User;
import za.gov.sars.domain.UserAtrApplication;

/**
 *
 * @author S2026064
 */
@Repository
public interface UserAtrApplicationRepository extends JpaRepository<UserAtrApplication, Long>, JpaSpecificationExecutor<UserAtrApplication> {

    //This Query is not neccesary the framework provides for it by default
//    @Query("SELECT a FROM ATRApplication a "
//            + "JOIN a.userATRApplications au "
//            + "JOIN au.user u "
//            + "WHERE u.idNumber =:idNumber OR u.passPortNumber=:passPortNumber")
//    List<UserAtrApplication> searchByIdNumberOrPassPortNumber(@Param("idNumber") String idNumber, @Param("passPortNumber") String passPortNumber, Pageable pageable);
    List<UserAtrApplication> findByUserIdNumberOrUserPassPortNumber(String idNumber, String passPortNumber);

    public List<UserAtrApplication> findByAtrApplication(ATRApplication atrApplication);

    public List<UserAtrApplication> findByUser(User user);

    public List<UserAtrApplication> findByUserAndResourceType(User user, ResourceType resourceType);

    Page<UserAtrApplication> findByUserAndAtrApplication_StatusIn(User user, List<Status> statuses, Pageable pageable);

    public UserAtrApplication findByAtrApplicationAndResourceType(ATRApplication ATRApplication, ResourceType resourceType);

    List<UserAtrApplication> findByAtrApplicationAndResourceTypeIn(ATRApplication ATRApplication, List<ResourceType> resourceTypes);

    @Query("SELECT ua.atrApplication FROM UserAtrApplication ua "
            + "WHERE ua.user = :user "
            + "AND ua.resourceType = :resourceType "
            + "AND ua.atrApplication.status IN :statuses")

    Page<ATRApplication> findAppByUserResourceTypeAndAppStatusIn(@Param("user") User user, @Param("resourceType") ResourceType resourceType,
            @Param("statuses") List<Status> statuses, Pageable pageable);

    @Query("SELECT ua.atrApplication FROM UserAtrApplication ua "
            + "WHERE ua.user = :user "
            + "AND ua.resourceType = :resourceType")

    Page<ATRApplication> findAppByUserResourceType(@Param("user") User user, @Param("resourceType") ResourceType resourceType, Pageable pageable);

    Page<UserAtrApplication> findByUserAndResourceTypeNotAndAtrApplication_StatusIn(User user, ResourceType resourceType, List<Status> statuses, Pageable pageable);

    // This queries are for extracting reports
    @Query("SELECT e FROM UserAtrApplication e WHERE e.atrApplication.status IN :statuses AND (CONVERT(date, e.createdDate) >=:startDate AND CONVERT(date, e.createdDate)<=:endDate)")
    List<UserAtrApplication> findByStatusesAndDate(@Param("statuses") List<Status> statuses, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT e FROM UserAtrApplication e WHERE e.atrApplication = :atrApplication AND (CONVERT(date, e.createdDate) >=:startDate AND CONVERT(date, e.createdDate)<=:endDate)")
    public List<UserAtrApplication> findByAtrApplicationAndDate(@Param("atrApplication") ATRApplication atrApplication, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT e FROM UserAtrApplication e WHERE (CONVERT(date, e.createdDate) >=:startDate AND CONVERT(date, e.createdDate)<=:endDate)")
    public List<UserAtrApplication> findByDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    public List<UserAtrApplication> findByUserAndResourceTypeIn(User user, List<ResourceType> resourceType);
    
    public List<UserAtrApplication> findByUserAndAtrApplication_StatusAndResourceTypeIn(User user,Status status ,List<ResourceType> resourceType);
}
