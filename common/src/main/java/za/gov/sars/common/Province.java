/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package za.gov.sars.common;

/**
 *
 * @author S2026064
 */
public enum Province {
    LIMPOPO("Limpopo"),
    MPUMALANGA("Mpumalanga"),
    GAUTENG("Gauteng"),
    FREESTATE("Free State"),
    EASTERN_CAPE("Eastern Cape"),
    WESTERN_CAPE("Western Cape"),
    NORTHERN_CAPE("Northern Cape"),
    NORTHWEST("North West"),
    KWAZULU_NATAL("KwaZulu - Natal");

    private final String name;

    Province(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
