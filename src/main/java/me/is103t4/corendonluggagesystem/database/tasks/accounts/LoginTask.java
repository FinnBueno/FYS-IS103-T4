/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.database.tasks.accounts;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.is103t4.corendonluggagesystem.account.Account;
import me.is103t4.corendonluggagesystem.account.AccountRole;
import me.is103t4.corendonluggagesystem.database.DBHandler.PreparingStatement;
import me.is103t4.corendonluggagesystem.database.DBTask;
import me.is103t4.corendonluggagesystem.database.PasswordAuthentication;

/**
 * A class that is to be used when trying to login to the application
 *
 * @author Finn Bon
 */
public class LoginTask extends DBTask<Account> {

    private final String username;
    private final char[] password;
    private final String code;

    /**
     * @param code The user's tag
     * @param username The user's 4-digit username
     * @param password The user's password
     */
    public LoginTask(String code, String username, String password) {
        this.code = code;
        this.username = username;
        this.password = password.toCharArray();
    }

    @Override
    protected Account call() throws Exception {
        // create query
        String query = "SELECT * FROM `accounts` WHERE code=? AND username=?";
        // create PreparedStatement
        try (PreparedStatement preparedStatement = conn.
                prepareStatement(query)) {
            PreparingStatement preparingStatement = new PreparingStatement(preparedStatement);
            preparingStatement.setString(1, code).
                    setString(2, username);

            // handle results
            ResultSet result = preparedStatement.executeQuery();
            boolean found = false;
            while (result.next()) {
                // try if passwords of queried account and input password match
                byte[] hashedPassword = result.getBytes("password");
                byte[] salt = result.getBytes("salt");
                // if so set found to true and break
                found = PasswordAuthentication.compare(password, salt, hashedPassword);
                if (found)
                    break;
            }

            // if match was not found return null
            if (!found)
                return null;

            // clear password array to prevent it from lingering around on the heap
            for (int i = 0; i < password.length; i++)
                password[i] = 0;

            // fetch all data from result
            String tag = result.getString("code");
            String username = result.getString("username");
            String firstName = result.getString("first_name");
            String lastName = result.getString("last_name");
            String phoneNumber = result.getString("phone_number");
            AccountRole role = AccountRole.fromId(result.getInt("role"));
            String email = result.getString("email");

            // return account instance
            return new Account(tag, username, firstName, lastName, phoneNumber, role, email);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;

    }

}
