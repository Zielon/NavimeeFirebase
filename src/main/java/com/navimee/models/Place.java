package com.navimee.models;

public class Place {
    public String name;
    public String id;
    public String category;

    @Override
    public boolean equals(Object obj) {
        Place place = (Place)obj;
        return place.id.equals(this.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
