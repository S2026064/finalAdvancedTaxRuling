/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import za.gov.sars.common.TimesheetStatus;

/**
 *
 * @author S2026095
 */
@Audited
@Entity
@Getter
@Setter
@Table(name = "timesheet")
public class Timesheet extends BaseEntity {

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = true, targetEntity = User.class)
    @JoinColumn(name = "resource_id")
    private User resource;

    @OneToMany(mappedBy = "timesheet", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<TimeSheetActivity> timeSheetActivities = new ArrayList<>();

    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TimesheetStatus timesheetStatus;

    public void addTimeSheetActivity(TimeSheetActivity timeSheetActivity) {
        this.timeSheetActivities.add(timeSheetActivity);
    }

    public void removeTimeSheetActivity(TimeSheetActivity timeSheetActivity) {
        this.timeSheetActivities.remove(timeSheetActivity);
    }

    public void addTimeSheetActivities(List<TimeSheetActivity> timeSheetActivitys) {
        this.timeSheetActivities.clear();
        for (TimeSheetActivity Activities : timeSheetActivitys) {
             Activities.setTimesheet(this);
            this.timeSheetActivities.add(Activities);
        }
    }
}
