package com.navimee.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.joda.time.DateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event implements Comparable {
    public String name;
    public String id;
    public String category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    public DateTime start_time;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    public DateTime end_time;

    public long attending_count;
    public long maybe_count;
    public String type;
    public Location place;

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        Event event = (Event) obj;
        return event.id.equals(this.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public int compareTo(Object o) {
        Event event = (Event) o;
        return event.id.compareTo(this.id);
    }
}
