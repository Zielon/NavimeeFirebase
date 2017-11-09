package com.navimee.places;

import com.navimee.contracts.models.places.Coordinate;

import java.util.*;

public class Coordinates {

    public static Map<String, List<Coordinate>> Get() {

        Map<String, List<Coordinate>> coordinates = new HashMap<>();

        coordinates.put("WARSAW",  new ArrayList<>(Arrays.asList(
                new Coordinate(52.231883, 21.005796),
                new Coordinate(52.240184, 21.03219),
                new Coordinate(52.223183, 21.042175),
                new Coordinate(52.217774, 21.013871))
        ));

        coordinates.put("GDANSK",  new ArrayList<>(Arrays.asList(
                new Coordinate(52.231883, 21.005796))
        ));

        coordinates.put("SOPOT",  new ArrayList<>(Arrays.asList(
                new Coordinate(52.231883, 21.005796))
        ));

        return coordinates;
    }
}
