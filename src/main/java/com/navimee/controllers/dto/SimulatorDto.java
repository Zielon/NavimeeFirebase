package com.navimee.controllers.dto;

public class SimulatorDto {
    private double latitude;
    private double longitude;
    private double carCount;
    private double steps;

    public double getCarCount() {
        return carCount;
    }

    public void setCarCount(double carCount) {
        this.carCount = carCount;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getSteps() {
        return steps;
    }

    public void setSteps(double steps) {
        this.steps = steps;
    }
}
