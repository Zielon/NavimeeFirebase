package com.navimee.initializeData;

import com.navimee.models.entities.Coordinate;

import java.util.*;

public class Coordinates {

    public static Map<String, List<Coordinate>> Get() {

        Map<String, List<Coordinate>> coordinates = new HashMap<>();

        coordinates.put("WARSAW",  new ArrayList<>(Arrays.asList(
                new Coordinate(52.231883, 21.005796, "Palac Kultury"),
                new Coordinate(52.240184, 21.03219, "Syrenka"),
                new Coordinate(52.223183, 21.042175, "Most"),
                new Coordinate(52.217774, 21.013871, "PWC"),
                new Coordinate(52.21766, 20.984489, "Akademik"),
                new Coordinate(52.208478, 21.009062, "SGH"),
                new Coordinate(52.210337, 21.037276, "Podchorazych"),
                new Coordinate(52.212088, 21.066101, "Bartycka"),
                new Coordinate(52.201449, 21.042353, "Burdel"),
                new Coordinate(52.194326, 21.018984, "Nic"),
                new Coordinate(52.191661, 21.048233, "Woda"),
                new Coordinate(52.177454, 21.066325, "Wrog"),
                new Coordinate(52.164966, 21.087207, "Palac Sobieskich"))
        ));

        coordinates.put("GDANSK",  new ArrayList<>(Arrays.asList(
                new Coordinate(52.231883, 21.005796, "Test"))
        ));

        coordinates.put("SOPOT",  new ArrayList<>(Arrays.asList(
                new Coordinate(52.231883, 21.005796, "Test"))
        ));

        return coordinates;
    }
}
