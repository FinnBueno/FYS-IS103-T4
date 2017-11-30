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
import me.is103t4.corendonluggagesystem.database.PasswordAuthentication;

/**
 * This DB task changes the password of a user to a new one
 *
 * @author Finn Bon
 */
public class ChangePasswordTask extends DBTask {

    // the user's email and new password
    private final String email, password;

    /**
     * @param email The email of the user whom wants their password changed
     * @param password The new password
     */
    public ChangePasswordTask(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    protected Object call() throws Exception {

        // create query
        String query = "UPDATE accounts SET password=? AND salt=? WHERE email=?";
        // create PreparedStatement
        try (PreparedStatement preparedStatement = conn.
                prepareStatement(query)) {
            PreparingStatement preparingStatement = new PreparingStatement(preparedStatement);

            // hash password and salt
            byte[][] hashed = PasswordAuthentication.hash(password.toCharArray());

            // set values
            preparingStatement.setBytes(1, hashed[0]);
            preparingStatement.setBytes(2, hashed[1]);
            preparingStatement.setString(3, email);

            // execute query
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
