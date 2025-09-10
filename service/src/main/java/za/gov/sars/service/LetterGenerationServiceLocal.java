/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package za.gov.sars.service;

import za.gov.sars.atr.gen.dto.res.PDFDocumentGenerationManagementResponse;
import za.gov.sars.domain.LetterDTO;


/**
 *
 * @author S2028398
 */
public interface LetterGenerationServiceLocal {
   PDFDocumentGenerationManagementResponse generateLetterDocument(LetterDTO letterDTO);
    public void downloadGeneratedDocument(String base64String); 
}
