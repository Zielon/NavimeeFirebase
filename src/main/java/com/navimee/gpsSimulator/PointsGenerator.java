package com.navimee.gpsSimulator;

import com.google.cloud.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PointsGenerator {

    public static final int RADIUS = 10000; // in meters

    public static List<GeoPoint> generate(double x0, double y0) {
        List<GeoPoint> points = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            points.add(getLocation(x0, y0));
        }

        return points;
    }

    public static GeoPoint getLocation(double x0, double y0) {
        Random random = new Random();

        // Convert radius from meters to degrees
        double radiusInDegrees = RADIUS / 111000f;

        double u = random.nextDouble();
        double v = random.nextDouble();
        double w = radiusInDegrees * Math.sqrt(u);
        double t = 2 * Math.PI * v;
        double x = w * Math.cos(t);
        double y = w * Math.sin(t);

        // Adjust the x-coordinate for the shrinking of the east-west distances
        double new_x = x / Math.cos(Math.toRadians(y0));

        double foundLongitude = new_x + x0;
        double foundLatitude = y + y0;

        return new GeoPoint(foundLatitude, foundLongitude);
    }
}
