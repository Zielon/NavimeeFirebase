package com.navimee.tasks;

import com.navimee.NavimeeApplication;
import com.navimee.contracts.repositories.FacebookRepository;
import com.navimee.contracts.services.FacebookService;
import com.navimee.models.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Scheduler {

    @Autowired
    FacebookService facebookService;

    @Autowired
    FacebookRepository facebookRepository;

    @Scheduled(fixedRate = 3000)
    public void updateTest() {

        NavimeeApplication.logs.add("Start -> " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));

        List<Event> events = new ArrayList<>();

        Event e = new Event();
        e.id = Integer.toString((int)(Math.random() * 100));
        e.start_time = new Date();

        Event e1 = new Event();
        e1.id = Integer.toString((int)(Math.random() * 100));
        e1.start_time = new Date();

        events.add(e);
        events.add(e1);

        facebookRepository.addEvents(events);

        NavimeeApplication.logs.add("End   -> " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
    }
}
