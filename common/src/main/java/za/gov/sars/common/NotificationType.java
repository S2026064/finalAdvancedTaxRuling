/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package za.gov.sars.common;

/**
 *
 * @author S2026064
 */
public enum NotificationType {
    APPLICATION_FEE_REMINDER("Application fee reminder"),
    APPLICATION_ALLOCATED_MANAGER("Application allocated"),
    APPLICATION_ALLOCATED_RESOURCE("Application allocated to primary resource"),
    APPLICATION_ASSIGNED_ADDITIONAL_RESOURCES("Application assigned additional resources"),
    APPLICATION_ACCEPTED("Application accepted"),
    APPLICATION_WITHDRAWN("Application withdrawn"),
    APPLICATION_PAYMENT_RECEIVED_ACTION_REQUIRED("Application payment received - action required"),
    APPLICATION_DOCUMENTS_UPLOADED("Documents uploaded"),
    APPLICATION_ONHOLD("Application onhold"),
    APPLICATION_REINSTATED("Application reinstated"),
    APPLICATION_ESTIMATE_IN_PROGRESS("Application in progress"),
    APPLICATION_EXPIRED("Application expired"),
    APPLICATION_DRAFT_RULING_WITHDRAWN("Application withdrawn"),
    APPLICATION_WITHDRAWN_BY_ATR_UNIT("Application withdrawn by ATR unit"),
    APPLICATION_SANITISED_RULING_WITHDRAWN("Application withdrawn"),
    APPLICATION_PAYMENT_REMINDER("Application payment reminder"),
    APPLICATION_REJECTION_REQUIRES_APPROVAL("Application rejection requires approval"),
    APPLICATION_WITHDRAWAL_REQUIRES_APPROVAL("Application withdrawal requires approval"),
    APPLICATION_SUBMITTED_IN_PROGRESS("Application in progress"),
    APPROVE_CHARGE_SHEET("Approve charge sheet"),
    AUTHORISE_RULING("Authorised ruling"),
    DRAFT_RULING_ACCEPTED("Draft ruling accepted"),
    DRAFT_RULING_FEEDBACK_RECEIVED("Draft ruling feedback received"),
    DRAFT_RULING_REQUIRE_APPROVAL("Draft ruling requires approval"),
    DRAFT_RULING_PUBLISHED("Draft ruling published"),
    DRAFT_RULING_REMINDER("Draft ruling reminder"),
    DRAFT_RULING_ACCEPTED_BY_ATR_UNIT_REQUIRES_APPROVAL("Draft ruling accepted on behalf of applicant requires approval"),
    ESTIMATION_INPROGRESS("Estimation in progress"),
    ESTIMATION_REQUIRE_APPROVAL("Estimation requires approval"),
    ESTIMATION_PAYMENT_REMINDER("Estimation payment reminder"),
    ESTIMATION_EXPIRED("Estimation expired"),
    ESTIMATION_EXPIRED_ACTION_REQUIRED("Estimation expired - action required"),
    ESTIMATION_REJECTED("Estimation rejected"),
    ESTIMATION_PUBLISHED("Estimation published"),
    ESTIMATION_REMINDER("Estimation reminder"),
    ESTIMATION_ACCEPTED_PENDING_DOCUMENTATION("Estimation accepted and paid - action required"),
    INVOICE_ISSUED("Invoice issued"),
    MANAGER_ESTIMATION_REJECTED("Manager rejected estimation plan"),
    NEW_APPLICATION_REJECTED("New application rejected"),
    NEW_APPLICATION_RECEIVED("New application received"),
    NEW_APPLICATION_FEE_PAID("New application fee paid"),
    REVISED_ESTIMATION_EXPIRED("Revised estimation expired"),
    REVISED_ESTIMATION_REJECTED("Revised estimation rejected"),
    REVISED_ESTIMATION_PUBLISHED("Revised estimation published"),
    REVISED_ESTIMATION_REMINDER("Revised estimation reminder"),
    RULING_UPLOADED_PENDING_OUTSTANDING_AMOUNT("Ruling uploaded pending outstanding amount"),
    RULING_AMEND_REQUIRES_APPROVAL("Sanitised ruling requires approval"),
    SANITISED_RULING_ACCEPTED("Sanitised ruling accepted"),
    SANITISED_RULING_FEEDBACK("Sanitised ruling feedback"),
    SANITISED_RULING_PUBLISHED("Sanitised ruling published"),
    RULING_PUBLISHED_PUBLICLY("Final ruling published publicly"),
    SANITISED_RULING_REQUIRES_APPROVAL("Sanitised ruling requires approval"),
    SYSTEM_QUERY_FROM_APPLICANT("System query from applicant"),
    SYSTEM_QUERY_FROM_ATR_UNIT("System query from ATR unit"),
    SANITISED_RULING_ACCEPTED_BY_ATR_UNIT_REQUIRES_APPROVAL("Sanitised ruling accepted on behalf of applicant requires approval"),
    TIME_SHEET_SUBMISSION("Time sheet submission"),
    ZERO_ACCOUNT_REQUIRES_AUTHORISATION("Zero account requires authorisation");

    private final String name;

    NotificationType(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
