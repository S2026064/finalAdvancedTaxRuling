/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package za.gov.sars.common;

/**
 *
 * @author S2026064
 */
public enum Status {
    NEW("New"),
    DRAFT_RULING_ACCEPTED("Draft Ruling Accepted"),
    SAVED("Saved"),
    APPLICATION_REJECTED("Application Rejected"),
    DRAFT_RULING_REQUIRES_AUTHORISATION("Draft Ruling Requires Authorisation"),
    DRAFT_RULING_FEEDBACK_RECIEVED("Draft Ruling Feedback Recieved"),
    DRAFT_RULING_PUBLISHED("Draft Ruling Published"),
    DRAFT_RULING_REJECTED("Draft Ruling Rejected"),
    REQUEST_WITHDRAWAL_DRAFT_RULING("Request Withdrawal"),
    ESTIMATE_ACCEPTED_PAYMENT_UNCONFIRMED("Estimate Accepted Payment Unconfirmed"),
    ESTIMATE_ACCEPTED_PAYMENT_CONFIRMED("Estimate Accepted Payment Confirmed"),
    ESTIMATE_ACCEPTED_PENDING_DOCUMENTATION("Estimate Accepted Pending Documentation"),
    ESTIMATE_EXPIRED("Estimate Expired"),
    ESTIMATE_INPROGRESS("Estimate In-Progress"),
    ESTIMATE_PUBLISHED("Estimate Published"),
    ESTIMATE_REJECTED("Estimate Rejected"),
    ESTIMATION_REQUIRE_APPROVAL("Estimate Requires Approval"),
    MANAGER_ESTIMATION_REJECTED("Manager Rejected Estimation"),
    ESTIMATE_REQUIRES_AUTHORISATION("Estimate Requires Authorisation"),
    REQUEST_WITHDRAWAL_ESTIMATE("Request Withdrawal"),
    SANITISED_RULING_ACCEPTED("Sanitised Ruling Accepted"),
    CASE_INPROGRESS("Case In-Progress"),
    RULING_REQUIRES_AUTHORISATION("Ruling Requires Authorisation"),
    REVISED_ESTIMATE_PUBLISHED("Revised Estimate Published"),
    RULING_PUBLISHED_PENDING_OUTSTANDING_AMOUNT("Ruling Published Pending Outstanding amount"),
    SANITISED_RULING_REQUIRES_AUTHORISATION("Sanitised Ruling Requires Authorisation"),
    SANITISED_RULING_FEEDBACK_RECIEVED("Sanitised Ruling Feedback Recieved"),
    SANITISED_RULING_PUBLISHED("Sanitised Ruling Published"),
    SANITISED_RULING_REJECTED("Sanitised Ruling Rejected"),
    REQUEST_WITHDRAWAL_SANITISED_RULING("Request Withdrawal"),
    SUBMITTED_PAYMENT_UNCONFIRMED("Submitted - payment unconfirmed "),
    SUBMITTED_AND_PAID("Submitted And Paid"),
    CASE_WITHDRAWN_PENDING_DOCUMENTATION("Case Withdrawn Pending Documentation"),
    CASE_WITHDRAWN("Case Withdrawn"),
    CHARGESHEET_SUBMITTED("Chargesheet submitted"),
    CHARGESHEET_SEND_FOR_REWORK("Chargesheet send for rework"),
    CHARGESHEET_APPROVED("Chargesheet approved"),
    PENDING_AUTHORISATION("Pending Authorisation"),
    RULING_REJECTED("Final Ruling rejected"),
    CHARGESHEET_IN_PROGRESS("Chargesheet in progress"),
    ///////////added for progress///////////
    READY_FOR_ALLOCATION("Ready For Alloctaion"),
    ALLOCATED_TO_MANAGER("Allocated To Manager"),
    ASSIGN_ACTIVITIES("Assign Activities"),
    APPLICATION_FINALISED("Application finilised"),
    CASE_FINALISED("Case finalised");

    

    private final String name;

    Status(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
