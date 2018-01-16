package me.is103t4.corendonluggagesystem.database.tasks.luggage;

import me.is103t4.corendonluggagesystem.database.DBHandler;
import me.is103t4.corendonluggagesystem.database.DBTask;
import me.is103t4.corendonluggagesystem.util.DateRange;
import me.is103t4.corendonluggagesystem.util.MonthYear;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FetchLuggageDataTask extends DBTask<Map<String, Map<MonthYear, Integer>>> {

    private final LocalDate startDate, endDate;
    private final boolean lost, found, damaged, handled, destroyed, depot;
    private final String airport;

    public FetchLuggageDataTask(LocalDate startDate, Integer timeSpan, boolean lost, boolean found, boolean damaged,
                                boolean handled, boolean destroyed, boolean depot, String airport) {
        this.startDate = startDate;
        this.endDate = startDate.plusMonths(timeSpan);
        this.lost = lost;
        this.found = found;
        this.damaged = damaged;
        this.handled = handled;
        this.destroyed = destroyed;
        this.depot = depot;
        this.airport = !airport.contains("-") ? "" :
                airport.split(" - ")[0];
        start();
    }

    @Override
    protected Map<String, Map<MonthYear, Integer>> call() {
        String[] statusses = getAllStatusses();
        String query = "SELECT s.value, l.date " +
                "FROM luggage l " +
                "JOIN statusses s " +
                "ON s.id = l.register_type " +
                "WHERE l.date >= ? AND " +
                "l.date <= ? AND " +
                // ignoring preparedstatement convention here because otherwise we'd require a huge if statement, and
                // one argument won't hurt.
                (airport.length() == 0 ? "" : "l.flight_id = '" + airport + "' AND ") +
                "((l.register_type = (SELECT id FROM statusses WHERE value = ?) AND ?) OR " +
                "(l.register_type = (SELECT id FROM statusses WHERE value = ?) AND ?) OR " +
                "(l.register_type = (SELECT id FROM statusses WHERE value = ?) AND ?) OR " +
                "(l.register_type = (SELECT id FROM statusses WHERE value = ?) AND ?) OR " +
                "(l.register_type = (SELECT id FROM statusses WHERE value = ?) AND ?) OR " +
                "(l.register_type = (SELECT id FROM statusses WHERE value = ?) AND ?)) " +
                "ORDER BY s.id;";
        try (PreparedStatement preparedStatement = DBHandler.INSTANCE.getConnection().prepareStatement(query)) {
            preparedStatement.setDate(1, Date.valueOf(startDate));
            preparedStatement.setDate(2, Date.valueOf(endDate));
            preparedStatement.setString(3, "Lost");
            preparedStatement.setBoolean(4, lost);
            preparedStatement.setString(5, "Found");
            preparedStatement.setBoolean(6, found);
            preparedStatement.setString(7, "Damaged");
            preparedStatement.setBoolean(8, damaged);
            preparedStatement.setString(9, "Handled");
            preparedStatement.setBoolean(10, handled);
            preparedStatement.setString(11, "Destroyed");
            preparedStatement.setBoolean(12, destroyed);
            preparedStatement.setString(13, "Depot");
            preparedStatement.setBoolean(14, depot);

            ResultSet set = preparedStatement.executeQuery();

            Map<String, Map<MonthYear, Integer>> result = new HashMap<>();
            for (String statusName : statusses) {
                Map<MonthYear, Integer> innerMap = new HashMap<>();
                result.put(statusName, innerMap);
                new DateRange(startDate, endDate).toList().forEach(date -> innerMap.put(new MonthYear(date), 0));
            }

            while (set.next()) {
                String status = set.getString(1);
                LocalDate date = set.getDate(2).toLocalDate();
                Map<MonthYear, Integer> innerMap = result.get(status);
                increaseValue(innerMap, date);
            }

            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    private void increaseValue(Map<MonthYear, Integer> innerMap, LocalDate date) {
        MonthYear replaceable = null;
        int amount = 0;
        for (MonthYear my : innerMap.keySet()) {
            if (my.getYear() == date.getYear() && my.getMonth() == date.getMonth()) {
                replaceable = my;
                amount = innerMap.get(my);
                break;
            }
        }
        if (replaceable == null)
            return;

        innerMap.put(replaceable, amount + 1);
    }

    private String[] getAllStatusses() {
        String query = "SELECT value FROM statusses WHERE " +
                "(value = ? AND ?) OR " +
                "(value = ? AND ?) OR " +
                "(value = ? AND ?) OR " +
                "(value = ? AND ?) OR " +
                "(value = ? AND ?) OR " +
                "(value = ? AND ?);";
        try (PreparedStatement preparedStatement = DBHandler.INSTANCE.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, "Lost");
            preparedStatement.setBoolean(2, lost);
            preparedStatement.setString(3, "Found");
            preparedStatement.setBoolean(4, found);
            preparedStatement.setString(5, "Damaged");
            preparedStatement.setBoolean(6, damaged);
            preparedStatement.setString(7, "Handled");
            preparedStatement.setBoolean(8, handled);
            preparedStatement.setString(9, "Destroyed");
            preparedStatement.setBoolean(10, destroyed);
            preparedStatement.setString(11, "Depot");
            preparedStatement.setBoolean(12, depot);

            ResultSet set = preparedStatement.executeQuery();
            List<String> list = new ArrayList<>();
            while (set.next())
                list.add(set.getString(1));
            return list.toArray(new String[list.size()]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new String[0];
    }

    private int get(Map<MonthYear, Integer> map, LocalDate date) {
        for (MonthYear my : map.keySet())
            if (my.getYear() == date.getYear() && my.getMonth() == date.getMonth())
                return map.get(my) + 1;
        return 0;
    }

    public int get(Map<MonthYear, Integer> map, MonthYear monthYear) {
        for (MonthYear my : map.keySet())
            if (monthYear.getMonth() == my.getMonth() && monthYear.getYear() == my.getYear())
                return map.get(my);
        return 0;
    }
}

