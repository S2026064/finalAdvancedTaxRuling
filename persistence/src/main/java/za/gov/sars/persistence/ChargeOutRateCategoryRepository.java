package za.gov.sars.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import za.gov.sars.common.ChargeCategory;
import za.gov.sars.domain.ChargeOutRateCategory;

/**
 *
 * @author S2026064
 */
@Repository
public interface ChargeOutRateCategoryRepository extends JpaRepository<ChargeOutRateCategory, Long>, JpaSpecificationExecutor<ChargeOutRateCategory> {

    ChargeOutRateCategory findByChargeCategory(ChargeCategory chargeCategory);
    List<ChargeOutRateCategory> findByChargeCategoryNot(ChargeCategory chargeCategory);
}
