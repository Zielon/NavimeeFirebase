package com.navimee.repositories.places;

import com.google.cloud.firestore.Firestore;
import com.navimee.contracts.repositories.places.PlacesDetailsRepository;
import com.navimee.enums.HotspotType;
import com.navimee.firestore.PathBuilder;
import com.navimee.firestore.operations.DbAdd;
import com.navimee.firestore.operations.DbGet;
import com.navimee.models.entities.places.foursquare.FsPlace;
import com.navimee.models.entities.places.foursquare.FsPlaceDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.navimee.firestore.FirebasePaths.FOURSQUARE_PLACES;
import static com.navimee.firestore.FirebasePaths.HOTSPOT;

@Repository
@Qualifier("foursquare")
public class FoursquarePlacesRepositoryImpl implements PlacesDetailsRepository<FsPlaceDetails, FsPlace> {

    @Autowired
    Firestore database;

    @Autowired
    DbAdd add;

    @Autowired
    DbGet dbGet;

    @Override
    public CompletableFuture<Void> setPlacesDetails(List<FsPlaceDetails> placeDetails) {
        return add.toCollection(database.collection(HOTSPOT), placeDetails);
    }

    @Override
    public CompletableFuture<List<FsPlaceDetails>> getPlacesDetails() {
        return dbGet.fromQuery(database.collection(HOTSPOT).whereEqualTo("hotspotType", HotspotType.FOURSQUARE_PLACE), FsPlaceDetails.class);
    }

    @Override
    public CompletableFuture<List<FsPlace>> getPlaces(String city) {
        String path = new PathBuilder().add(FOURSQUARE_PLACES).add(city).build();
        return dbGet.fromCollection(database.collection(path), FsPlace.class);
    }

    @Override
    public CompletableFuture<Void> setPlaces(List<FsPlace> places, String city) {
        String path = new PathBuilder().add(FOURSQUARE_PLACES).add(city).build();
        return add.toCollection(database.collection(path), places);
    }
}