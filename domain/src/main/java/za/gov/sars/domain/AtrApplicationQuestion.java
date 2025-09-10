/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package za.gov.sars.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import za.gov.sars.common.ResponseOption;

/**
 *
 * @author S2026064
 */
@Entity
@Getter
@Setter
@Table(name = "atr_app_question")
public class AtrApplicationQuestion extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = true, targetEntity = ATRApplication.class)
    @JoinColumn(name = "atr_app_id")
    private ATRApplication atrApplication;

    @Column(name = "response_option", nullable = true)
    @Enumerated(EnumType.STRING)
    private ResponseOption responseOption;

}
