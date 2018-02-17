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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.navimee.asyncCollectors.CompletionCollector.sequence;
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
    public CompletableFuture<List<Void>> setCoordinates(Map<String, List<Coordinate>> coordinatesMap) {
        List<CompletableFuture<Void>> tasks = new ArrayList<>();
        coordinatesMap.forEach((city, value) -> {
            String path = new PathBuilder(1).add(COORDINATES).add(city).build();
            tasks.add(dbAdd.toCollection(database.collection(path), value));
        });
        return sequence(tasks);
    }

    @Override
    public CompletableFuture<List<Coordinate>> getCoordinates(String city) {
        String path = new PathBuilder(1).add(COORDINATES).add(city).build();
        return dbGet.fromCollection(database.collection(path), Coordinate.class);
    }

    @Override
    public CompletableFuture<List<City>> getAvailableCities() {
        String path = new PathBuilder(1).add(AVAILABLE_CITIES).build();
        return dbGet.fromCollection(database.collection(path), City.class);
    }

    @Override
    public CompletableFuture<List<Void>> setAvailableCities(List<City> cities) {
        List<CompletableFuture<Void>> tasks = new ArrayList<>();
        cities.forEach(city -> {
            String path = new PathBuilder(1).add(AVAILABLE_CITIES).add(city.getName()).build();
            tasks.add(dbAdd.toCollection(database.collection(path), city));
        });
        return sequence(tasks);
    }
}
