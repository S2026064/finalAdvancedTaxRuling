/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package za.gov.sars.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import za.gov.sars.common.NotificationType;
import za.gov.sars.domain.EmailNotification;

/**
 *
 * @author S2026064
 */
public interface EmailNotificationRepository extends JpaRepository<EmailNotification, Long> {
    EmailNotification findByNotificationType(NotificationType notificationType);
}
