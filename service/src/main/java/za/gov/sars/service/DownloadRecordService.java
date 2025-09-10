/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.gov.sars.domain.DownloadRecord;
import za.gov.sars.persistence.DownloadRecordRepository;


/**
 *
 * @author S2028873
 */
@Service
@Transactional
public class DownloadRecordService implements DownloadRecordServiceLocal {

    @Autowired
    private DownloadRecordRepository downloadRecordRepository;

   

    @Override
    public DownloadRecord save(DownloadRecord downloadRecord) {
        return downloadRecordRepository.save(downloadRecord);
    }

    @Override
    public DownloadRecord findById(Long id) {
        return downloadRecordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                "The requested id [" + id
                + "] does not exist."));
    }

    @Override
    public DownloadRecord update(DownloadRecord downloadRecord) {
        return  downloadRecordRepository.save(downloadRecord);
    }

    @Override
    public DownloadRecord deleteById(Long id) {
         DownloadRecord downloadRecord = findById(id);

        if (downloadRecord != null) {
            downloadRecordRepository.delete(downloadRecord);
        }
        return downloadRecord;
    }

    @Override
    public List<DownloadRecord> listAll() {
        return downloadRecordRepository.findAll();
    }

}
