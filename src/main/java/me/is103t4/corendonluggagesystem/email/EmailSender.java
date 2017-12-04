/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.email;

import com.sendgrid.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

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
    private static SendGrid SENDGRID;
    private static final String EMAIL_ADDRESS = "noreply@corendonluggage.com";

    static {
        try {
            SENDGRID = new SendGrid(SENDGRID_API_KEY);
        } catch (Exception exc) {
            System.err.println("SendGrid API key is invalid!");
        }
    }

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

        // set attachments (if there are any)
        if (iEmail.getAttachments().size() > 0) {
            Attachments attachments = new Attachments();
            for (File file : iEmail.getAttachments())
                try {
                    String imageDataString = Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath()));
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
