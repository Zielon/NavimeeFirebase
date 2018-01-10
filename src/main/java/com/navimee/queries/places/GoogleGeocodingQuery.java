package com.navimee.queries.places;

import com.navimee.configuration.specific.GoogleGeoConfiguration;
import com.navimee.contracts.services.HttpClient;
import com.navimee.general.JSON;
import com.navimee.models.dto.geocoding.GooglePlaceDto;
import com.navimee.queries.Query;
import com.navimee.queries.places.params.PlacesParams;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class GoogleGeocodingQuery extends Query<GooglePlaceDto, GoogleGeoConfiguration, PlacesParams> {

    public GoogleGeocodingQuery(GoogleGeoConfiguration configuration,
                                ExecutorService executorService,
                                HttpClient httpClient) {
        super(configuration, executorService, httpClient);
    }

    @Override
    public Callable<GooglePlaceDto> execute(PlacesParams params) {

        URI uri = null;
        try {
            URIBuilder builder = new URIBuilder(configuration.getApiUrl());
            builder.setPath("/maps/api/geocode/json");
            builder.setParameter("latlng", params.lat + "," + params.lon);
            builder.setParameter("key", configuration.getClientId());
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        URI finalUri = uri;
        return () -> map(httpClient.GET(finalUri), params);
    }

    @Override
    protected GooglePlaceDto map(Callable<JSONObject> task, PlacesParams params) {
        List<GooglePlaceDto> output = null;
        try {
            JSONObject object = task.call();
            output = JSON.arrayMapper(object.getJSONArray("results"), GooglePlaceDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output != null && output.size() > 0 ? output.get(0) : null;
    }
}
