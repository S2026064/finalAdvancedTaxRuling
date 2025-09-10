/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author S2026080
 */
@Entity
@Table(name = "download_trail")
@Getter
@Setter
public class DownloadRecord extends BaseEntity {

    @Column(name = "download_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date downloadDate;

    @Column(name = "downloader")
    private String downloader;

    @Column(name = "document_name")
    private String description;

    @Column(name = "case_number")
    private Long caseNumber;

}
