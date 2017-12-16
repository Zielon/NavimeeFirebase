package com.navimee.models.bo;

import com.google.cloud.firestore.GeoPoint;
import com.google.cloud.firestore.annotation.IgnoreExtraProperties;
import com.navimee.enums.EventType;
import com.navimee.enums.HotspotType;

import java.util.Date;

@IgnoreExtraProperties
public class PhqEvent {
    private String id;
    private String scope;
    private String title;
    private String description;
    private String category;
    private String timezone = "Europe/Warsaw";
    private int rank;
    private GeoPoint geoPoint;
    private Date startTime;
    private Date endTime;
    private HotspotType hotspotType = HotspotType.EVENT;
    private EventType source = EventType.PREDICT_HQ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTimezone() {
        return timezone;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public HotspotType getHotspotType() {
        return hotspotType;
    }

    public EventType getSource() {
        return source;
    }
}
