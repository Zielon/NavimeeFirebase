package com.navimee.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navimee.models.entities.contracts.Entity;
import com.navimee.models.entities.contracts.FcmSendable;

import java.util.Date;
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
    private int rank;
    private boolean sent;
    private Double lat;
    private Double lon;

    @JsonProperty("place")
    private void getGeoPoint(Map<String, Object> json) {
        lat = Double.parseDouble(json.get("lat").toString());
        lon = Double.parseDouble(json.get("lon").toString());
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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    @Override
    public Map<String, Object> toDictionary() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(this, Map.class);
    }

    @Override
    public boolean isSent() {
        return sent;
    }

    @Override
    public void setSent(boolean sent) {
        this.sent = sent;
    }
}
