package za.gov.sars.domain.service;

import java.util.List;
import za.gov.sars.common.ActivityType;
import za.gov.sars.domain.Activity;

/**
 *
 * @author S2024726
 */
public interface ActivityServiceLocal {

    void deleteAll();

    Activity findByDescription(String description);

    List<Activity> findAll();

    Activity save(Activity activity);

    Activity findById(Long id);

    Activity update(Activity activity);

    Activity deleteById(Long id);

    boolean isExist(Activity activity);
    
    List<Activity> findByActivityType(ActivityType activityType);
}
