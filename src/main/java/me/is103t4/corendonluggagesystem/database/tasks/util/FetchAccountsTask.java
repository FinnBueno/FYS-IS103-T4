package me.is103t4.corendonluggagesystem.database.tasks.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import me.is103t4.corendonluggagesystem.account.Account;
import me.is103t4.corendonluggagesystem.account.AccountRole;
import me.is103t4.corendonluggagesystem.database.DBTask;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class FetchAccountsTask extends DBTask<List<Account>> {

    public FetchAccountsTask() {
        start();
    }

    @Override
    protected ObservableList<Account> call() throws Exception {
        String query = "SELECT * FROM `accounts`";
        try (PreparedStatement ps = conn.prepareStatement(query)){
            ResultSet set = ps.executeQuery();
            ObservableList<Account> accounts = FXCollections.observableArrayList();
            while (set.next()) {
                accounts.add(new Account(
                        set.getInt("account_id"), set.getString("code"),
                        set.getString("username"),
                        set.getString("first_name"),
                        set.getString("last_name"),
                        set.getString("phone_number"),
                        AccountRole.fromId(set.getInt("role")),
                        set.getString("email"),
                        set.getBoolean("active")));
            }
            return accounts;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return FXCollections.observableArrayList();
    }

}
