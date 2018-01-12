package me.is103t4.corendonluggagesystem.database.tasks.accounts;

import me.is103t4.corendonluggagesystem.account.Account;
import me.is103t4.corendonluggagesystem.account.AccountRole;
import me.is103t4.corendonluggagesystem.database.DBHandler;
import me.is103t4.corendonluggagesystem.database.DBTask;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateAccountDataTask extends DBTask<Boolean> {

    private final String firstName, lastName, email, phoneNumber;
    private final boolean active;
    private final AccountRole role;
    private final int id;

    public UpdateAccountDataTask(int id, String firstName, String lastName, String email, AccountRole role, String phoneNumber,
                                 boolean active) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.active = active;
        start();
    }

    @Override
    protected Boolean call() {
        String query = "UPDATE `accounts` " +
                "SET first_name = ?, " +
                "last_name = ?, " +
                "email = ?, " +
                "phone_number = ?, " +
                "active = ?," +
                "role = ? " +
                "WHERE account_id = ?;";
        try (PreparedStatement preparedStatement = DBHandler.INSTANCE.getConnection().prepareStatement(query)){
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, phoneNumber);
            preparedStatement.setBoolean(5, active);
            preparedStatement.setInt(6, role.getId());
            preparedStatement.setInt(7, id);

            return preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
