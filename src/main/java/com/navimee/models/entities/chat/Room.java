package com.navimee.models.entities.chat;

import com.google.cloud.firestore.annotation.Exclude;
import com.google.cloud.firestore.annotation.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Room {
    private String admin;
    private String name;
    private String id;
    private boolean editable;
    private boolean advertisement;

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @Exclude
    public boolean isAdvertisement() {
        return advertisement;
    }

    @Exclude
    public void setAdvertisement(boolean advertisement) {
        this.advertisement = advertisement;
    }
}