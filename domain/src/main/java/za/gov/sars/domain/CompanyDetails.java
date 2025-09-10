/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
import za.gov.sars.common.OrganisationType;
import za.gov.sars.common.CloseType;

/**
 *
 * @author S2026095
 */
@Audited
@Entity
@Getter
@Setter
@Table(name = "company_details")
public class CompanyDetails extends BaseEntity {

    @Column(name = "registered_name")
    private String registeredName;

    @Column(name = "trading_name")
    private String tradingName;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "documentation")
    private String documentation;

    @Column(name = "organisation_description")
    private String description;

    @Column(name = "org_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrganisationType organisationType;

    @Column(name = "public_company", nullable = false)
    @Enumerated(EnumType.STRING)
    private CloseType publicCompany;

    @Column(name = "listed", nullable = false)
    @Enumerated(EnumType.STRING)
    private CloseType listed;

    @Column(name = "group_member", nullable = false)
    @Enumerated(EnumType.STRING)
    private CloseType groupMember;

    @Column(name = "subsidiary", nullable = false)
    @Enumerated(EnumType.STRING)
    private CloseType subsidiary;

    @Column(name = "registered_in_rsa", nullable = false)
    @Enumerated(EnumType.STRING)
    private CloseType registered;

    @Column(name = "ngo", nullable = false)
    @Enumerated(EnumType.STRING)
    private CloseType ngo;

    @Column(name = "pbo", nullable = false)
    @Enumerated(EnumType.STRING)
    private CloseType pbo;

    @Column(name = "close_corp", nullable = false)
    @Enumerated(EnumType.STRING)
    private CloseType closeCorp;

}
