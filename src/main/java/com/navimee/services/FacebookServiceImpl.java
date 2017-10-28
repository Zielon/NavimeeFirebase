package com.navimee.services;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.navimee.configuration.FacebookConfiguration;
import com.navimee.contracts.repositories.NavimeeRepository;
import com.navimee.contracts.services.FacebookService;
import com.navimee.contracts.services.HttpClient;
import com.navimee.models.Event;
import com.navimee.models.Place;
import com.navimee.queries.PlacesQuery;
import com.navimee.queries.QueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class FacebookServiceImpl implements FacebookService {

    @Autowired
    NavimeeRepository navimeeRepository;

    @Autowired
    FacebookConfiguration facebookConfiguration;

    private QueryFactory factory = new QueryFactory();

    @Override
    public List<Event> getEvents() {
        return null;
    }

    @Override
    public List<Place> getPlaces() {

        try {
            navimeeRepository.getCoordinates().forEach(c ->{
                PlacesQuery query = factory.getQuery(PlacesQuery.class);
                query.setCoordinates(c.getLatitude(), c.getLongitude());
                Future<List<Place>> q = query.get(facebookConfiguration.getAccessToken());

                try {
                    List<Place> l = q.get();
                    String s = "2";
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }
}
