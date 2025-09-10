package za.gov.sars.service;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import za.gov.sars.common.ResourceType;
import za.gov.sars.common.Status;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.User;
import za.gov.sars.domain.UserAtrApplication;

/**
 *
 * @author S2024726
 */
public interface UserAtrApplicationServiceLocal extends GenericServiceLocal<UserAtrApplication> {

    public void deleteAll();

    public List<UserAtrApplication> findAll();

    List< UserAtrApplication> findByIdNumberOrPassPortNumber(String idNumber, String passPortNumber);

    public List<UserAtrApplication> findByAtrApplication(ATRApplication atrApplication);

    public List<UserAtrApplication> findByUser(User user);

    public UserAtrApplication findByAtrApplicationAndResourceType(ATRApplication atrApplication, ResourceType resourceType);

    Page<UserAtrApplication> listAll(Pageable pageable);

    List<UserAtrApplication> findAtrApplicationsByUserId(User user);

    List<UserAtrApplication> findUsersByAtrApplicationId(ATRApplication atrApplication);

    UserAtrApplication findUsersByAtrApplicationIdAndResourceType(ATRApplication atrApplication, ResourceType resourceType);

    Page<ATRApplication> findAppByUserResourceTypeAndAppStatusIn(User user, ResourceType resourceType, List<Status> statuses, Pageable pageable);

    List<UserAtrApplication> findByUserAndResourceType(User user, ResourceType resourceType);

    List<UserAtrApplication> findByAtrApplicationAndResourceTypeIn(ATRApplication ATRApplication, List<ResourceType> resourceTypes);

    Page<UserAtrApplication> findByUserAndAtrApplication_StatusIn(User user, List<Status> statuses, Pageable pageable);

    Page<ATRApplication> findAppByUserResourceType(User user, ResourceType resourceType, Pageable pageable);
    
     public List<UserAtrApplication> findByUserAndResourceTypeIn(User user, List<ResourceType> resourceType);

    Page<UserAtrApplication> findByUserAndResourceTypeNotAndAtrApplicationStatus(User user, ResourceType resourceType, List<Status> statuses, Pageable pageable);

    //This methods are for extracding reports
    List<UserAtrApplication> findByStatusesAndDate(List<Status> statuses, Date startDate, Date endDate);

    public List<UserAtrApplication> findByAtrApplicationAndDate(ATRApplication atrApplication, Date startDate, Date endDate);

    public List<UserAtrApplication> findByDate(Date startDate, Date endDate);
    
    public List<UserAtrApplication> findByUserAndAtrApplication_StatusAndResourceTypeIn(User user,Status status ,List<ResourceType> resourceType);

}
