/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package za.gov.sars.common;

/**
 *
 * @author S2026064
 */
public enum ApplicationType {
    APPLICANT("the applicant(natural Person)"),
    REPRESENTATIVE("a representative taxpayer of the applicant"),
    THIRD_PARTY("on behalf of a third party/class");

    private final String name;

    ApplicationType(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
