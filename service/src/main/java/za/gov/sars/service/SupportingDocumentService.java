/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.gov.sars.common.DocumentType;
import za.gov.sars.domain.SupportingDocument;
import za.gov.sars.persistence.SupportingDocumentRepository;


/**
 *
 * @author S2028873
 */
@Service
@Transactional
public class SupportingDocumentService implements SupportingDocumentServiceLocal {

    @Autowired
    private SupportingDocumentRepository SupportingDocumentRepository;

    @Override
    public SupportingDocument save(SupportingDocument supportingDocument) {
        return SupportingDocumentRepository.save(supportingDocument);
    }

    @Override
    public SupportingDocument findById(Long id) {
        return SupportingDocumentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                "The requested id [" + id
                + "] does not exist."));
    }

    @Override
    public void deleteAll() {
        SupportingDocumentRepository.deleteAll();
    }

    @Override
    public SupportingDocument update(SupportingDocument supportingDocument) {
        return SupportingDocumentRepository.save(supportingDocument);
    }

    @Override
    public List<SupportingDocument> listAll() {
        return SupportingDocumentRepository.findAll();
    }

    @Override
    public boolean isExist(SupportingDocument supportingDocument) {
        return SupportingDocumentRepository.findById(supportingDocument.getId()) != null;
    }
    
    @Override
    public SupportingDocument deleteById(Long id) {
     SupportingDocument supportingDocument = findById(id);
        if (supportingDocument != null) {
            SupportingDocumentRepository.delete(supportingDocument);
        }
        return supportingDocument;
    }

    @Override
    public List<SupportingDocument> findByDocumentType(DocumentType documentType) {
        return SupportingDocumentRepository.findByDocumentType(documentType);
    }

  

}
