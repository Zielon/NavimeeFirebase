package com.navimee.contracts.repositories;

import com.navimee.models.City;
import com.navimee.models.Coordinate;

import java.util.List;

public interface NavimeeRepository {
    List<City> getCities();
    List<Coordinate> getCoordinates();
    void addCoordinates();
    void addCities();
}
