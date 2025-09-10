/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.common;

/**
 *
 * @author S2030702
 */
public enum TaxAct {
    INCOME_TAX_ACT("Income Tax Act"),
    VAT_TAX_ACT("VAT Tax Act"),
    SECURITIES_TRANSFER_TAX_ACT("Securities Transfer Tax Act"),
    TRANSFER_DUTY_ACT("Transfer Duty Act");
    
    private final String name;

    TaxAct(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
