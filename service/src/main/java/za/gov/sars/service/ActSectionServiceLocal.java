/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import za.gov.sars.common.TaxAct;
import za.gov.sars.domain.ActSection;

/**
 *
 * @author S2030702
 */

public interface ActSectionServiceLocal extends GenericServiceLocal<ActSection>{
    public void deleteAll();
    public List<ActSection> findAll();
    Page<ActSection> listAll(Pageable pageable);
    public List<ActSection> findByTaxAct(TaxAct taxAct);
    
}
