package com.navimee.contracts.repositories;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.navimee.entities.City;
import com.navimee.entities.Coordinate;

import java.io.IOException;
import java.util.List;

public interface NavimeeRepository {
    List<City> getCities() throws IOException, UnirestException;
    List<Coordinate> getCoordinates() throws IOException, UnirestException;
    void addCoordinates();
    void addCities();
}
