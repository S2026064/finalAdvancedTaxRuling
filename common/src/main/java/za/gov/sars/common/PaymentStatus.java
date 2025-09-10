/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package za.gov.sars.common;

/**
 *
 * @author S2026064
 */
public enum PaymentStatus {
    PRIVATE_RULING("Private Ruling"),
    EXTENSION("Extension"),
    STATUS_RULING("Status Ruling"),
    RECONFIRMATION("Reconfirmation"),
    CLASS_RULING("Class Ruling");

    private final String name;

    PaymentStatus(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
