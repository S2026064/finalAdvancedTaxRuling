package za.gov.sars.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import za.gov.sars.common.EstimatePlanStatus;
import za.gov.sars.common.EstimatePlanType;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.EstimatePlan;
import za.gov.sars.dto.EstimationPlanRevisionDTO;

/**
 *
 * @author S2024726
 */
public interface EstimatePlanServiceLocal extends GenericServiceLocal<EstimatePlan> {

    List<EstimatePlan> findAll();

    Page<EstimatePlan> listAll(Pageable pageable);

    public void deleteAll();

    List<EstimatePlan> findByAtrApplication(ATRApplication atrApplication);

    List<EstimatePlan> findByAtrApplicationAndStatus(ATRApplication atrApplication, EstimatePlanStatus status);

   List<EstimatePlan> findByAtrApplicationAndStatusIn(ATRApplication application, List<EstimatePlanStatus> statuses);

    List<EstimatePlan> findByStatus(EstimatePlanStatus status);

    Optional<EstimatePlan> getLatestPlan(ATRApplication atrApplication);

    List<EstimatePlan> getEditablePlans(ATRApplication atrApplication);
    
    List<EstimationPlanRevisionDTO> findRevisionsByEstimationPlanId(Long estimationPlanId, List<EstimatePlanStatus> planStatuses);
    
    EstimatePlan findEstimationPlanAtRevision(Long estimationPlanId, Integer revisionNumber);
    
    EstimatePlan findByPlanTypeAndAtrApplication (EstimatePlanType planType,ATRApplication application);
    
    EstimatePlan findByPlanTypeAndStatusAndAtrApplication (EstimatePlanType planType, ATRApplication atrApplication, EstimatePlanStatus status);

}
