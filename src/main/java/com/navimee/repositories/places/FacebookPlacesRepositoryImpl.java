package com.navimee.repositories.places;

import com.google.cloud.firestore.Firestore;
import com.navimee.contracts.repositories.places.PlacesRepository;
import com.navimee.firestore.PathBuilder;
import com.navimee.firestore.operations.DbAdd;
import com.navimee.firestore.operations.DbGet;
import com.navimee.models.entities.places.facebook.FbPlace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.navimee.firestore.FirebasePaths.FACEBOOK_PLACES;

@Repository
@Qualifier("facebook")
public class FacebookPlacesRepositoryImpl implements PlacesRepository<FbPlace> {

    @Autowired
    Firestore database;

    @Autowired
    DbAdd add;

    @Autowired
    DbGet dbGet;

    @Override
    public CompletableFuture<Void> setPlaces(List<FbPlace> places, String city) {
        String path = new PathBuilder().add(FACEBOOK_PLACES).add(city).build();
        return add.toCollection(database.collection(path), places);
    }

    @Override
    public CompletableFuture<List<FbPlace>> getPlaces(String city) {
        String path = new PathBuilder().add(FACEBOOK_PLACES).add(city).build();
        return dbGet.fromCollection(database.collection(path), FbPlace.class);
    }
}