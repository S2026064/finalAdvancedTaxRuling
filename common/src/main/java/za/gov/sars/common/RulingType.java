/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package za.gov.sars.common;

/**
 *
 * @author S2026064
 */
public enum RulingType {
    PRIVATE_RULING("Binding Private Ruling"),
    CLASS_RULING("Binding Class Ruling"),
    RECONFIRMATION("Reconfirmation"),
    EXTENSION("Extension"),
    STATUS_RULING("Status Ruling");

    private final String name;

    RulingType(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
