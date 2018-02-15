package com.navimee.repositories;

import com.google.cloud.firestore.SetOptions;
import com.navimee.contracts.repositories.PlacesRepository;
import com.navimee.enums.HotspotType;
import com.navimee.firestore.Database;
import com.navimee.firestore.operations.DbAdd;
import com.navimee.firestore.operations.DbDelete;
import com.navimee.firestore.operations.DbGet;
import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Log;
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
        return add.toCollection(database.getCollectionByCity(FACEBOOK_PLACES, city), places, city);
    }

    @Override
    public Future setFoursquarePlaces(List<FsPlace> places, String city) {
        return add.toCollection(database.getCollectionByCity(FOURSQUARE_PLACES, city), places);
    }

    @Override
    public Future setFoursquarePlacesDetails(List<FsPlaceDetails> details) {
        return add.toCollection(database.getCollection(HOTSPOT), details);
    }

    @Override
    public Future setAvailableCities(List<City> cities) {
        return executorService.submit(() ->
                cities.forEach(city -> {
                    try {
                        add.toCollection(database.getCollectionByCity(AVAILABLE_CITIES, city.getName()), city).get();
                    } catch (Exception e) {
                        Logger.LOG(new Log(LogTypes.EXCEPTION, e));
                    }
                }));
    }

    @Override
    public Future setCoordinates(Map<String, List<Coordinate>> coordinatesMap) {
        return executorService.submit(() ->
                coordinatesMap.forEach((k, v) -> {
                    try {
                        add.toCollection(database.getCollectionByCity(COORDINATES, k), v).get();
                    } catch (Exception e) {
                        Logger.LOG(new Log(LogTypes.EXCEPTION, e));
                    }
                }));
    }

    @Override
    public Future addCoordinates(Coordinate coordinate, String city) {
        return add.toCollection(database.getCollectionByCity(COORDINATES, city), coordinate, SetOptions.merge());
    }

    // DELETE
    @Override
    public Future deleteCoordinates(String document, String city) {
        return executorService.submit(() -> delete.document(database.getDocumentByCity(COORDINATES).collection(city).document(document)));
    }

    // GETTERS
    @Override
    public List<FbPlace> getFacebookPlaces(String city) {
        return dbGet.fromCollection(database.getCollectionByCity(FACEBOOK_PLACES, city), FbPlace.class);
    }

    @Override
    public List<FsPlace> getFoursquarePlaces(String city) {
        return dbGet.fromCollection(database.getCollectionByCity(FOURSQUARE_PLACES, city), FsPlace.class);
    }

    @Override
    public List<FsPlaceDetails> getFoursquarePlacesDetails() {
        return dbGet.fromCollection(database.getCollection(HOTSPOT).whereEqualTo("hotspotType", HotspotType.FOURSQUARE_PLACE), FsPlaceDetails.class);
    }

    @Override
    public List<Coordinate> getCoordinates(String city) {
        return dbGet.fromCollection(database.getCollectionByCity(COORDINATES, city), Coordinate.class);
    }

    @Override
    public List<City> getAvailableCities() {
        return dbGet.fromDocument(database.getDocumentByCity(AVAILABLE_CITIES), City.class);
    }
}
