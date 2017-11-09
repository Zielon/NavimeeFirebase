package com.navimee.contracts.repositories.palces;

import com.navimee.contracts.models.places.Coordinate;

import java.util.List;

public interface PlacesRepository {

    String coordinatesPath = "coordinates";
    String availableCitiesPath = "availableCities";

    List<Coordinate> getCoordinates(String city);

    List<String> getAvailableCities();

    void setCoordinates();

    void setAvailableCities();
}
