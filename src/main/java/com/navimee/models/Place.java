package com.navimee.models;

public class Place implements Comparable {
    public String name;
    public String id;
    public String category;

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
