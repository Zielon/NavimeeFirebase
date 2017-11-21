package com.navimee.places.queries;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.navimee.configuration.specific.GoogleConfiguration;
import com.navimee.contracts.models.places.GooglePlace;
import com.navimee.places.queries.params.PlacesParams;
import com.navimee.queries.Query;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GoogleGeocodingQuery extends Query<GooglePlace, GoogleConfiguration, PlacesParams> {

    public GoogleGeocodingQuery(GoogleConfiguration configuration) {
        super(configuration);
    }

    @Override
    public Future<GooglePlace> execute(PlacesParams params) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<HttpResponse<JsonNode>> response =
                Unirest.get(configuration.apiUrl + "/geocode/json")
                        .queryString("latlng", params.lat + "," + params.lon)
                        .queryString("key", configuration.clientId)
                        .asJsonAsync();

        return executor.submit(() -> map(response.get().getBody().getObject()));
    }

    @Override
    protected GooglePlace map(JSONObject object) {
        JSONArray array = object.getJSONArray("results");
        List<GooglePlace> list = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for (int n = 0; n < array.length(); n++) {
            JSONObject placeJson = array.getJSONObject(n);
            try {
                GooglePlace mapped = mapper.readValue(placeJson.toString(), GooglePlace.class);
                list.add(mapped);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list.size() > 0 ? list.get(0) : null;
    }
}
