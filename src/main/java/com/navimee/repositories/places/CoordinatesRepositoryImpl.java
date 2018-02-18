package com.navimee.repositories.places;

import com.google.cloud.firestore.Firestore;
import com.navimee.contracts.repositories.places.CoordinatesRepository;
import com.navimee.firestore.PathBuilder;
import com.navimee.firestore.operations.DbAdd;
import com.navimee.firestore.operations.DbGet;
import com.navimee.models.entities.coordinates.City;
import com.navimee.models.entities.coordinates.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.navimee.firestore.FirebasePaths.AVAILABLE_CITIES;
import static com.navimee.firestore.FirebasePaths.COORDINATES;

@Repository
public class CoordinatesRepositoryImpl implements CoordinatesRepository {

    @Autowired
    Firestore database;

    @Autowired
    DbAdd dbAdd;

    @Autowired
    DbGet dbGet;

    @Override
    public CompletableFuture<Void> setCoordinates(Map<String, List<Coordinate>> coordinatesMap) {
        return CompletableFuture.runAsync(() -> coordinatesMap.forEach((city, value) -> {
            String path = new PathBuilder().add(COORDINATES).addCountry().add(city).build();
            dbAdd.toCollection(database.collection(path), value).join();
        }));
    }

    @Override
    public CompletableFuture<List<Coordinate>> getCoordinates(String city) {
        String path = new PathBuilder().add(COORDINATES).addCountry().add(city).build();
        return dbGet.fromCollection(database.collection(path), Coordinate.class);
    }

    @Override
    public CompletableFuture<List<City>> getAvailableCities() {
        String path = new PathBuilder().add(AVAILABLE_CITIES).addCountry().build();
        return dbGet.fromDocumentCollection(database.document(path), City.class);
    }

    @Override
    public CompletableFuture<Void> setAvailableCities(List<City> cities) {
        return CompletableFuture.runAsync(() -> cities.forEach(city -> {
            String path = new PathBuilder().add(AVAILABLE_CITIES).addCountry().add(city.getName()).build();
            dbAdd.toCollection(database.collection(path), city).join();
        }));
    }
}
