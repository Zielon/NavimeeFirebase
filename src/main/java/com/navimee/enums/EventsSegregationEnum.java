package com.navimee.enums;

import com.navimee.models.entities.events.FbEvent;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.function.Predicate;

public enum EventsSegregationEnum {
    FIRST_DAY((FbEvent event) -> compareDates(event.getStartTime(), 0)),
    SECOND_DAY((FbEvent event) -> compareDates(event.getStartTime(), 1)),
    THIRD_DAY((FbEvent event) -> compareDates(event.getStartTime(), 2)),
    FOURTH_DAY((FbEvent event) -> compareDates(event.getStartTime(), 3)),
    FIFTH_DAY((FbEvent event) -> compareDates(event.getStartTime(), 4)),
    SIXTH_DAY((FbEvent event) -> compareDates(event.getStartTime(), 5)),
    SEVENTH_DAY((FbEvent event) -> compareDates(event.getStartTime(), 6));

    private final Predicate<FbEvent> predicate;

    EventsSegregationEnum(Predicate<FbEvent> predicate) {
        this.predicate = predicate;
    }

    public Predicate<FbEvent> getPredicate() {
        return predicate;
    }

    private static DateTime getCurrentTime() {
        DateTimeZone zone = DateTimeZone.forID("Europe/Warsaw");
        LocalDateTime warsawCurrent = LocalDateTime.now(zone);
        return warsawCurrent.toDateTime();
    }

    private static boolean compareDates(String startTime, int addDays) {
        // Facebook date format
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");
        DateTime currentTime = getCurrentTime().plusDays(addDays);
        DateTime startDateTime = formatter.parseDateTime(startTime);

        return startDateTime.getDayOfMonth() == currentTime.getDayOfMonth()
                && startDateTime.getMonthOfYear() == currentTime.getMonthOfYear();
    }

    @Override
    public String toString() {
        return super.toString().split("_")[0].toUpperCase() + "_DAY";
    }
}
