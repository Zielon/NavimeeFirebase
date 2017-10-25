package com.navimee.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.navimee.FirebaseConfiguration;
import com.navimee.contracts.repositories.NavimeeRepository;
import com.navimee.contracts.services.HttpClient;
import com.navimee.entities.Coordinate;
import com.navimee.firebaseInitData.Cities;
import com.navimee.firebaseInitData.Coordinates;
import com.navimee.services.HttpClientImpl;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;


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
    public List<String> getCities() {

        List<String> cities = new ArrayList<>();
        return cities;
    }

    @Override
    public List<Coordinate> getCoordinates() throws IOException, UnirestException, ExecutionException, InterruptedException {

        List<Coordinate> coordinates = httpClient.GetAsync("coordinates");

        return null;
    }

    @Override
    public void AddCoordinates() {
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
    public void AddCities() {
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
