/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

/**
 *
 * @author S2026095
 */
@Audited
@Getter
@Setter
@Entity
@Table(name = "admin_sett")
public class AdministrationSettings extends BaseEntity {

    @Column(name = "sys_user")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean users;

    @Column(name = "user_role")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean userRoles;

    @Column(name = "question")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean question;

    @Column(name = "activity")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean activity;

    @Column(name = "documentum")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean documentum;

    @Column(name = "atr_trail")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean atrTrail;

    @Column(name = "admin_dash")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean adminDash;
    @Column(name = "category")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean category;

    public AdministrationSettings() {

        users = Boolean.FALSE;
        userRoles = Boolean.FALSE;
        documentum = Boolean.FALSE;
        activity = Boolean.FALSE;
        question = Boolean.FALSE;
        atrTrail = Boolean.FALSE;
        adminDash = Boolean.FALSE;
    }

    public boolean isAdministrator() {
        return this.users || this.userRoles || this.documentum || this.activity || this.question || this.atrTrail || this.category || this.adminDash;
        
    }

    

}
