/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.AtrApplicationQuestion;
import za.gov.sars.persistence.AtrApplicationQuestionRepository;

/**
 *
 * @author S2026064
 */
@Service
@Transactional
public class AtrApplicationQuestionService implements AtrApplicationQuestionServiceLocal {

    @Autowired
    private AtrApplicationQuestionRepository atrApplicationRepository;

    @Override
    public void deleteAll() {
        atrApplicationRepository.deleteAll();
    }

    @Override
    public List<AtrApplicationQuestion> findAll() {
        return atrApplicationRepository.findAll();
    }

    @Override
    public Page<AtrApplicationQuestion> listAll(Pageable pageable) {
        return atrApplicationRepository.findAll(pageable);
    }

    @Override
    public AtrApplicationQuestion save(AtrApplicationQuestion entity) {
        return atrApplicationRepository.save(entity);

    }

    @Override
    public AtrApplicationQuestion findById(Long id) {
        return atrApplicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                "The requested id [" + id
                + "] does not exist."));
    }

    @Override
    public AtrApplicationQuestion update(AtrApplicationQuestion entity) {
        return atrApplicationRepository.save(entity);
    }

    @Override
    public AtrApplicationQuestion deleteById(Long id) {
        AtrApplicationQuestion userAtrApplication = findById(id);
        if (userAtrApplication != null) {
            atrApplicationRepository.delete(userAtrApplication);
        }
        return userAtrApplication;
    }

    @Override
    public boolean isExist(AtrApplicationQuestion entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Page<AtrApplicationQuestion> findAll(Specification specification, Pageable pageable) {
        return atrApplicationRepository.findAll(specification, pageable);
    }

    @Override
    public List<AtrApplicationQuestion> findByAtrApplication(ATRApplication atrApplication) {
        return atrApplicationRepository.findByAtrApplication(atrApplication);
    }

}
