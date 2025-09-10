/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package za.gov.sars.service;

import java.util.List;
import za.gov.sars.domain.DownloadRecord;

/**
 *
 * @author S2026064
 */
public interface DownloadRecordServiceLocal {

    DownloadRecord save(DownloadRecord downloadRecord);

    DownloadRecord findById(Long id);

    DownloadRecord update(DownloadRecord downloadRecord);

    DownloadRecord deleteById(Long id);

    List<DownloadRecord> listAll();
}
