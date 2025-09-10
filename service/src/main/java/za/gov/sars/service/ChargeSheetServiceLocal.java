/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.service;

import za.gov.sars.domain.ChargeSheet;

/**
 *
 * @author S2030702
 */
public interface ChargeSheetServiceLocal extends GenericServiceLocal<ChargeSheet>{
     ChargeSheet findByIdWithEntries(Long id);
}
