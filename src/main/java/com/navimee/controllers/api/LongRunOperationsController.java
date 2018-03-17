package com.navimee.controllers.api;

import com.navimee.contracts.services.places.PlacesDetailsService;
import com.navimee.controllers.dto.FoursquareDetailsCity;
import com.navimee.tasks.PlacesDetailsTask;
import com.navimee.tasks.PlacesTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/longrunoperation")
public class LongRunOperationsController {

    @Autowired
    PlacesDetailsTask detailsTask;

    @Autowired
    PlacesTask placesTask;

    @Autowired
    PlacesDetailsService placesService;

    @RequestMapping(value = "update/foursquaredetails", method = RequestMethod.POST)
    public ResponseEntity<?> fsPlacesDetails() {
        detailsTask.executeDetailsTask();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "update/places", method = RequestMethod.POST)
    public ResponseEntity<?> updatePlaces() {
        placesTask.executePlacesTask();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "update/foursquaredetails/city", method = RequestMethod.POST)
    public ResponseEntity<?> updatePlacesCity(@RequestBody FoursquareDetailsCity dto) {
        placesService.savePlacesDetails(dto.getCity().toUpperCase());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
