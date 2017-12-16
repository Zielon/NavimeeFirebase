package com.navimee.repositories;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.FirebaseDatabase;
import com.navimee.contracts.repositories.FirebaseRepository;
import com.navimee.firestore.Paths;
import com.navimee.logger.LogEnum;
import com.navimee.logger.Logger;
import com.navimee.models.entities.Entity;
import com.navimee.models.entities.HotspotEvent;
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

@Repository
public class FirebaseRepositoryImpl implements FirebaseRepository {

    @Autowired
    FirebaseDatabase firebaseDatabase;

    @Autowired
    ExecutorService executorService;

    private final String hotspotCurrent = Paths.HOTSPOT + "_CURRENT";

    @Override
    public Future transferEvents(List<HotspotEvent> events) {
        return executorService.submit(() -> {
            GeoFire geoFire = new GeoFire(firebaseDatabase.getReference(Paths.HOTSPOT));
            Map<String, HotspotEvent> entities = events.stream().collect(Collectors.toMap(Entity::getId, Function.identity()));
            entities.forEach((key, v) -> geoFire.setLocation(key, new GeoLocation(v.getGeoPoint().getLatitude(), v.getGeoPoint().getLongitude())));
            Logger.LOG(new Log(LogEnum.TRANSFER, "Transfer facebook events details [Firebase]"));
        });
    }

    @Override
    public Future transferPlaces(List<FsPlaceDetails> placeDetails) {
        return executorService.submit(() -> {
            GeoFire geoFire = new GeoFire(firebaseDatabase.getReference(Paths.HOTSPOT));
            Map<String, FsPlaceDetails> entities = placeDetails.stream().collect(Collectors.toMap(Entity::getId, Function.identity()));
            entities.forEach((key, v) -> geoFire.setLocation(key, new GeoLocation(v.getLocationLat(), v.getLocationLng())));
            Logger.LOG(new Log(LogEnum.TRANSFER, "Transfer foursquare details [Firebase]"));
        });
    }

    @Override
    public void deleteCurrentHotspot() {
        try {
            firebaseDatabase.getReference(hotspotCurrent).removeValueAsync().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T extends Entity> Future filterAndTransfer(List<T> entities, Predicate<T> predicate, Function<T, GeoLocation> function) {
        return executorService.submit(() -> {
            Logger.LOG(new Log(LogEnum.TRANSFER, String.format("Transfer %s [Firebase]", entities.get(0).getClass().getSimpleName())));

            GeoFire geoFire = new GeoFire(firebaseDatabase.getReference(hotspotCurrent));
            Map<String, T> filtered = entities.stream().filter(predicate).collect(Collectors.toMap(Entity::getId, Function.identity()));
            filtered.forEach((key, value) -> geoFire.setLocation(key, function.apply(value)));
        });
    }

    @Override
    public Future deleteEvents(List<HotspotEvent> events) {
        return executorService.submit(() ->
                events.forEach(event -> firebaseDatabase.getReference(String.format("%s/%s", Paths.HOTSPOT, event.getId())).removeValueAsync()));
    }
}