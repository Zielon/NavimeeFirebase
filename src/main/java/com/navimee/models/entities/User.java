package com.navimee.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Entity {
    private String id;
    private String token;
    private String email;
    private boolean bigEventsNotification;
    private boolean dayScheduleNotification;
    private List<Event> events;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public boolean isBigEventsNotification() {
        return bigEventsNotification;
    }

    public void setBigEventsNotification(boolean bigEventsNotification) {
        this.bigEventsNotification = bigEventsNotification;
    }

    public boolean isDayScheduleNotification() {
        return dayScheduleNotification;
    }

    public void setDayScheduleNotification(boolean dayScheduleNotification) {
        this.dayScheduleNotification = dayScheduleNotification;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
