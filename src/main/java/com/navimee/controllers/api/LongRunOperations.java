package com.navimee.controllers.api;

import com.navimee.tasks.DetailsTask;
import com.navimee.tasks.PlacesTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/longrunoperation")
public class LongRunOperations {

    @Autowired
    DetailsTask detailsTask;

    @Autowired
    PlacesTask placesTask;

    @RequestMapping(value = "update/foursquaredetails", method = RequestMethod.POST)
    public ResponseEntity<?> fsPlacesDetails() {
        try {
            detailsTask.executeDetailsTask();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "update/places", method = RequestMethod.POST)
    public ResponseEntity<?> updatePlaces() {
        try {
            placesTask.executePlacesTask();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
