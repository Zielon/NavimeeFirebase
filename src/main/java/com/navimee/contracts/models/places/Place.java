package com.navimee.contracts.models.places;

public abstract class Place implements Comparable {
    public String name;
    public String id;

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
