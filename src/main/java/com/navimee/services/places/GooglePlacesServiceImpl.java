package com.navimee.services.places;

import com.navimee.configuration.specific.GoogleGeoConfiguration;
import com.navimee.contracts.services.HttpClient;
import com.navimee.contracts.services.places.GeoService;
import com.navimee.models.dto.geocoding.GooglePlaceDto;
import com.navimee.models.entities.coordinates.Coordinate;
import com.navimee.queries.places.GoogleGeocodingQuery;
import com.navimee.queries.places.params.PlacesParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
public class GooglePlacesServiceImpl implements GeoService<GooglePlaceDto> {

    @Autowired
    GoogleGeoConfiguration googleGeoConfiguration;

    @Autowired
    ExecutorService executorService;

    @Autowired
    HttpClient httpClient;

    @Override
    public CompletableFuture<GooglePlaceDto> downloadReverseGeocoding(Coordinate coordinate) {
        GoogleGeocodingQuery query = new GoogleGeocodingQuery(googleGeoConfiguration, executorService, httpClient);
        return CompletableFuture.supplyAsync(() ->
                query.execute(new PlacesParams(coordinate.getLatitude(), coordinate.getLongitude())).join());
    }
}
