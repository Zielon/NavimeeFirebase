package com.navimee.enums;

import com.navimee.contracts.models.bussinesObjects.Event;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import java.util.function.Predicate;

public enum EventsSegregation {
    FIRST_DAY((Event event) -> compareDates(event.start_time, 0)),
    SECOND_DAY((Event event) -> compareDates(event.start_time, 1)),
    THIRD_DAY((Event event) -> compareDates(event.start_time, 2)),
    FOURTH_DAY((Event event) -> compareDates(event.start_time, 3)),
    FIFTH_DAY((Event event) -> compareDates(event.start_time, 4)),
    SIXTH_DAY((Event event) -> compareDates(event.start_time, 5)),
    SEVENTH_DAY((Event event) -> compareDates(event.start_time, 6));

    private final Predicate<Event> predicate;

    EventsSegregation(Predicate<Event> predicate) {
        this.predicate = predicate;
    }

    public Predicate<Event> getPredicate() {
        return predicate;
    }

    private static DateTime getCurrentTime() {
        DateTimeZone zone = DateTimeZone.forID("Europe/Warsaw");
        LocalDateTime warsawCurrent = LocalDateTime.now(zone);
        return warsawCurrent.toDateTime();
    }

    private static boolean compareDates(DateTime startTime, int addDays) {
        DateTime currentTime = getCurrentTime().plusDays(addDays);

        return startTime.getDayOfMonth() == currentTime.getDayOfMonth()
                && startTime.getMonthOfYear() == currentTime.getMonthOfYear();
    }

    @Override
    public String toString() {
        return super.toString().split("_")[0].toLowerCase() + "Day";
    }
}
