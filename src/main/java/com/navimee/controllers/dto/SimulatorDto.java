package com.navimee.controllers.dto;

public class SimulatorDto {
    private double latitude;
    private double longitude;
    private int carCount;
    private int steps;
    private int timeInMilliseconds;

    public int getCarCount() {
        return carCount;
    }

    public void setCarCount(int carCount) {
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

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getTimeInMilliseconds() {
        return timeInMilliseconds;
    }

    public void setTimeInMilliseconds(int timeInMilliseconds) {
        this.timeInMilliseconds = timeInMilliseconds;
    }
}
