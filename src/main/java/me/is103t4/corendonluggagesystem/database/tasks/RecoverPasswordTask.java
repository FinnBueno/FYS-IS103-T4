/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.database.tasks;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.is103t4.corendonluggagesystem.database.DBHandler.PreparingStatement;
import me.is103t4.corendonluggagesystem.database.DBTask;
import me.is103t4.corendonluggagesystem.email.Email;
import me.is103t4.corendonluggagesystem.email.EmailSender;

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

	Email email = new Email("Password Reset", true, address);
	email.setContentFromURL(getClass().
		getResource("/email/email.html"), true);
	email.setContent(email.getContent().
		replace("%%code%%", code));
	
	EmailSender.getInstance().
		send(email);

	return Integer.parseInt(code);
    }

}
