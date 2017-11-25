package com.navimee.places.services;

import com.navimee.configuration.specific.FacebookConfiguration;
import com.navimee.configuration.specific.FoursquareConfiguration;
import com.navimee.configuration.specific.GoogleConfiguration;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.places.PlacesService;
import com.navimee.models.entities.general.Coordinate;
import com.navimee.models.entities.places.FsPlaceDetails;
import com.navimee.models.entities.places.Place;
import com.navimee.models.externalDto.geocoding.GooglePlaceDto;
import com.navimee.models.externalDto.placeDetails.FsPlaceDetailsDto;
import com.navimee.models.externalDto.places.FbPlaceDto;
import com.navimee.models.externalDto.places.FsPlaceDto;
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
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.navimee.asyncCollectors.HelperMethods.*;
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

    @Override
    public Future saveFacebookPlaces(String city) {

        // Get data from the external facebook API
        List<Coordinate> coordinates = placesRepository.getCoordinates(city);
        FacebookPlacesQuery facebookPlacesQuery = new FacebookPlacesQuery(facebookConfiguration);
        List<Future<List<FbPlaceDto>>> futures =
                coordinates.stream()
                        .map(c -> facebookPlacesQuery.execute(new PlacesParams(c.getLatitude(), c.getLongitude())))
                        .collect(Collectors.toList());

        List<Place> entities = waitForMany(futures)
                .stream()
                .map(dto -> modelMapper.map(dto, Place.class))
                .filter(distinctByKey(Place::getId))
                .collect(Collectors.toList());

        // Save data to the repository
        return placesRepository.setFacebookPlaces(entities, city);
    }

    @Override
    public Future saveFoursquarePlaces(String city) {

        // Get data from the external foursquare API
        List<Coordinate> coordinates = placesRepository.getCoordinates(city);
        FoursquarePlacesQuery foursquarePlacesQuery = new FoursquarePlacesQuery(foursquareConfiguration);
        List<Future<List<FsPlaceDto>>> futures =
                coordinates.stream()
                        .map(c -> foursquarePlacesQuery.execute(new PlaceDetailsParams(c.getLatitude(), c.getLongitude(), "/venues/search")))
                        .collect(Collectors.toList());

        List<Place> entities = waitForMany(futures)
                .stream()
                .map(dto -> modelMapper.map(dto, Place.class))
                .filter(distinctByKey(Place::getId))
                .collect(Collectors.toList());

        // Save data to the repository
        return placesRepository.setFoursquarePlaces(entities, city);
    }

    @Override
    public Future saveFoursquarePlacesDetails(String city) {

        // Get data from the external foursquare API
        List<Place> places = placesRepository.getFoursquarePlaces(city);
        FoursquareDetailsQuery query = new FoursquareDetailsQuery(foursquareConfiguration);
        List<Future<FsPlaceDetailsDto>> futures = new ArrayList<>();
        places.forEach(p -> futures.add(query.execute(new PlaceDetailsParams("venues", p.getId()))));

        List<FsPlaceDetails> entities = waitForAll(futures)
                .stream()
                .map(dto -> modelMapper.map(dto, FsPlaceDetails.class))
                .filter(distinctByKey(FsPlaceDetails::getId))
                .filter(d -> d.getStatsCheckinsCount() > 500)
                .collect(Collectors.toList());

        // Save data to the repository
        return placesRepository.setFoursquarePlacesDetails(entities, city);
    }

    @Override
    public GooglePlaceDto downloadReverseGeocoding(Coordinate coordinate) {
        GoogleGeocodingQuery query = new GoogleGeocodingQuery(googleConfiguration);
        return waitForSingle(query.execute(new PlacesParams(coordinate.getLatitude(), coordinate.getLongitude())));
    }
}
