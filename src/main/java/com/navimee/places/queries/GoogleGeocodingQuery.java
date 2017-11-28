package com.navimee.places.queries;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navimee.configuration.specific.GoogleConfiguration;
import com.navimee.contracts.services.HttpClient;
import com.navimee.models.dto.geocoding.GooglePlaceDto;
import com.navimee.places.queries.params.PlacesParams;
import com.navimee.queries.Query;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class GoogleGeocodingQuery extends Query<GooglePlaceDto, GoogleConfiguration, PlacesParams> {

    public GoogleGeocodingQuery(GoogleConfiguration configuration,
                                ExecutorService executorService,
                                HttpClient httpClient) {
        super(configuration, executorService, httpClient);
    }

    @Override
    public Callable<GooglePlaceDto> execute(PlacesParams params) {

        URI uri = null;
        try {
            URIBuilder builder = new URIBuilder(configuration.apiUrl);
            builder.setPath("/maps/api/geocode/json");
            builder.setParameter("latlng", params.lat + "," + params.lon);
            builder.setParameter("key", configuration.clientId);
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        URI finalUri = uri;
        return () -> map(httpClient.GET(finalUri), params);
    }

    @Override
    protected GooglePlaceDto map(Callable<JSONObject> task, PlacesParams params) {
        List<GooglePlaceDto> list = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JSONObject object = task.call();
            JSONArray array = object.getJSONArray("results");
            for (int n = 0; n < array.length(); n++) {
                JSONObject placeJson = array.getJSONObject(n);
                GooglePlaceDto mapped = mapper.readValue(placeJson.toString(), GooglePlaceDto.class);
                list.add(mapped);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list.size() > 0 ? list.get(0) : null;
    }
}
