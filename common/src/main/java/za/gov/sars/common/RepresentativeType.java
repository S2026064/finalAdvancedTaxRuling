/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package za.gov.sars.common;

/**
 *
 * @author S2026064
 */
public enum RepresentativeType {

    COMPANY("Company"),
    TRUST("Trust"),
    CLASS("Class"),
    FOREIGN_ENTITY("Foreign Entity"),
    PUBLIC_BENEFIT_ORGANISATION("Public Benefit Organisation"),
    INDIVIDUAL("Individual");

    private final String name;

    RepresentativeType(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
