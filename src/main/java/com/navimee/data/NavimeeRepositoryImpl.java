package com.navimee.data;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.navimee.contracts.repositories.NavimeeRepository;
import com.navimee.contracts.services.HttpClient;
import com.navimee.entities.City;
import com.navimee.entities.Coordinate;
import com.navimee.initializeData.Cities;
import com.navimee.initializeData.Coordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;


@Repository
public class NavimeeRepositoryImpl implements NavimeeRepository {

    private static final String coordinatesPath = "coordinates";
    private static final String citiesPath = "cities";

    @Autowired
    @Qualifier("dbContext")
    DatabaseReference dbContext;

    @Autowired
    HttpClient httpClient;

    @Override
    public List<City> getCities() throws IOException, UnirestException {
        return httpClient.getFromFirebase(City.class, citiesPath);
    }

    @Override
    public List<Coordinate> getCoordinates() throws IOException, UnirestException{
        return httpClient.getFromFirebase(Coordinate.class, coordinatesPath);
    }

    @Override
    public void addCoordinates() {
        dbContext.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.hasChild(coordinatesPath)) {
                    Coordinates.Get().forEach(c -> dbContext.child(coordinatesPath).push().setValueAsync(c));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void addCities() {
        dbContext.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.hasChild(citiesPath)) {
                    Cities.Get().forEach(c -> dbContext.child(citiesPath).push().setValueAsync(c));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
