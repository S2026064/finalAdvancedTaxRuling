package za.gov.sars.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.gov.sars.common.ChargeCategory;
import za.gov.sars.domain.ChargeOutRateCategory;
import za.gov.sars.persistence.ChargeOutRateCategoryRepository;

/**
 *
 * @author S2028873
 */
@Service
@Transactional
public class ChargeOutRateCategoryService implements ChargeOutRateCategoryServiceLocal {

    @Autowired
    private ChargeOutRateCategoryRepository chargeOutRateCategoryRepository;

    @Override
    public void deleteAll() {
        chargeOutRateCategoryRepository.deleteAll();
    }

    @Override
    public List<ChargeOutRateCategory> findAll() {
        return chargeOutRateCategoryRepository.findAll();
    }

    @Override
    public Page<ChargeOutRateCategory> listAll(Pageable pageable) {
        return chargeOutRateCategoryRepository.findAll(pageable);
    }

    
    @Override
    public ChargeOutRateCategory save(ChargeOutRateCategory entity) {
        return chargeOutRateCategoryRepository.save(entity);
    }

    @Override
    public ChargeOutRateCategory findById(Long id) {
        return chargeOutRateCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                "The requested id [" + id
                + "] does not exist."));
    }

    @Override
    public ChargeOutRateCategory update(ChargeOutRateCategory entity) {
        return chargeOutRateCategoryRepository.save(entity);
    }

    @Override
    public ChargeOutRateCategory deleteById(Long id) {
        ChargeOutRateCategory category = findById(id);
        if (category != null) {
            chargeOutRateCategoryRepository.delete(category);
        }
        return category;
    }

    @Override
    public ChargeOutRateCategory findByChargeCategory(ChargeCategory chargeCategory) {
        return chargeOutRateCategoryRepository.findByChargeCategory(chargeCategory);
    }

    @Override
    public boolean isExist(ChargeOutRateCategory chargeOutRateCategory) {
       return chargeOutRateCategoryRepository.findById(chargeOutRateCategory.getId()) != null;
    }

    @Override
    public Page<ChargeOutRateCategory> findAll(Specification specification, Pageable pageable) {
        return chargeOutRateCategoryRepository.findAll(specification, pageable);
    }

    @Override
    public List<ChargeOutRateCategory> findByChargeCategoryNot(ChargeCategory chargeCategory) {
        return chargeOutRateCategoryRepository.findByChargeCategoryNot(chargeCategory);
    }


   


}
