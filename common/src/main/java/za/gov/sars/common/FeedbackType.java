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
public enum FeedbackType {
    REQUEST_WITHDRAWAL("Request withdrawal"),
    REQUEST_ADDITIONAL_DOCS("Request additional documentation"),
    REQUEST_APPROVED("Request approved"),
    REQUEST_DECLINED("Request declined"),
    RULING_REJECTED("Ruling rejected"),
    REWORK("Rework"),
    FEEDBACK("Feedback");
    
    private final String name;

    FeedbackType(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
