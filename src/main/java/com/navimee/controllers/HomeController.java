package com.navimee.controllers;

import com.navimee.contracts.models.places.FacebookPlace;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
public class HomeController {

    @Autowired
    PlacesRepository placesRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public String home() {
        return placesRepository.getPlaces("SOPOT", FacebookPlace.class)
                .stream()
                .map(c -> String.format("%s -> %s", c.name, c.id))
                .collect(Collectors.joining("\n"));
    }
}
