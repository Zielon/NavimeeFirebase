package com.navimee.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.navimee.serializers.DateSerializer;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event implements Comparable {
    public String name;
    public String id;
    public String category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    @JsonSerialize(using = DateSerializer.class)
    public Date start_time;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    @JsonSerialize(using = DateSerializer.class)
    public Date end_time;

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
