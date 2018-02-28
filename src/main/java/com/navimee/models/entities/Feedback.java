package com.navimee.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navimee.models.entities.contracts.Entity;
import com.navimee.models.entities.contracts.FcmSendable;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Feedback implements Entity, FcmSendable {
    private int durationInSec;
    private String locationName;
    private String locationAddress;
    private int distance;
    private String userId;
    private boolean sent;
    private String id;
    private int feedbackAnswer;

    @JsonIgnore
    private String token;

    public Feedback() {
        this.feedbackAnswer = -1;
    }

    public int getDurationInSec() {
        return durationInSec;
    }

    public void setDurationInSec(int durationInSec) {
        this.durationInSec = durationInSec;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Map<String, Object> toDictionary() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(this, Map.class);
    }

    public int getFeedbackAnswer() {
        return feedbackAnswer;
    }

    public void setFeedbackAnswer(int feedbackAnswer) {
        this.feedbackAnswer = feedbackAnswer;
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