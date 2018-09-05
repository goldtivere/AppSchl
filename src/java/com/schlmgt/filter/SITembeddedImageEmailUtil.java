/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schlmgt.filter;

/**
 *
 * @author Abobade Oludayo Michael
 * @mobile 08065711043
 * @email pagims2003@yahoo.com
 */
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SITembeddedImageEmailUtil {

    private String messangerOfTruth;

    //this is for sending out mails with images...
    public boolean sendOut(String toAddress,
            String subject, String htmlBody)
            throws AddressException, MessagingException {

        try {

            String host = "smtp.gmail.com";

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "587");

            // Get the Session object.
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("goldtivere@gmail.com", "GOLDEMILAN");
                }
            });

            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress("goldtivere@gmail.com"));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toAddress));

            // Set Subject: header field
            message.setSubject(subject);

            // Now set the actual message
            message.setContent(htmlBody, "text/html; charset=utf-8");

            // Send message
            Transport.send(message);

            System.out.println("Sent message successfully....");

            return true;

        } catch (Exception ex) {

            System.out.println("**** Not Sent **** ");
            ex.printStackTrace();
            return false;

        }

    }//end class EmbeddedImageEmailUtil

    /**
     * @return the messangerOfTruth
     */
    public String getMessangerOfTruth() {
        return messangerOfTruth;
    }

    /**
     * @param messangerOfTruth the messangerOfTruth to set
     */
    public void setMessangerOfTruth(String messangerOfTruth) {
        this.messangerOfTruth = messangerOfTruth;
    }
}
