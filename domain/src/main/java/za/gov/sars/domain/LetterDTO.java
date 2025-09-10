/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.domain;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author S2028398
 */
@Getter
@Setter
public class LetterDTO implements Serializable {

    private String district;
    private String caseNumber;
    private String capturedDate;
    private String agent;
    private String importer;
    private String billOfEntry;
    private String billOfEntryDate;
    private String capturer;
    private String status;
    private String statusDate;
    private String updatedDate;
    private String ammendmentDate;
    private String tariffSpecialist;
    private String lawAndAnalysis;
    private String tariffCode;
    private String determination;
    private String description;

}
