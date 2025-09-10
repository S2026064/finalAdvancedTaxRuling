package za.gov.sars.service;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.Activity;
import za.gov.sars.domain.User;
import za.gov.sars.domain.UserApplicationActivity;
import za.gov.sars.persistence.UserApplicationActivityRepository;

/**
 *
 * @author S2028873
 */
@Service
@Transactional
public class UserApplicationActivityService implements UserApplicationActivityServiceLocal {

    @Autowired
    private UserApplicationActivityRepository userApplicationActivityRepository;

    @Override
    public void deleteAll() {
        userApplicationActivityRepository.deleteAll();
    }

    @Override
    public List<UserApplicationActivity> findAll() {
        return userApplicationActivityRepository.findAll();
    }

    @Override
    public Page<UserApplicationActivity> listAll(Pageable pageable) {
        return userApplicationActivityRepository.findAll(pageable);
    }

    @Override
    public UserApplicationActivity save(UserApplicationActivity userApplicationActivity) {
        return userApplicationActivityRepository.save(userApplicationActivity);
    }

    @Override
    public UserApplicationActivity findById(Long id) {
        return userApplicationActivityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                                "The requested id [" + id
                                + "] does not exist."));
    }

    @Override
    public UserApplicationActivity update(UserApplicationActivity userApplicationActivity) {
        return userApplicationActivityRepository.save(userApplicationActivity);
    }

    @Override
    public UserApplicationActivity deleteById(Long id) {
        UserApplicationActivity userActivity = findById(id);
        if (userActivity != null) {
            userApplicationActivityRepository.delete(userActivity);
        }
        return userActivity;
    }

    @Override
    public Page<UserApplicationActivity> findAll(Specification specification, Pageable pageable) {
        return userApplicationActivityRepository.findAll(specification, pageable);
    }

    @Override
    public boolean isExist(UserApplicationActivity entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<UserApplicationActivity> findUsersByAtrApplicationId(Long atrApplicationId) {
        return userApplicationActivityRepository.findByAtrApplicationId(atrApplicationId);
    }

    @Override
    public List<UserApplicationActivity> findAtrApplicationsByUserId(Long userId) {
        return userApplicationActivityRepository.findByUserId(userId);
    }

    @Override
    public List<UserApplicationActivity> findActivitiesByUserIdAndAtrApplicationId(Long userId, Long atrApplicationId) {
        return userApplicationActivityRepository.findByUserIdAndAtrApplicationId(userId, atrApplicationId);
    }

    @Override
    public List<UserApplicationActivity> findByUsersAndApplication(List<User> users, ATRApplication application) {
        return userApplicationActivityRepository.findByUserInAndAtrApplication(users, application);
    }

    @Override
    @Modifying
    public void deleteByUsersAndApplication(List<User> users, ATRApplication application) {
        userApplicationActivityRepository.deleteByUserInAndATRApplication(users, application);
    }

    @Override
    public void saveAll(List<UserApplicationActivity> assignments) {
        userApplicationActivityRepository.saveAll(assignments);
    }

    @Override
    public List<UserApplicationActivity> findByAtrApplicationAndDate(ATRApplication atrApplication, Date startDate, Date endDate) {
        return userApplicationActivityRepository.findByAtrApplicationAndDate(atrApplication, startDate, endDate);
    }

    @Override
    public UserApplicationActivity findByAtrApplicationAndActivity(ATRApplication application,Activity activity) {
        return userApplicationActivityRepository.findByAtrApplicationAndActivity(application,activity);
    }
}
