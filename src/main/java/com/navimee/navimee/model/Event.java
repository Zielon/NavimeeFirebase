package com.navimee.navimee.model;

/**
 * Created by Wojtek on 2017-10-23.
 */
public class Event {

    private Integer id;
    private String text;
    private String name;
    private String photoUrl;

    public Event() {
    }


    public Event(Integer id, String text, String name, String photoUrl) {
        this.id = id;
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public String toString() {
        return "FriendlyMessage{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", name='" + name + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}
