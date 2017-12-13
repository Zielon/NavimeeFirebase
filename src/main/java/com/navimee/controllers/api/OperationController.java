package com.navimee.controllers.api;

import com.navimee.tasks.HotspotTask;
import com.navimee.tasks.NotificationsTask;
import com.navimee.tasks.RemovalTask;
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

    @Autowired
    NotificationsTask notificationsTask;

    @Autowired
    RemovalTask removeEventsTask;

    @Autowired
    HotspotTask hotspotTask;

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

    @RequestMapping(value = "notifications/send", method = RequestMethod.POST)
    public ResponseEntity<?> notificationsSend() {
        try {
            notificationsTask.sendNotification();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "remove/events", method = RequestMethod.POST)
    public ResponseEntity<?> removeFbEvents() {
        try {
            removeEventsTask.addRemoveEventsTask();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "hotspot/update", method = RequestMethod.POST)
    public ResponseEntity<?> updateHotspot() {
        try {
            hotspotTask.addHotspotTask();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
