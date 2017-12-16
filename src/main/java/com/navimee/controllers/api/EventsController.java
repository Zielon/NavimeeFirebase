package com.navimee.controllers.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navimee.contracts.repositories.EventsRepository;
import com.navimee.contracts.repositories.PlacesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/fb")
public class EventsController {

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    EventsRepository eventsRepository;

    @RequestMapping(value = "places/{city}", method = RequestMethod.GET, produces = "application/json")
    public String places(@PathVariable("city") String city) throws JsonProcessingException {
        return mapper.writeValueAsString(placesRepository.getFacebookPlaces(city.toUpperCase()));
    }

    @RequestMapping(value = "events", method = RequestMethod.GET, produces = "application/json")
    public String events() throws JsonProcessingException {
        return mapper.writeValueAsString(eventsRepository.getEvents());
    }
}
