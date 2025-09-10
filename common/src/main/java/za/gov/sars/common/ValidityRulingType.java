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
public enum ValidityRulingType {
    POSITIVE("Positive"),
    NEGATIVE("Negative"),
    PARTLY("Partly postive/negation");


    private final String name;

    ValidityRulingType(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
    
}
