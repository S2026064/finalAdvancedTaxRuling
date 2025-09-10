/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.domain;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import za.gov.sars.common.TimesheetStatus;

/**
 *
 * @author S2028398
 */
@Audited
@Entity
@Getter
@Setter
@Table(name = "timesheet_activity")
public class TimeSheetActivity extends BaseEntity {

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = true, targetEntity = User.class)
    @JoinColumn(name = "resource_id")
    private User resource;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = true, targetEntity = ATRApplication.class)
    @JoinColumn(name = "application_id")
    private ATRApplication atrApplication;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = true, targetEntity = Activity.class)
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = true, targetEntity = Timesheet.class)
    @JoinColumn(name = "timesheet_id")
    private Timesheet timesheet;

    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TimesheetStatus timesheetStatus;

    @Column(name = "captured_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date capturedDate;

    @Column(name = "number_of_hours")
    private double numberOfHours;

    @Lob
    @Column(name = "description")
    private String description;
    
    @Column(name = "description_active")
    private boolean descriptionActive=Boolean.FALSE;
}
