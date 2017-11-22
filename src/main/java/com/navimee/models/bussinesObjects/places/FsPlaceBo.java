package com.navimee.models.bussinesObjects.places;

public class FsPlaceBo extends PlaceBo {
    private String facebookId;

    public FsPlaceBo(String name, String id, double lat, double lon, String city, String address) {
        super(name, id, lat, lon, city, address);
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }
}
