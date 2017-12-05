package com.navimee.services;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.FirebaseDatabase;
import com.navimee.contracts.repositories.HotspotRepository;
import com.navimee.contracts.services.FirebaseService;
import com.navimee.firestore.Paths;
import com.navimee.models.entities.Entity;
import com.navimee.models.entities.events.FbEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FirebaseServiceImpl implements FirebaseService {

    @Autowired
    FirebaseDatabase firebaseDatabase;

    @Autowired
    ExecutorService executorService;

    @Autowired
    HotspotRepository hotspotRepository;

    @Override
    public Future transferEvents(List<FbEvent> events) {

        return executorService.submit(() -> {
            GeoFire geoFire = new GeoFire(firebaseDatabase.getReference(Paths.HOTSPOT));
            Map<String, FbEvent> entityMap = events.stream().collect(Collectors.toMap(Entity::getId, Function.identity()));

            entityMap.forEach((k, v) -> {
                geoFire.setLocation(k, new GeoLocation(v.getPlace().getGeoPoint().getLatitude(), v.getPlace().getGeoPoint().getLongitude()));
            });
        });
    }
}
