package com.navimee.controllers.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.controllers.dto.CoordinateDto;
import com.navimee.models.entities.general.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(value = "api/coords")
public class CoodrinatesController {

    private ObjectMapper mapper = new ObjectMapper();

    private Random random = new Random();

    @Autowired
    PlacesRepository placesRepository;

    @RequestMapping(value = "actual/{city}", method = RequestMethod.GET, produces = "application/json")
    public String coordinates(@PathVariable("city") String city) throws JsonProcessingException {
        return mapper.writeValueAsString(placesRepository.getCoordinates(city.toUpperCase()));
    }

    @RequestMapping(value = "cities", method = RequestMethod.GET, produces = "application/json")
    public String availableCities() throws JsonProcessingException {
        return mapper.writeValueAsString(placesRepository.getAvailableCities());
    }

    @RequestMapping(value = "add/{city}", method = RequestMethod.POST)
    public ResponseEntity<?> addCoordinate(@PathVariable String city, @RequestBody CoordinateDto dto) {
        try {
            Coordinate coordinate = new Coordinate(dto.getLatitude(), dto.getLongitude());
            List<Integer> ids = placesRepository.getCoordinates(city.toUpperCase()).stream().map(c -> Integer.parseInt(c.getId())).collect(toList());
            coordinate.setId(Integer.toString(Collections.max(ids) + 1));
            placesRepository.addCoordinates(coordinate, city.toUpperCase()).get();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "delete/{city}/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> addCoordinate(@PathVariable String city, @PathVariable String id) {
        try {
            placesRepository.deleteCoordinates(id, city.toUpperCase()).get();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
