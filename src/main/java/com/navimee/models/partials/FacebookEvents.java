package com.navimee.models.partials;

import com.navimee.models.entities.Event;

import java.util.Map;

public class FacebookEvents {
    public Map<String, Event> events;
    public Map<String, Event> todayEvents;
    public Map<String, Event> tomorrowEvents;
    public Map<String, Event> dayAfterTomorrowEvents;
}
