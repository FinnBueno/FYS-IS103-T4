/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.database.tasks;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import me.is103t4.corendonluggagesystem.database.DBHandler.PreparingStatement;
import me.is103t4.corendonluggagesystem.database.DBTask;

/**
 *
 * @author finnb
 */
public class ChangePasswordTask extends DBTask {

    private final String email, password;

    public ChangePasswordTask(String email, String password) {
	this.email = email;
	this.password = password;
    }

    @Override
    protected Object call() throws Exception {

	String query = "UPDATE `accounts` SET password=md5(?) WHERE email=?";
	try (PreparedStatement preparedStatement = conn.
		prepareStatement(query)) {
	    PreparingStatement preparingStatement = new PreparingStatement(preparedStatement);
	    preparingStatement.setString(1, password).
		    setString(2, email);

	    preparedStatement.executeUpdate();
	} catch (SQLException ex) {
	    ex.printStackTrace();
	}
	return null;
    }
}
