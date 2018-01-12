package me.is103t4.corendonluggagesystem.database.tasks.accounts;

import me.is103t4.corendonluggagesystem.account.Account;
import me.is103t4.corendonluggagesystem.database.DBHandler;
import me.is103t4.corendonluggagesystem.database.DBTask;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ToggleActivationTask extends DBTask<Boolean> {

    private final Account account;

    public ToggleActivationTask(Account acc) {
        this.account = acc;
        start();
    }

    @Override
    protected Boolean call() {
        String query = "UPDATE `accounts` SET active = NOT active WHERE account_id = ?;";
        try (PreparedStatement preparedStatement = DBHandler.INSTANCE.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, account.getId());
            return preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
