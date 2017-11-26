package com.navimee.places.services;

import com.navimee.configuration.specific.FacebookConfiguration;
import com.navimee.configuration.specific.FoursquareConfiguration;
import com.navimee.configuration.specific.GoogleConfiguration;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.places.PlacesService;
import com.navimee.models.dto.geocoding.GooglePlaceDto;
import com.navimee.models.dto.placeDetails.FsPlaceDetailsDto;
import com.navimee.models.dto.places.facebook.FbPlaceDto;
import com.navimee.models.dto.places.foursquare.FsPlaceDto;
import com.navimee.models.entities.general.Coordinate;
import com.navimee.models.entities.places.Place;
import com.navimee.models.entities.places.facebook.FbPlace;
import com.navimee.models.entities.places.foursquare.FsPlace;
import com.navimee.models.entities.places.foursquare.FsPlaceDetails;
import com.navimee.places.queries.FacebookPlacesQuery;
import com.navimee.places.queries.FoursquareDetailsQuery;
import com.navimee.places.queries.FoursquarePlacesQuery;
import com.navimee.places.queries.GoogleGeocodingQuery;
import com.navimee.places.queries.params.PlaceDetailsParams;
import com.navimee.places.queries.params.PlacesParams;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.navimee.asyncCollectors.CompletionCollector.waitForMany;
import static com.navimee.asyncCollectors.CompletionCollector.waitForSingle;
import static com.navimee.linq.Distinct.distinctByKey;

@Service
public class PlacesServiceImpl implements PlacesService {

    @Autowired
    FacebookConfiguration facebookConfiguration;

    @Autowired
    FoursquareConfiguration foursquareConfiguration;

    @Autowired
    GoogleConfiguration googleConfiguration;

    @Autowired
    PlacesRepository placesRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ExecutorService executorService;

    @Override
    public Future saveFacebookPlaces(String city) {

        List<Coordinate> coordinates = placesRepository.getCoordinates(city);

        // Get data from the external facebook API
        FacebookPlacesQuery facebookPlacesQuery = new FacebookPlacesQuery(facebookConfiguration, executorService);
        List<Future<List<FbPlaceDto>>> futures =
                coordinates.stream()
                        .map(c -> facebookPlacesQuery.execute(new PlacesParams(c.getLatitude(), c.getLongitude())))
                        .collect(Collectors.toList());

        // Collect
        List<Place> entities = waitForMany(futures)
                .stream()
                .map(dto -> modelMapper.map(dto, FbPlace.class))
                .filter(distinctByKey(Place::getId))
                .collect(Collectors.toList());

        // Save data to the repository
        return placesRepository.setFacebookPlaces(entities, city);
    }

    @Override
    public Future saveFoursquarePlaces(String city) {

        List<Coordinate> coordinates = placesRepository.getCoordinates(city);

        // Get data from the external foursquare API
        FoursquarePlacesQuery foursquarePlacesQuery = new FoursquarePlacesQuery(foursquareConfiguration, executorService);
        List<Future<List<FsPlaceDto>>> futures = coordinates.stream()
                        .map(c -> foursquarePlacesQuery.execute(new PlaceDetailsParams(c.getLatitude(), c.getLongitude(), "/venues/search")))
                        .collect(Collectors.toList());

        // Collect
        List<Place> entities = waitForMany(futures)
                .stream()
                .map(dto -> modelMapper.map(dto, FsPlace.class))
                .filter(distinctByKey(Place::getId))
                .collect(Collectors.toList());

        // Save data to the repository
        return placesRepository.setFoursquarePlaces(entities, city);
    }

    @Override
    public Future saveFoursquarePlacesDetails(String city) {

        List<FsPlace> places = placesRepository.getFoursquarePlaces(city);

        // Get data from the external foursquare API
        FoursquareDetailsQuery query = new FoursquareDetailsQuery(foursquareConfiguration, executorService);
        List<Future<FsPlaceDetailsDto>> futures = new ArrayList<>();
        places.forEach(p -> futures.add(query.execute(new PlaceDetailsParams("venues", p.getId()))));

        // Collect
        List<FsPlaceDetails> entities = waitForSingle(futures)
                .stream()
                .filter(Objects::nonNull)
                .map(dto -> modelMapper.map(dto, FsPlaceDetails.class))
                .filter(distinctByKey(FsPlaceDetails::getId))
                .filter(d -> d.getStatsCheckinsCount() > 500)
                .collect(Collectors.toList());

        // Save data to the repository
        return placesRepository.setFoursquarePlacesDetails(entities, city);
    }

    @Override
    public Future<GooglePlaceDto> downloadReverseGeocoding(Coordinate coordinate) {
        GoogleGeocodingQuery query = new GoogleGeocodingQuery(googleConfiguration, executorService);
        return query.execute(new PlacesParams(coordinate.getLatitude(), coordinate.getLongitude()));
    }
}
