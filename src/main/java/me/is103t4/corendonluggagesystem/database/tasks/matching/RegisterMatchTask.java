package me.is103t4.corendonluggagesystem.database.tasks.matching;

import me.is103t4.corendonluggagesystem.account.Account;
import me.is103t4.corendonluggagesystem.database.DBHandler;
import me.is103t4.corendonluggagesystem.database.DBTask;
import me.is103t4.corendonluggagesystem.matching.Luggage;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterMatchTask extends DBTask<Boolean> {

    private final Luggage lost;
    private final Luggage found;

    public RegisterMatchTask(Luggage lost, Luggage found) {
        this.lost = lost;
        this.found = found;

        start();
    }

    @Override
    protected Boolean call() {
        String query = "INSERT INTO `matches` (lost_id, found_id, employee) VALUES (?, ?, ?)";
        boolean returnValue = false;
        try (PreparedStatement preparedStatement = DBHandler.INSTANCE.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, lost.getId());
            preparedStatement.setInt(2, found.getId());
            preparedStatement.setInt(3, Account.getLoggedInUser().getId());

            returnValue = preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!returnValue)
            return false;
        query = "UPDATE `luggage` " +
                "SET register_type = 3 " +
                "WHERE luggage_id = ? OR luggage_id = ?";
        try (PreparedStatement preparedStatement = DBHandler.INSTANCE.getConnection().prepareStatement(query)){
            preparedStatement.setInt(1, lost.getId());
            preparedStatement.setInt(2, found.getId());

            return preparedStatement.executeUpdate() != -1 && returnValue;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
