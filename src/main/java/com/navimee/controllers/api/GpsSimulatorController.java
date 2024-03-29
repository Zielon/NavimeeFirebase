package com.navimee.controllers.api;

import com.google.cloud.firestore.GeoPoint;
import com.google.firebase.database.FirebaseDatabase;
import com.navimee.controllers.dto.CarDto;
import com.navimee.controllers.dto.SimulatorDto;
import com.navimee.gpsSimulator.Simulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

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
        Map<CarDto, Simulator> cars = new ConcurrentHashMap<>();
        List<String> corporations = new ArrayList<>();

        corporations.add("Uber");
        corporations.add("myTaxi");
        corporations.add("iTaxi");
        corporations.add("Taxi");

        firebaseDatabase.getReference(USER_LOCATION).removeValueAsync();

        for (int i = 0; i < dto.getCarCount(); i++) {
            int taxi = random.nextInt(4);
            String uid = UUID.randomUUID().toString();

            CarDto car = new CarDto();
            car.setDriverType(corporations.get(taxi));
            car.setUserId(corporations.get(taxi) + "_" + uid);
            car.setDelayTimeInMilliseconds(dto.getTimeInMilliseconds());

            GeoPoint point = getLocation(dto.getLongitude(), dto.getLatitude());
            Simulator simulator = new Simulator(point.getLatitude(), point.getLongitude(), firebaseDatabase);
            cars.put(car, simulator);
        }

        IntStream.range(0, dto.getSteps()).parallel().forEach(j -> cars.keySet().parallelStream().forEach(key -> cars.get(key).move(key)));
        firebaseDatabase.getReference(USER_LOCATION).removeValueAsync();
    }
}
