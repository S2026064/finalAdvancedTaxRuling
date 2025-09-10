/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author S2026095
 */
@Entity
@Table(name = "documentum_host")
@Getter
@Setter
public class DocumentumUrl extends BaseEntity {

    @Column(name = "upload_url")
    private String uploadUrl;
    
    @Column(name = "download_url")
    private String downloadUrl;

}
