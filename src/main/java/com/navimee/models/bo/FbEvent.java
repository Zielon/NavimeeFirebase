package com.navimee.models.bo;

import com.google.cloud.firestore.annotation.IgnoreExtraProperties;
import com.navimee.enums.EventType;
import com.navimee.enums.HotspotType;
import com.navimee.models.entities.places.facebook.FbPlace;

import java.util.Date;

@IgnoreExtraProperties
public class FbEvent {
    private String name;
    private String id;
    private String category;
    private Date startTime;
    private Date endTime;
    private long attendingCount;
    private long maybeCount;
    private String type;
    private FbPlace place;
    private FbPlace searchPlace;
    private HotspotType hotspotType = HotspotType.EVENT;
    private String timezone = "UTC";
    private EventType source = EventType.FACEBOOK;

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

    public FbPlace getPlace() {
        return place;
    }

    public void setPlace(FbPlace place) {
        this.place = place;
    }

    public FbPlace getSearchPlace() {
        return searchPlace;
    }

    public void setSearchPlace(FbPlace searchPlace) {
        this.searchPlace = searchPlace;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public HotspotType getHotspotType() {
        return hotspotType;
    }

    public String getTimezone() {
        return timezone;
    }

    public EventType getSource() {
        return source;
    }
}
