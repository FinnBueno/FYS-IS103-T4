package me.is103t4.corendonluggagesystem.database.tasks.luggage;

import me.is103t4.corendonluggagesystem.database.DBTask;
import me.is103t4.corendonluggagesystem.matching.Luggage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FetchAllLuggageTask extends DBTask<Luggage[]> {

    public FetchAllLuggageTask() {
        start();
    }

    @Override
    protected Luggage[] call() {
        // create query
        String query = "SELECT l.luggage_id, s.value, lt.lug_type_value, l.luggage_tag, " +
                "l.brand, l.colour, l.characteristics, l.first_name, l.last_name, " +
                "l.city, l.address, l.flight_id, l.date " +
                "FROM luggage l " +
                "JOIN luggage_types lt " +
                "ON lt.lug_type_id = l.luggage_type " +
                "JOIN statusses s " +
                "ON s.id = l.register_type " +
                "ORDER BY s.id ASC;";
        // create PreparedStatement
        try (PreparedStatement preparedStatement = conn.
                prepareStatement(query)) {
            ResultSet set = preparedStatement.executeQuery();
            List<Luggage> result = new ArrayList<>();
            while (set.next()) {
                Date sqlDate = set.getDate(13);
                LocalDate date = sqlDate == null ? null : sqlDate.toLocalDate();
                result.add(new Luggage(set.getInt(1),
                        set.getString(2),
                        set.getString(3),
                        set.getString(4),
                        set.getString(5),
                        set.getString(6),
                        set.getString(7),
                        set.getString(8),
                        set.getString(9),
                        set.getString(10),
                        set.getString(11),
                        set.getString(12),
                        date));
            }
            return result.toArray(new Luggage[result.size()]);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new Luggage[0];
    }

}
