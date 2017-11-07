package com.navimee.services;

import com.navimee.asynchronous.HelperMethods;
import com.navimee.configuration.FacebookConfiguration;
import com.navimee.contracts.repositories.NavimeeRepository;
import com.navimee.contracts.services.FacebookService;
import com.navimee.models.entities.Coordinate;
import com.navimee.models.entities.Event;
import com.navimee.models.entities.Place;
import com.navimee.queries.EventsQuery;
import com.navimee.queries.PlacesQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

        getPlaces().parallelStream().forEach(p -> {
            query.setId(p.id);
            tasks.add(query.execute());
        });

        return HelperMethods.waitForAll(tasks).stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<Place> getPlaces() {

        PlacesQuery query = new PlacesQuery(facebookConfiguration);
        List<Future<List<Place>>> tasks = new ArrayList<>();

        Map<String, List<Coordinate>> coordinates = navimeeRepository.getCoordinates();

        coordinates.keySet().forEach(key ->
                coordinates.get(key).forEach((Coordinate c) -> {
                    query.setCoordinates(c.latitude, c.longitude);
                    tasks.add(query.execute());
        }));

        return HelperMethods.waitForAll(tasks).stream().distinct().collect(Collectors.toList());
    }
}
