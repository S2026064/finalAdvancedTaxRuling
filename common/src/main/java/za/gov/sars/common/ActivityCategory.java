/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package za.gov.sars.common;

/**
 *
 * @author S2026064
 */
public enum ActivityCategory {
    SCREENING_AND_ALLOCATION("Screening and Allocation"),
    INITIAL_RESEARCH("Initial Research"),
    DRAFT_INTERNAL_DOCUMENTS_AND_REVIEW("Draft Internal Documents and Review"),
    CONSULTING("Consulting"),
    RULING_DRAFTING("Ruling Drafting"),
    COMMITTEE_REVIEW("Committee Review"),
    RULING_SANITISATION("Ruling Sanitasation"),
    RESEARCH_AND_MEETINGS("Research and Meetings"),
    ADMIN_AND_PROJECT_MANAGEMENT("Admin and Project Management"),
    RESOURCE_SCHEDULING_AND_PLANNING("Resource Scheduling and Planning"),
    CLIENT_COMMUNICATION_PRIO_TO_ACCEPTANCE_OF_ESTIMATE("Client Communication prio to Acceptance of Estimate"),
    FEE_ESTIMATE_PREPARATION("Fee Estimate Preparation");

    private final String name;

    ActivityCategory(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
