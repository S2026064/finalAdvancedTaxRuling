/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package za.gov.sars.common;

/**
 *
 * @author S2026064
 */
public enum ClassDescription {
    BENEFICIARIES("Beneficiaries"),
    EMPLOYEES("Employees"),
    SHAREHOLDERS("Shareholders"),
    OTHER("Other");

    private final String name;

    ClassDescription(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
