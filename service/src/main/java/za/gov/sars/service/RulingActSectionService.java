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
import za.gov.sars.domain.RulingActSection;
import za.gov.sars.persistence.RulingActSectionRepository;

/**
 *
 * @author S2030702
 */
@Service
@Transactional
public class RulingActSectionService implements RulingActSectionServiceLocal{
    
    @Autowired
    private RulingActSectionRepository rulingActSectionRepo;

    @Override
    public RulingActSection save(RulingActSection entity) {
        return rulingActSectionRepo.save(entity);
    }

    @Override
    public RulingActSection findById(Long id) {
        return rulingActSectionRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                "The requested id [" + id
                + "] does not exist."));
    }

    @Override
    public RulingActSection update(RulingActSection entity) {
        return rulingActSectionRepo.save(entity);
    }

    @Override
    public RulingActSection deleteById(Long id) {
        RulingActSection rulingActSection = findById(id);

        if (rulingActSection != null) {
            rulingActSectionRepo.delete(rulingActSection);
        }
        return rulingActSection;
    }

    @Override
    public boolean isExist(RulingActSection entity) {
        return findById(entity.getId()) != null;
    }

    @Override
    public Page<RulingActSection> findAll(Specification specification, Pageable pageable) {
        return rulingActSectionRepo.findAll(specification, pageable);
    }
}
