package com.navimee.controllers.api;

import com.navimee.tasks.events.EventsTask;
import com.navimee.tasks.places.DetailsTask;
import com.navimee.tasks.places.PlacesTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/operation")
public class OperationController {

    @Autowired
    PlacesTask placesTask;

    @Autowired
    EventsTask eventsTask;

    @Autowired
    DetailsTask detailsTask;

    @RequestMapping(value = "update/places", method = RequestMethod.POST)
    public ResponseEntity<?> updatePlaces() {
        try {
            placesTask.addPlacesTask();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "update/events", method = RequestMethod.POST)
    public ResponseEntity<?> updateFbEvents() {
        try {
            eventsTask.addEventsTask();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "update/details", method = RequestMethod.POST)
    public ResponseEntity<?> fsPlacesDetails() {
        try {
            detailsTask.addDetailsTask();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
