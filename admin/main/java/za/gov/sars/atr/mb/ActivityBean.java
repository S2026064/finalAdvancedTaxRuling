/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.atr.mb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.springframework.beans.factory.annotation.Autowired;
import za.gov.sars.common.ActivityCategory;
import za.gov.sars.common.ActivityType;
import za.gov.sars.domain.Activity;
import za.gov.sars.domain.service.ActivityServiceLocal;

/**
 *
 * @author S2030707
 */
@ManagedBean
@ViewScoped
public class ActivityBean extends BaseBean<Activity> {

    @Autowired
    private ActivityServiceLocal activityService;
    private List<ActivityType> activityTypes = new ArrayList();
     private List<ActivityCategory> activityCategories = new ArrayList();

    @PostConstruct
    public void init() {
        reset().setList(true);
        activityTypes.addAll(Arrays.asList(ActivityType.values()));
        activityCategories.addAll(Arrays.asList(ActivityCategory.values()));
        addCollections(activityService.findAll());
    }

    public void addorUpdate(Activity activity) {
        reset().setAdd(true);
        if (activity != null) {
            activity.setUpdatedBy(getActiveUser().getSid());
            activity.setUpdatedDate(new Date());
        } else {
            activity = new Activity();
            activity.setCreatedBy(getActiveUser().getSid());
            activity.setCreatedDate(new Date());
            addCollection(activity);
        }
        addEntity(activity);

    }

    public void save(Activity activity) {
        if (activity.getId() != null) {
            activityService.update(activity);
            addInformationMessage("Activity successfully updated");
        } else {
            activityService.save(activity);
            addInformationMessage("Activity successfully saved");
        }
        reset().setList(true);
    }

    public void cancel(Activity activity) {
        if (activity.getId() == null && getActivities().contains(activity)){
            remove(activity);
        }
           reset().setList(true);
    }

    public void delete(Activity activity) {
        activityService.deleteById(activity.getId());
        remove(activity);
        addInformationMessage("Activity successfully deleted");
        reset().setList(true);
    }

    public List<Activity> getActivities() {
        return this.getCollections();
    }

    public List<ActivityType> getActivityTypes() {
        return activityTypes;
    }

    public void setActivityTypes(List<ActivityType> activityTypes) {
        this.activityTypes = activityTypes;
    }

    public List<ActivityCategory> getActivityCategories() {
        return activityCategories;
    }

    public void setActivityCategories(List<ActivityCategory> activityCategories) {
        this.activityCategories = activityCategories;
    }

}
