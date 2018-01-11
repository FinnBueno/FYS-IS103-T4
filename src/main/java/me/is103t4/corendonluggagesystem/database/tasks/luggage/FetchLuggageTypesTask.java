package me.is103t4.corendonluggagesystem.database.tasks.luggage;

import me.is103t4.corendonluggagesystem.database.DBHandler;
import me.is103t4.corendonluggagesystem.database.DBTask;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FetchLuggageTypesTask extends DBTask<List<String>> {

    public FetchLuggageTypesTask() {
        start();
    }

    @Override
    protected List<String> call() {
        String query = "SELECT lug_type_value FROM luggage_types";
        try (PreparedStatement preparedStatement = DBHandler.INSTANCE.getConnection().prepareStatement(query)){
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
