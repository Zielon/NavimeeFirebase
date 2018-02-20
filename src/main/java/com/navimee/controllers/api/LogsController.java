package com.navimee.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.firestore.Firestore;
import com.navimee.contracts.repositories.FirestoreRepository;
import com.navimee.firestore.FirebasePaths;
import com.navimee.models.entities.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(value = "api/logs")
public class LogsController {

    @Autowired
    FirestoreRepository firestoreRepository;

    @Autowired
    Firestore db;

    private ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value = "all", method = RequestMethod.GET, produces = "application/json")
    public String allLogs() throws Exception {
        List<Log> logs = db.collection(FirebasePaths.LOGS).get().get()
                .getDocuments()
                .stream()
                .map(document -> {
                    Log log = mapper.convertValue(document.getData(), Log.class);
                    log.setId(document.getId());
                    return log;
                })
                .sorted()
                .collect(toList());

        return mapper.writeValueAsString(logs);
    }

    @RequestMapping(value = "period", method = RequestMethod.GET, produces = "application/json")
    public String specificLogs(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate) throws Exception {
        List<Log> logs = db.collection(FirebasePaths.LOGS)
                .whereGreaterThan("time", startDate)
                .get().get().getDocuments()
                .stream()
                .map(document -> {
                    Log log = mapper.convertValue(document.getData(), Log.class);
                    log.setId(document.getId());
                    return log;
                })
                .sorted()
                .collect(toList());

        return mapper.writeValueAsString(logs);
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public ResponseEntity<?> delete() {
        try {
            firestoreRepository.deleteCollection(FirebasePaths.LOGS);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
