package com.navimee.contracts.repositories.palces;

import com.navimee.contracts.models.places.Coordinate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlacesRepository {

    String coordinatesPath = "coordinates";
    String availableCitiesPath = "availableCities";

    List<Coordinate> getCoordinates(String city);

    List<String> getAvailableCities();

    void setCoordinates();

    void setAvailableCities();
}
