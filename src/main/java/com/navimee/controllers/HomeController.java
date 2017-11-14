package com.navimee.controllers;

import com.navimee.contracts.models.places.Place;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.StringJoiner;

@RestController
public class HomeController {

    @Autowired
    PlacesRepository placesRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public String home() {

        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("All places");

        placesRepository.getAvailableCities()
                .forEach(c -> joiner.add(c.name + ": " + placesRepository.getPlaces(c.name, Place.class).size()));

        joiner.add("-----------------------");
        joiner.add("Foursquare");

        placesRepository.getAvailableCities()
                .forEach(c -> joiner.add(c.name + ": " + placesRepository.getFoursquarePlaces(c.name, Place.class).size()));

        return joiner.toString();
    }
}
