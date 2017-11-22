package com.navimee.models.bussinesObjects.events;

import com.navimee.models.bussinesObjects.places.PlaceBo;
import org.joda.time.DateTime;

public class FbEventBo implements Comparable {
    private String name;
    private String id;
    private String category;
    private DateTime startTime;
    private DateTime endTime;
    private long attendingCount;
    private long maybeCount;
    private String type;
    private PlaceBo place;
    private PlaceBo searchPlace;

    @Override
    public boolean equals(Object obj) {
        FbEventBo event = (FbEventBo) obj;
        return event.id.equals(this.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public int compareTo(Object o) {
        FbEventBo event = (FbEventBo) o;
        return event.id.compareTo(this.id);
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

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime start_time) {
        this.startTime = start_time;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(DateTime end_time) {
        this.endTime = end_time;
    }

    public long getAttendingCount() {
        return attendingCount;
    }

    public void setAttendingCount(long attending_count) {
        this.attendingCount = attending_count;
    }

    public long getMaybeCount() {
        return maybeCount;
    }

    public void setMaybeCount(long maybe_count) {
        this.maybeCount = maybe_count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PlaceBo getPlace() {
        return place;
    }

    public void setPlace(PlaceBo place) {
        this.place = place;
    }

    public PlaceBo getSearchPlace() {
        return searchPlace;
    }

    public void setSearchPlace(PlaceBo searchPlace) {
        this.searchPlace = searchPlace;
    }
}
