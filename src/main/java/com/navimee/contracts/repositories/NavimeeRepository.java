package com.navimee.contracts.repositories;

import com.navimee.entities.City;
import com.navimee.entities.Coordinate;

import java.util.List;

public interface NavimeeRepository {
    List<City> getCities();
    List<Coordinate> getCoordinates();
    void addCoordinates();
    void addCities();
}
