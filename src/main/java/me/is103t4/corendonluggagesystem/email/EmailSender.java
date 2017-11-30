/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.email;

import java.io.File;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author Finn Bon
 */
public class EmailSender {

    private final static String FROM = "noreply@corendon.com";
    private final static String HOST = "localhost";

    private static final EmailSender INSTANCE = new EmailSender();

    public static EmailSender getInstance() {
        return INSTANCE;
    }

    private EmailSender() {
    }

    public void send(Email email) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", "25");

        Session session = Session.getDefaultInstance(props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(FROM, "hboictis103t4");
                    }
                });

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(FROM));
            // Set To: header field of the header.
            for (String rec : email.getRecipients()) {
                message.
                        addRecipient(Message.RecipientType.TO, new InternetAddress(rec));
            }

            // Set Subject: header field
            message.setSubject(email.getSubject());

            // body
            BodyPart body = new MimeBodyPart();
            if (email.usesHTML()) {
                body.setContent(email.getContent(), "text/html");
            } else {
                body.setText(email.getContent());
            }

            Multipart multi = new MimeMultipart();
            multi.addBodyPart(body);

            // attachments
            for (File file : email.getAttachments()) {
                MimeBodyPart secBody = new MimeBodyPart();
                DataSource src = new FileDataSource(file);

                secBody.setDataHandler(new DataHandler(src));
                secBody.setHeader("Content-ID", "<image>");

                multi.addBodyPart(secBody);
            }

            message.setContent(multi);

            // Send message
            Transport.send(message);
        } catch (MessagingException ex) {
            Logger.getLogger(EmailSender.class.getName()).
                    log(Level.WARNING, "'hMailServer' doesn't seem to be running. This program must be started in order for the luggage system to send emails!");
            Logger.getLogger(EmailSender.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

}
