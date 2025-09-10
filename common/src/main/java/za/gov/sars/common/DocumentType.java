/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package za.gov.sars.common;

/**
 *
 * @author S2026064
 */
public enum DocumentType {
    DRAFT_RULING("Draft Ruling"),
    SANITISED_RULING("Sanitised ruling"),
    FINAL_RULING("Final Ruling"),
    LOE("Letter of engagement"),
    APPLICATION_FEE("Application fee"),
    ADVAVANCED_FEE("Advanced fee"),
    OUTSTANDING_FEE("Outstanding fee"),
    AD_HOC("Ad_hoc"),
    FEEDBACK("Feedback"),
    INVOICE("Invoice"),
    OTHER("Supporting Document");

    private final String name;

    DocumentType(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
