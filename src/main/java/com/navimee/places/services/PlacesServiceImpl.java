package com.navimee.places.services;

import com.navimee.configuration.specific.FacebookConfiguration;
import com.navimee.contracts.models.places.Coordinate;
import com.navimee.contracts.models.places.FacebookPlace;
import com.navimee.contracts.models.places.Place;
import com.navimee.contracts.services.places.PlacesService;
import com.navimee.places.queries.FacebookPlacesQuery;
import com.navimee.places.queries.PlacesParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.navimee.asynchronous.HelperMethods.waitForAll;

@Service
public class PlacesServiceImpl implements PlacesService {

    @Autowired
    FacebookConfiguration facebookConfiguration;

    @Override
    public List<Place> getFacebookPlaces(List<Coordinate> coordinates) {
        FacebookPlacesQuery facebookPlacesQuery = new FacebookPlacesQuery(facebookConfiguration);
        List<Future<List<FacebookPlace>>> futures = coordinates.stream().map(
                c -> {
                    PlacesParams params = new PlacesParams();
                    params.lat = c.latitude;
                    params.lon = c.longitude;
                    return facebookPlacesQuery.execute(params);
                }
        ).collect(Collectors.toList());

        return waitForAll(futures).stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<Place> getFoursquarePlaces(List<Coordinate> coordinates) {
        return null;
    }
}
