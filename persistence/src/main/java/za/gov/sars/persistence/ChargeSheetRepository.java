/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.gov.sars.domain.ChargeSheet;

/**
 *
 * @author S2030702
 */
@Repository
public interface ChargeSheetRepository extends JpaRepository<ChargeSheet, Long>, JpaSpecificationExecutor<ChargeSheet> {

    @Query("SELECT c FROM ChargeSheet c LEFT JOIN FETCH c.chargeSheetEntrys WHERE c.id = :id")
    ChargeSheet findByIdWithEntries(@Param("id") Long id);
}
