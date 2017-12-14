package com.navimee.repositories;

import com.navimee.contracts.repositories.PlacesRepository;
import com.navimee.enums.HotspotType;
import com.navimee.firestore.Database;
import com.navimee.firestore.operations.DbAdd;
import com.navimee.firestore.operations.DbDelete;
import com.navimee.firestore.operations.DbGet;
import com.navimee.firestore.operations.enums.AdditionEnum;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.coordinates.City;
import com.navimee.models.entities.coordinates.Coordinate;
import com.navimee.models.entities.places.facebook.FbPlace;
import com.navimee.models.entities.places.foursquare.FsPlace;
import com.navimee.models.entities.places.foursquare.FsPlaceDetails;
import com.navimee.repositories.inmemory.InMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.navimee.enums.CollectionType.*;

@Repository
public class PlacesRepositoryImpl implements PlacesRepository {

    @Autowired
    Database database;

    @Autowired
    ExecutorService executorService;

    @Autowired
    DbAdd add;

    @Autowired
    DbGet dbGet;

    @Autowired
    DbDelete delete;

    // SETTERS
    @Override
    public Future setFacebookPlaces(List<FbPlace> places, String city) {
        InMemoryRepository.SET(city, places, FbPlace.class);
        return add.toCollection(database.getCollection(FACEBOOK_PLACES, city), places);
    }

    @Override
    public Future setFoursquarePlaces(List<FsPlace> places, String city) {
        InMemoryRepository.SET(city, places, FsPlace.class);
        return add.toCollection(database.getCollection(FOURSQUARE_PLACES, city), places);
    }

    @Override
    public Future setFoursquarePlacesDetails(List<FsPlaceDetails> details) {
        InMemoryRepository.SET(details, FsPlaceDetails.class);
        return add.toCollection(database.getHotspot(), details);
    }

    @Override
    public Future setAvailableCities(List<City> cities) {
        return executorService.submit(() ->
                cities.forEach(city -> {
                    try {
                        add.toCollection(database.getCollection(AVAILABLE_CITIES, city.getName()), city).get();
                    } catch (Exception e) {
                        Logger.LOG(new Log(LogEnum.EXCEPTION, e));
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
                        Logger.LOG(new Log(LogEnum.EXCEPTION, e));
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
        List<FbPlace> places = InMemoryRepository.GET(city, FbPlace.class);
        if (places == null) {
            places = dbGet.fromCollection(database.getCollection(FACEBOOK_PLACES, city), FbPlace.class);
            InMemoryRepository.SET(city, places, FbPlace.class);
        }
        return places;
    }

    @Override
    public List<FsPlace> getFoursquarePlaces(String city) {
        List<FsPlace> places = InMemoryRepository.GET(city, FsPlace.class);
        if (places == null) {
            places = dbGet.fromCollection(database.getCollection(FOURSQUARE_PLACES, city), FsPlace.class);
            InMemoryRepository.SET(city, places, FsPlace.class);
        }
        return places;
    }

    @Override
    public List<FsPlaceDetails> getFoursquarePlacesDetails() {
        List<FsPlaceDetails> details = InMemoryRepository.GET(FsPlaceDetails.class);
        if (details == null) {
            details = dbGet.fromCollection(database.getHotspot().whereEqualTo("hotspotType", HotspotType.FOURSQUARE_PLACE), FsPlaceDetails.class);
            InMemoryRepository.SET(details, FsPlaceDetails.class);
        }
        return details;
    }

    @Override
    public List<Coordinate> getCoordinates(String city) {
        return dbGet.fromCollection(database.getCollection(COORDINATES, city), Coordinate.class);
    }

    @Override
    public List<City> getAvailableCities() {
        return dbGet.fromDocument(database.getDocument(AVAILABLE_CITIES), City.class);
    }
}
