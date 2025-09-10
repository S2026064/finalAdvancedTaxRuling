/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package za.gov.sars.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.gov.sars.domain.DownloadRecord;

/**
 *
 * @author S2026064
 */
@Repository
public interface DownloadRecordRepository extends JpaRepository<DownloadRecord, Long>{
    
   
}
