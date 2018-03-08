package com.navimee.controllers.dto;

public class CarDto {

    private String userId;
    private String driverType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDriverType() {
        return driverType;
    }

    public void setDriverType(String driverType) {
        this.driverType = driverType;
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        return userId.equals(((CarDto) object).userId);
    }
}