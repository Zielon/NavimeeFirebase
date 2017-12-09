package com.navimee.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.navimee.models.entities.events.FbEvent;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Entity {
    private String token;
    private String email;
    private List<FbEvent> events;

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

    @Override
    public String getId() {
        return token;
    }

    public List<FbEvent> getEvents() {
        return events;
    }

    public void setEvents(List<FbEvent> events) {
        this.events = events;
    }
}
