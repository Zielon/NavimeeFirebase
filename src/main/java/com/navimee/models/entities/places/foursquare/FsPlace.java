package com.navimee.models.entities.places.foursquare;

import com.navimee.models.entities.places.Place;

public class FsPlace extends Place {
    private String facebook;

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }
}
