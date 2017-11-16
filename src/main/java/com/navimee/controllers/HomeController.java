package com.navimee.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navimee.contracts.models.places.FoursquarePlace;
import com.navimee.contracts.models.places.Place;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.StringJoiner;

@RestController
public class HomeController {

    @Autowired
    PlacesRepository placesRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public String home() {
        return "Navimee...";
    }

    @RequestMapping(value = "/4s/places/{city}", method = RequestMethod.GET, produces = "application/json")
    public String foursquarePlaces(@PathVariable("city") String city) throws JsonProcessingException {
        return mapper.writeValueAsString(placesRepository.getFoursquarePlaces(city.toUpperCase(), FoursquarePlace.class));
    }

    @RequestMapping(value = "/4s/details/{city}", method = RequestMethod.GET, produces = "application/json")
    public String foursquareDetailsPlaces(@PathVariable("city") String city) throws JsonProcessingException {
        return mapper.writeValueAsString(placesRepository.getFoursquarePlacesDetails(city.toUpperCase()));
    }
}
