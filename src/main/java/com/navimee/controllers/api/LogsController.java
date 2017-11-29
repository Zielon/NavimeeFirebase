package com.navimee.controllers.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.firestore.Firestore;
import com.navimee.configuration.specific.FirebaseInitialization;
import com.navimee.firestore.Paths;
import com.navimee.logger.Log;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(value = "api/logs")
public class LogsController {

    private Firestore db = FirebaseInitialization.firestore;
    private ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value = "all", method = RequestMethod.GET, produces = "application/json")
    public String places() throws Exception {
        List<Log> logs = db.collection(Paths.LOGS).get().get()
                .getDocuments()
                .stream()
                .map(document -> {
                    Log log = mapper.convertValue(document.getData(), Log.class);
                    log.setId(document.getId());
                    return log;
                }).collect(toList());

        return mapper.writeValueAsString(logs);
    }
}
