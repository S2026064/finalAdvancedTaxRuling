package za.gov.sars.service;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.gov.sars.common.ResourceType;
import za.gov.sars.common.Status;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.User;
import za.gov.sars.domain.UserAtrApplication;
import za.gov.sars.persistence.UserAtrApplicationRepository;

/**
 *
 * @author S2028873
 */
@Service
@Transactional
public class UserAtrApplicationService implements UserAtrApplicationServiceLocal {

    @Autowired
    private UserAtrApplicationRepository userAtrApplicationRepository;

    @Override
    public void deleteAll() {
        userAtrApplicationRepository.deleteAll();
    }

    @Override
    public List<UserAtrApplication> findAll() {
        return userAtrApplicationRepository.findAll();
    }

    @Override
    public Page<UserAtrApplication> listAll(Pageable pageable) {
        return userAtrApplicationRepository.findAll(pageable);
    }

    @Override
    public UserAtrApplication save(UserAtrApplication userAtrApplication) {
        return userAtrApplicationRepository.save(userAtrApplication);
    }

    @Override
    public UserAtrApplication findById(Long id) {
        return userAtrApplicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                "The requested id [" + id
                + "] does not exist."));
    }

    @Override
    public UserAtrApplication update(UserAtrApplication userAtrApplication) {
        return userAtrApplicationRepository.save(userAtrApplication);
    }

    @Override
    public UserAtrApplication deleteById(Long id) {
        UserAtrApplication userAtrApplication = findById(id);
        if (userAtrApplication != null) {
            userAtrApplicationRepository.delete(userAtrApplication);
        }
        return userAtrApplication;
    }

    @Override
    public Page<UserAtrApplication> findAll(Specification specification, Pageable pageable) {
        return userAtrApplicationRepository.findAll(specification, pageable);
    }

    @Override
    public boolean isExist(UserAtrApplication entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<UserAtrApplication> findByIdNumberOrPassPortNumber(String idNumber, String passPortNumber) {
        return userAtrApplicationRepository.findByUserIdNumberOrUserPassPortNumber(idNumber, passPortNumber);
    }

    @Override
    public List<UserAtrApplication> findByAtrApplication(ATRApplication atrApplication) {
        return userAtrApplicationRepository.findByAtrApplication(atrApplication);
    }

    @Override
    public UserAtrApplication findByAtrApplicationAndResourceType(ATRApplication atrApplication, ResourceType resourceType) {
        return userAtrApplicationRepository.findByAtrApplicationAndResourceType(atrApplication, resourceType);
    }

    @Override
    public List<UserAtrApplication> findByUser(User user) {
        return userAtrApplicationRepository.findByUser(user);
    }

    @Override
    public List<UserAtrApplication> findAtrApplicationsByUserId(User user) {
        return userAtrApplicationRepository.findByUser(user);
    }

    @Override
    public List<UserAtrApplication> findUsersByAtrApplicationId(ATRApplication atrApplication) {
        return userAtrApplicationRepository.findByAtrApplication(atrApplication);
    }

    @Override
    public UserAtrApplication findUsersByAtrApplicationIdAndResourceType(ATRApplication atrApplication, ResourceType resourceType) {
        return userAtrApplicationRepository.findByAtrApplicationAndResourceType(atrApplication, resourceType);
    }

    @Override
    public Page<ATRApplication> findAppByUserResourceTypeAndAppStatusIn(User user, ResourceType resourceType, List<Status> statuses, Pageable pageable) {
        return userAtrApplicationRepository.findAppByUserResourceTypeAndAppStatusIn(user, resourceType, statuses, pageable);
    }

    @Override
    public List<UserAtrApplication> findByAtrApplicationAndResourceTypeIn(ATRApplication ATRApplication, List<ResourceType> resourceTypes) {
        return userAtrApplicationRepository.findByAtrApplicationAndResourceTypeIn(ATRApplication, resourceTypes);
    }

    @Override
    public Page<UserAtrApplication> findByUserAndAtrApplication_StatusIn(User user, List<Status> statuses, Pageable pageable) {
        return userAtrApplicationRepository.findByUserAndAtrApplication_StatusIn(user, statuses, pageable);
    }

    @Override
    public Page<ATRApplication> findAppByUserResourceType(User user, ResourceType resourceType, Pageable pageable) {
        return userAtrApplicationRepository.findAppByUserResourceType(user, resourceType, pageable);
    }

    @Override
    public List<UserAtrApplication> findByUserAndResourceType(User user, ResourceType resourceType) {
        return userAtrApplicationRepository.findByUserAndResourceType(user, resourceType);
    }

    @Override
    public Page<UserAtrApplication> findByUserAndResourceTypeNotAndAtrApplicationStatus(User user, ResourceType resourceType, List<Status> statuses, Pageable pageable) {
        return userAtrApplicationRepository.findByUserAndResourceTypeNotAndAtrApplication_StatusIn(user, resourceType, statuses, pageable);
    }

    @Override
    public List<UserAtrApplication> findByStatusesAndDate(List<Status> statuses, Date startDate, Date endDate) {
        return userAtrApplicationRepository.findByStatusesAndDate(statuses, startDate, endDate);
    }

    @Override
    public List<UserAtrApplication> findByAtrApplicationAndDate(ATRApplication atrApplication, Date startDate, Date endDate) {
        return userAtrApplicationRepository.findByAtrApplicationAndDate(atrApplication, startDate, endDate);
    }

    @Override
    public List<UserAtrApplication> findByDate(Date startDate, Date endDate) {
        return userAtrApplicationRepository.findByDate(startDate, endDate);
    }

    @Override
    public List<UserAtrApplication> findByUserAndResourceTypeIn(User user, List<ResourceType> resourceType) {
   return userAtrApplicationRepository.findByUserAndResourceTypeIn(user, resourceType);
    }

    @Override
    public List<UserAtrApplication> findByUserAndAtrApplication_StatusAndResourceTypeIn(User user, Status status, List<ResourceType> resourceType) {
        return userAtrApplicationRepository.findByUserAndAtrApplication_StatusAndResourceTypeIn(user, status, resourceType);
    }

}
