package za.gov.sars.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.gov.sars.common.ActivityType;
import za.gov.sars.domain.Activity;

/**
 *
 * @author S2026064
 */
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long>, JpaSpecificationExecutor<Activity> {

  //This Query is not neccesary the framework provides for it by default
    @Query("SELECT e FROM Activity e WHERE e.description LIKE %:description%")
    Activity searchByDescription(@Param("description") String description);
 
    Activity findByDescription(String description);
    
    List<Activity> findByActivityType(ActivityType activityType);
}
