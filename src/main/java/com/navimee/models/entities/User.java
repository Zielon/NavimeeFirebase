package com.navimee.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.navimee.models.entities.contracts.Entity;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Entity {
    private String email;
    private String id;
    private boolean online;
    private String name;
    private String avatar;
    private String token;
    private String city;
    private String country;

    //Settings
    private boolean dayScheduleNotification;
    private boolean bigEventsNotification;
    private boolean chatPrivateNotification;
    private boolean chatGroupNotification;
    private boolean shareLocalization;
    private String driverType;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isBigEventsNotification() {
        return bigEventsNotification;
    }

    public void setBigEventsNotification(boolean bigEventsNotification) {
        this.bigEventsNotification = bigEventsNotification;
    }

    public boolean isDayScheduleNotification() {
        return dayScheduleNotification;
    }

    public void setDayScheduleNotification(boolean dayScheduleNotification) {
        this.dayScheduleNotification = dayScheduleNotification;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChatGroupNotification() {
        return chatGroupNotification;
    }

    public void setChatGroupNotification(boolean chatGroupNotification) {
        this.chatGroupNotification = chatGroupNotification;
    }

    public boolean isChatPrivateNotification() {
        return chatPrivateNotification;
    }

    public void setChatPrivateNotification(boolean chatPrivateNotification) {
        this.chatPrivateNotification = chatPrivateNotification;
    }

    public boolean isShareLocalization() {
        return shareLocalization;
    }

    public void setShareLocalization(boolean shareLocalization) {
        this.shareLocalization = shareLocalization;
    }

    public String getDriverType() {
        return driverType;
    }

    public void setDriverType(String driverType) {
        this.driverType = driverType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}