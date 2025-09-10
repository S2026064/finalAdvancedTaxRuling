/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package za.gov.sars.common;

/**
 *
 * @author S2026064
 */
public enum ReportType {

    OPEN_APPLICATION("All open applications"),// rulingType
    APPLICATION_ASSIGNED_BY_RESOURCE("Applications by assigned resource"),
    REJECTED_WITHDRAWN_APPLICATION("Application rejected/withdrawn"),//dateRange, rulingType
    CHARGE_PER_RESOURCE("Charge per resource report"),// resource
    CHARGE_PER_INVOICE("Charge per invoiced application"),// application
    CLASS_RULING("Class Ruling"),
    DUE_DATE_VARIANCE("Due date variance"),// dateRange, manager, variance, application
    ESTIMATION_VARIANCE("Estimation variance"),// dateRange, application, rulingType
    ESTIMATION_DEVIATION("Estimation deviation"),// dateRange, rulingType
    ESTIMATE_ACTUAL_COMPARISON("Estimate/actual comparison"),// dateRange, application
    ESTIMATE_ACTUAL_COMPARISON_BY_RESOURCE_TYPE("Estimate/actual comparison by resource type"),// dateRange, application, resourceType
    FINANCIAL_OVERVIEW("Financial overview"),// dateRange, rulingType
    HIT_COUNT("Hit count"), // dateRange
    KNOWLEDGE_MANAGEMENT("Knowledge management"),// dateRange, activityCode?
    PRODUCTIVITY_DETAIL("Productivity detail"),// dateRange, resource, planActivity
    PRODUCTIVITY_SUMMARY("Productivity summary"),// dateRange
    PROGRESS_TRACKING_DAYS("Progress tracking days report"),// dateRange, rulingType
    REPRESENTATIVE_DETAILS("Representative details report"),// dateRange
    RESOURCE_APPLICATIONS("Resource applications"),// rulingType
    RULING_VALIDITY("Ruling validity"),// dateRange
    STUFF_HOURS_ACTIVITY("Staff hours per activity Report"),// dateRange, resource, planActivity, rulingType
    STAFF_HOURS_PER_ATR("Staff hours per application"),// dateRange, application, rulingType
    STATUS_CHANGE_DATES("Status change dates Report"),// dateRange
    STATUS_DAY_COUNT("Status day count"),// application
    TIME_SHEET("Timesheet summary report");// dateRange

    private final String name;

    ReportType(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
