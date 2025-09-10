package za.gov.sars.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.gov.sars.common.ActivityType;
import za.gov.sars.domain.Activity;
import za.gov.sars.domain.service.ActivityServiceLocal;
import za.gov.sars.persistence.ActivityRepository;

/**
 *
 * @author S2028873
 */
@Service
@Transactional
public class ActivityService implements ActivityServiceLocal {

    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public void deleteAll() {
        activityRepository.deleteAll();
    }

    @Override
    public List<Activity> findAll() {
        return activityRepository.findAll();
    }

    @Override
    public Activity save(Activity activity) {
        return activityRepository.save(activity);
    }

    @Override
    public Activity findById(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                "The requested id [" + id
                + "] does not exist."));
    }

    @Override
    public Activity update(Activity activity) {
        return activityRepository.save(activity);
    }

    @Override
    public Activity deleteById(Long id) {
        Activity activity = findById(id);
        if (activity != null) {
            activityRepository.delete(activity);
        }
        return activity;
    }

    @Override
    public Activity findByDescription(String description) {
        return activityRepository.findByDescription(description);
    }

    @Override
    public boolean isExist(Activity activity) {
        return activityRepository.findById(activity.getId())!= null;
    }

    @Override
    public List<Activity> findByActivityType(ActivityType activityType) {
        return activityRepository.findByActivityType(activityType);
    }

}
