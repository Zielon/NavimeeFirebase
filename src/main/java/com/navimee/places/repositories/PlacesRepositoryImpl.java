package com.navimee.places.repositories;

import com.google.cloud.firestore.Firestore;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.firestore.operations.AdditionEnum;
import com.navimee.firestore.Database;
import com.navimee.firestore.operations.Add;
import com.navimee.firestore.operations.Delete;
import com.navimee.firestore.operations.Get;
import com.navimee.models.entities.general.City;
import com.navimee.models.entities.general.Coordinate;
import com.navimee.models.entities.places.foursquare.FsPlaceDetails;
import com.navimee.models.entities.places.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.navimee.enums.CollectionEnum.*;
import static com.navimee.firestore.Paths.BY_CITY;

@Repository
public class PlacesRepositoryImpl implements PlacesRepository {

    @Autowired
    Firestore db;

    @Autowired
    Database database;

    // SETTERS

    @Override
    public Future setFacebookPlaces(List<Place> places, String city) {
        return Add.toCollection(database.getCollection(FACEBOOK_PLACES, city), places);
    }

    @Override
    public Future setFoursquarePlaces(List<Place> places, String city) {
        return Add.toCollection(database.getCollection(FOURSQUARE_PLACES, city), places);
    }

    @Override
    public Future setFoursquarePlacesDetails(List<FsPlaceDetails> details, String city) {
        return Add.toCollection(database.getCollection(FOURSQUARE_PLACES_DETAILS, city), details);
    }

    @Override
    public Future setAvailableCities(List<City> cities) {
        return Executors.newSingleThreadExecutor()
                .submit(() -> cities.forEach(city -> {
                    try {
                        Add.toCollection(database.getCollection(AVAILABLE_CITIES, city.getName()), city).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));
    }

    @Override
    public Future setCoordinates(Map<String, List<Coordinate>> coordinatesMap) {
        return Executors.newSingleThreadExecutor().submit(() ->
                coordinatesMap.forEach((k, v) -> {
                    try {
                        Add.toCollection(database.getCollection(COORDINATES, k), v).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));
    }

    @Override
    public Future addCoordinates(Coordinate coordinate, String city) {
        return Add.toCollection(database.getCollection(COORDINATES, city), coordinate, AdditionEnum.MERGE);
    }

    // DELETE

    @Override
    public Future deleteCollection(String collection) {
        return Executors.newSingleThreadExecutor().submit(() ->
                getAvailableCities().forEach(city ->
                        Delete.collection(db.collection(collection).document(BY_CITY).collection(city.getName()))));
    }

    @Override
    public Future deleteCoordinates(String document, String city) {
        return Executors.newSingleThreadExecutor().submit(() ->
                Delete.document(database.getDocument(COORDINATES).collection(city).document(document)));
    }

    // GETTERS

    @Override
    public List<Place> getFacebookPlaces(String city) {
        return Get.fromCollection(database.getCollection(FACEBOOK_PLACES, city), Place.class);
    }

    @Override
    public List<Place> getFoursquarePlaces(String city) {
        return Get.fromCollection(database.getCollection(FOURSQUARE_PLACES, city), Place.class);
    }

    @Override
    public List<FsPlaceDetails> getFoursquarePlacesDetails(String city) {
        return Get.fromCollection(database.getCollection(FOURSQUARE_PLACES_DETAILS, city), FsPlaceDetails.class);
    }

    @Override
    public List<Coordinate> getCoordinates(String city) {
        return Get.fromCollection(database.getCollection(COORDINATES, city), Coordinate.class);
    }

    @Override
    public List<City> getAvailableCities() {
        return Get.fromDocument(database.getDocument(AVAILABLE_CITIES), City.class);
    }
}
