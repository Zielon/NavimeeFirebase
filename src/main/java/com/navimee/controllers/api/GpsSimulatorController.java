package com.navimee.controllers.api;

import com.google.cloud.firestore.GeoPoint;
import com.google.firebase.database.FirebaseDatabase;
import com.navimee.controllers.dto.SimulatorDto;
import com.navimee.firestore.PathBuilder;
import com.navimee.gpsSimulator.Simulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.navimee.firestore.FirebasePaths.USER_LOCATION;
import static com.navimee.gpsSimulator.PointsGenerator.getLocation;

@RestController
@RequestMapping(value = "api/simulator")
public class GpsSimulatorController {

    @Autowired
    FirebaseDatabase firebaseDatabase;

    @RequestMapping(value = "gps", method = RequestMethod.POST)
    public void simulator(@RequestBody SimulatorDto dto) throws InterruptedException {
        Random random = new Random();
        Map<String, Simulator> cars = new HashMap<>();
        List<String> corporations = new ArrayList<>();

        corporations.add("Uber");
        corporations.add("myTaxi");
        corporations.add("iTaxi");
        corporations.add("Taxi");

        for (int i = 0; i < dto.getCarCount(); i++) {
            int taxi = random.nextInt(4);
            String uid = corporations.get(taxi) + "_USER_LOCATION_" + UUID.randomUUID().toString() + "-TEST";
            GeoPoint point = getLocation(dto.getLongitude(), dto.getLatitude());
            Simulator simulator = new Simulator(point.getLatitude(), point.getLongitude(), firebaseDatabase);
            cars.put(uid, simulator);
        }

        for (int j = 0; j < dto.getSteps(); j++) {
            try {
                cars.forEach((key, simulator) -> simulator.move(key));
                TimeUnit.MILLISECONDS.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        cars.forEach((key, simulator) ->
                firebaseDatabase.getReference(new PathBuilder().add(USER_LOCATION).add(key).build()).removeValueAsync());
    }
}
