package com.navimee.tasks;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.navimee.configuration.FirebaseInitialization;
import com.navimee.contracts.repositories.FacebookRepository;
import com.navimee.models.Event;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class RemoveEvents {

    @Autowired
    FacebookRepository facebookRepository;

    @Scheduled(fixedRate = 1000 * 60 * 30)
    public void removeEvents() throws ExecutionException, InterruptedException {

        DateTimeZone zone = DateTimeZone.forID("Europe/Warsaw");
        LocalDateTime warsawCurrent = LocalDateTime.now(zone);

        ValueEventListener listener = new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {

                if (!snapshot.exists()) return;

                List<Event> events = new ArrayList<>();
                snapshot.getChildren().forEach(e -> events.add(e.getValue(Event.class)));

                List<Event> eventsToRemove = events.stream()
                        .filter(e -> e.end_time == null || warsawCurrent.toDate().after(e.end_time))
                        .collect(Collectors.toList());

                facebookRepository.updateHistorical(eventsToRemove);
                facebookRepository.removeEvents(eventsToRemove);
            }

            @Override
            public void onCancelled(DatabaseError _) {
            }
        };

        FirebaseInitialization.getDatabaseReference()
                .child(FacebookRepository.eventsPath)
                .addListenerForSingleValueEvent(listener);
    }
}
