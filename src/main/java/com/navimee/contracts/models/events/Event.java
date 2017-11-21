package com.navimee.contracts.models.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.navimee.contracts.models.Pojo;
import com.navimee.contracts.models.events.pojo.EventPojo;
import com.navimee.contracts.models.places.Place;
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
    public Place place;

    @JsonIgnore
    public Place searchPlace;

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

    public EventPojo toPojo() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        EventPojo pojo = null;
        try {
            pojo = mapper.readValue(mapper.writeValueAsString(this), EventPojo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pojo;
    }
}
