package za.gov.sars.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.gov.sars.common.RulingType;
import za.gov.sars.common.Status;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.EstimatePlan;
import za.gov.sars.domain.User;
import za.gov.sars.dto.AtrApplicationRevisionDTO;
import za.gov.sars.dto.EstimationPlanRevisionDTO;
import za.gov.sars.persistence.AtrApplicationRepository;

/**
 *
 * @author S2028873
 */
@Service
@Transactional
public class AtrApplicationService implements AtrApplicationServiceLocal {

    @Autowired
    private AtrApplicationRepository applicationRepository;

    @Autowired
    private AuditReader auditReader;

    @Override
    public List<Number> getRevisions(Class<?> entityClass, Long entityId) {
        return auditReader.getRevisions(entityClass, entityId);

    }

    @Override
    public List<Object[]> getAllVersions(Class<?> entityClass, Long entityId) {
        List<Number> revisions = getRevisions(entityClass, entityId);

        return revisions.stream()
                .map(rev -> {
                    Object entity = auditReader.find(entityClass, entityId, rev);
                    Date timestamp = auditReader.getRevisionDate(rev);
                    return new Object[] { rev, timestamp, entity };
                })
                .collect(Collectors.toList());
    }

    @Override
    public <AtrApplication> AtrApplication getVersion(Class<AtrApplication> entityClass, Long entityId,
            Number revision) {
        return auditReader.find(entityClass, entityId, revision);
    }

