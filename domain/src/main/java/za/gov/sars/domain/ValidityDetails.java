/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import za.gov.sars.common.ValidityRulingType;

/**
 *
 * @author S2026064
 */
@Audited
@Getter
@Setter
@Entity
@Table(name = "validity_details")
public class ValidityDetails extends BaseEntity{
    
    @Column(name = "validity_description")
    private String validityDescription;
 
    @Column(name = "validity_start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validityStartDate;
    
    @Column(name = "validity_assessment_year")
    private int validityAssessmentYear;
    
    @Column(name = "validity_period_years")
    private int validityPeriodYears;
    
    @Column(name = "validity_ruling_type")
    @Enumerated(EnumType.STRING)
    private ValidityRulingType validityRulingType;
    
}
