package com.navimee.models.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.firestore.GeoPoint;
import com.navimee.enums.EventType;
import com.navimee.enums.HotspotType;
import com.navimee.models.entities.contracts.Entity;
import com.navimee.models.entities.places.Place;

import java.util.Date;
import java.util.Map;

public class Event implements Entity {
    private String id;
    private String title;
    private String description;
    private String category;
    private String timezone;
    private int rank;
    private GeoPoint geoPoint;
    private Date startTime;
    private Date endTime;
    private HotspotType hotspotType;
    private EventType source;
    private Place place;

    @JsonProperty("geoPoint")
    private void getGeoPoint(Map<String, Double> json) {
        double lat = Double.parseDouble(json.get("latitude").toString());
        double lon = Double.parseDouble(json.get("longitude").toString());
        geoPoint = new GeoPoint(lat, lon);
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setTimezone(String timezone) {
        this.timezone = timezone;
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

    public void setHotspotType(HotspotType hotspotType) {
        this.hotspotType = hotspotType;
    }

    public EventType getSource() {
        return source;
    }

    public void setSource(EventType source) {
        this.source = source;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}
