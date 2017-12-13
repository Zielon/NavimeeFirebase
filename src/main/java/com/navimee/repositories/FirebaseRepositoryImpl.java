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

    private final String hotspotEvents = Paths.HOTSPOT + "_EVENTS";
    private final String hotspotPlaces = Paths.HOTSPOT + "_PLACES";

    @Override
    public Future transferEvents(List<FbEvent> events) {
        return executorService.submit(() -> {
            GeoFire geoFireForFiltered = new GeoFire(firebaseDatabase.getReference(hotspotEvents));
            GeoFire getFireAll  = new GeoFire(firebaseDatabase.getReference(Paths.HOTSPOT));

            Map<String, FbEvent> filtered = events.stream().filter(filterFbEvents()).collect(Collectors.toMap(Entity::getId, Function.identity()));
            Map<String, FbEvent> all = events.stream().collect(Collectors.toMap(Entity::getId, Function.identity()));

            filtered.forEach((key, v) -> geoFireForFiltered.setLocation(key, new GeoLocation(v.getPlace().getGeoPoint().getLatitude(), v.getPlace().getGeoPoint().getLongitude())));
            all.forEach((key, v) -> getFireAll.setLocation(key, new GeoLocation(v.getPlace().getGeoPoint().getLatitude(), v.getPlace().getGeoPoint().getLongitude())));
        });
    }

    @Override
    public Future transferPlaces(List<FsPlaceDetails> placeDetails) {
        return executorService.submit(() -> {
            GeoFire geoFireForFiltered = new GeoFire(firebaseDatabase.getReference(hotspotPlaces));
            GeoFire getFireAll  = new GeoFire(firebaseDatabase.getReference(Paths.HOTSPOT));

            Map<String, FsPlaceDetails> filtered = placeDetails.stream().filter(filterFsPopular()).collect(Collectors.toMap(Entity::getId, Function.identity()));
            Map<String, FsPlaceDetails> all = placeDetails.stream().collect(Collectors.toMap(Entity::getId, Function.identity()));

            filtered.forEach((key, v) -> geoFireForFiltered.setLocation(key, new GeoLocation(v.getLocationLat(), v.getLocationLng())));
            all.forEach((key, v) -> getFireAll.setLocation(key, new GeoLocation(v.getLocationLat(), v.getLocationLng())));
        });
    }

    @Override
    public Future deleteEvents(List<FbEvent> events) {
        return executorService.submit(() -> {
            events.forEach(event -> firebaseDatabase.getReference(String.format("%s/%s", Paths.HOTSPOT, event.getId())).removeValueAsync());
            events.forEach(event -> firebaseDatabase.getReference(String.format("%s/%s", hotspotEvents, event.getId())).removeValueAsync());
        });
    }

    @Override
    public Future deletePlaces(List<FsPlaceDetails> places) {
        return executorService.submit(
                () -> places.forEach(event -> firebaseDatabase.getReference(String.format("%s/%s", hotspotPlaces, event.getId())).removeValueAsync()));
    }
}
