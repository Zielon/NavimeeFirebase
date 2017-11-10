package com.navimee.contracts.models.places;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Place implements Comparable {
    public String name;
    public String id;

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        Place place = (Place) obj;
        return place.id.equals(this.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public int compareTo(Object o) {
        Place place = (Place) o;
        return place.id.compareTo(this.id);
    }
}
