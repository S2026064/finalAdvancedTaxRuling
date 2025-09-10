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
@Table(name = "atr_app_sett")
public class AtrApplicationSettings extends BaseEntity {

    @Column(name = "assign_link")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean assign;
    @Column(name = "allocation_link")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean allocation;

    @Column(name = "app_link")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean application;
    @Column(name = "estimation_link")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean estimationPlan;
    @Column(name = "ruling_link")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean ruling;
    @Column(name = "document_link")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean document;
    @Column(name = "timesheet_link")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean timesheet;
    @Column(name = "query_link")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean query;
    @Column(name = "payment")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean payment;
    @Column(name = "timesheet_review")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean timesheetReview;
    @Column(name = "re_assign")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean reAssign;

    public AtrApplicationSettings() {
        assign = Boolean.FALSE;
        allocation = Boolean.FALSE;
        reAssign = Boolean.FALSE;
        ruling = Boolean.FALSE;
        payment = Boolean.FALSE;
        timesheet = Boolean.FALSE;
        query = Boolean.FALSE;
        document = Boolean.FALSE;
        estimationPlan = Boolean.FALSE;
        application = Boolean.FALSE;
        timesheetReview= Boolean.FALSE;
    }

    public boolean isApplication() {
        return this.reAssign || this.estimationPlan || this.ruling || this.assign || this.payment || this.timesheet || this.allocation || this.document || this.query || this.application || this.timesheetReview;
    }

}
