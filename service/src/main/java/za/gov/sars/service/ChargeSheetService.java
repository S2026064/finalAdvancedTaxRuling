/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.gov.sars.domain.ChargeSheet;
import za.gov.sars.persistence.ChargeSheetRepository;

/**
 *
 * @author S2030702
 */
@Service
@Transactional
public class ChargeSheetService implements ChargeSheetServiceLocal {
    
    @Autowired
    private ChargeSheetRepository chargeSheetService;

    @Override
    public ChargeSheet save(ChargeSheet entity) {
        return chargeSheetService.save(entity);
    }

    @Override
    public ChargeSheet findById(Long id) {
        return chargeSheetService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                                "The requested id [" + id
                                + "] does not exist."));
    }

    @Override
    public ChargeSheet update(ChargeSheet entity) {
        return chargeSheetService.save(entity);
    }

    @Override
    public ChargeSheet deleteById(Long id) {
        ChargeSheet chargeSheet = findById(id);
        if (chargeSheet != null) {
            chargeSheetService.deleteById(id);
        }
        return chargeSheet;
    }

    @Override
    public boolean isExist(ChargeSheet entity) {
        return chargeSheetService.findById(entity.getId()) != null;
    }

    @Override
    public Page<ChargeSheet> findAll(Specification specification, Pageable pageable) {
        return chargeSheetService.findAll(specification, pageable);
    }

    @Override
    public ChargeSheet findByIdWithEntries(Long id) {
        return chargeSheetService.findByIdWithEntries(id);
    }
    
}
