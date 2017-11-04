package com.navimee.tasks;

import com.navimee.contracts.repositories.FacebookRepository;
import com.navimee.contracts.services.FacebookService;
import com.navimee.models.Event;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UpdateEvents {

    @Autowired
    FacebookService facebookService;

    @Autowired
    FacebookRepository facebookRepository;

    @Scheduled(fixedRate = 1000 * 60 * 30)
    public void updateEvents() {
        List<Event> events = new ArrayList<>(); // facebookService.getEvents();

        DateTimeZone zone = DateTimeZone.forID("Europe/Warsaw");
        LocalDateTime warsawCurrent = LocalDateTime.now(zone);

        Event event1 = new Event();
        event1.id = "1";
        event1.attending_count = (int)(Math.random() * 100);
        event1.end_time = warsawCurrent.minusDays(1).toDate();

        Event event2 = new Event();
        event2.id = "2";
        event2.attending_count = (int)(Math.random() * 100);
        event2.end_time = warsawCurrent.plusMonths(1).toDate();

        events.add(event1);
        events.add(event2);

        facebookRepository.updateEvents(events);
    }
}
