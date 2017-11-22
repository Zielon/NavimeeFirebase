package com.navimee.enums;

import com.navimee.models.bussinesObjects.events.FbEventBo;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import java.util.function.Predicate;

public enum EventsSegregation {
    FIRST_DAY((FbEventBo event) -> compareDates(event.getStartTime(), 0)),
    SECOND_DAY((FbEventBo event) -> compareDates(event.getStartTime(), 1)),
    THIRD_DAY((FbEventBo event) -> compareDates(event.getStartTime(), 2)),
    FOURTH_DAY((FbEventBo event) -> compareDates(event.getStartTime(), 3)),
    FIFTH_DAY((FbEventBo event) -> compareDates(event.getStartTime(), 4)),
    SIXTH_DAY((FbEventBo event) -> compareDates(event.getStartTime(), 5)),
    SEVENTH_DAY((FbEventBo event) -> compareDates(event.getStartTime(), 6));

    private final Predicate<FbEventBo> predicate;

    EventsSegregation(Predicate<FbEventBo> predicate) {
        this.predicate = predicate;
    }

    public Predicate<FbEventBo> getPredicate() {
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
