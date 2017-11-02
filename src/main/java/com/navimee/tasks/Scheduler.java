package com.navimee.tasks;

import com.navimee.contracts.repositories.FacebookRepository;
import com.navimee.contracts.services.FacebookService;
import com.navimee.models.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Scheduler {

    @Autowired
    FacebookService facebookService;

    @Autowired
    FacebookRepository facebookRepository;

    @Scheduled(fixedRate = 1000 * 60 * 30)
    public void updateEvents() {
        List<Event> events = facebookService.getEvents();
        facebookRepository.addEvents(events);
    }
}
