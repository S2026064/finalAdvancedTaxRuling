/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package za.gov.sars.common;

/**
 *
 * @author S2026064
 */
public enum QuestionType {
    PRE_SCREENING("Pre Screening"),
    COMPANY_QUESTIONS("Company questions"),
    TRUST_QUESTIONS("Trust questions"),
    BPO("BPO"),
    NGO("NGO"),
    INDIVIDUAL("Individual");
   

    private final String name;

    QuestionType(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
