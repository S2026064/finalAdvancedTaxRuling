/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.gov.sars.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.gov.sars.dto.ProductivitySummaryReportDTO;
import za.gov.sars.persistence.TimeSheetActivityRepository;

/**
 *
 * @author S2030702
 */
@Service
public class ReportService {

    @Autowired
    private TimeSheetActivityRepository repository;

    public List<ProductivitySummaryReportDTO> getProductivitySummaryReport(Date startDate, Date endDate) {
        List<ProductivitySummaryReportDTO> userSummaries = repository.fetchUserProductivityReport(startDate, endDate);

        double totalProd = userSummaries.stream().mapToDouble(ProductivitySummaryReportDTO::getProductiveHours).sum();
        double totalNonProd = userSummaries.stream().mapToDouble(ProductivitySummaryReportDTO::getNonProductiveHours).sum();
        double grandTotal = userSummaries.stream().mapToDouble(ProductivitySummaryReportDTO::getTotalHours).sum();

        return userSummaries.stream()
                .map(dto -> new ProductivitySummaryReportDTO(
                        dto.getFullName(),
                        dto.getProductiveHours(),
                        dto.getNonProductiveHours(),
                        dto.getTotalHours(),
                        totalProd,
                        totalNonProd,
                        grandTotal
                ))
                .collect(Collectors.toList());
    }
}

