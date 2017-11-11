package com.navimee.enums;

import com.navimee.contracts.models.events.Event;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import java.util.function.Predicate;

public enum EventsSegregation {
    FIRST_DAY((Event event) -> event.start_time.getDayOfMonth() == getCurrentTime().getDayOfMonth()),
    SECOND_DAY((Event event) -> event.start_time.getDayOfMonth() == getCurrentTime().plusDays(1).getDayOfMonth()),
    THIRD_DAY((Event event) -> event.start_time.getDayOfMonth() == getCurrentTime().plusDays(2).getDayOfMonth()),
    FOURTH_DAY((Event event) -> event.start_time.getDayOfMonth() == getCurrentTime().plusDays(3).getDayOfMonth()),
    FIFTH_DAY((Event event) -> event.start_time.getDayOfMonth() == getCurrentTime().plusDays(4).getDayOfMonth()),
    SIXTH_DAY((Event event) -> event.start_time.getDayOfMonth() == getCurrentTime().plusDays(5).getDayOfMonth()),
    SEVENTH_DAY((Event event) -> event.start_time.getDayOfMonth() == getCurrentTime().plusDays(6).getDayOfMonth());

    private final Predicate<Event> predicate;

    EventsSegregation(Predicate<Event> predicate) {
        this.predicate = predicate;
    }

    private static DateTime getCurrentTime() {
        DateTimeZone zone = DateTimeZone.forID("Europe/Warsaw");
        LocalDateTime warsawCurrent = LocalDateTime.now(zone);
        return warsawCurrent.toDateTime();
    }

    public Predicate<Event> getPredicate() {
        return predicate;
    }

    @Override
    public String toString() {
        return super.toString().split("_")[0].toLowerCase() + "Day";
    }
}
