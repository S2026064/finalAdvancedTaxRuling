/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import za.gov.sars.common.TaxAct;

/**
 *
 * @author S2030702
 */

@Audited
@Entity
@Getter
@Setter
@Table(name = "act_section")
public class ActSection extends BaseEntity{
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "type")
    private String type;
    
    @Column(name = "tax_act")
    @Enumerated(EnumType.STRING)
    private TaxAct taxAct;
    
    @Override
    public String toString() {
        return description; 
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActSection)) return false;
        ActSection other = (ActSection) o;
        return this.getId() != null && this.getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getId() == null ? 0 : getId().hashCode();
    }
    
}
