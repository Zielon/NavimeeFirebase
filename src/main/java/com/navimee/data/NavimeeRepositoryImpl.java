package com.navimee.data;

import com.google.firebase.database.DatabaseReference;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.navimee.configuration.FirebaseInitialization;
import com.navimee.contracts.repositories.NavimeeRepository;
import com.navimee.contracts.services.HttpClient;
import com.navimee.initializeData.Cities;
import com.navimee.initializeData.Coordinates;
import com.navimee.models.City;
import com.navimee.models.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;


@Repository
public class NavimeeRepositoryImpl implements NavimeeRepository {

    private final DatabaseReference dbContext = FirebaseInitialization.getDatabaseReference();

    @Autowired
    HttpClient httpClient;

    @Override
    public List<City> getCities() {
        List<City> cities = null;
        try {
            cities = httpClient.getFromFirebase(City.class, citiesPath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return cities;
    }

    @Override
    public List<Coordinate> getCoordinates() {
        List<Coordinate> coordinates = null;
        try {
            coordinates = httpClient.getFromFirebase(Coordinate.class, coordinatesPath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return coordinates;
    }

    @Override
    public void addCoordinates() {
        Coordinates.Get().forEach(c -> dbContext.child(coordinatesPath).push().setValueAsync(c));
    }

    @Override
    public void addCities() {
        Cities.Get().forEach(c -> dbContext.child(citiesPath).push().setValueAsync(c));
    }
}
