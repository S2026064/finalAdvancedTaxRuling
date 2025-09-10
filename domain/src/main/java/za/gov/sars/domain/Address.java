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
import za.gov.sars.common.ActivityType;
import za.gov.sars.common.AddressType;
import za.gov.sars.common.Province;

/**
 *
 * @author S2026095
 */
@Entity
@Getter
@Setter
@Table(name = "address")
public class Address extends BaseEntity {

    @Column(name = "line1")
    private String line1;

    @Column(name = "line2")
    private String line2;

    @Column(name = "line3")
    private String line3;

    @Column(name = "province")
    @Enumerated(EnumType.STRING)
    private Province province;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "address_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AddressType addressType;

}
