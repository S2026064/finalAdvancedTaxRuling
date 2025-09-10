/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import za.gov.sars.common.QuestionType;

/**
 *
 * @author S2026064
 */
@Entity
@Getter
@Setter
@Table(name = "question")
public class Question extends BaseEntity {

    @Column(name = "description")
    private String description;
    
    @Column(name = "question_info", length = 1000)
    private String questionDetail;

    @Column(name = "question_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

}
