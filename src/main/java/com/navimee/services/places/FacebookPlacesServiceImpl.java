package com.navimee.services.places;

import com.navimee.configuration.Qualifiers;
import com.navimee.configuration.specific.FacebookConfiguration;
import com.navimee.contracts.repositories.places.CoordinatesRepository;
import com.navimee.contracts.repositories.places.PlacesRepository;
import com.navimee.contracts.services.HttpClient;
import com.navimee.contracts.services.places.PlacesService;
import com.navimee.logger.LogTypes;
import com.navimee.logger.Logger;
import com.navimee.models.dto.places.facebook.FbPlaceDto;
import com.navimee.models.entities.Log;
import com.navimee.models.entities.coordinates.Coordinate;
import com.navimee.models.entities.places.Place;
import com.navimee.models.entities.places.facebook.FbPlace;
import com.navimee.queries.places.FacebookPlacesQuery;
import com.navimee.queries.places.params.PlacesParams;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static com.navimee.asyncCollectors.Completable.sequence;
import static com.navimee.linq.Distinct.distinctByKey;
import static java.util.stream.Collectors.toList;

@Service
@Qualifier(Qualifiers.FACEBOOK)
public class FacebookPlacesServiceImpl implements PlacesService {

    @Autowired
    FacebookConfiguration facebookConfiguration;

    @Autowired
    CoordinatesRepository coordinatesRepository;

    @Autowired
    @Qualifier(Qualifiers.FACEBOOK)
    PlacesRepository<FbPlace> facebookRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ExecutorService executorService;

    @Autowired
    HttpClient httpClient;

    @Override
    public CompletableFuture<Void> savePlaces(String city) {

        List<Coordinate> coordinates = coordinatesRepository.getCoordinates(city).join();

        FacebookPlacesQuery facebookPlacesQuery =
                new FacebookPlacesQuery(facebookConfiguration, executorService, httpClient);

        List<CompletableFuture<List<FbPlaceDto>>> tasks = coordinates.stream()
                .map(c -> facebookPlacesQuery.execute(new PlacesParams(c.getLatitude(), c.getLongitude())))
                .collect(toList());

        return sequence(tasks).thenAcceptAsync(places -> {
            List<FbPlace> entities = places.stream()
                    .flatMap(Collection::stream)
                    .filter(Objects::nonNull)
                    .map(dto -> modelMapper.map(dto, FbPlace.class))
                    .filter(distinctByKey(Place::getId))
                    .collect(toList());

            facebookRepository.setPlaces(entities, city);

        }).thenRunAsync(() -> Logger.LOG(new Log(LogTypes.TASK, "Facebook places update for %s [FB]", city)));
    }
}
