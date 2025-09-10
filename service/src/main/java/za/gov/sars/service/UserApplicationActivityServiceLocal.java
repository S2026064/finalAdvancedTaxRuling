package za.gov.sars.service;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import za.gov.sars.common.ResourceType;
import za.gov.sars.domain.Activity;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.User;
import za.gov.sars.domain.UserApplicationActivity;

/**
 *
 * @author S2024726
 */
public interface UserApplicationActivityServiceLocal extends GenericServiceLocal<UserApplicationActivity> {

    public void deleteAll();

    public List<UserApplicationActivity> findAll();

    Page<UserApplicationActivity> listAll(Pageable pageable);

    List<UserApplicationActivity> findUsersByAtrApplicationId(Long atrApplicationId);

    List<UserApplicationActivity> findAtrApplicationsByUserId(Long userId);

    List<UserApplicationActivity> findActivitiesByUserIdAndAtrApplicationId(Long userId, Long atrApplicationId);

    List<UserApplicationActivity> findByUsersAndApplication(List<User> users, ATRApplication application);

    void deleteByUsersAndApplication(List<User> users, ATRApplication application);

    void saveAll(List<UserApplicationActivity> assignments);

    // This methods are for reports
    public List<UserApplicationActivity> findByAtrApplicationAndDate(ATRApplication atrApplication, Date startDate, Date endDate);

     UserApplicationActivity findByAtrApplicationAndActivity(ATRApplication application,Activity activity);
}
