package com.navimee.controllers.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api")
public class FoursquareController {

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    PlacesRepository placesRepository;

    @RequestMapping(value = "4s/places/{city}", method = RequestMethod.GET, produces = "application/json")
    public String foursquarePlaces(@PathVariable("city") String city) throws JsonProcessingException {
        return mapper.writeValueAsString(placesRepository.getFoursquarePlaces(city.toUpperCase()));
    }

    @RequestMapping(value = "4s/details/{city}", method = RequestMethod.GET, produces = "application/json")
    public String foursquareDetailsPlaces(@PathVariable("city") String city) throws JsonProcessingException {
        return mapper.writeValueAsString(placesRepository.getFoursquarePlacesDetails(city.toUpperCase()));
    }
}
