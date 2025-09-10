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
@Entity
@Getter
@Setter
@Table(name = "tax_type")
public class TaxType extends BaseEntity {

    @Column(name = "income_tax_act")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean incomeTaxAct;

    @Column(name = "securities_trans_tax_act")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean securitiesTransferTaxAct;

    @Column(name = "vat_tax_act")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean vatTaxAct;

    @Column(name = "trans_duty_act")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean transferDutyAct;

    public TaxType() {
        this.incomeTaxAct = Boolean.FALSE;
        this.securitiesTransferTaxAct = Boolean.FALSE;
        this.vatTaxAct = Boolean.FALSE;
        this.transferDutyAct = Boolean.FALSE;

    }

    public TaxType reset() {
        this.setIncomeTaxAct(false);
        this.setSecuritiesTransferTaxAct(false);
        this.setTransferDutyAct(false);
        this.setVatTaxAct(false);
        return this;
    }
}
