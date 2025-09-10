/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.mail.Message;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.gov.sars.common.DateUtil;
import za.gov.sars.common.NotificationType;
import za.gov.sars.common.ResourceType;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.EmailNotification;
import za.gov.sars.domain.User;
import za.gov.sars.service.mail.LDAPService;
import za.gov.sars.service.mail.MailService;

/**
 *
 * @author S2026064
 */
@Service
public class EmailNotificationSenderService implements EmailNotificationSenderServiceLocal {

    @Autowired
    private EmailNotificationServiceLocal emailNotificationService;

    @Autowired
    private UserAtrApplicationServiceLocal userAtrApplicationService;

    @Autowired
    private UserServiceLocal userService;

    private MailService mailService = new MailService();
    private LDAPService lDAPService = new LDAPService();

    private EmailNotification emailNotification;

    private List<String> emailNotificationRecipients = new ArrayList<>();

    @Override
    public boolean sendEmailNotification(NotificationType notificationType, String caseNumber, String applicantName, Date date, User recipient) {
        emailNotification = emailNotificationService.findByNotificationType(notificationType);
        if (emailNotification != null) {
            boolean isMailSent = false;
            emailNotificationRecipients.clear();
            emailNotificationRecipients.add(lDAPService.getUserEmailAddress("s2030707"));
            //  emailNotificationRecipients.add(recipient.getEmailAddress());
            StringBuilder builder = new StringBuilder();
            if (!emailNotification.getNotificationType().equals(NotificationType.APPLICATION_DOCUMENTS_UPLOADED)) {
                if (recipient.getUserRole().getDescription().equalsIgnoreCase("Applicant")) {
                    builder.append("Dear Applicatant");
                    builder.append("<br /><br />");
                }
            }
            if (emailNotification.getNotificationType().equals(NotificationType.APPLICATION_DOCUMENTS_UPLOADED)) {
                builder.append("Dear ");
                builder.append(" ");
                builder.append(recipient.getFullName());
                builder.append("<br /><br />");
            }
            builder.append(emailNotification.getLine1());
            builder.append(" ");
            builder.append(applicantName);
            builder.append(": ");
            builder.append(caseNumber);
            if (emailNotification.getNotificationType().equals(NotificationType.APPLICATION_DOCUMENTS_UPLOADED)) {
                builder.append(" ");
                builder.append(emailNotification.getLine2());
                builder.append(" ");
                builder.append(DateUtil.convertStringToDate(date));
            }
            if (!emailNotification.getNotificationType().equals(NotificationType.APPLICATION_DOCUMENTS_UPLOADED)) {
                builder.append(emailNotification.getLine2());
                builder.append(" ");
                if (!notificationType.equals(NotificationType.RULING_PUBLISHED_PUBLICLY) || !notificationType.equals(NotificationType.ESTIMATION_REJECTED) || !notificationType.equals(NotificationType.ESTIMATION_REQUIRE_APPROVAL) || !notificationType.equals(NotificationType.APPLICATION_FEE_REMINDER) || !notificationType.equals(NotificationType.ESTIMATION_INPROGRESS)) {
                    builder.append(DateUtil.convertStringToDate(date));
                }
                builder.append(" ");
                builder.append(emailNotification.getLine3());
                builder.append(" ");
                builder.append(emailNotification.getLine4());
            }
            builder.append("<br/><br/>");
            builder.append("Kindly note, this is a system-generated email.");
            builder.append("<br/>");
            builder.append("Please do not reply to this message.");
            builder.append("<br/>");
            builder.append("Should you require any assistance please send your query to ATRinfo@sars.gov.za.");

            if (mailService.send(emailNotificationRecipients, "ADVANCED TAX RULING", builder.toString())) {
                return true;
            }

        }
        return false;
    }

