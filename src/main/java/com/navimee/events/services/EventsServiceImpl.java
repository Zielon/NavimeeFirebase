package com.navimee.events.services;

import com.navimee.configuration.specific.FacebookConfiguration;
import com.navimee.contracts.models.events.Event;
import com.navimee.contracts.models.places.Place;
import com.navimee.contracts.services.events.EventsService;
import com.navimee.events.queries.EventsParams;
import com.navimee.events.queries.FacebookEventsQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.navimee.asynchronous.HelperMethods.waitForAll;

@Service
public class EventsServiceImpl implements EventsService {

    @Autowired
    FacebookConfiguration facebookConfiguration;

    @Override
    public List<Event> getFacebookEvents(List<Place> places) {

        FacebookEventsQuery query = new FacebookEventsQuery(facebookConfiguration);
        List<Future<List<Event>>> events = new ArrayList<>();

        places.forEach(p -> {
            EventsParams params = new EventsParams();
            params.id = p.id;
            events.add(query.execute(params));
        });

        return waitForAll(events).stream().distinct().collect(Collectors.toList());
    }
}
