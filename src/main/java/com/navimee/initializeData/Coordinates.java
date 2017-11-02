package com.navimee.initializeData;

import com.navimee.models.Coordinate;

import java.util.ArrayList;
import java.util.Arrays;

public class Coordinates {

    public static ArrayList<Coordinate> Get() {
        return new ArrayList<>(Arrays.asList(
                new Coordinate("WARSAW", "POLAND", 52.231883, 21.005796, "Palac Kultury"),
                new Coordinate("WARSAW", "POLAND", 52.240184, 21.03219, "Syrenka"),
                new Coordinate("WARSAW", "POLAND", 52.223183, 21.042175, "Most"),
                new Coordinate("WARSAW", "POLAND", 52.217774, 21.013871, "PWC"),
                new Coordinate("WARSAW", "POLAND", 52.21766, 20.984489, "Akademik"),
                new Coordinate("WARSAW", "POLAND", 52.208478, 21.009062, "SGH"),
                new Coordinate("WARSAW", "POLAND", 52.210337, 21.037276, "Podchorazych"),
                new Coordinate("WARSAW", "POLAND", 52.212088, 21.066101, "Bartycka"),
                new Coordinate("WARSAW", "POLAND", 52.201449, 21.042353, "Burdel"),
                new Coordinate("WARSAW", "POLAND", 52.194326, 21.018984, "Nic"),
                new Coordinate("WARSAW", "POLAND", 52.191661, 21.048233, "Woda"),
                new Coordinate("WARSAW", "POLAND", 52.177454, 21.066325, "Wrog"),
                new Coordinate("WARSAW", "POLAND", 52.164966, 21.087207, "Palac Sobieskich"))
        );
    }
}
