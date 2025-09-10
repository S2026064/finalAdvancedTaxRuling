/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author S2026095
 */

@Entity
@Getter
@Setter
@Table(name = "system_query")
public class SystemQuery extends BaseEntity {

    @Column(name = "description", length = 4000)
    private String description;
    
    @Column(name = "unread_admin")
    private boolean unreadByAdmin;
    
    @Column(name = "unread_applicant")
    private boolean unreadByApplicant;
    
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = true, targetEntity = ATRApplication.class)
    @JoinColumn(name = "atr_app_id")
    private ATRApplication atrApplication;

}
