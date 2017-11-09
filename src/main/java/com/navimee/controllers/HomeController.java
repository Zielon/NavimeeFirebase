package com.navimee.controllers;

import com.navimee.contracts.models.firestore.City;
import com.navimee.contracts.models.places.Coordinate;
import com.navimee.contracts.models.places.FacebookPlace;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.mockups.NavimeeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class HomeController {

    @Autowired
    PlacesRepository placesRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public String home() {

        NavimeeData navimeeData = new NavimeeData();
        List<City> cities = navimeeData.getCities();

        return cities//placesRepository.getPlaces("SOPOT", FacebookPlace.class)
                .stream()
                .map(c -> String.format("%s", c.name))
                .collect(Collectors.joining("\n"));
    }
}
