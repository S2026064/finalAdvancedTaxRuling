/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import za.gov.sars.common.ResourceType;

/**
 *
 * @author S2026095
 */
@Audited
@Entity
@Getter
@Setter
@Table(name = "user_atr_app")
public class UserAtrApplication extends BaseEntity {

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = true, targetEntity = ATRApplication.class)
    @JoinColumn(name = "atr_app_id")
    private ATRApplication atrApplication;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "resource_type", nullable = true)
    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;

}
