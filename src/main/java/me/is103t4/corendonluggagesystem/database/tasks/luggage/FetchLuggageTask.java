package me.is103t4.corendonluggagesystem.database.tasks.luggage;

import me.is103t4.corendonluggagesystem.database.DBTask;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FetchLuggageTask extends DBTask<String[]> {

    private final int id;

    public FetchLuggageTask(int id) {
        this.id = id;
        start();
    }

    @Override
    protected Object[] call() {
        // create query
        String query = "SELECT l.*, s.value " +
                "FROM luggage l " +
                "JOIN statusses s " +
                "ON s.id = l.register_type " +
                "WHERE luggage_id = ?;";
        // create PreparedStatement
        try (PreparedStatement preparedStatement = conn.
                prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet set = preparedStatement.executeQuery();
            if (!set.next())
                return null;
            return new Object[] {
                    set.getString(3), set.getString(4), set.getString(5), set.getString(6),
                    set.getString(7), set.getLong(8), set.getString(9),
                    set.getString(10), set.getInt(11), set.getString(12), set.getString(13),
                    set.getString(14), set.getString(15), set.getString(17), set.getInt(1),
                    set.getString(20)
            };
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
