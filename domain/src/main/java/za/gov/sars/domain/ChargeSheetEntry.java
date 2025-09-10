/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

/**
 *
 * @author S2030702
 */
@Audited
@Entity
@Getter
@Setter
@Table(name = "chargesheet_entries")
public class ChargeSheetEntry extends BaseEntity {
    
  
    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = true, targetEntity = ChargeSheet.class)
    @JoinColumn(name = "chargesheet_id")
    private ChargeSheet chargeSheet;
    
    @Column(name = "estimated_hours")
    private double estimatedHours;
    
    @Column(name = "estimated_amount")
    private double estimatedAmount;
    
    @Column(name = "invoice_to_date")
    private double invoiceToDate;
    
    @Column(name = "balance")
    private double balance;
    
    @Column(name = "this_period_hours")
    private double thisPeriodHours;
    
    @Column(name = "thisPeriodAmount")
    private double thisPeriodAmount;
    
    @Column(name = "invoice_total")
    private double invoiceTotal;
   
}
