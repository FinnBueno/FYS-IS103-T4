package me.is103t4.corendonluggagesystem.database.tasks.util;

import me.is103t4.corendonluggagesystem.database.DBTask;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FetchCountriesTask extends DBTask<List<String>> {

    public FetchCountriesTask() {
        start();
    }

    @Override
    protected Object call() {
        String query = "SELECT country_name FROM `countries`";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet set = ps.executeQuery();
            List<String> result = new ArrayList<>();
            while (set.next())
                result.add(set.getString("country_name"));
            return result;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

}
