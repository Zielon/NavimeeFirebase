package com.navimee.tasks;

import com.navimee.contracts.services.FirebaseService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class EventTasks {

    private static final Logger log = LoggerFactory.getLogger(EventTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    FirebaseService firebaseService;

    @Scheduled(fixedRate = 1000)
    public void reportCurrentTime() {
        firebaseService.saveEvents();
    }
}