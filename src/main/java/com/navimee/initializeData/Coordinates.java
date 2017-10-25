package com.navimee.initializeData;

import com.navimee.entities.Coordinate;

import java.util.ArrayList;
import java.util.Arrays;

public class Coordinates {

    public static ArrayList<Coordinate> Get(){

        return new ArrayList<>(Arrays.asList(
                new Coordinate("WARSAW", "POLAND", 52.240184, 21.03219, "Syrenka"),
                new Coordinate("WARSAW", "POLAND", 52.240184, 21.03219, "Syrenka"),
                new Coordinate("WARSAW", "POLAND", 52.240184, 21.03219, "Syrenka"))
        );
    }
}
