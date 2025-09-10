/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package za.gov.sars.common;

/**
 *
 * @author S2026064
 */
public enum ResourceType {
    PRIMARY_RESOURCE("Primary Resource"),
    SECONDARY_RESOURCE("Secondary Resource"),
    MANAGER("Manager"),
    APPLICANT("Applicant");

    private final String name;

    ResourceType(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
