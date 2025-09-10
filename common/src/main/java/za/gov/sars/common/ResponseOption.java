/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package za.gov.sars.common;

/**
 *
 * @author S2026064
 */
public enum ResponseOption {
    YES("Yes"),
    NO("No");

    private final String name;

    ResponseOption(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
