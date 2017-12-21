package com.navimee.repositories;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.FirebaseDatabase;
import com.navimee.contracts.repositories.FirebaseRepository;
import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Entity;
import com.navimee.models.entities.Event;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.places.foursquare.FsPlaceDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.navimee.firestore.Paths.HOTSPOT_COLLECTION;
import static com.navimee.firestore.Paths.HOTSPOT_CURRENT_COLLECTION;

@Repository
public class FirebaseRepositoryImpl implements FirebaseRepository {

    @Autowired
    FirebaseDatabase firebaseDatabase;

    @Autowired
    ExecutorService executorService;

    @Override
    public Future transferEvents(List<Event> events) {
        return executorService.submit(() -> {
            GeoFire geoFire = new GeoFire(firebaseDatabase.getReference(HOTSPOT_COLLECTION));
            Map<String, Event> entities = events.stream().collect(Collectors.toMap(Entity::getId, Function.identity()));
            entities.forEach((key, v) -> geoFire.setLocation(key, new GeoLocation(v.getGeoPoint().getLatitude(), v.getGeoPoint().getLongitude())));
            Logger.LOG(new Log(LogTypes.TRANSFER, "Transfer facebook events details [Firebase]", events.size()));
        });
    }

    @Override
    public Future transferPlaces(List<FsPlaceDetails> placeDetails) {
        return executorService.submit(() -> {
            GeoFire geoFire = new GeoFire(firebaseDatabase.getReference(HOTSPOT_COLLECTION));
            Map<String, FsPlaceDetails> entities = placeDetails.stream().collect(Collectors.toMap(Entity::getId, Function.identity()));
            entities.forEach((key, v) -> geoFire.setLocation(key, new GeoLocation(v.getLocationLat(), v.getLocationLng())));
            Logger.LOG(new Log(LogTypes.TRANSFER, "Transfer foursquare details [Firebase]", placeDetails.size()));
        });
    }

    @Override
    public void deleteCurrentHotspot() {
        try {
            firebaseDatabase.getReference(HOTSPOT_CURRENT_COLLECTION).removeValueAsync().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T extends Entity> Future filterAndTransfer(List<T> entities, Predicate<T> predicate, Function<T, GeoLocation> function) {
        return executorService.submit(() -> {
            if (entities.isEmpty()) return;
            GeoFire geoFire = new GeoFire(firebaseDatabase.getReference(HOTSPOT_CURRENT_COLLECTION));
            Map<String, T> filtered = entities.stream().filter(predicate).collect(Collectors.toMap(Entity::getId, Function.identity()));
            Logger.LOG(new Log(LogTypes.TRANSFER, String.format("Transfer %s [Firebase]", entities.get(0).getClass().getSimpleName(), filtered.size())));
            filtered.forEach((key, value) -> geoFire.setLocation(key, function.apply(value)));
        });
    }

    @Override
    public Future deleteEvents(List<Event> events) {
        return executorService.submit(() -> {
            events.forEach(event -> firebaseDatabase.getReference(String.format("%s/%s", HOTSPOT_COLLECTION, event.getId())).removeValueAsync());
            events.forEach(event -> firebaseDatabase.getReference(String.format("%s/%s", HOTSPOT_CURRENT_COLLECTION, event.getId())).removeValueAsync());
        });
    }
}