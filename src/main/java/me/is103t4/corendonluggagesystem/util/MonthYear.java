package me.is103t4.corendonluggagesystem.util;

import java.time.LocalDate;
import java.time.Month;

/**
 * Class to hold a month along with a year
 *
 * @author Finn Bon
 */
public class MonthYear implements Comparable<MonthYear> {

    private final Month month;
    private final int year;

    public MonthYear(LocalDate date) {
        this.month = date.getMonth();
        this.year = date.getYear();
    }

    @Override
    public int compareTo(MonthYear o) {
        LocalDate date1 = LocalDate.of(year, month.getValue(), 1);
        LocalDate date2 = LocalDate.of(o.year, o.month.getValue(), 1);
        return date1.compareTo(date2);
    }

    public Month getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
}
