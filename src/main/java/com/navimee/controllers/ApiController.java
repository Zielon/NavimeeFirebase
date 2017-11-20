package com.navimee.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navimee.contracts.models.places.FoursquarePlace;
import com.navimee.contracts.models.places.Place;
import com.navimee.contracts.repositories.events.EventsRepository;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api")
public class ApiController {

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    EventsRepository eventsRepository;

    @RequestMapping(value = "4s/places/{city}", method = RequestMethod.GET, produces = "application/json")
    public String foursquarePlaces(@PathVariable("city") String city) throws JsonProcessingException {
        return mapper.writeValueAsString(placesRepository.getFoursquarePlaces(city.toUpperCase(), FoursquarePlace.class));
    }

    @RequestMapping(value = "4s/details/{city}", method = RequestMethod.GET, produces = "application/json")
    public String foursquareDetailsPlaces(@PathVariable("city") String city) throws JsonProcessingException {
        return mapper.writeValueAsString(placesRepository.getFoursquarePlacesDetails(city.toUpperCase()));
    }

    @RequestMapping(value = "all/places/{city}", method = RequestMethod.GET, produces = "application/json")
    public String allPlaces(@PathVariable("city") String city) throws JsonProcessingException {
        return mapper.writeValueAsString(placesRepository.getPlaces(city.toUpperCase(), Place.class));
    }

    @RequestMapping(value = "events/{city}", method = RequestMethod.GET, produces = "application/json")
    public String events(@PathVariable("city") String city) throws JsonProcessingException {
        return mapper.writeValueAsString(eventsRepository.getEvents(city.toUpperCase()).stream().map(e -> e.toPojo()).collect(Collectors.toList()));
    }
}
