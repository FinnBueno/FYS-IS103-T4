package me.is103t4.corendonluggagesystem.database.tasks.accounts;

import me.is103t4.corendonluggagesystem.account.Account;
import me.is103t4.corendonluggagesystem.database.DBHandler;
import me.is103t4.corendonluggagesystem.database.DBTask;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SaveAccountDataTask extends DBTask<Boolean> {

    public SaveAccountDataTask() {
        start();
    }

    @Override
    protected Boolean call() throws Exception {
        String query = "UPDATE `accounts` " +
                "SET first_name = ?, " +
                "last_name = ?, " +
                "email = ?, " +
                "phone_number = ? " +
                "WHERE account_id = ?";
        Account acc = Account.getLoggedInUser();
        try (PreparedStatement preparedStatement = DBHandler.INSTANCE.getConnection().prepareStatement(query)){
            preparedStatement.setString(1, acc.getFirstName());
            preparedStatement.setString(2, acc.getLastName());
            preparedStatement.setString(3, acc.getEmail());
            preparedStatement.setString(4, acc.getPhoneNumber());
            preparedStatement.setInt(5, acc.getId());

            return preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
