package za.gov.sars.persistence;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.Activity;
import za.gov.sars.domain.User;
import za.gov.sars.domain.UserApplicationActivity;

/**
 *
 * @author S2026064
 */
@Repository
public interface UserApplicationActivityRepository extends JpaRepository<UserApplicationActivity, Long>, JpaSpecificationExecutor<UserApplicationActivity> {

    List<UserApplicationActivity> findByAtrApplicationId(Long ATRApplicationId);

    List<UserApplicationActivity> findByUserId(Long userId);

    List<UserApplicationActivity> findByUserIdAndAtrApplicationId(Long userId, Long ATRApplicationId);

    List<UserApplicationActivity> findByUserInAndAtrApplication(List<User> users, ATRApplication application);
    
    UserApplicationActivity findByAtrApplicationAndActivity(ATRApplication application,Activity activity);

    @Modifying
    @Query("DELETE FROM UserApplicationActivity uaa WHERE uaa.user IN :users AND uaa.atrApplication = :application")
    void deleteByUserInAndATRApplication(@Param("users") List<User> users,
            @Param("application") ATRApplication application);
    
    // This quiries are for extracting report data
    
       @Query("SELECT e FROM UserApplicationActivity e WHERE e.atrApplication = :atrApplication AND (CONVERT(date, e.createdDate) >=:startDate AND CONVERT(date, e.createdDate)<=:endDate)")
    public List<UserApplicationActivity> findByAtrApplicationAndDate(@Param("atrApplication")ATRApplication atrApplication, @Param("startDate") Date startDate, @Param("endDate") Date endDate);


}
