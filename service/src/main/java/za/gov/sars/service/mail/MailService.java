/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.gov.sars.service.mail;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author S2026064
 */
public class MailService {

    private final int MAIL_SERVER_PORT = 25;
    private final String SMTP_SERVER = "smtp.sars.gov.za";
    private final String SOURCE_ADDRESS = "noreplyatrdev@sars.gov.za";

    public MailService() {

    }

    public boolean send(List<String> destinationAddress, String subject, String message) {

        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_SERVER);
        props.put("mail.debug", "true");
        Session session = Session.getInstance(props);

        try {
            Message msg = new MimeMessage(session);
            InternetAddress addressFrom = new InternetAddress(SOURCE_ADDRESS);
            msg.setFrom(addressFrom);
            setReceipients(destinationAddress, msg);
            msg.setSubject(subject);
            msg.setContent(message, "text/html; charset=utf-8");
            msg.setSentDate(new Date());
            Transport.send(msg);
            return true;
        } catch (MessagingException e) {
            Logger.getLogger(MailService.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    // send to TO+CC/BCC 
    public boolean send(Map<Message.RecipientType, List<String>> recipientsAddressesMap, String subject, String message) { 

        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_SERVER);
        props.put("mail.debug", "true");
        Session session = Session.getInstance(props);

        try {
            Message msg = new MimeMessage(session);
            InternetAddress addressFrom = new InternetAddress(SOURCE_ADDRESS);
            msg.setFrom(addressFrom);
            setReceipients(recipientsAddressesMap, msg);
            msg.setSubject(subject);
            msg.setContent(message, "text/html; charset=utf-8");
            msg.setSentDate(new Date());
            Transport.send(msg);
            return true;
        } catch (MessagingException e) {
            Logger.getLogger(MailService.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    public boolean send(List<String> destinationAddress, String subject, String message, String attachmentPath) {

        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_SERVER);
        props.put("mail.debug", "true");
        Session session = Session.getInstance(props);

        try {

            Message msg = new MimeMessage(session);
            InternetAddress addressFrom = new InternetAddress(SOURCE_ADDRESS);
            msg.setFrom(addressFrom);
            setReceipients(destinationAddress, msg);
            msg.setSubject(subject);

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();
            // Now set the actual message
            messageBodyPart.setText(message);
            // Create a multipar message
            Multipart multipart = new MimeMultipart();
            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Set the attachment file
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(attachmentPath);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(attachmentPath);
            multipart.addBodyPart(messageBodyPart);

            msg.setContent(multipart);
            msg.setSentDate(new Date());
            Transport.send(msg);
            return true;
        } catch (MessagingException e) {
            Logger.getLogger(MailService.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    private void setReceipients(List<String> to, Message message)
            throws AddressException, MessagingException {
        InternetAddress[] addressTo = new InternetAddress[to.size()];
        int index = 0;
        for (String toAddress : to) {
            if (toAddress != null && toAddress.length() > 0) {
                addressTo[index] = new InternetAddress(toAddress);
                index++;
            }
        }
        message.setRecipients(Message.RecipientType.TO, addressTo);
    }

    private void setReceipients(Map<Message.RecipientType, List<String>> to, Message message) throws AddressException, MessagingException { //
        if (to != null && !to.isEmpty()) {
            for (Map.Entry<Message.RecipientType, List<String>> entry : to.entrySet()) {
                Message.RecipientType recipientType = entry.getKey();
                List<String> addresses = entry.getValue();
                InternetAddress[] addressArray = addresses.stream()
                        .map(email -> {
                            try {
                                return new InternetAddress(email);
                            } catch (AddressException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .toArray(InternetAddress[]::new);

                message.setRecipients(recipientType, addressArray);
            }
        }
    }
}
