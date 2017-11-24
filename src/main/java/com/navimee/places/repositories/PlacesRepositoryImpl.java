package com.navimee.places.repositories;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.firestoreHelpers.EntitiesOperations;
import com.navimee.models.entities.general.City;
import com.navimee.models.entities.general.Coordinate;
import com.navimee.models.entities.places.FsPlaceDetails;
import com.navimee.models.entities.places.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

@Repository
public class PlacesRepositoryImpl implements PlacesRepository {

    @Autowired
    Firestore db;

    @Override
    public List<Place> getPlaces(String city) {
        return EntitiesOperations.getFromDocument(db.collection(placesPath).document(city), Place.class, placesPath);
    }

    @Override
    public List<Place> getFoursquarePlaces(String city) {
        return EntitiesOperations.getFromDocument(db.collection(foursquarePlacesPath).document(city), Place.class, placesPath);
    }

    @Override
    public List<FsPlaceDetails> getFoursquarePlacesDetails(String city) {
        return EntitiesOperations.getFromDocument(db.collection(foursquarePlacesDetailsPath).document(city), FsPlaceDetails.class, placesPath);
    }

    @Override
    public Future setPlaces(List<Place> places, String city) {
        CollectionReference collectionReference = db.collection(placesPath).document("byCity").collection(city);
        return EntitiesOperations.addToDocument(collectionReference, places, p -> p.getId());
    }

    @Override
    public Future setFoursquarePlaces(List<Place> places, String city) {
        CollectionReference collectionReference = db.collection(foursquarePlacesPath).document("byCity").collection(city);
        return EntitiesOperations.addToDocument(collectionReference, places, p -> p.getId());
    }

    @Override
    public Future setFoursquarePlacesDetails(List<FsPlaceDetails> details, String city) {
        CollectionReference collectionReference = db.collection(foursquarePlacesDetailsPath).document("byCity").collection(city);
        return EntitiesOperations.addToDocument(collectionReference, details, p -> p.getId());
    }

    @Override
    public Future deleteCollection(String collection) {
        return Executors.newSingleThreadExecutor().submit(() -> EntitiesOperations.deleteCollection(db.collection(collection)));
    }

    @Override
    public Future setAvailableCities(List<City> cities) {
        return Executors.newSingleThreadExecutor()
                .submit(() -> cities.forEach(city -> db.collection(availableCitiesPath).document(city.getName()).set(city)));
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
        return Executors.newSingleThreadExecutor()
                .submit(() -> coordinatesMap.keySet()
                        .forEach(city -> {
                            List<Coordinate> coordinates = coordinatesMap.get(city);
                            Map<String, Coordinate> map = IntStream.range(0, coordinates.size()).boxed().collect(toMap(i -> i.toString(), coordinates::get));
                            try {
                             //   EntitiesOperations.addToDocument(db.collection(coordinatesPath).document(city), map).get();
                            } catch (Exception e) {
                            }
                        }));
    }

    @Override
    public List<Coordinate> getCoordinates(String city) {
        return EntitiesOperations.getFromDocument(db.collection(coordinatesPath).document(city), Coordinate.class);
    }
}
