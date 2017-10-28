package com.navimee.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
    public String name;
    public String id;
    public String category;
    public String start_time;
    public String end_time;
    public long attending_count;
    public long maybe_count;
    public String type;
    public Location place;

    @Override
    public boolean equals(Object obj) {
        Event event = (Event) obj;
        return event.id.equals(this.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
