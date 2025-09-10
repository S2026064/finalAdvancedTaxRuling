/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.RevisionType;

/**
 *
 * @author S2026080
 * @param <T>
 */
@Getter
@Setter
public class AtrApplicationRevisionDTO {

    private long id;
    private int revisionNumber;
    private long revisionTimestamp;
    private String username;
    private RevisionType revisionType;
    private Long applicationId;
  
    public AtrApplicationRevisionDTO(
            Long id, int revisionNumber, long revisionTimestamp, String username,
            RevisionType revisionType, Long applicationId
    ) {
        this.id = id;
        this.revisionNumber = revisionNumber;
        this.revisionTimestamp = revisionTimestamp;
        this.username = username;
        this.revisionType = revisionType;
        this.applicationId = applicationId;
    }
    
}
