package com.navimee.contracts.repositories;

import com.navimee.models.City;
import com.navimee.models.Coordinate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NavimeeRepository {

    String coordinatesPath = "coordinates";
    String citiesPath = "cities";

    List<City> getCities();

    List<Coordinate> getCoordinates();

    void addCoordinates();

    void addCities();
}
