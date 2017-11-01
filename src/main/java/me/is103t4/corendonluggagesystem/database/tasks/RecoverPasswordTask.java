/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.database.tasks;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import me.is103t4.corendonluggagesystem.database.DBHandler.PreparingStatement;
import me.is103t4.corendonluggagesystem.database.DBTask;

/**
 *
 * @author finnb
 */
public class RecoverPasswordTask extends DBTask<Integer> {

    private final String address;

    public RecoverPasswordTask(String address) {
	this.address = address;
    }

    @Override
    protected Integer call() throws Exception {
	String query = "SELECT * FROM `accounts` WHERE email=?";
	try (PreparedStatement preparedStatement = conn.
		prepareStatement(query)) {
	    PreparingStatement preparingStatement = new PreparingStatement(preparedStatement);
	    preparingStatement.setString(1, address);

	    ResultSet result = preparedStatement.executeQuery();
	    boolean success = result.next();
	    if (!success) {
		return -1;
	    }

	    return generateResetCode();
	} catch (SQLException ex) {
	    Logger.getLogger(RecoverPasswordTask.class.getName()).
		    log(Level.SEVERE, null, ex);
	    return -1;
	}
    }

    private int generateResetCode() {
	String code = "";
	for (int i = 0; i < 6; i++)
	    code += ThreadLocalRandom.current().
		    nextInt(10);

	try {
	    final String username = "noreply.corendonluggage@gmail.com";
	    final String password = "hboictis103t4";

	    Properties props = new Properties();
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.port", "587");

	    Session session;
	    session = Session.getInstance(props,
		    new javax.mail.Authenticator() {
		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
		    return new PasswordAuthentication(username, password);
		}
	    });

	    Message message = new MimeMessage(session);
	    message.setFrom(new InternetAddress(username));
	    message.setRecipients(Message.RecipientType.TO,
		    InternetAddress.parse(address));
	    message.setSubject("Reset password");
	    message.
		    setText("Corendon Password Reset\n\nA password reset has "
			    + "been requested for this account. Please enter the "
			    + "following code in the code textfield in order to "
			    + "reset your password.\n\nCode: " + code);

	    Transport.send(message);

	    return Integer.parseInt(code);
	} catch (MessagingException ex) {
	    Logger.getLogger(RecoverPasswordTask.class.getName()).
		    log(Level.SEVERE, null, ex);
	    return -1;
	}
    }

}
