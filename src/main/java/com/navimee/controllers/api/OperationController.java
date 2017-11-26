package com.navimee.controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/operation")
public class OperationController {

    @RequestMapping(value = "update/places", method = RequestMethod.POST)
    public ResponseEntity<?> updatePlaces() {
        try{
           // placesTask.addPlacesTask();
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "update/events", method = RequestMethod.POST)
    public ResponseEntity<?> updateFbEvents() {
        try{
          //  eventsTask.addEventsTask();
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "update/segregate", method = RequestMethod.POST)
    public ResponseEntity<?> segregateFbEvents() {
        try{
          //  segregationTask.addSegregationTask();
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "update/4s/details", method = RequestMethod.POST)
    public ResponseEntity<?> fsPlacesDetails() {
        try{
          //  detailsTask.addDetailsTask();
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
