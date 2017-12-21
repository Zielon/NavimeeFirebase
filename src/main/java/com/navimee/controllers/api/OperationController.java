package com.navimee.controllers.api;

import com.navimee.tasks.*;
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
    FacebookEventsTask facebookEventsTask;

    @Autowired
    PredicHqEventsTasks predicHqEventsTasks;

    @Autowired
    NotificationsTask notificationsTask;

    @Autowired
    RemovalTask removeEventsTask;

    @Autowired
    HotspotTask hotspotTask;

    @RequestMapping(value = "update/facebookEvents", method = RequestMethod.POST)
    public ResponseEntity<?> updateFbEvents() {
        try {
            facebookEventsTask.executeEventsTask();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "update/predicthqEvents", method = RequestMethod.POST)
    public ResponseEntity<?> updatePhqEvents() {
        try {
            predicHqEventsTasks.executeEventsTask();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "update/hotspot", method = RequestMethod.POST)
    public ResponseEntity<?> updateHotspot() {
        try {
            hotspotTask.executeHotspotTask();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "notifications/send", method = RequestMethod.POST)
    public ResponseEntity<?> notificationsSend() {
        try {
            notificationsTask.executeSendNotification();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "remove/events", method = RequestMethod.POST)
    public ResponseEntity<?> removeFbEvents() {
        try {
            removeEventsTask.executeRemoveEventsTask();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
