package com.navimee.contracts.repositories;

import com.navimee.models.entities.Coordinate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface NavimeeRepository {

    String coordinatesPath = "coordinates";
    String availableCities = "availableCities";

    Map<String, List<Coordinate>> getCoordinates();

    List<String> getAvailableCities();

    void addCoordinates();

    void addAvailableCities();
}
