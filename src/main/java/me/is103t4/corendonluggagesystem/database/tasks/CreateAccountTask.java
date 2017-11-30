package me.is103t4.corendonluggagesystem.database.tasks;

import me.is103t4.corendonluggagesystem.account.Account;
import me.is103t4.corendonluggagesystem.account.AccountRole;
import me.is103t4.corendonluggagesystem.database.DBHandler;
import me.is103t4.corendonluggagesystem.database.DBTask;
import me.is103t4.corendonluggagesystem.database.PasswordAuthentication;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import me.is103t4.corendonluggagesystem.database.DBHandler.PreparingStatement;

public class CreateAccountTask extends DBTask<Account> {

    private final String username;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phoneNumber;
    private final char[] password;
    private final String tag;
    private final LocalDate dateOfBirth;
    private final AccountRole role;

    /**
     * The main constructor to create this task
     * @param firstName The user's first name
     * @param lastName The user's last name
     * @param email The user's email address
     * @param phoneNumber The user's phone number
     * @param password The user's password
     * @param tag The user's tag
     * @param dateOfBirth The user's date of birth
     * @param role The user's role
     */
    public CreateAccountTask(String firstName, String lastName, String email, String phoneNumber, String password, String tag, LocalDate dateOfBirth, AccountRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password.toCharArray();
        this.tag = tag;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.username = generateUsername();
    }

    /**
     * Method to generate a 4-digit username
     *
     * @return The 4-digit username
     */
    private String generateUsername() {
        return lastName.substring(0, lastName.length() < 3 ? lastName.length() : 3) + firstName.toCharArray()[0];
    }

    @Override
    protected Account call() throws Exception {
        
        //Write query 
        String query = "SELECT * FROM accounts WHERE code = ? AND username = ?";
        //Created prepared statement
        try(PreparedStatement ps = DBHandler.INSTANCE.getConnection().prepareStatement(query)){
        
            //Populate statement
            PreparingStatement preparing = new PreparingStatement(ps);
            preparing.setString(1, tag);
            preparing.setString(2, username);
            ResultSet set = ps.executeQuery();
            
            //Test return
            if (!set.next())
                return null;
            
        }catch(SQLException ex) {
            ex.printStackTrace();
        }
        
        // create query
        query = "INSERT INTO accounts (username, code, password, email, role, salt, last_name, first_name, phone_number, birth) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        // create PreparedStatement
        try (PreparedStatement ps = DBHandler.INSTANCE.getConnection().prepareStatement(query)) {

            // set values
            DBHandler.PreparingStatement preparing = new DBHandler.PreparingStatement(ps);
            byte[][] hashing = PasswordAuthentication.hash(password);
            preparing.setString(1, username);
            preparing.setString(2, tag);
            preparing.setBytes(3, hashing[0]);
            preparing.setString(4, email);
            preparing.setInt(5, role.getId());
            preparing.setBytes(6, hashing[1]);
            preparing.setString(7, lastName);
            preparing.setString(8, firstName);
            preparing.setString(9, phoneNumber);
            preparing.setDate(10, Date.valueOf(dateOfBirth));

            // execute
            ps.executeUpdate();

            // return account object
            return new Account(tag, username, firstName, lastName, phoneNumber, role, email);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

}