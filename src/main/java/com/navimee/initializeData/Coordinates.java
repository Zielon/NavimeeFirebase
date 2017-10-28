package com.navimee.initializeData;

import com.navimee.entities.Coordinate;

import java.util.ArrayList;
import java.util.Arrays;

public class Coordinates {

    public static ArrayList<Coordinate> Get(){
        return new ArrayList<>(Arrays.asList(
                new Coordinate("WARSAW", "POLAND", 52.231883, 21.005796, "Palac Kultury"),
                new Coordinate("WARSAW", "POLAND", 52.240184, 21.03219, "Syrenka"),
                new Coordinate("WARSAW", "POLAND", 52.223183, 21.042175, "Most"),
                new Coordinate("WARSAW", "POLAND", 52.217774, 21.013871, "PWC"),
                new Coordinate("WARSAW", "POLAND", 52.21766, 20.984489, "Akademik"),
                new Coordinate("WARSAW", "POLAND", 52.208478, 21.009062, "SGH"))
        );
    }
}
