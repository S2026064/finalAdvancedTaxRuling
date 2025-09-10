/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
import za.gov.sars.common.EstimatePlanStatus;
import za.gov.sars.common.EstimatePlanType;
import za.gov.sars.common.ResourceType;
import za.gov.sars.domain.service.ActivityServiceLocal;

/**
 *
 * @author S2026095
 */
@Audited
@Entity
@Getter
@Setter
@Table(name = "estimate_plan")
public class EstimatePlan extends BaseEntity {

   
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = true)
    @JoinColumn(name = "charge_id")
    private ChargeOutRateCategory chargeOutRateCategory;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atr_application_id", nullable = false)
    private ATRApplication atrApplication;

    //we use this if user selected urgent
    @Column(name = "custom_days")
    private Integer customDays;

    @Column(name = "resource_type")
    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;

    @Column(name = "total_hours")
    private double totalHours;

    @Column(name = "subtotal_fee")
    private Double subtotalFee;

    @Column(name = "grand_total")
    private double grandTotal;

    @Column(name = "deposit_percentage")
    private Double depositPercentage;

    @Column(name = "deposit_amount")
    private Double depositAmount;

    @Column(name = "travel_expense")
    private double travelExp;

    @Column(name = "external_resource")
    private double externalResource;

    @Column(name = "days_to_complete")
    private Integer daysToComplete;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EstimatePlanStatus status;
    
    @Column(name = "estimate_type")
    @Enumerated(EnumType.STRING)
    private EstimatePlanType planType;

    @Column(name = "letter_accepted")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean letterConditions;

    @OneToMany(mappedBy = "estimatePlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EstimateRecord> estimateRecords = new ArrayList<>();

    public void synchronizeWithActivities(ActivityServiceLocal activityProvider) {
        List<Activity> activeActivities = activityProvider.findAll();

        if (this.estimateRecords == null
                || this.estimateRecords.size() != activeActivities.size()
                || !containsAllActivities(activeActivities)) {

            this.estimateRecords = activeActivities.stream()
                    .map(activity -> createEstimateItemForActivity(activity))
                    .collect(Collectors.toList());
        }
    }

    private boolean containsAllActivities(List<Activity> activities) {
        List<String> activityDescriptions = activities.stream()
                .map(Activity::getDescription)
                .collect(Collectors.toList());

        return estimateRecords.stream()
                .map(EstimateRecord::getDescription)
                .collect(Collectors.toList())
                .containsAll(activityDescriptions);
    }

    private EstimateRecord createEstimateItemForActivity(Activity activity) {
        EstimateRecord item = new EstimateRecord();
        item.setDescription(activity.getDescription());
        item.setActivityCategory(activity.getActivityCategory());
        item.setActivityType(activity.getActivityType());
        item.setConsultantHours(0.0);
        item.setManagerHours(0.0); // Default value
        item.setTotalHours(0);
        item.setHourlyRate(0.0);
        item.setEstimatePlan(this);
        return item;
    }

    public boolean isEditable() {
        return status == EstimatePlanStatus.ESTIMATE_REJECTED;
    }
}
