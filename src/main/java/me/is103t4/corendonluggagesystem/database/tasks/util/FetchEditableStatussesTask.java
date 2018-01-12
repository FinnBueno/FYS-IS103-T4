package me.is103t4.corendonluggagesystem.database.tasks.util;

import me.is103t4.corendonluggagesystem.database.DBHandler;
import me.is103t4.corendonluggagesystem.database.DBTask;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FetchEditableStatussesTask extends DBTask<List<String>> {

    public FetchEditableStatussesTask() {
        start();
    }

    @Override
    protected List<String> call() {
        String query = "SELECT s.value FROM statusses s WHERE id != 3;";
        try (PreparedStatement preparedStatement = DBHandler.INSTANCE.getConnection().prepareStatement(query)) {
            ResultSet set = preparedStatement.executeQuery();
            List<String> list = new ArrayList<>();
            while (set.next())
                list.add(set.getString(1));
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
