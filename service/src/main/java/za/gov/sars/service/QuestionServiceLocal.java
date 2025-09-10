/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package za.gov.sars.service;

import java.util.List;
import za.gov.sars.domain.Question;

/**
 *
 * @author S2026064
 */
public interface QuestionServiceLocal {

    public Question save(Question question);

    public Question findById(Long id);

    public void deleteAll();

    Question deleteById(Long id);

    public Question update(Question question);

    public List<Question> listAll();

    public List<Question> findAll();

    public boolean isExist(Question question);
}
