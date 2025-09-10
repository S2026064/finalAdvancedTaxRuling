package za.gov.sars.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
import za.gov.sars.common.EstimatePlanStatus;
import za.gov.sars.common.EstimatePlanType;
import za.gov.sars.common.Status;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.EstimatePlan;
import za.gov.sars.dto.EstimationPlanRevisionDTO;
import za.gov.sars.persistence.EstimatePlanRepository;

/**
 *
 * @author S2028873
 */
@Service
@Transactional
public class EstimatePlanService implements EstimatePlanServiceLocal {

    @Autowired
    private EstimatePlanRepository estimatePlanRepository;
    
    @Autowired
    private AuditReader auditReader;

    @Override
    public List<EstimatePlan> findAll() {
        return estimatePlanRepository.findAll();

    }

    @Override
    public Page<EstimatePlan> listAll(Pageable pageable) {
        return estimatePlanRepository.findAll(pageable);
    }

    @Override
    public void deleteAll() {
        estimatePlanRepository.deleteAll();
    }

    @Override
    public List<EstimatePlan> findByAtrApplication(ATRApplication atrApplication) {
        return estimatePlanRepository.findByAtrApplication(atrApplication);
    }

    
    @Override
    public Optional<EstimatePlan> getLatestPlan(ATRApplication atrApplication) {
        List<EstimatePlan> plans = estimatePlanRepository.findByAtrApplicationOrderByIdAsc(atrApplication);

        if (plans.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(plans.get(plans.size() - 1));
    }

   
    @Override
    public EstimatePlan save(EstimatePlan estimatePlan) {
        return estimatePlanRepository.save(estimatePlan);
    }

    @Override
    public EstimatePlan findById(Long id) {
        return estimatePlanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                "The requested id [" + id + "] does not exist."));
    }

    @Override
    public EstimatePlan update(EstimatePlan estimatePlan) {
        return estimatePlanRepository.save(estimatePlan);
    }

    @Override
    public EstimatePlan deleteById(Long id) {
        EstimatePlan estimatePlan = findById(id);
        estimatePlanRepository.delete(estimatePlan);
        return estimatePlan;
    }

    @Override
    public boolean isExist(EstimatePlan estimatePlan) {
        if (estimatePlan.getId() == null) {
            return false;
        }
        return estimatePlanRepository.existsById(estimatePlan.getId());
    }

    @Override
    public Page<EstimatePlan> findAll(Specification specification, Pageable pageable) {
        return estimatePlanRepository.findAll(specification, pageable);
    }

    

    @Override
    public EstimatePlan findByPlanTypeAndAtrApplication(EstimatePlanType planType, ATRApplication application) {
        return estimatePlanRepository.findByPlanTypeAndAtrApplication(planType, application);
    }

    @Override
    public EstimatePlan findEstimationPlanAtRevision(Long estimationPlanId, Integer revisionNumber) {
        return auditReader.find(EstimatePlan.class, estimationPlanId, revisionNumber);
    }
    
    @Override
    public List<EstimationPlanRevisionDTO> findRevisionsByEstimationPlanId(Long estimationPlanId, List<EstimatePlanStatus> planStatuses) {
        List<Object[]> revisions = auditReader.createQuery()
                .forRevisionsOfEntity(EstimatePlan.class, false, true)
                .add(AuditEntity.id().eq(estimationPlanId))
                .add(AuditEntity.property("status").in(planStatuses))
                .addProjection(AuditEntity.id())
                .addProjection(AuditEntity.revisionNumber())
                .addProjection(AuditEntity.revisionProperty("timestamp"))
                .addProjection(AuditEntity.revisionProperty("username"))
                .addProjection(AuditEntity.revisionType())
                .addProjection(AuditEntity.property("grandTotal"))
                .addProjection(AuditEntity.property("depositPercentage"))
                .addProjection(AuditEntity.property("daysToComplete"))
                .addProjection(AuditEntity.property("depositAmount"))
                .addProjection(AuditEntity.property("status"))
                .addOrder(AuditEntity.revisionNumber().desc())
                .getResultList();
 
        return revisions.stream()
                .map(row -> new EstimationPlanRevisionDTO(
                        (Long) row[0], // id
                        (Integer) row[1], // revisionNumber
                        (Long) row[2], // revisionTimestamp
                        (String) row[3],
                        (RevisionType) row[4], // revisionType
                        // revisionTimestamp
                        (Double) row[5],
                        (Double) row[6],
                        (Integer) row[7], // revisionTimestamp
                        (Double) row[8],
                        (String) row[9],
                        estimationPlanId // applicationId (same as id)
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<EstimatePlan> findByAtrApplicationAndStatus(ATRApplication atrApplication, EstimatePlanStatus status) {
        return estimatePlanRepository.findByAtrApplicationAndStatus(atrApplication, status);
    }

    @Override
    public List<EstimatePlan> findByAtrApplicationAndStatusIn(ATRApplication application, List<EstimatePlanStatus> statuses) {
        return estimatePlanRepository.findByAtrApplicationAndStatusIn(application, statuses);
    }

    @Override
    public List<EstimatePlan> findByStatus(EstimatePlanStatus status) {
        return estimatePlanRepository.findByStatus(status);
    }

    @Override
    public List<EstimatePlan> getEditablePlans(ATRApplication atrApplication) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public EstimatePlan findByPlanTypeAndStatusAndAtrApplication(EstimatePlanType planType, ATRApplication atrApplication, EstimatePlanStatus status) {
        return estimatePlanRepository.findByPlanTypeAndAtrApplication(planType, atrApplication);
    }

}
