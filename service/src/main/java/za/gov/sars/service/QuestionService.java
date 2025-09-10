/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.gov.sars.domain.Question;
import za.gov.sars.persistence.QuestionRepository;

/**
 *
 * @author S2026064
 */
@Service
@Transactional
public class QuestionService implements QuestionServiceLocal {

    @Autowired

    private QuestionRepository questionRepository;

    @Override
    public Question save(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public Question findById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                "The requested id [" + id
                + "] does not exist."));
    }
    
    @Override
    public void deleteAll() {
        questionRepository.deleteAll();
    }

    @Override
    public Question update(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public List<Question> listAll() {
        return questionRepository.findAll();
    }
    
    @Override
    public List<Question> findAll() {
        return questionRepository.findAll();
    }


    @Override
    public boolean isExist(Question question) {
        return questionRepository.findById(question.getId()) != null;
    }

    @Override
    public Question deleteById(Long id) {
        Question question = findById(id);
        if (question != null) {
            questionRepository.delete(question);
        }
        return question;
    }

}
