package com.navimee.gpsSimulator;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.cloud.firestore.GeoPoint;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static com.navimee.firestore.FirebasePaths.USER_LOCATION;

public class Simulator {

    private static final double SIMULATOR_MOVEMENT_SPEED = 0.000075;
    private static final double ARRIVAL_RADIUS_IN_KM = 0.5 / 1000;
    private double initialLatitude;
    private double initialLongitude;
    private GeoPoint currentLocation;
    private int wayPointCounter = 0;
    private List<GeoPoint> points;
    private GeoFire geoFire;

    public Simulator(double initialLatitude, double initialLongitude, FirebaseDatabase firebaseDatabase) {
        this.initialLatitude = initialLatitude;
        this.initialLongitude = initialLongitude;
        this.points = PointsGenerator.generate(initialLongitude, initialLatitude);
        this.currentLocation = new GeoPoint(initialLatitude, initialLongitude);
        this.geoFire = new GeoFire(firebaseDatabase.getReference(USER_LOCATION));
    }

    public void move(String uid) {
        GeoPoint nextWayPoint = points.get(wayPointCounter);
        if (GeoHelper.calcGeoDistanceInKm(currentLocation, nextWayPoint) < ARRIVAL_RADIUS_IN_KM) {
            wayPointCounter++;
            if (wayPointCounter > points.size() - 1) {
                currentLocation = new GeoPoint(initialLatitude, initialLongitude);
                wayPointCounter = 0;
            }
            nextWayPoint = points.get(wayPointCounter);
        }

        double angle = GeoHelper.calcAngleBetweenGeoLocationsInRadians(currentLocation, nextWayPoint);
        double newLat = currentLocation.getLatitude() + Math.sin(angle) * SIMULATOR_MOVEMENT_SPEED;
        double newLon = currentLocation.getLongitude() + Math.cos(angle) * SIMULATOR_MOVEMENT_SPEED;

        currentLocation = new GeoPoint(newLat, newLon);
        geoFire.setLocation(uid, new GeoLocation(newLat, newLon), (locationKey, databaseError) -> {
        });
    }
}
