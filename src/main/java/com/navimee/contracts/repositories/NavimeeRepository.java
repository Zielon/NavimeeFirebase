package com.navimee.contracts.repositories;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.navimee.entities.Coordinate;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface NavimeeRepository {
    List<String> getCities();
    List<Coordinate> getCoordinates() throws IOException, UnirestException, ExecutionException, InterruptedException;
    void AddCoordinates();
    void AddCities();
}
