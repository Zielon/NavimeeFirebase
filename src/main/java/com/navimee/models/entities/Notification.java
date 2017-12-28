package com.navimee.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.firestore.GeoPoint;
import com.navimee.models.entities.contracts.Entity;
import com.navimee.models.entities.contracts.FcmSendable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Notification implements Entity, FcmSendable {
    private String id;
    private String token;
    private String userId;
    private String hotspotType;
    private String title;
    private Date startTime;
    private Date endTime;
    private boolean isSent;
    private GeoPoint geoPoint;

    @JsonProperty("place")
    private void getGeoPoint(Map<String, Object> json) {
        HashMap point = ((HashMap) json.get("geoPoint"));
        double lat = Double.parseDouble(point.get("latitude").toString());
        double lon = Double.parseDouble(point.get("longitude").toString());
        geoPoint = new GeoPoint(lat, lon);
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHotspotType() {
        return hotspotType;
    }

    public void setHotspotType(String type) {
        this.hotspotType = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }
}
