package com.navimee.places.repositories;

import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.firestore.Database;
import com.navimee.firestore.operations.Add;
import com.navimee.firestore.operations.Delete;
import com.navimee.firestore.operations.Get;
import com.navimee.firestore.operations.enums.AdditionEnum;
import com.navimee.models.entities.coordinates.City;
import com.navimee.models.entities.coordinates.Coordinate;
import com.navimee.models.entities.places.facebook.FbPlace;
import com.navimee.models.entities.places.foursquare.FsPlace;
import com.navimee.models.entities.places.foursquare.FsPlaceDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.navimee.enums.CollectionEnum.*;

@Repository
public class PlacesRepositoryImpl implements PlacesRepository {

    @Autowired
    Database database;

    @Autowired
    ExecutorService executorService;

    @Autowired
    Add add;

    @Autowired
    Get get;

    @Autowired
    Delete delete;

    // SETTERS
    @Override
    public Future setFacebookPlaces(List<FbPlace> places, String city) {
        InMemoryRepository.SET(city, places);
        return add.toCollection(database.getCollection(FACEBOOK_PLACES, city), places);
    }

    @Override
    public Future setFoursquarePlaces(List<FsPlace> places, String city) {
        return add.toCollection(database.getCollection(FOURSQUARE_PLACES, city), places);
    }

    @Override
    public Future setFoursquarePlacesDetails(List<FsPlaceDetails> details, String city) {
        return add.toCollection(database.getCollection(FOURSQUARE_PLACES_DETAILS, city), details);
    }

    @Override
    public Future setAvailableCities(List<City> cities) {
        return executorService.submit(() ->
                cities.forEach(city -> {
                    try {
                        add.toCollection(database.getCollection(AVAILABLE_CITIES, city.getName()), city).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));
    }

    @Override
    public Future setCoordinates(Map<String, List<Coordinate>> coordinatesMap) {
        return executorService.submit(() ->
                coordinatesMap.forEach((k, v) -> {
                    try {
                        add.toCollection(database.getCollection(COORDINATES, k), v).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));
    }

    @Override
    public Future addCoordinates(Coordinate coordinate, String city) {
        return add.toCollection(database.getCollection(COORDINATES, city), coordinate, AdditionEnum.MERGE);
    }

    // DELETE
    @Override
    public Future deleteCoordinates(String document, String city) {
        return executorService.submit(() -> delete.document(database.getDocument(COORDINATES).collection(city).document(document)));
    }

    // GETTERS
    @Override
    public List<FbPlace> getFacebookPlaces(String city) {
        List<FbPlace> places = InMemoryRepository.GET(city);
        if (places == null) {
            places = get.fromCollection(database.getCollection(FACEBOOK_PLACES, city), FbPlace.class);
            InMemoryRepository.SET(city, places);
        }
        return places;
    }

    @Override
    public List<FsPlace> getFoursquarePlaces(String city) {
        return get.fromCollection(database.getCollection(FOURSQUARE_PLACES, city), FsPlace.class);
    }

    @Override
    public List<FsPlaceDetails> getFoursquarePlacesDetails(String city) {
        return get.fromCollection(database.getCollection(FOURSQUARE_PLACES_DETAILS, city), FsPlaceDetails.class);
    }

    @Override
    public List<Coordinate> getCoordinates(String city) {
        return get.fromCollection(database.getCollection(COORDINATES, city), Coordinate.class);
    }

    @Override
    public List<City> getAvailableCities() {
        return get.fromDocument(database.getDocument(AVAILABLE_CITIES), City.class);
    }
}
