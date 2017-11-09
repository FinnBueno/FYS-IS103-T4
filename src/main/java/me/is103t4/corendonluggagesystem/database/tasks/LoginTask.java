/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.database.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import me.is103t4.corendonluggagesystem.account.Account;
import me.is103t4.corendonluggagesystem.account.AccountRole;
import me.is103t4.corendonluggagesystem.database.DBHandler.PreparingStatement;
import me.is103t4.corendonluggagesystem.database.DBTask;

/**
 *
 * @author finnb
 */
public class LoginTask extends DBTask<Account> {

    private final String username, password;
    private final int code;

    public LoginTask(int code, String username, String password) {
	this.code = code;
	this.username = username;
	this.password = password;
    }

    @Override
    protected Account call() throws Exception {
	
	String query = "SELECT * FROM `accounts` WHERE code=? AND username=? AND password=md5(?)";
	try (PreparedStatement preparedStatement = conn.
		prepareStatement(query)) {
	    PreparingStatement preparingStatement = new PreparingStatement(preparedStatement);
	    preparingStatement.setInt(1, code).
		    setString(2, username).
		    setString(3, password);
	    
	    ResultSet result = preparedStatement.executeQuery();

	    return result.next() ? new Account(result.getInt("code"), result.
		    getString("username"), AccountRole.fromId(result.
		    getInt("role")), result.getString("email")) : null;
	} catch (SQLException ex) {
	    ex.printStackTrace();
	}
	return null;
	
    }

}
