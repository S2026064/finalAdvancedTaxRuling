package za.gov.sars.service;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import za.gov.sars.common.RulingType;
import za.gov.sars.common.Status;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.EstimatePlan;
import za.gov.sars.domain.User;
import za.gov.sars.dto.AtrApplicationRevisionDTO;
import za.gov.sars.dto.EstimationPlanRevisionDTO;

/**
 *
 * @author S2024726
 */
public interface AtrApplicationServiceLocal extends GenericServiceLocal<ATRApplication> {

    public void deleteAll();

    List<ATRApplication> findBySid(String sid);

    public <AtrApplication> List<AtrApplication> getEntityHistory(Class<AtrApplication> entityClass, Long entityId);

    public <AtrApplication> AtrApplication getVersion(Class<AtrApplication> entityClass, Long entityId, Number revision);

    public List<Object[]> getAllVersions(Class<?> entityClass, Long entityId);

    public List<Number> getRevisions(Class<?> entityClass, Long entityId);

    public List<ATRApplication> getAtrApplicationRevisions(Long atrApplicationId);

    public List<ATRApplication> findAll();

    Page<ATRApplication> listAll(Pageable pageable);

    Page<ATRApplication> findByIdNumberOrPassPortNumber(String idNumber, String passPortNumber, Pageable pageable);

    Page<ATRApplication> findByIdNumberOrPassPortNumberAndStatus(String idNumber, String passPortNumber, List<Status> statuses, Pageable pageable);

    ATRApplication findByCaseNum(Long caseNum);

    Page<ATRApplication> findBySid(String sid, Pageable pageable);

    Page<ATRApplication> findByApplicantName(String applicantName, Pageable pageable);

    //   Page<AtrApplication> findBySidAndStatus(String sid, Status status, Pageable pageable);
    Page<ATRApplication> findBySidAndStatus(String sid, List<Status> statuses, Pageable pageable);

    ATRApplication findLastInsertedRecord();

    Page<ATRApplication> findByStatusIn(List<Status> statuses, Pageable pageable);

    Page<ATRApplication> findByStatus(Status status, Pageable pageable);

    Page<ATRApplication> findByStatusInOrderByCreatedDateDesc(List<Status> statuses, Pageable pageable);

    Page<ATRApplication> findByStatusAndAllocatedManagerOrAssignedUser(List<Status> statuses, User user, Pageable pageable);

    Page<ATRApplication> findAllApplications(Pageable pageable);

    // Fetch revisions for a specific application
    List<AtrApplicationRevisionDTO> findRevisionsByApplicationId(Long applicationId);

    // Fetch application at a specific revision
    ATRApplication findApplicationAtRevision(Long applicationId, Integer revisionNumber);

    List<AtrApplicationRevisionDTO> getRevisionsByRelatedEstimationPlanId(EstimatePlan estimatePlan);

    ATRApplication findATRApplicationAtEstimationPlanRevision(Long estimationPlanId, Integer estimationPlanRevisionNumber);


    Page<ATRApplication> findByUserAndStatus(User user, List<Status> statuses, Pageable pageable);

    Page<ATRApplication> findByUser(User user, Pageable pageable);

    Page<ATRApplication> findByStatusNotIn(List<Status> statuses, Pageable pageable);

    Page<ATRApplication> findByStatusNotInAndUserAtrApplication(List<Status> statuses, User user, Pageable pageable);

    List<ATRApplication> findByStatusIn(List<Status> statuses, Date startDate, Date endDate);

    List<ATRApplication> findByDate(Date startDate, Date endDate);

    List<ATRApplication> findByRulingTypeAndStatusIn(RulingType rulingType, List<Status> statuses, Date startDate, Date endDate);

   
   List<ATRApplication> findByRulingTypeAndDate(RulingType rulingType, Date startDate, Date endDate);
   
    // This methods are for extracding reports
    List<ATRApplication> findByStatusesAndDate(List<Status> statuses, Date startDate, Date endDate);

}
