/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

/**
 *
 * @author S2030702
 */
@Audited
@Entity
@Getter
@Setter
@Table(name = "ruling_act_section")
public class RulingActSection extends BaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "act_section_id")
    private ActSection actSection;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = true, targetEntity = ATRApplication.class)
    @JoinColumn(name = "atr_app_id")
    private ATRApplication atrApplication;
    
}
