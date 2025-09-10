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
public class EstimationPlanRevisionDTO {

    private Long id;
    private int revisionNumber;
    private long revisionTimestamp;
    private String username;
    private RevisionType revisionType;
    private Long estimationPlanId;
    private double grandTotal;
    private Double depositPercentage;
    private Integer daysToComplete;
    private Double depositAmount;
    private String status;

    public EstimationPlanRevisionDTO(
            Long id, int revisionNumber, long revisionTimestamp, String username,
            RevisionType revisionType, Long estimationPlanId
    ) {
        this.id = id;
        this.revisionNumber = revisionNumber;
        this.revisionTimestamp = revisionTimestamp;
        this.username = username;
        this.revisionType = revisionType;
        this.estimationPlanId = estimationPlanId;
    }

    public EstimationPlanRevisionDTO(Long id, int revisionNumber, long revisionTimestamp, String username, RevisionType revisionType,double grandTotal, Double depositPercentage, Integer daysToComplete, Double depositAmount, String status, Long estimationPlanId) {
        this.id = id;
        this.revisionNumber = revisionNumber;
        this.revisionTimestamp = revisionTimestamp;
        this.username = username;
        this.revisionType = revisionType;
        this.estimationPlanId = estimationPlanId;
        this.grandTotal = grandTotal;
        this.depositPercentage = depositPercentage;
        this.daysToComplete = daysToComplete;
        this.depositAmount = depositAmount;
        this.status = status;
    }

    

    
}
