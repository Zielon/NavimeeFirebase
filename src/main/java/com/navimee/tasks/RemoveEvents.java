package com.navimee.tasks;

import com.google.firebase.tasks.TaskCompletionSource;
import com.navimee.contracts.repositories.FacebookRepository;
import com.navimee.contracts.services.FacebookService;
import com.navimee.models.Event;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class RemoveEvents {

    @Autowired
    FacebookService facebookService;

    @Autowired
    FacebookRepository facebookRepository;

    @Scheduled(fixedRate = 1000 * 60 * 30)
    public void removeEvents() throws ExecutionException, InterruptedException {

        DateTimeZone zone = DateTimeZone.forID("Europe/Warsaw");
        LocalDateTime warsawCurrent = LocalDateTime.now(zone);

        List<Event> events = facebookRepository.getEvents().get();

   //     List<Event> eventsToRemove = facebookRepository.getEvents().stream()
    //            .filter(e -> warsawCurrent.toDate().after(e.end_time))
   //             .collect(Collectors.toList());

        facebookRepository.updateHistorical(events);
    }
}
