/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import za.gov.sars.common.Status;

/**
 *
 * @author S2030702
 */
@Audited
@Entity
@Getter
@Setter
@Table(name = "chargesheet")
public class ChargeSheet extends BaseEntity {

    @Column(name = "travel_expense")
    private double travelExp;

    @Column(name = "external_resource")
    private double externalResource;

    @Column(name = "grand_total")
    private double grandTotal;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Audited(targetAuditMode = RelationTargetAuditMode.AUDITED)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "chargeSheet")
    private List<ChargeSheetEntry> chargeSheetEntrys;
    
    public double getTotalInvoiceAmount() {
        return this.chargeSheetEntrys.stream().mapToDouble(entry -> entry.getInvoiceTotal()).sum();
    }

    public void addChargeSheetEntry(ChargeSheetEntry chargeSheetEntry) {
        this.chargeSheetEntrys.add(chargeSheetEntry);
    }

    public void addChargeSheetEntry(Integer index, ChargeSheetEntry chargeSheetEntry) {
        this.chargeSheetEntrys.add(0, chargeSheetEntry);
    }

    public void removeChargeSheetEntry(ChargeSheetEntry chargeSheetEntry) {
        this.chargeSheetEntrys.remove(chargeSheetEntry);
    }

    public void removeChargeSheetEntrys(List<ChargeSheetEntry> chargeSheetEntrys) {
        this.chargeSheetEntrys.clear();
        for (ChargeSheetEntry chargeSheetEntry : chargeSheetEntrys) {
            this.chargeSheetEntrys.remove(chargeSheetEntry);
        }
    }

}
