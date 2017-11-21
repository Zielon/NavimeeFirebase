package com.navimee.contracts.models.dataTransferObjects.places.subelement;

public class CoordinateDto {

    public Double latitude;
    public Double longitude;

    public CoordinateDto() {
    }

    public CoordinateDto(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
