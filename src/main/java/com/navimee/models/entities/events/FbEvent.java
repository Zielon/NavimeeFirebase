package com.navimee.models.entities.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.cloud.firestore.annotation.Exclude;
import com.google.cloud.firestore.annotation.IgnoreExtraProperties;
import com.navimee.models.entities.Entity;
import com.navimee.models.entities.places.Place;

@IgnoreExtraProperties
public class FbEvent implements Entity {
    private String name;
    private String id;
    private String category;
    private String startTime;
    private String endTime;
    private long attendingCount;
    private long maybeCount;
    private String type;
    private Place place;

    @Exclude
    @JsonIgnore
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

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
