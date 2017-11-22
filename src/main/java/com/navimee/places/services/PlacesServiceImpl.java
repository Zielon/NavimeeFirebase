package com.navimee.places.services;

import com.navimee.configuration.specific.FacebookConfiguration;
import com.navimee.configuration.specific.FoursquareConfiguration;
import com.navimee.configuration.specific.GoogleConfiguration;
import com.navimee.contracts.repositories.palces.PlacesRepository;
import com.navimee.contracts.services.places.PlacesService;
import com.navimee.mappers.dtoToBo.FbPlaceDtoMapper;
import com.navimee.mappers.dtoToBo.FsPlaceDetailsDtoMapper;
import com.navimee.mappers.dtoToBo.FsPlaceDtoMapper;
import com.navimee.models.bussinesObjects.general.CoordinateBo;
import com.navimee.models.bussinesObjects.places.FsPlaceBo;
import com.navimee.models.bussinesObjects.places.FsPlaceDetailsBo;
import com.navimee.models.bussinesObjects.places.PlaceBo;
import com.navimee.models.entities.general.Coordinate;
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
import org.modelmapper.convention.MatchingStrategies;
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
    public List<PlaceBo> downloadFacebookPlaces(String city) {
        List<Coordinate> coordinates = placesRepository.getCoordinates(city);
        FacebookPlacesQuery facebookPlacesQuery = new FacebookPlacesQuery(facebookConfiguration);
        List<Future<List<FbPlaceDto>>> futures =
                coordinates.stream()
                        .map(c -> facebookPlacesQuery.execute(new PlacesParams(c.latitude, c.longitude)))
                        .collect(Collectors.toList());

        List<FbPlaceDto> placeDtos = waitForMany(futures);

        FbPlaceDto dto = placeDtos.get(0);

        PlaceBo placeBo = modelMapper.map(dto, PlaceBo.class);

        return null;/* p
                .stream()
                .filter(distinctByKey(PlaceBo::getId))
                .collect(Collectors.toList());*/
    }

    @Override
    public List<FsPlaceBo> downloadFoursquarePlaces(String city) {
        List<Coordinate> coordinates = placesRepository.getCoordinates(city);
        FoursquarePlacesQuery foursquarePlacesQuery = new FoursquarePlacesQuery(foursquareConfiguration);
        List<Future<List<FsPlaceDto>>> futures =
                coordinates.stream()
                        .map(c -> foursquarePlacesQuery.execute(new PlaceDetailsParams(c.latitude, c.longitude, "/venues/search")))
                        .collect(Collectors.toList());

        return waitForMany(futures)
                .stream()
                .map(FsPlaceDtoMapper.INSTANCE::toFsPlaceBo)
                .filter(distinctByKey(PlaceBo::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<FsPlaceDetailsBo> downloadFoursquarePlacesDetails(String city) {
        List<Place> places = placesRepository.getFoursquarePlaces(city);
        FoursquareDetailsQuery query = new FoursquareDetailsQuery(foursquareConfiguration);
        List<Future<FsPlaceDetailsDto>> futures = new ArrayList<>();
        places.forEach(p -> futures.add(query.execute(new PlaceDetailsParams("venues", p.id))));

        return waitForAll(futures).stream()
                .map(FsPlaceDetailsDtoMapper.INSTANCE::toFsPlaceDetailsBo)
                .filter(distinctByKey(FsPlaceDetailsBo::getId))
                .filter(d -> d.getStatsCheckinsCount() > 500)
                .collect(Collectors.toList());
    }

    @Override
    public GooglePlaceDto downloadReverseGeocoding(CoordinateBo coordinate) {
        GoogleGeocodingQuery query = new GoogleGeocodingQuery(googleConfiguration);
        return waitForSingle(query.execute(new PlacesParams(coordinate.getLatitude(), coordinate.getLongitude())));
    }
}
