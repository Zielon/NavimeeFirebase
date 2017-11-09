package com.navimee.events;

import com.navimee.contracts.models.events.Event;

import java.util.Map;

public class FacebookEvents {
    public Map<String, Event> events;
    public Map<String, Event> todayEvents;
    public Map<String, Event> tomorrowEvents;
    public Map<String, Event> dayAfterTomorrowEvents;
}
