package com.navimee.tasks;

import com.navimee.NavimeeApplication;
import com.navimee.contracts.repositories.FacebookRepository;
import com.navimee.contracts.services.FacebookService;
import com.navimee.models.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Scheduler {

    @Autowired
    FacebookService facebookService;

    @Autowired
    FacebookRepository facebookRepository;

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void updateEvents() {
        NavimeeApplication.logs.add("Start -> " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
        List<Event> events = facebookService.getEvents();
        facebookRepository.addEvents(events);
        NavimeeApplication.logs.add("End   -> " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
    }
}
