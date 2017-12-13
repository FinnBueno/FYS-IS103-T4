package me.is103t4.corendonluggagesystem.database.tasks.util;

import me.is103t4.corendonluggagesystem.database.DBTask;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FetchAirlinesTask extends DBTask<List<String>> {

    public FetchAirlinesTask() {
        start();
    }

    @Override
    protected Object call() throws Exception {
        String query = "SELECT * FROM flight_lines ORDER BY flight_id";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet set = ps.executeQuery();
            List<String> flightIdResult = new ArrayList<>();
            while (set.next()) {
                flightIdResult.add(set.getString("flight_id"));
            }
            return flightIdResult;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

}
