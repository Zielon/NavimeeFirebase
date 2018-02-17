package com.navimee.controllers.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navimee.contracts.repositories.places.CoordinatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;

@RestController
@RequestMapping(value = "api/coords")
public class CoodrinatesController {

    @Autowired
    CoordinatesRepository coordinatesRepository;
    private ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value = "actual/{city}", method = RequestMethod.GET, produces = "application/json")
    public Future<String> coordinates(@PathVariable("city") String city) {
        return coordinatesRepository.getCoordinates(city.toUpperCase()).thenApplyAsync(coordinates -> {
            try {
                return mapper.writeValueAsString(coordinates);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return "Error";
        });
    }

    @RequestMapping(value = "cities", method = RequestMethod.GET, produces = "application/json")
    public Future<String> availableCities() {
        return coordinatesRepository.getAvailableCities().thenApplyAsync(cities -> {
            try {
                return mapper.writeValueAsString(cities);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return "Error";
        });
    }
}
