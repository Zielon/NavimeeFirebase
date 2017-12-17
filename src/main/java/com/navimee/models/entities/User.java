package com.navimee.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Entity {
    private String id;
    private String token;
    private String email;
    private boolean bigEventsNotification;
    private boolean dayScheduleNotification;

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
