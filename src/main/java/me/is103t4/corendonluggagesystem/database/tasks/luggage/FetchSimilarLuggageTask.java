package me.is103t4.corendonluggagesystem.database.tasks.luggage;

import javafx.scene.paint.Color;
import me.is103t4.corendonluggagesystem.database.DBHandler;
import me.is103t4.corendonluggagesystem.database.DBTask;
import me.is103t4.corendonluggagesystem.matching.Luggage;
import me.is103t4.corendonluggagesystem.util.ColorUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FetchSimilarLuggageTask extends DBTask<Luggage[]> {

    private static final double COLOR_THRESHOLD = 150;
    private final Luggage baseLuggage;
    private final int severity;

    public FetchSimilarLuggageTask(Luggage luggage, int severity) {
        this.baseLuggage = luggage;
        this.severity = severity;

        start();
    }

    @Override
    protected Luggage[] call() {
        switch (severity) {
            case 0:
                return searchStrict();
            case 1:
                return searchNormal();
            case 2:
                return searchLoose();
        }
        return new Luggage[0];
    }

    private Luggage[] searchNormal() {
        String query =
                "SELECT l.luggage_id, s.value, lt.lug_type_value, l.luggage_tag, l.brand, l.colour, l" +
                        ".characteristics, l.first_name, l.last_name, l.city, l.address, l.flight_id " +
                        "FROM luggage l " +
                        "JOIN luggage_types lt " +
                        "ON lt.lug_type_id = l.luggage_type " +
                        "JOIN statusses s " +
                        "ON s.id = l.register_type " +

                        "WHERE (SELECT COUNT(m.match_id) FROM matches m WHERE l.luggage_id = m.lost_id OR l.luggage_id = m.found_id) = 0 AND " +
                        "l.register_type < 3 " +
                        "s.value = ? AND " +
                        "lt.lug_type_value LIKE ? AND " +
                        "(l.luggage_tag IS NULL OR UPPER(l.luggage_tag) LIKE UPPER(?)) AND " +
                        "(l.brand IS NULL OR UPPER(l.brand) LIKE UPPER(?)) AND " +
                        "(l.characteristics IS NULL OR UPPER(l.characteristics) LIKE UPPER(?)) AND " +
                        "(l.first_name IS NULL OR UPPER(l.first_name) LIKE UPPER(?)) AND " +
                        "(l.last_name IS NULL OR UPPER(l.last_name) LIKE UPPER(?)) AND " +
                        "(l.city IS NULL OR UPPER(l.city) LIKE UPPER(?)) AND " +
                        "(l.address IS NULL OR UPPER(l.address) LIKE UPPER(?)) AND " +
                        "(l.flight_id IS NULL OR UPPER(l.flight_id) LIKE UPPER(?));";
        String searchingFor = getSearchingFor();
        if (searchingFor == null)
            return new Luggage[0];

        return findLuggage(query, searchingFor, true);
    }

    private Luggage[] searchLoose() {
        String query =
                "SELECT l.luggage_id, s.value, lt.lug_type_value, l.luggage_tag, l.brand, l.colour, l" +
                        ".characteristics, l.first_name, l.last_name, l.city, l.address, l.flight_id " +
                        "FROM luggage l " +
                        "JOIN luggage_types lt " +
                        "ON lt.lug_type_id = l.luggage_type " +
                        "JOIN statusses s " +
                        "ON s.id = l.register_type " +

                        "WHERE (SELECT COUNT(m.match_id) FROM matches m WHERE l.luggage_id = m.lost_id OR l.luggage_id = m.found_id) = 0 AND " +
                        "l.register_type < 3 AND " +
                        "s.value = ? AND " +
                        "lt.lug_type_value LIKE ? OR " +
                        "(l.luggage_tag IS NULL OR UPPER(l.luggage_tag) LIKE UPPER(?)) OR " +
                        "(l.brand IS NULL OR UPPER(l.brand) LIKE UPPER(?)) OR " +
                        "(l.characteristics IS NULL OR UPPER(l.characteristics) LIKE UPPER(?)) OR " +
                        "(l.first_name IS NULL OR UPPER(l.first_name) LIKE UPPER(?)) OR " +
                        "(l.last_name IS NULL OR UPPER(l.last_name) LIKE UPPER(?)) OR " +
                        "(l.city IS NULL OR UPPER(l.city) LIKE UPPER(?)) OR " +
                        "(l.address IS NULL OR UPPER(l.address) LIKE UPPER(?)) OR " +
                        "(l.flight_id IS NULL OR UPPER(l.flight_id) LIKE UPPER(?));";
        String searchingFor = getSearchingFor();
        if (searchingFor == null)
            return new Luggage[0];

        return findLuggage(query, searchingFor, true);
    }

    private Luggage[] searchStrict() {
        String query =
                "SELECT l.luggage_id, s.value, lt.lug_type_value, l.luggage_tag, l.brand, l.colour, l" +
                        ".characteristics, l.first_name, l.last_name, l.city, l.address, l.flight_id " +
                        "FROM luggage l " +
                        "JOIN luggage_types lt " +
                        "ON lt.lug_type_id = l.luggage_type " +
                        "JOIN statusses s " +
                        "ON s.id = l.register_type " +

                        "WHERE (SELECT COUNT(m.match_id) FROM matches m JOIN luggage l ON l.luggage_id = m.lost_id OR l.luggage_id = m.found_id) = 0 AND " +
                        "l.register_type < 3 AND " +
                        "s.value = '?' AND " +
                        "lt.lug_type_value LIKE ? AND " +
                        "(l.luggage_tag IS NULL OR l.UPPER(luggage_tag) = UPPER(?)) AND " +
                        "(l.brand IS NULL OR UPPER(l.brand) = UPPER(?)) AND " +
                        "(l.characteristics IS NULL OR UPPER(l.characteristics) = UPPER(?)) AND " +
                        "(l.first_name IS NULL OR UPPER(l.first_name) = UPPER(?)) AND " +
                        "(l.last_name IS NULL OR UPPER(l.last_name) = UPPER(?)) AND " +
                        "(l.city IS NULL OR UPPER(l.city) = UPPER(?)) AND " +
                        "(l.address IS NULL OR UPPER(l.address) = UPPER(?)) AND " +
                        "(l.flight_id IS NULL OR UPPER(l.flight_id) = UPPER(?));";
        String searchingFor = getSearchingFor();
        if (searchingFor == null)
            return new Luggage[0];

        return findLuggage(query, searchingFor, false);
    }

    private Luggage[] findLuggage(String query, String searchingFor, boolean wildcard) {
        try (PreparedStatement preparedStatement = DBHandler.INSTANCE.getConnection().prepareStatement(query)) {
            // preparedStatement.setInt(1, baseLuggage.getId());
            preparedStatement.setString(1, searchingFor);
            preparedStatement.setString(2, wildcard ? baseLuggage.getType() : wildcard(baseLuggage.getType()));
            preparedStatement.setString(3, wildcard ? baseLuggage.getTag() : wildcard(baseLuggage.getTag()));
            preparedStatement.setString(4, wildcard ? baseLuggage.getBrand() : wildcard(baseLuggage.getBrand()));
            preparedStatement.setString(5, wildcard ? baseLuggage.getCharacteristics() : wildcard(baseLuggage.getCharacteristics()));
            preparedStatement.setString(6, wildcard ? baseLuggage.getFirstName() : wildcard(baseLuggage.getFirstName()));
            preparedStatement.setString(7, wildcard ? baseLuggage.getLastName() : wildcard(baseLuggage.getLastName()));
            preparedStatement.setString(8, wildcard ? baseLuggage.getCity() : wildcard(baseLuggage.getCity()));
            preparedStatement.setString(9, wildcard ? baseLuggage.getAddress() : wildcard(baseLuggage.getAddress()));
            preparedStatement.setString(10, wildcard ? baseLuggage.getFlight() : wildcard(baseLuggage.getFlight()));

            System.out.println(searchingFor);
            ResultSet set = preparedStatement.executeQuery();
            List<Luggage> result = new ArrayList<>();
            while (set.next()) {
                String status = set.getString(2);
                if (!status.equalsIgnoreCase(searchingFor))
                    continue;
                String hex = set.getString(6);
                double distance = ColorUtil.getDistance(baseLuggage.getColour(), hex);
                if (distance > COLOR_THRESHOLD)
                    continue;
                result.add(new Luggage(set.getInt(1),
                        status,
                        set.getString(3),
                        set.getString(4),
                        set.getString(5),
                        hex,
                        set.getString(7),
                        set.getString(8),
                        set.getString(9),
                        set.getString(10),
                        set.getString(11),
                        set.getString(12)));
            }
            return result.toArray(new Luggage[result.size()]);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new Luggage[0];
    }

    private String wildcard(String value) {
        return value == null || value.length() == 0 ? "%" : ("%" + value + "%");
    }

    private String getSearchingFor() {
        String status = baseLuggage.getStatus();
        if (status.equalsIgnoreCase("lost")) return "Found";
        else if (status.equalsIgnoreCase("found")) return "Lost";
        else return null;
    }
}
