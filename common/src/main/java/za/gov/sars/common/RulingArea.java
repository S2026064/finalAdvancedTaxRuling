/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package za.gov.sars.common;

/**
 *
 * @author S2026064
 */
public enum RulingArea {
    CONTROL_FOREIGN_COMPANIES("Control Foreign Companies"),
    GROSS_INCOME("Gross Income"),
    DEDUCTIONS("Deductions"),
    CORPORATE_RULES("Corporate Rules"),
    DONATIONS_TAX("Donations Tax"),
    SECONDARY_TAX_ON_COMPANIES("Secondary Tax On Companies"),
    ALLOWANCES("Allowances"),
    CAPITAL_GAIN_TAX("Capital Gain Tax"),
    OTHER_INCOME_TAX("Other Income Tax"),
    IMPORTS_EXPORTS("Imports Exports"),
    GOING_CONCERNS("Going Concerns"),
    VENDOR_STATUS("Vendor Status"),
    OTHER("Other"),
    THE_STAMP_DUTY_ACT_1968("The Stamp Duty Act 1968"),
    THE_TRANSFER_DUTY_ACT_1949("The Transfer Duty Act 1949"),
    THE_UNCERTIFICATED_SECURITIES_TAX_ACT_1998("The Uncertificated Securities Tax Act 1998");

    private final String name;

    RulingArea(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
