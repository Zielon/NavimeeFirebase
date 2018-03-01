package com.navimee.controllers.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navimee.contracts.repositories.places.PlacesDetailsRepository;
import com.navimee.models.entities.places.foursquare.FsPlace;
import com.navimee.models.entities.places.foursquare.FsPlaceDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;

@RestController
@RequestMapping(value = "api/4s")
public class FoursquareController {

    @Autowired
    @Qualifier("foursquare")
    PlacesDetailsRepository<FsPlaceDetails, FsPlace> foursquareRepository;
    private ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value = "places/{city}", method = RequestMethod.GET, produces = "application/json")
    public Future<String> places(@PathVariable("city") String city) throws JsonProcessingException {
        return foursquareRepository.getPlaces(city.toUpperCase()).thenApplyAsync(fsPlaces -> {
            try {
                return mapper.writeValueAsString(fsPlaces);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return "Error";
        });
    }

    @RequestMapping(value = "details", method = RequestMethod.GET, produces = "application/json")
    public Future<String> details() throws JsonProcessingException {
        return foursquareRepository.getPlacesDetails().thenApplyAsync(fsDetails -> {
            try {
                return mapper.writeValueAsString(fsDetails);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return "Error";
        });
    }
}
