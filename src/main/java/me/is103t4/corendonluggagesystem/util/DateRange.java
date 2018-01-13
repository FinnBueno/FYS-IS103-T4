package me.is103t4.corendonluggagesystem.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class DateRange implements Iterable<LocalDate> {

    private final LocalDate startDate;
    private final LocalDate endDate;

    public DateRange(LocalDate startDate, LocalDate endDate) {
        //check that range is valid (null, start < end)
        if (startDate == null)
            throw new IllegalArgumentException("StartDate cannot be null!");
        if (endDate == null)
            throw new IllegalArgumentException("EndDate cannot be null!");
        if (endDate.isBefore(startDate))
            throw new IllegalArgumentException("StartDate cannot be after EndDate!");
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public Iterator<LocalDate> iterator() {
        return stream().iterator();
    }

    private Stream<LocalDate> stream() {
        return Stream.iterate(startDate, d -> d.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startDate, endDate) + 1);
    }

    public List<LocalDate> toList() { //could also be built from the stream() method
        List<LocalDate> dates = new ArrayList<>();
        for (LocalDate d = startDate; !d.isAfter(endDate); d = d.plusMonths(1))
            dates.add(d);
        Collections.sort(dates);
        dates.remove(dates.size() - 1);
        return dates;
    }
}