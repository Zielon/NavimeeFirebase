package com.navimee.places.repositories;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.navimee.configuration.specific.FirebaseInitialization;
import com.navimee.contracts.models.firestore.City;
import com.navimee.contracts.models.firestore.Coordinates;
import com.navimee.contracts.models.placeDetails.FoursquarePlaceDetails;
import com.navimee.contracts.models.places.Coordinate;
import com.navimee.contracts.models.places.Place;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.firestoreHelpers.EntitiesOperations;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Repository
public class PlacesRepositoryImpl implements PlacesRepository {

    private Firestore db = FirebaseInitialization.getDatabaseReference();

    @Override
    public <T extends Place> List<T> getPlaces(String city, Class<T> type) {
        return EntitiesOperations.getFromDocument(db.collection(placesPath).document(city), type, placesChunks);
    }

    @Override
    public <T extends Place> List<T> getFoursquarePlaces(String city, Class<T> type) {
        return EntitiesOperations.getFromDocument(db.collection(foursquarePlacesPath).document(city), type, placesChunks);
    }

    @Override
    public List<FoursquarePlaceDetails> getFoursquarePlacesDetails(String city) {
        return EntitiesOperations.getFromDocument(db.collection(foursquarePlacesDetailsPath).document(city), FoursquarePlaceDetails.class, placesChunks);
    }

    @Override
    public Future setAvailableCities(List<City> cities) {
        return Executors.newSingleThreadExecutor()
                .submit(() -> cities.forEach(city -> db.collection(availableCitiesPath).document(city.name).set(city)));
    }

    @Override
    public Future setPlaces(List<? extends Place> places, String city) {
        DocumentReference targetDocument = db.collection(placesPath).document(city);
        return EntitiesOperations.addToDocument(targetDocument, places, p -> p.id, placesChunks);
    }

    @Override
    public Future setFoursquarePlaces(List<? extends Place> places, String city) {
        DocumentReference targetDocument = db.collection(foursquarePlacesPath).document(city);
        return EntitiesOperations.addToDocument(targetDocument, places, p -> p.id, placesChunks);
    }

    @Override
    public Future setFoursquarePlacesDetails(List<FoursquarePlaceDetails> details, String city) {
        DocumentReference targetDocument = db.collection(foursquarePlacesDetailsPath).document(city);
        return EntitiesOperations.addToDocument(targetDocument, details, p -> p.id, placesChunks);
    }

    @Override
    public Future deleteCollection(String collection) {
        return Executors.newSingleThreadExecutor().submit(
                () -> EntitiesOperations.deleteCollection(db.collection(collection), getAvailableCities(), placesChunks));
    }

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
    public Future setCoordinates(Map<String, List<Coordinate>> coordinatesMap) {

        return Executors.newSingleThreadExecutor().submit(
                () -> coordinatesMap.keySet().forEach(city -> {
                            List<Coordinate> coordinates = coordinatesMap.get(city);
                            Map<String, List<Coordinate>> elements = new HashMap<>();
                            elements.put("points", coordinates);
                            db.collection(coordinatesPath).document(city).set(elements);
                        }
                ));
    }
}
