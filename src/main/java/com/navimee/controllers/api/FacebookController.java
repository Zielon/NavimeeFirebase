package com.navimee.controllers.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navimee.contracts.repositories.EventsRepository;
import com.navimee.contracts.repositories.places.PlacesRepository;
import com.navimee.models.entities.places.facebook.FbPlace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;


@RestController
@RequestMapping(value = "api/events")
public class FacebookController {

    @Autowired
    @Qualifier("facebook")
    PlacesRepository<FbPlace> facebookRepository;
    @Autowired
    EventsRepository eventsRepository;
    private ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value = "places/{city}", method = RequestMethod.GET, produces = "application/json")
    public Future<String> places(@PathVariable("city") String city) {
        return facebookRepository.getPlaces(city.toUpperCase()).thenApplyAsync(fbPlaces -> {
            try {
                return mapper.writeValueAsString(fbPlaces);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return "Error";
        });
    }

    @RequestMapping(value = "all", method = RequestMethod.GET, produces = "application/json")
    public Future<String> events() {
        return eventsRepository.getEvents().thenApplyAsync(events -> {
            try {
                return mapper.writeValueAsString(events);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return "Error";
        });
    }
}
