/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package za.gov.sars.common;

/**
 *
 * @author S2026064
 */
public enum EstimatePlanStatus {
  
    ESTIMATE_SUBMITTED("Estimate Submitted"),
    ESTIMATE_REVISED("Estimate Revised"),
    ESTIMATE_ACCEPTED("Estimate Accepted"),
    ESTIMATE_PUBLISHED("Estimate Published"),
    ESTIMATE_NOT_ACCEPTED("Estimate Not Accepted"),
    ESTIMATE_REJECTED("Estimate Rejected");
    

    private final String name;

    EstimatePlanStatus(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
