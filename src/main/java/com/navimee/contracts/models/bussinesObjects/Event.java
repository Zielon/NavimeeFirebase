package com.navimee.contracts.models.bussinesObjects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.navimee.contracts.models.dataTransferObjects.Pojo;
import com.navimee.contracts.models.dataTransferObjects.events.EventDto;
import com.navimee.contracts.models.dataTransferObjects.places.PlaceDto;
import org.joda.time.DateTime;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event implements Comparable, Pojo {
    public String name;
    public String id;
    public String category;
    public DateTime start_time;
    public DateTime end_time;
    public long attending_count;
    public long maybe_count;
    public String type;
    public PlaceDto place;

    @JsonIgnore
    public PlaceDto searchPlace;

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
}
