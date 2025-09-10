/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.gov.sars.common.TaxAct;
import za.gov.sars.domain.ActSection;
import za.gov.sars.persistence.ActSectionRepository;

/**
 *
 * @author S2030702
 */
@Service
@Transactional
public class ActSectionService implements ActSectionServiceLocal {
    
    @Autowired
    private ActSectionRepository actSectionRepo;

    @Override
    public ActSection save(ActSection entity) {
        return actSectionRepo.save(entity);
    }

    @Override
    public ActSection findById(Long id) {
        return actSectionRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                "The requested id [" + id
                + "] does not exist."));
    }

    @Override
    public ActSection update(ActSection entity) {
        return actSectionRepo.save(entity);
    }

    @Override
    public ActSection deleteById(Long id) {
        ActSection rulingActSection = findById(id);

        if (rulingActSection != null) {
            actSectionRepo.delete(rulingActSection);
        }
        return rulingActSection;
    }

    @Override
    public boolean isExist(ActSection entity) {
        return findById(entity.getId()) != null;
    }

    @Override
    public Page<ActSection> findAll(Specification specification, Pageable pageable) {
        return actSectionRepo.findAll(specification, pageable);
    }

    @Override
    public void deleteAll() {
        actSectionRepo.deleteAll();
    }

    @Override
    public List<ActSection> findAll() {
        return actSectionRepo.findAll();
    }

    @Override
    public Page<ActSection> listAll(Pageable pageable) {
        return actSectionRepo.findAll(pageable);
    }

    @Override
    public List<ActSection> findByTaxAct(TaxAct taxAct) {
        return actSectionRepo.findByTaxAct(taxAct);
    }
    
}