    @Override
    public <AtrApplication> List<AtrApplication> getEntityHistory(Class<AtrApplication> entityClass, Long entityId) {
        List<Number> revisions = auditReader.getRevisions(entityClass, entityId);

        return revisions.stream()
                .map(rev -> auditReader.find(entityClass, entityId, rev))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<ATRApplication> getAtrApplicationRevisions(Long atrApplicationId) {
        List<ATRApplication> atrApplicationRevisions = new ArrayList<>();
        List<Number> allRevisions = auditReader.getRevisions(ATRApplication.class, atrApplicationId);

        allRevisions.stream().map(n -> auditReader.find(ATRApplication.class, atrApplicationId, n))
                .forEachOrdered(auditedAtrApplication -> {
                    System.out.print(auditedAtrApplication.getApplicantName());
                    atrApplicationRevisions.add(auditedAtrApplication);
                });
        return atrApplicationRevisions;
    }

    @Override
    public void deleteAll() {
        applicationRepository.deleteAll();
    }

    @Override
    public List<ATRApplication> findAll() {
        return applicationRepository.findAll();
    }

    @Override
    public Page<ATRApplication> listAll(Pageable pageable) {
        return applicationRepository.findAll(pageable);
    }

    @Override
    public Page<ATRApplication> findByIdNumberOrPassPortNumber(String idNumber, String passPortNumber,
            Pageable pageable) {
        return applicationRepository.findByIdNumberOrPassPortNumber(idNumber, passPortNumber, pageable);
    }

    @Override
    public ATRApplication save(ATRApplication atrApplication) {
        return applicationRepository.save(atrApplication);
    }

    @Override
    public ATRApplication findById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "The requested id [" + id
                                + "] does not exist."));
    }

    @Override
    public ATRApplication update(ATRApplication atrApplication) {
        return applicationRepository.save(atrApplication);
    }

    @Override
    public ATRApplication deleteById(Long id) {
        ATRApplication atrApplication = findById(id);
        if (atrApplication != null) {
            applicationRepository.delete(atrApplication);
        }
        return atrApplication;
    }

    @Override
    public boolean isExist(ATRApplication atrApplication) {
        return applicationRepository.findByCaseNum(atrApplication.getCaseNum()) != null;
    }

    @Override
    public Page<ATRApplication> findAll(Specification specification, Pageable pageable) {
        return applicationRepository.findAll(specification, pageable);
    }

    @Override
    public ATRApplication findByCaseNum(Long caseNum) {
        return applicationRepository.findByCaseNum(caseNum);
    }

    @Override
    public Page<ATRApplication> findBySid(String sid, Pageable pageable) {
        return applicationRepository.findBySid(sid, pageable);
    }

    @Override
    public Page<ATRApplication> findByApplicantName(String applicantName, Pageable pageable) {
        return applicationRepository.findByApplicantName(applicantName, pageable);
    }

    @Override
    public ATRApplication findLastInsertedRecord() {
        return applicationRepository.findLastInsertedRecord();
    }

    // @Override
    // public Page<AtrApplication> findBySidAndStatus(String sid, Status status,
    // Pageable pageable) {
    // throw new UnsupportedOperationException("Not supported yet."); // Generated
    // from
    // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    // }
    @Override
    public Page<ATRApplication> findBySidAndStatus(String sid, List<Status> statuses, Pageable pageable) {
        return applicationRepository.findBySidAndStatus(sid, statuses, pageable);
    }

    @Override
    public Page<ATRApplication> findByStatusIn(List<Status> statuses, Pageable pageable) {
        return applicationRepository.findByStatusIn(statuses, pageable);
    }

    @Override
    public Page<ATRApplication> findByStatus(Status status, Pageable pageable) {
        return applicationRepository.findByStatus(status, pageable);
    }

    @Override
    public Page<ATRApplication> findByStatusAndAllocatedManagerOrAssignedUser(List<Status> statuses, User user,
            Pageable pageable) {
        return applicationRepository.findByStatusInAndUserAtrApplications_User(statuses, user, pageable);
    }

    @Override
    public Page<ATRApplication> findByIdNumberOrPassPortNumberAndStatus(String idNumber, String passPortNumber,
            List<Status> statuses, Pageable pageable) {
        return applicationRepository.findByIdNumberOrPassPortNumberAndStatus(idNumber, passPortNumber, statuses,
                pageable);
    }

    @Override
    public List<ATRApplication> findBySid(String sid) {
        return applicationRepository.findBySid(sid);
    }

    @Override
    public Page<ATRApplication> findAllApplications(Pageable pageable) {
        return applicationRepository.findAllOrdered(pageable);
    }

    @Override
    public List<AtrApplicationRevisionDTO> findRevisionsByApplicationId(Long applicationId) {
        List<Object[]> revisions = auditReader.createQuery()
                .forRevisionsOfEntity(ATRApplication.class, false, true)
                .add(AuditEntity.id().eq(applicationId))
                .addProjection(AuditEntity.id())
                .addProjection(AuditEntity.revisionNumber())
                .addProjection(AuditEntity.revisionProperty("timestamp"))
                .addProjection(AuditEntity.revisionProperty("username"))
                .addProjection(AuditEntity.revisionType())
                .addOrder(AuditEntity.revisionNumber().desc())
                .getResultList();

        return revisions.stream()
                .map(row -> new AtrApplicationRevisionDTO(
                        (Long) row[0], // id
                        (Integer) row[1], // revisionNumber
                        (Long) row[2], // revisionTimestamp
                        (String) row[3],
                        (RevisionType) row[4], // revisionType
                        applicationId // applicationId (same as id)
                ))
                .collect(Collectors.toList());
    }

    @Override
    public ATRApplication findApplicationAtRevision(Long applicationId, Integer revisionNumber) {
        return auditReader.find(ATRApplication.class, applicationId, revisionNumber);
    }

    @Override
    public List<AtrApplicationRevisionDTO> getRevisionsByRelatedEstimationPlanId(EstimatePlan estimatePlan) {
        List<Object[]> revisionData = auditReader.createQuery()
                .forRevisionsOfEntity(ATRApplication.class, false, true)
                // Use the actual audit table column name
                .add(AuditEntity.property("estimatePlan_id").eq(estimatePlan.getId()))
                .addProjection(AuditEntity.id())
                .addProjection(AuditEntity.revisionNumber())
                .addProjection(AuditEntity.revisionProperty("timestamp"))
                .addProjection(AuditEntity.revisionProperty("username"))
                .addProjection(AuditEntity.revisionType())
                .addProjection(AuditEntity.property("estimatePlan_id"))
                .addOrder(AuditEntity.revisionNumber().desc())
                .getResultList();

        return revisionData.stream()
                .map(row -> new AtrApplicationRevisionDTO(
                        (Long) row[0], // id
                        (Integer) row[1], // revisionNumber
                        (Long) row[2], // revisionTimestamp
                        (String) row[3], // username
                        (RevisionType) row[4], // revisionType
                        (Long) row[5] // estimatePlan.id
                ))
                .collect(Collectors.toList());
    }
    // }

    @Override
    public ATRApplication findATRApplicationAtEstimationPlanRevision(Long estimationPlanId, Integer estimationPlanRevisionNumber) {

        // 1. Get the revision timestamp for X's revision
        Date revisionDate = auditReader.getRevisionDate(estimationPlanRevisionNumber);

        // 2. Query Y's audit history directly to find which Y referenced this X
        // at or before the specified X revision
        List<ATRApplication> aTRApplicationList = auditReader.createQuery()
                .forRevisionsOfEntity(ATRApplication.class, false, true)
                .add(AuditEntity.relatedId("estimationPlan").eq(estimationPlanId)) // "x" is the field name in Y that
                                                                                   // references X
                .add(AuditEntity.revisionNumber().le(estimationPlanRevisionNumber))
                .addOrder(AuditEntity.revisionNumber().desc())
                .setMaxResults(1)
                .getResultList();

        return aTRApplicationList.isEmpty() ? null : aTRApplicationList.get(0);
    }

    @Override
    public Page<ATRApplication> findByStatusInOrderByCreatedDateDesc(List<Status> statuses, Pageable pageable) {
        return applicationRepository.findByStatusInOrderByCreatedDateDesc(statuses, pageable);
    }

    public Page<ATRApplication> findByUser(User user, Pageable pageable) {
        return applicationRepository.findByUserAtrApplications_User(user, pageable);
    }

    @Override
    public Page<ATRApplication> findByUserAndStatus(User user, List<Status> statuses, Pageable pageable) {
        return applicationRepository.findByStatusInAndUserAtrApplications_User(statuses, user, pageable);
    }

    @Override
    public Page<ATRApplication> findByStatusNotIn(List<Status> statuses, Pageable pageable) {
        return applicationRepository.findByStatusNotInOrderByCreatedDateDesc(statuses, pageable);
    }

    @Override
    public Page<ATRApplication> findByStatusNotInAndUserAtrApplication(List<Status> statuses, User user,
            Pageable pageable) {
        return applicationRepository.findByStatusNotInAndUserAtrApplications_User(statuses, user, pageable);
    }

    @Override
    public List<ATRApplication> findByStatusIn(List<Status> statuses, Date startDate, Date endDate) {
        return applicationRepository.findByStatusIn(statuses, startDate, endDate);
    }

    @Override
    public List<ATRApplication> findByDate(Date startDate, Date endDate) {
        return applicationRepository.findByDate(startDate, endDate);
    }

    @Override
    public List<ATRApplication> findByRulingTypeAndStatusIn(RulingType rulingType, List<Status> statuses,
            Date startDate, Date endDate) {
        return applicationRepository.findByRulingTypeAndStatusIn(rulingType, statuses, startDate, endDate);
    }

    @Override
    public List<ATRApplication> findByRulingTypeAndDate(RulingType rulingType, Date startDate, Date endDate) {
        return applicationRepository.findByRulingTypeAndDate(rulingType, startDate, endDate);
    }

    @Override
    public List<ATRApplication> findByStatusesAndDate(List<Status> statuses, Date startDate, Date endDate) {
        return applicationRepository.findByStatusesAndDate(statuses, startDate, endDate);
    }

}
