/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.is103t4.corendonluggagesystem.database.tasks.accounts;

import me.is103t4.corendonluggagesystem.database.DBHandler.PreparingStatement;
import me.is103t4.corendonluggagesystem.database.DBTask;
import me.is103t4.corendonluggagesystem.email.EmailSender;
import me.is103t4.corendonluggagesystem.email.IEmail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Database task to recover a user's password
 *
 * @author Finn Bon
 */
public class RecoverPasswordTask extends DBTask<Integer> {

    // email address
    private final String address;

    /**
     * @param address The email address to check
     */
    public RecoverPasswordTask(String address) {
        this.address = address;

        start();
    }

    @Override
    protected Integer call() throws Exception {
        // write query
        String query = "SELECT * FROM `accounts` WHERE email=?";
        // create prepared statement
        try (PreparedStatement preparedStatement = conn.
                prepareStatement(query)) {
            PreparingStatement preparingStatement = new PreparingStatement(preparedStatement);
            // set value
            preparingStatement.setString(1, address);

            // fetch result
            ResultSet result = preparedStatement.executeQuery();
            boolean success = result.next();
            // if nothing was found return
            if (!success) {
                return -1;
            }

            // generate and return reset code
            return generateResetCode();
        } catch (SQLException ex) {
            Logger.getLogger(RecoverPasswordTask.class.getName()).
                    log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    /**
     * Method to randomly generate a reset code
     *
     * @return A random reset code
     */
    private int generateResetCode() {
        // generate base
        String code = "";
        // for loop to run 6 times
        for (int i = 0; i < 6; i++)
            // add random number between 0 - 9
            code += ThreadLocalRandom.current().
                    nextInt(10);

        // create email and populate it with a html file content
        IEmail email = new IEmail("Password Reset", true, address);
        email.setContentFromURL(getClass().
                getResource("/email/passwordEmail.html"), true);
        email.setContent(email.getContent().
                replace("%%code%%", code));

        // send email
        EmailSender.getInstance().send(email);

        // return generated reset code
        return Integer.parseInt(code);
    }

}
