package com.navimee.events;

import com.navimee.contracts.models.events.Event;
import com.navimee.enums.EventsSegregation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Events {

    public static Map<String, List<Event>> sevenDaysSegregation(List<Event> events) {

        Map<String, List<Event>> segregated = new HashMap<>();

        for (EventsSegregation eventsSegregation : EventsSegregation.values()) {
            List<Event> filtered = events.stream().filter(eventsSegregation.getPredicate()).collect(Collectors.toList());
            segregated.put(eventsSegregation.toString(), filtered);
        }

        return segregated;
    }
}
