package com.navimee.places.queries;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.navimee.configuration.specific.GoogleConfiguration;
import com.navimee.models.dto.geocoding.GooglePlaceDto;
import com.navimee.places.queries.params.PlacesParams;
import com.navimee.queries.Query;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class GoogleGeocodingQuery extends Query<GooglePlaceDto, GoogleConfiguration, PlacesParams> {

    public GoogleGeocodingQuery(GoogleConfiguration configuration, ExecutorService executorService) {
        super(configuration, executorService);
    }

    @Override
    public Future<GooglePlaceDto> execute(PlacesParams params) {
        Future<HttpResponse<JsonNode>> response =
                Unirest.get(configuration.apiUrl + "/geocode/json")
                        .queryString("latlng", params.lat + "," + params.lon)
                        .queryString("key", configuration.clientId)
                        .asJsonAsync();

        return executorService.submit(() -> map(response));
    }

    @Override
    protected GooglePlaceDto map(Future<HttpResponse<JsonNode>> future) {
        List<GooglePlaceDto> list = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JSONObject object =  future.get().getBody().getObject();
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
