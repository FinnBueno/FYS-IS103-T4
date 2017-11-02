/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.email;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
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
	// Get system properties
	Properties properties = System.getProperties();
	// Setup mail server
	properties.setProperty("mail.smtp.host", HOST);
	// Get the default Session object
	Session session = Session.getDefaultInstance(properties);
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

	    // Now set the actual message
	    if (email.usesHTML())
		message.setContent(email.getContent(), "text/html");
	    else
		message.setText(email.getContent());

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
