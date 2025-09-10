/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

/**
 *
 * @author S2026095
 */
@Audited
@Entity
@Getter
@Setter
@Table(name = "user_role")
public class UserRole extends BaseEntity {

    @Column(name = "description")
    private String description;

    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "admin_sett_id")
    private AdministrationSettings administrationSettings;

    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "atr_app_sett_id")
    private AtrApplicationSettings atrApplicationSettings;

//    @OneToOne(cascade = CascadeType.ALL, optional = true)
//    @JoinColumn(name = "query_sett_id")
//    private TimesheetSettings timesheetSettings;

    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "user_perm_id")
    private UserPermission userPermission;

}
