package com.navimee.places.queries;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.navimee.configuration.specific.FoursquareConfiguration;
import com.navimee.contracts.models.places.FoursquarePlace;
import com.navimee.places.queries.params.PlacesParams;
import com.navimee.queries.Query;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FoursquarePlacesQuery extends Query<FoursquarePlace, FoursquareConfiguration, PlacesParams> {

    public FoursquarePlacesQuery(FoursquareConfiguration configuration) {
        super(configuration);
    }

    @Override
    public Future<List<FoursquarePlace>> execute(PlacesParams params) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        DateTimeZone zone = DateTimeZone.forID("Europe/Warsaw");
        LocalDateTime warsawCurrent = LocalDateTime.now(zone);
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyddMM");

        Future<HttpResponse<JsonNode>> response =
                Unirest.get(configuration.apiUrl + params.type)
                        .queryString("v", fmt.print(warsawCurrent))
                        .queryString("client_id", configuration.clientId)
                        .queryString("client_secret", configuration.clientSecret)
                        .queryString("ll", params.lat + "," + params.lon)
                        .asJsonAsync();

        return executor.submit(() -> map(response.get().getBody().getObject()));
    }

    @Override
    protected List<FoursquarePlace> map(JSONObject object) {
        JSONArray array = object.getJSONObject("response").getJSONArray("venues");
        List<FoursquarePlace> list = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for (int n = 0; n < array.length(); n++) {
            JSONObject placeJson = array.getJSONObject(n);
            try {
                FoursquarePlace mapped = mapper.readValue(placeJson.toString(), FoursquarePlace.class);
                list.add(mapped);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
