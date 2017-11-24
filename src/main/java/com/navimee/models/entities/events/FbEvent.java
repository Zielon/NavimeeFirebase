package com.navimee.models.entities.events;

import com.navimee.models.entities.places.Place;
import org.joda.time.DateTime;

public class FbEvent {
    private String name;
    private String id;
    private String category;
    private DateTime startTime;
    private DateTime endTime;
    private long attendingCount;
    private long maybeCount;
    private String type;
    private Place place;
    private Place searchPlace;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }

    public long getAttendingCount() {
        return attendingCount;
    }

    public void setAttendingCount(long attendingCount) {
        this.attendingCount = attendingCount;
    }

    public long getMaybeCount() {
        return maybeCount;
    }

    public void setMaybeCount(long maybeCount) {
        this.maybeCount = maybeCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Place getSearchPlace() {
        return searchPlace;
    }

    public void setSearchPlace(Place searchPlace) {
        this.searchPlace = searchPlace;
    }
}
