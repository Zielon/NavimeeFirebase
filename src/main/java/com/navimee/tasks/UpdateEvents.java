package com.navimee.tasks;

import com.navimee.contracts.repositories.FacebookRepository;
import com.navimee.contracts.services.FacebookService;
import com.navimee.models.entities.Event;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdateEvents {

    @Autowired
    FacebookService facebookService;

    @Autowired
    FacebookRepository facebookRepository;

    //@Scheduled(fixedRate = 1000 * 60 * 30)
    public void updateEvents() {

        DateTimeZone zone = DateTimeZone.forID("Europe/Warsaw");
        LocalDateTime warsawCurrent = LocalDateTime.now(zone);

        List<Event> events = facebookService.getEvents();
        facebookRepository.updateEvents(events, FacebookRepository.eventsPath);
    }
}
