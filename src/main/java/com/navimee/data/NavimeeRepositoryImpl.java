package com.navimee.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.firebase.database.DatabaseReference;
import com.navimee.configuration.FirebaseInitialization;
import com.navimee.contracts.repositories.NavimeeRepository;
import com.navimee.contracts.services.HttpClient;
import com.navimee.initializeData.AvailableCities;
import com.navimee.initializeData.Coordinates;
import com.navimee.models.entities.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@Repository
public class NavimeeRepositoryImpl implements NavimeeRepository {

    private final DatabaseReference dbContext = FirebaseInitialization.getDatabaseReference();

    @Autowired
    HttpClient httpClient;

    @Override
    public Map<String, List<Coordinate>> getCoordinates() {

        Map<String, List<Coordinate>> coordinates = null;
        TypeReference<HashMap<String, List<Coordinate>>> type = new TypeReference<HashMap<String, List<Coordinate>>>() {};

        try {
            coordinates = httpClient.getFromFirebase(type, coordinatesPath).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return coordinates;
    }

    @Override
    public List<String> getAvailableCities() {

        List<String> cities = null;
        TypeReference<List<String>> type = new TypeReference<List<String> >() {};

        try {
            cities = httpClient.getFromFirebase(type, availableCities).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return cities;
    }

    @Override
    public void addCoordinates() {
        dbContext.child(coordinatesPath).setValueAsync(Coordinates.Get());
    }

    @Override
    public void addAvailableCities() {
        dbContext.child(availableCities).setValueAsync(AvailableCities.Get());
    }
}
