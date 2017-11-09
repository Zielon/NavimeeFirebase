package com.navimee.places.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.navimee.configuration.specific.FirebaseInitialization;
import com.navimee.contracts.models.firestore.City;
import com.navimee.contracts.models.firestore.Coordinates;
import com.navimee.contracts.models.places.Coordinate;
import com.navimee.contracts.models.places.Place;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class PlacesRepositoryImpl implements PlacesRepository {

    private Firestore db = FirebaseInitialization.getDatabaseReference();

    @Override
    public List<Coordinate> getCoordinates(String city) {
        ApiFuture<DocumentSnapshot> documentSnapshot = db.collection(coordinatesPath).document(city).get();
        DocumentSnapshot document = null;
        try {
            document = documentSnapshot.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return document.toObject(Coordinates.class).points;
    }

    @Override
    public List<City> getAvailableCities() {
        List<City> cities = null;
        ApiFuture<QuerySnapshot> query = db.collection(availableCitiesPath).get();
        try {
            cities = query.get().getDocuments().stream().map(c -> c.toObject(City.class)).collect(Collectors.toList());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return cities;
    }

    @Override
    public <T extends Place> List<T> getPlaces(String city, Class<T> type) {
        List<T> places = new ArrayList<>();
        ApiFuture<DocumentSnapshot> documentSnapshot = db.collection(placesPath).document(city).get();
        ObjectMapper mapper = new ObjectMapper();
        try {
            documentSnapshot.get().getData().forEach((k, v) -> places.add(mapper.convertValue(v, type)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return places;
    }

    @Override
    public void setCoordinates(Map<String, List<Coordinate>> coordinatesMap) {
        coordinatesMap.keySet().forEach(key -> {
            List<Coordinate> coordinates = coordinatesMap.get(key);
            Map<String, List<Coordinate>> elements = new HashMap<>();
            elements.put("points", coordinates);
            db.collection(coordinatesPath).document(key).set(elements);
        });
    }

    @Override
    public void setAvailableCities(List<City> cities) {
        cities.forEach(c -> db.collection(availableCitiesPath).document(c.name).set(c));
    }

    @Override
    public void setPlaces(List<Place> places, String city) {
        Map<String, Place> p = places.stream().collect(Collectors.toMap(Place::getId, Function.identity()));
        db.collection(placesPath).document(city).set(p);
    }
}
