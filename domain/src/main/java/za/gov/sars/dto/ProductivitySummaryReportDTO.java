/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.dto;

/**
 *
 * @author S2030702
 */

public class ProductivitySummaryReportDTO {

    private String fullName;
    private double productiveHours;
    private double nonProductiveHours;
    private double totalHours;

    // Totals across everyone
    private double totalProductiveHoursAll;
    private double totalNonProductiveHoursAll;
    private double grandTotalHoursAll;

    public ProductivitySummaryReportDTO(String fullName,
                                        double productiveHours,
                                        double nonProductiveHours,
                                        double totalHours,
                                        double totalProductiveHoursAll,
                                        double totalNonProductiveHoursAll,
                                        double grandTotalHoursAll) {
        this.fullName = fullName;
        this.productiveHours = productiveHours;
        this.nonProductiveHours = nonProductiveHours;
        this.totalHours = totalHours;
        this.totalProductiveHoursAll = totalProductiveHoursAll;
        this.totalNonProductiveHoursAll = totalNonProductiveHoursAll;
        this.grandTotalHoursAll = grandTotalHoursAll;
    }

    // Getters only, to keep it immutable (optional)
    public String getFullName() { return fullName; }
    public double getProductiveHours() { return productiveHours; }
    public double getNonProductiveHours() { return nonProductiveHours; }
    public double getTotalHours() { return totalHours; }
    public double getTotalProductiveHoursAll() { return totalProductiveHoursAll; }
    public double getTotalNonProductiveHoursAll() { return totalNonProductiveHoursAll; }
    public double getGrandTotalHoursAll() { return grandTotalHoursAll; }
}



