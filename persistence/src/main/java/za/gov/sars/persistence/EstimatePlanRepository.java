package za.gov.sars.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import za.gov.sars.common.EstimatePlanStatus;
import za.gov.sars.common.EstimatePlanType;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.EstimatePlan;

/**
 *
 * @author S2026064
 */
@Repository
public interface EstimatePlanRepository extends JpaRepository<EstimatePlan, Long>, JpaSpecificationExecutor<EstimatePlan> {

    List<EstimatePlan> findByAtrApplication(ATRApplication application);

    List<EstimatePlan> findByAtrApplicationAndStatus(ATRApplication atrApplication, EstimatePlanStatus status);

    List<EstimatePlan> findByStatus(EstimatePlanStatus status);
    
    EstimatePlan findByPlanTypeAndAtrApplication (EstimatePlanType planType,ATRApplication application);
    
    EstimatePlan findByPlanTypeAndStatusAndAtrApplication (EstimatePlanType planType, ATRApplication atrApplication, EstimatePlanStatus status);

    List<EstimatePlan> findByAtrApplicationOrderByIdAsc(ATRApplication application);

    List<EstimatePlan> findByAtrApplicationAndStatusIn(ATRApplication application, List<EstimatePlanStatus> statuses);
}
