package com.navimee.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.navimee.models.entities.contracts.Entity;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Entity {
    private String id;
    private String token;
    private String email;
    private String name;
    private boolean bigEventsNotification;
    private boolean dayScheduleNotification;
    private boolean chatPrivateNotification;
    private boolean chatGroupNotification;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChatGroupNotification() {
        return chatGroupNotification;
    }

    public void setChatGroupNotification(boolean chatGroupNotification) {
        this.chatGroupNotification = chatGroupNotification;
    }

    public boolean isChatPrivateNotification() {
        return chatPrivateNotification;
    }

    public void setChatPrivateNotification(boolean chatPrivateNotification) {
        this.chatPrivateNotification = chatPrivateNotification;
    }
}