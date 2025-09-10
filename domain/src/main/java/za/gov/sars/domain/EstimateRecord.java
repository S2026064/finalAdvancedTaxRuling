/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import za.gov.sars.common.ActivityCategory;
import za.gov.sars.common.ActivityType;

/**
 *
 * @author S2026095
 */
@Audited
@Entity
@Getter
@Setter
@Table(name = "estimate_record")
public class EstimateRecord extends BaseEntity {

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityCategory activityCategory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityType activityType;

    @Column(name = "consultant_hours")
    private double consultantHours;

    @Column(name = "manager_hours")
    private double managerHours;

    @Column(name = "hourly_rate")
    private double hourlyRate;

    @Column(name = "total_hours")
    private double totalHours;

    @Column(name = "summing_amount")
    private double totalAmount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "estimate_plan_id")
    private EstimatePlan estimatePlan;

    public Double getHourlyRate() {
        if (activityType.equals(ActivityType.NON_BILLABLE)) {
            return 0.0;
        } else {
            if (estimatePlan.getChargeOutRateCategory() != null) {
                return estimatePlan.getChargeOutRateCategory().getHourlyRate();
            } else {
                return 0.0;
            }

        }

    }

    @Transient
    public Double getTotalAmount() {
        if (activityType.equals(ActivityType.BILLABLE)) {
            return hourlyRate * (managerHours + consultantHours);
        }
        return 0.00;
    }

    @Transient
    public Double getTotalHours() {
        if (consultantHours == 0.00 && managerHours == 0.00) {
            return 0.0;
        }
        return managerHours + consultantHours;
    }

}
