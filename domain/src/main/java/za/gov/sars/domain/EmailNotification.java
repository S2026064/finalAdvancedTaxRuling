/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import za.gov.sars.common.NotificationType;

/**
 *
 * @author S2026064
 */
@Entity
@Table(name = "email_notification")
@Getter
@Setter
public class EmailNotification extends BaseEntity {

    @Column(name = "line_1", length = 2000)
    private String line1;

    @Column(name = "line_2", length = 4000)
    private String line2;

    @Column(name = "line_3", length = 6000)
    private String line3;

    @Column(name = "line_4", length = 6000)
    private String line4;

    @Column(name = "body", length = 6000)
    private String body;

    @Column(name = "subject", length = 100)
    private String subject;

    @Column(name = "notification_type")
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    // One primary recipient (must exist beforehand)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "primary_recipient_id", nullable = false)
    private UserRole primaryRecipient;

    // Many additional recipients (must exist beforehand)
    @ManyToMany
    @JoinTable(
            name = "email_notification_additional_recipients",
            joinColumns = @JoinColumn(name = "email_notification_id"),
            inverseJoinColumns = @JoinColumn(name = "user_role_id")
    )
    private List<UserRole> additionalRecipients = new ArrayList<>();
}
