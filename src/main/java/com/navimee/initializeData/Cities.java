package com.navimee.initializeData;

import com.navimee.models.City;

import java.util.ArrayList;
import java.util.Arrays;

public class Cities {
    public static ArrayList<City> Get(){
        return new ArrayList<>(Arrays.asList(new City("WARSAW"), new City("GDANSK"), new City("SOPOT")));
    }
}
