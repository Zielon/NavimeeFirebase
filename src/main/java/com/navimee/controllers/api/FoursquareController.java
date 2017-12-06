package com.navimee.controllers.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navimee.contracts.repositories.PlacesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/4s")
public class FoursquareController {

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    PlacesRepository placesRepository;

    @RequestMapping(value = "places/{city}", method = RequestMethod.GET, produces = "application/json")
    public String places(@PathVariable("city") String city) throws JsonProcessingException {
        return mapper.writeValueAsString(placesRepository.getFoursquarePlaces(city.toUpperCase()));
    }

    @RequestMapping(value = "details/{city}", method = RequestMethod.GET, produces = "application/json")
    public String details(@PathVariable("city") String city) throws JsonProcessingException {
        return mapper.writeValueAsString(placesRepository.getFoursquarePlacesDetails(city.toUpperCase()));
    }
}
