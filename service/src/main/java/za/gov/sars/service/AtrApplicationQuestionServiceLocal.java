/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package za.gov.sars.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.AtrApplicationQuestion;

/**
 *
 * @author S2026064
 */
public interface AtrApplicationQuestionServiceLocal extends GenericServiceLocal<AtrApplicationQuestion> {

    public void deleteAll();

    public List<AtrApplicationQuestion> findAll();

    Page<AtrApplicationQuestion> listAll(Pageable pageable);

    public List<AtrApplicationQuestion> findByAtrApplication(ATRApplication atrApplication);

}
