/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package za.gov.sars.service;

import za.gov.sars.domain.JsonDocumentDto;

/**
 *
 * @author S2026064
 */
public interface DocumentAttachmentServiceLocal {

    public JsonDocumentDto getDocumentJsonValue(String code);

    public String uploadDocument(JsonDocumentDto document);

    public String uploadDocument();

   public void download(String objectId);


}
