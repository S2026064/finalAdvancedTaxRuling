/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package za.gov.sars.service;

import java.util.Date;
import java.util.List;
import za.gov.sars.common.NotificationType;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.User;

/**
 *
 * @author S2026064
 */
public interface EmailNotificationSenderServiceLocal {

    public boolean sendEmailNotification(NotificationType notificationType, String caseNumber, String applicantName, Date date, User recipient);

    public boolean sendEmailNotification(NotificationType notificationType, String caseNumber, String applicantName, Date date, List<User> recipients);

    public boolean sendEmailNotificationTimesheet(NotificationType notificationType, String requestor, Date startDate, Date endDate, User recipient);
    
    public boolean sendEmailNotification(NotificationType notificationType, ATRApplication application);
}
