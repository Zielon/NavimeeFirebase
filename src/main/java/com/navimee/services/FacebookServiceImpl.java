package com.navimee.services;

import com.navimee.asynchronous.Task;
import com.navimee.configuration.FacebookConfiguration;
import com.navimee.contracts.repositories.NavimeeRepository;
import com.navimee.contracts.services.FacebookService;
import com.navimee.models.Coordinate;
import com.navimee.models.Event;
import com.navimee.models.Place;
import com.navimee.queries.EventsQuery;
import com.navimee.queries.PlacesQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class FacebookServiceImpl implements FacebookService {

    @Autowired
    NavimeeRepository navimeeRepository;

    @Autowired
    FacebookConfiguration facebookConfiguration;

    @Override
    public List<Event> getEvents() {

        EventsQuery query = new EventsQuery(facebookConfiguration);
        List<Future<List<Event>>> tasks = new ArrayList<>();

        getPlaces().forEach(p -> {
            query.setId(p.id);
            tasks.add(query.execute());
        });

        return Task.waitForAll(tasks).stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<Place> getPlaces() {

        PlacesQuery query = new PlacesQuery(facebookConfiguration);
        List<Future<List<Place>>> tasks = new ArrayList<>();

        navimeeRepository.getCoordinates().forEach((Coordinate c) -> {
            query.setCoordinates(c.latitude, c.longitude);
            tasks.add(query.execute());
        });

        return Task.waitForAll(tasks).stream().distinct().collect(Collectors.toList());
    }
}
