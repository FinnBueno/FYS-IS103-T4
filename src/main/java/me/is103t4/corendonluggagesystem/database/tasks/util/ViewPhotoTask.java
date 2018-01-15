package me.is103t4.corendonluggagesystem.database.tasks.util;

import me.is103t4.corendonluggagesystem.database.DBHandler;
import me.is103t4.corendonluggagesystem.database.DBTask;
import me.is103t4.corendonluggagesystem.matching.Luggage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewPhotoTask extends DBTask<String> {

    private final Luggage luggage;

    public ViewPhotoTask(Luggage select) {
        this.luggage = select;
        start();
    }

    @Override
    protected String call() {
        String query = "SELECT photo FROM luggage WHERE luggage_id = ?";
        try (PreparedStatement preparedStatement = DBHandler.INSTANCE.getConnection().prepareStatement(query)) {
            preparedStatement.setInt(1, luggage.getId());

            ResultSet set = preparedStatement.executeQuery();
            set.next();
            return set.getString("photo");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
