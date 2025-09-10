/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package za.gov.sars.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import za.gov.sars.common.NotificationType;
import za.gov.sars.domain.EmailNotification;

/**
 *
 * @author S2026064
 */
public interface EmailNotificationServiceLocal {

    EmailNotification save(EmailNotification emailNotification);

    EmailNotification findById(Long id);

    EmailNotification update(EmailNotification emailNotification);

    EmailNotification deleteById(Long id);

    List<EmailNotification> listAll();

    Page<EmailNotification> findAll(Pageable pageable);

    boolean isExist(EmailNotification emailNotification);

    EmailNotification findByNotificationType(NotificationType notificationType);
}
