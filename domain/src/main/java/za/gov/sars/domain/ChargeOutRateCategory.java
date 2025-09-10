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
import za.gov.sars.common.ChargeCategory;

/**
 *
 * @author S2026095
 */
@Audited
@Entity
@Getter
@Setter
@Table(name = "charge_out_rate_category")
public class ChargeOutRateCategory extends BaseEntity {

    @Column(name = "charge_category", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private ChargeCategory chargeCategory;

    @Column(name = "predefined_days")
    private Integer predefinedDays;

    @Column(name = "hourly_rate")
    private double hourlyRate;

    @Column(name = "fee_range_min")
    private double feeRangeMin;

    @Column(name = "dep_percentage")
    private double depositPercentage;

    @Column(name = "fee_range_max")
    private double feeRangeMax;
}
