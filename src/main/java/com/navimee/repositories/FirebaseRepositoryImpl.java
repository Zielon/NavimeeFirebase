package com.navimee.repositories;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.FirebaseDatabase;
import com.navimee.contracts.repositories.FirebaseRepository;
import com.navimee.firestore.Paths;
import com.navimee.models.entities.Entity;
import com.navimee.models.entities.events.FbEvent;
import com.navimee.models.entities.places.foursquare.FsPlaceDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.navimee.linq.HotspotFilters.filterFbEvents;
import static com.navimee.linq.HotspotFilters.filterFsPopular;

@Repository
public class FirebaseRepositoryImpl implements FirebaseRepository {

    @Autowired
    FirebaseDatabase firebaseDatabase;

    @Autowired
    ExecutorService executorService;

    @Override
    public Future transferEvents(List<FbEvent> events) {
        return executorService.submit(() -> {
            GeoFire geoFire = new GeoFire(firebaseDatabase.getReference(Paths.HOTSPOT + "_EVENTS"));
            Map<String, FbEvent> entityMap = events.stream().filter(filterFbEvents()).collect(Collectors.toMap(Entity::getId, Function.identity()));
            entityMap.forEach((key, v) ->
                    geoFire.setLocation(key, new GeoLocation(v.getPlace().getGeoPoint().getLatitude(), v.getPlace().getGeoPoint().getLongitude())));
        });
    }

    @Override
    public Future transferPlaces(List<FsPlaceDetails> placeDetails) {
        return executorService.submit(() -> {
            GeoFire geoFire = new GeoFire(firebaseDatabase.getReference(Paths.HOTSPOT + "_PLACES"));
            Map<String, FsPlaceDetails> entityMap = placeDetails.stream().filter(filterFsPopular()).collect(Collectors.toMap(Entity::getId, Function.identity()));
            entityMap.forEach((key, v) -> geoFire.setLocation(key, new GeoLocation(v.getLocationLat(), v.getLocationLng())));
        });
    }

    @Override
    public Future deleteEvents(List<FbEvent> events) {
        return executorService.submit(() -> events.forEach(event -> firebaseDatabase.getReference(event.getId()).removeValueAsync()));
    }
}
