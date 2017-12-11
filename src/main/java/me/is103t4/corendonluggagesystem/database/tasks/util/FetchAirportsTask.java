package me.is103t4.corendonluggagesystem.database.tasks.util;

import me.is103t4.corendonluggagesystem.database.DBTask;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FetchAirportsTask extends DBTask<List<String>> {

    public FetchAirportsTask() {
        start();
    }

    @Override
    protected List<String> call() throws Exception {
        String query = "SELECT airport_name FROM `airports` ORDER BY airport_name";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet set = ps.executeQuery();
            List<String> result = new ArrayList<>();
            while (set.next())
                result.add(set.getString("airport_name"));
            return result;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

}