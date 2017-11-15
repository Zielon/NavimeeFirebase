package com.navimee.tasks.places;

import com.navimee.asynchronous.HelperMethods;
import com.navimee.configuration.specific.FoursquareConfiguration;
import com.navimee.contracts.models.placeDetails.FoursquarePlaceDetails;
import com.navimee.contracts.models.places.FoursquarePlace;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.places.PlacesService;
import com.navimee.places.queries.FoursquareDetailsQuery;
import com.navimee.places.queries.params.PlacesParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
public class DetailsTask {

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    PlacesService placesService;

    @Autowired
    FoursquareConfiguration foursquareConfiguration;

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void addDetailsTask() throws ExecutionException, InterruptedException {

        List<FoursquarePlace> places = placesRepository.getFoursquarePlaces("GDANSK", FoursquarePlace.class);
        FoursquareDetailsQuery query = new FoursquareDetailsQuery(foursquareConfiguration);
        List<Future<FoursquarePlaceDetails>> futures = new ArrayList<>();

        places.forEach(p -> {
            futures.add(query.execute(new PlacesParams("venues", p.id)));
        });

        List<FoursquarePlaceDetails> detailes = HelperMethods.waitForAll(futures);

        detailes.size();

    }
}
