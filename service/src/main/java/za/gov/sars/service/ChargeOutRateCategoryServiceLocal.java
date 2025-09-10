package za.gov.sars.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import za.gov.sars.common.ChargeCategory;
import za.gov.sars.domain.ChargeOutRateCategory;

/**
 *
 * @author S2024726
 */
public interface ChargeOutRateCategoryServiceLocal extends GenericServiceLocal<ChargeOutRateCategory> {

     void deleteAll();

     List<ChargeOutRateCategory> findAll();

    Page<ChargeOutRateCategory> listAll(Pageable pageable);

    ChargeOutRateCategory findByChargeCategory(ChargeCategory chargeCategory);
    
    List<ChargeOutRateCategory> findByChargeCategoryNot(ChargeCategory chargeCategory);

}
