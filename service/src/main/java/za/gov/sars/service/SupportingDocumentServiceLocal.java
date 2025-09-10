/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package za.gov.sars.service;

import java.util.List;
import za.gov.sars.common.DocumentType;
import za.gov.sars.domain.SupportingDocument;


/**
 *
 * @author S2028873
 */
public interface SupportingDocumentServiceLocal {

    public SupportingDocument save(SupportingDocument supportingDocument);

    public SupportingDocument findById(Long id);

    public void deleteAll();

    SupportingDocument deleteById(Long id);

    public SupportingDocument update(SupportingDocument supportingDocument);

    public List<SupportingDocument> listAll();

  
    public boolean isExist(SupportingDocument supportingDocument);
    
    List<SupportingDocument> findByDocumentType(DocumentType documentType);
}
