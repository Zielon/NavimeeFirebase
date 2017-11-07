package com.navimee.tasks;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.navimee.configuration.FirebaseInitialization;
import com.navimee.contracts.repositories.FacebookRepository;
import com.navimee.models.entities.Event;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class RemoveWindowEvents {

    @Autowired
    FacebookRepository facebookRepository;

    //@Scheduled(fixedRate = 1000 * 60 * 10)
    public void removeWindowEvents() throws ExecutionException, InterruptedException {

        DateTimeZone zone = DateTimeZone.forID("Europe/Warsaw");
        LocalDateTime warsawCurrent = LocalDateTime.now(zone);

        ValueEventListener today = new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) return;
                List<Event> events = new ArrayList<>();
                snapshot.getChildren().forEach(e -> events.add(e.getValue(Event.class)));
            //    List<Event> eventsToRemove = events.stream().filter(e -> warsawCurrent.toDate().after(e.start_time)).collect(Collectors.toList());
            //    facebookRepository.updateHistorical(eventsToRemove);
            //    facebookRepository.removeEvents(eventsToRemove, FacebookRepository.todayEventsPath);
            }

            @Override
            public void onCancelled(DatabaseError _) {
            }
        };

        ValueEventListener tomorrow = new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) return;
                List<Event> events = new ArrayList<>();
                snapshot.getChildren().forEach(e -> events.add(e.getValue(Event.class)));
             //   List<Event> eventsToRemove = events.stream().filter(e -> warsawCurrent.plusDays(1).toDate().before(e.start_time)).collect(Collectors.toList());
            //    facebookRepository.updateHistorical(eventsToRemove);
             //   facebookRepository.removeEvents(eventsToRemove, FacebookRepository.tomorrowEventsPath);
            }

            @Override
            public void onCancelled(DatabaseError _) {
            }
        };

        ValueEventListener dayAfterTomorrow = new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) return;
                List<Event> events = new ArrayList<>();
                snapshot.getChildren().forEach(e -> events.add(e.getValue(Event.class)));
            //    List<Event> eventsToRemove = events.stream().filter(e -> warsawCurrent.plusDays(2).toDate().before(e.start_time)).collect(Collectors.toList());
      //          facebookRepository.updateHistorical(eventsToRemove);
       //         facebookRepository.removeEvents(eventsToRemove, FacebookRepository.dayAfterTomorrowEventsPath);
            }

            @Override
            public void onCancelled(DatabaseError _) {
            }
        };

        FirebaseInitialization.getDatabaseReference().child(FacebookRepository.todayEventsPath).addListenerForSingleValueEvent(today);
        FirebaseInitialization.getDatabaseReference().child(FacebookRepository.tomorrowEventsPath).addListenerForSingleValueEvent(tomorrow);
        FirebaseInitialization.getDatabaseReference().child(FacebookRepository.dayAfterTomorrowEventsPath).addListenerForSingleValueEvent(dayAfterTomorrow);
    }
}