    @Override
    public boolean sendEmailNotification(NotificationType notificationType, String caseNumber, String applicantName, Date date, List<User> recipients) {
        emailNotification = emailNotificationService.findByNotificationType(notificationType);
        if (emailNotification != null) {
            boolean isMailSent = false;
            for (User recipient : recipients) {
                emailNotificationRecipients.clear();
//                emailNotificationRecipients.add(lDAPService.getUserEmailAddress("S2026064"));
                emailNotificationRecipients.add(lDAPService.getUserEmailAddress("S2030702"));
                //  emailNotificationRecipients.add(recipient.getEmailAddress());
                String emailBody = generateEmailBody(emailNotification, applicantName, caseNumber, date);
                System.out.println("=====" + emailBody);
                if (mailService.send(emailNotificationRecipients, emailNotification.getSubject(), emailBody)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean sendEmailNotification(NotificationType notificationType, ATRApplication application) {
        emailNotification = emailNotificationService.findByNotificationType(notificationType);
        if (emailNotification != null) {
            boolean isMailSent = false;
//            emailNotificationRecipients.clear();
//            emailNotificationRecipients.add(lDAPService.getUserEmailAddress("S2026064"));
//            emailNotificationRecipients.add(lDAPService.getUserEmailAddress("S2030702"));
            //  emailNotificationRecipients.add(recipient.getEmailAddress());
            // measure sanitising time
            long startrecipientsAddressesMap = System.nanoTime();
            Map<Message.RecipientType, List<String>> recipientsAddressesMap = generateRecipientsEmailsMap(emailNotification, application);
            System.out.println("RecipientsMap==== " + recipientsAddressesMap);
            long endrecipientsAddressesMap = System.nanoTime();
             long generateRecipientsEmailsMapDuration = endrecipientsAddressesMap - startrecipientsAddressesMap;

            long startgenerateEmailBody = System.nanoTime();
            Date date = application.getUpdatedDate() != null ? application.getUpdatedDate() : application.getCreatedDate();
            String emailBody = generateEmailBody(emailNotification, application.getApplicantName(), Long.toString(application.getCaseNum()), date);
            long endgenerateEmailBody = System.nanoTime();
            long generateEmailBodyDuration = endgenerateEmailBody - startgenerateEmailBody;

            long startSend = System.nanoTime();
            if (mailService.send(recipientsAddressesMap, emailNotification.getSubject(), emailBody)) {
//                return true;
            }
            long endSend = System.nanoTime();
            long sendDuration = endSend - startSend;

            // print results
            System.out.println("generateRecipientsEmailsMap took: " + formatNanos(generateRecipientsEmailsMapDuration));
            System.out.println("generateEmailBody took: " + formatNanos(generateEmailBodyDuration));
            System.out.println("Sending took:    " + formatNanos(sendDuration));
        }
        return false;
    }

    @Override
    public boolean sendEmailNotificationTimesheet(NotificationType notificationType, String requestor, Date startDate, Date endDate, User recipient) {
        emailNotification = emailNotificationService.findByNotificationType(notificationType);
        if (emailNotification != null) {
            boolean isMailSent = false;
            emailNotificationRecipients.clear();
            emailNotificationRecipients.add(lDAPService.getUserEmailAddress("s2030707"));
            StringBuilder builder = new StringBuilder();
            builder.append("Dear ");
            builder.append(" ");
            builder.append(recipient.getFullName());
            builder.append("<br /><br />");
            builder.append(requestor);
            builder.append(" ");
            builder.append(emailNotification.getLine1());
            builder.append(" ");
            builder.append(DateUtil.convertStringToDate(startDate));
            builder.append(" ");
            builder.append(emailNotification.getLine2());
            builder.append(" ");
            builder.append(DateUtil.convertStringToDate(endDate));
            builder.append("<br />");
            builder.append(emailNotification.getLine3());
            builder.append("<br/><br/>");
            builder.append("Kindly note, this is a system-generated email.");
            builder.append("<br/>");
            builder.append("Please do not reply to this message.");
            builder.append("<br/>");
            builder.append("Should you require any assistance please send your query to ATRinfo@sars.gov.za.");

            if (mailService.send(emailNotificationRecipients, "Time Sheet Review", builder.toString())) {
                return true;
            }

        }
        return false;
    }

    private String generateEmailBody(EmailNotification emailNotification, String applicantName, String atrCaseNumber, Date createdDate) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("ApplicantName", applicantName);
        placeholders.put("ARRefNum", atrCaseNumber);
        placeholders.put("CreatedDate", createdDate != null ? DateUtil.convertStringToDate(createdDate) : "");

        String bodyFromEditor = emailNotification.getBody();
        String decodedBody = StringEscapeUtils.unescapeHtml4(bodyFromEditor);

        StringBuilder result = new StringBuilder(decodedBody);

        // Replace placeholders that appear only once
        placeholders.entrySet().forEach(entry -> {
            String placeholder = "@" + entry.getKey();
            String replacement = "<b>" + entry.getValue() + "</b>";
            int index = result.indexOf(placeholder);
            if (index != -1) {
                result.replace(index, index + placeholder.length(), replacement);
            }
        });

        // Replace line breaks with <br />
        int newlineIndex = result.indexOf("\n");
        while (newlineIndex != -1) {
            result.replace(newlineIndex, newlineIndex + 1, "<br />");
            newlineIndex = result.indexOf("\n", newlineIndex + 6); // 6 = length of "<br />"
        }

        return result.toString();
    }

    private Map<Message.RecipientType, List<String>> generateRecipientsEmailsMap(EmailNotification emailNotification, ATRApplication application) {
        List<ResourceType> resourceTypes = EnumSet.allOf(ResourceType.class).stream().collect(Collectors.toList());
        List<User> recipients = userService.findByUserRole(Arrays.asList("Administrator", "Senior Manager"));

        recipients.addAll(userAtrApplicationService.findByAtrApplicationAndResourceTypeIn(application, resourceTypes)
                .stream()
                .map(uap -> uap.getUser())
                .collect(Collectors.toList()));

        // Hardcoded emails for testing
        Iterator<User> iterator = recipients.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            switch (user.getUserRole().getDescription()) { 
                case "Applicant":
                    user.setEmailAddress("NRabalao@sars.gov.za");
                    break;
                case "Resource":
                    user.setEmailAddress("TMakau@sars.gov.za");
                    break;
                case "Manager":
                    user.setEmailAddress("MMorudi@sars.gov.za");
                    break;
                case "Administrator":
                    user.setEmailAddress("APhangisa@sars.gov.za");
                    break;
                case "Senior Manager":
                    user.setEmailAddress("TRamurebiwa@sars.gov.za");
                    break;
                default:
                    user.setEmailAddress("NRabalao@sars.gov.za");
                    break;
            }
        } // End---------------

        Map<Message.RecipientType, List<String>> recipientsAddressMap = new HashMap<>();

        // Map primary recipient email address
        recipientsAddressMap.put(
                Message.RecipientType.TO,
                Collections.singletonList(recipients
                        .stream()
                        .filter(user -> user.getUserRole().getDescription().equalsIgnoreCase(emailNotification.getPrimaryRecipient().getDescription()))
                        .findFirst().get().getEmailAddress()));

        // Map additional recipients' email address
        recipientsAddressMap.put(
                emailNotification.getPrimaryRecipient().getDescription().equalsIgnoreCase("Applicant") ? Message.RecipientType.BCC : Message.RecipientType.CC,
                recipients
                        .stream()
                        .filter(user -> emailNotification.getAdditionalRecipients().contains(user.getUserRole()))
                        .map(User::getEmailAddress)
                        .collect(Collectors.toList()));

        return recipientsAddressMap;
    }

    // helper to format nanoseconds nicely
    private static String formatNanos(long nanos) {
        return String.format("%d ms", TimeUnit.NANOSECONDS.toMillis(nanos));
    }

}
