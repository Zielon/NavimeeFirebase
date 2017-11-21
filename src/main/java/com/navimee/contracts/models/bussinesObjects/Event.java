package com.navimee.contracts.models.bussinesObjects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.navimee.contracts.models.dataTransferObjects.Pojo;
import com.navimee.contracts.models.dataTransferObjects.events.EventDto;
import com.navimee.contracts.models.dataTransferObjects.places.PlaceDto;
import org.joda.time.DateTime;

import java.io.IOException;

public class Event implements Comparable, Pojo {
    private String name;
    private String id;
    private String category;
    private DateTime start_time;
    private DateTime end_time;
    private long attending_count;
    private long maybe_count;
    private String type;
    private PlaceDto place;

    @JsonIgnore
    public PlaceDto searchPlace;

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

    public EventDto toPojo() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        EventDto pojo = null;
        try {
            pojo = mapper.readValue(mapper.writeValueAsString(this), EventDto.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pojo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public DateTime getStart_time() {
        return start_time;
    }

    public void setStart_time(DateTime start_time) {
        this.start_time = start_time;
    }

    public DateTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(DateTime end_time) {
        this.end_time = end_time;
    }

    public long getAttending_count() {
        return attending_count;
    }

    public void setAttending_count(long attending_count) {
        this.attending_count = attending_count;
    }

    public long getMaybe_count() {
        return maybe_count;
    }

    public void setMaybe_count(long maybe_count) {
        this.maybe_count = maybe_count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PlaceDto getPlace() {
        return place;
    }

    public void setPlace(PlaceDto place) {
        this.place = place;
    }
}
