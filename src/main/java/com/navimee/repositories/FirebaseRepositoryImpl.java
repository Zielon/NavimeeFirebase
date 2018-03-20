package com.navimee.repositories;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.FirebaseDatabase;
import com.navimee.contracts.repositories.FirebaseRepository;
import com.navimee.firestore.PathBuilder;
import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Event;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.contracts.Entity;
import com.navimee.models.entities.places.foursquare.FsPlaceDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.navimee.firestore.FirebasePaths.HOTSPOT;
import static com.navimee.firestore.FirebasePaths.HOTSPOT_CURRENT;
import static com.navimee.utils.Converters.toMap;

@Repository
public class FirebaseRepositoryImpl implements FirebaseRepository {

    @Autowired
    FirebaseDatabase firebaseDatabase;

    @Autowired
    ExecutorService executorService;

    @Override
    public CompletableFuture<Void> transferEvents(List<Event> events) {
        return CompletableFuture.runAsync(() -> {
            GeoFire geoFire = new GeoFire(firebaseDatabase.getReference(HOTSPOT));
            Map<String, Event> entities = events.stream().collect(Collectors.toMap(Entity::getId, Function.identity()));

            entities.forEach((key, v) -> geoFire.setLocation(key,
                    new GeoLocation(v.getGeoPoint().getLatitude(), v.getGeoPoint().getLongitude()),
                    (locationKey, databaseError) ->
                            firebaseDatabase.getReference(new PathBuilder().add(HOTSPOT).add(locationKey).build())
                                    .updateChildrenAsync(toMap(v))
            ));
        }, executorService).thenRunAsync(() -> Logger.LOG(new Log(LogTypes.TRANSFER, "Transfer facebook events (%d) details [Firebase]", events.size())));
    }

    @Override
    public CompletableFuture<Void> transferPlaces(List<FsPlaceDetails> placeDetails) {
        return CompletableFuture.runAsync(() -> {
            GeoFire geoFire = new GeoFire(firebaseDatabase.getReference(HOTSPOT));
            Map<String, FsPlaceDetails> entities = placeDetails.stream().collect(Collectors.toMap(Entity::getId, Function.identity()));

            entities.forEach((key, v) -> geoFire.setLocation(key,
                    new GeoLocation(v.getLocationLat(), v.getLocationLng()), (locationKey, databaseError) ->
                            firebaseDatabase.getReference(new PathBuilder().add(HOTSPOT).add(locationKey).build())
                                    .updateChildrenAsync(toMap(v))
            ));
        }, executorService).thenRunAsync(() -> Logger.LOG(new Log(LogTypes.TRANSFER, "Transfer foursquare details (%d) [Firebase]", placeDetails.size())));
    }

    @Override
    public CompletableFuture<Void> deleteCurrentHotspot() {
        return CompletableFuture.runAsync(() -> {
            try {
                firebaseDatabase.getReference(HOTSPOT_CURRENT).removeValueAsync().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public <T extends Entity> CompletableFuture<Void> filterAndTransfer(List<T> entities, Predicate<T> predicate, Function<T, GeoLocation> function) {
        return CompletableFuture.runAsync(() -> {
            if (entities.isEmpty()) return;
            GeoFire geoFire = new GeoFire(firebaseDatabase.getReference(HOTSPOT_CURRENT));
            Map<String, T> filtered = entities.stream().filter(predicate).collect(Collectors.toMap(Entity::getId, Function.identity()));

            filtered.forEach((key, value) -> geoFire.setLocation(key, function.apply(value), (locationKey, databaseError) ->
                    firebaseDatabase.getReference(new PathBuilder().add(HOTSPOT_CURRENT).add(locationKey).build())
                            .updateChildrenAsync(toMap(value))));

        }, executorService).thenRunAsync(() -> Logger.LOG(new Log(LogTypes.TRANSFER, "Transfer %s [Firebase]", entities.get(0).getClass().getSimpleName())));
    }

    @Override
    public CompletableFuture<Void> deleteEvents(List<Event> events) {
        return CompletableFuture.runAsync(() -> {
            events.forEach(event -> firebaseDatabase.getReference(new PathBuilder().add(HOTSPOT).add(event.getId()).build()).removeValueAsync());
            events.forEach(event -> firebaseDatabase.getReference(new PathBuilder().add(HOTSPOT_CURRENT).add(event.getId()).build()).removeValueAsync());
        });
    }
}