/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.email;

import com.sendgrid.*;
import org.apache.commons.codec.binary.Base64;
import sun.misc.IOUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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

    private static final String SENDGRID_API_KEY = "<insert key>";
    private static final SendGrid SENDGRID = new SendGrid(SENDGRID_API_KEY);
    private static final String EMAIL_ADDRESS = "noreply@corendonluggage.com";

    private EmailSender() {
    }

    public void send(IEmail iEmail) {

        // create email instance
        Mail mail = new Mail();

        // set from
        mail.setFrom(new Email(EMAIL_ADDRESS));

        // set recipients
        Personalization personalization = new Personalization();
        iEmail.getRecipients().forEach(to -> personalization.addTo(new Email(to, "Receiver")));
        mail.addPersonalization(personalization);

        // set content (html) and subject
        Content content = new Content();
        content.setType("text/html");
        content.setValue(iEmail.getContent());
        mail.addContent(content);
        mail.setSubject(iEmail.getSubject());

        if (iEmail.getAttachments().size() > 0) {
            // set attachments (if there are any)
            Attachments attachments = new Attachments();
            Base64 x = new Base64();
            for (File file : iEmail.getAttachments()) {
                try {
                    String imageDataString = x.encodeAsString(Files.readAllBytes(file.toPath()));
                    attachments.setContent(imageDataString);
                    String[] split = file.getAbsolutePath().split("\\.");
                    System.out.println(file.getAbsolutePath());
                    String extension = split[split.length - 1];
                    attachments.setType("image/" + extension);
                    attachments.setFilename("luggage." + extension);
                    attachments.setDisposition("attachment");
                    attachments.setContentId("Luggage");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mail.addAttachments(attachments);
        }

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = SENDGRID.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
