package com.navimee.places.queries;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.navimee.configuration.specific.FoursquareConfiguration;
import com.navimee.contracts.models.placeDetails.FoursquarePlaceDetails;
import com.navimee.places.queries.params.PlaceDetailsParams;
import com.navimee.queries.Query;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FoursquareDetailsQuery extends Query<FoursquarePlaceDetails, FoursquareConfiguration, PlaceDetailsParams> {

    public FoursquareDetailsQuery(FoursquareConfiguration configuration) {
        super(configuration);
    }

    @Override
    public Future<FoursquarePlaceDetails> execute(PlaceDetailsParams params) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        DateTimeZone zone = DateTimeZone.forID("Europe/Warsaw");
        LocalDateTime warsawCurrent = LocalDateTime.now(zone);
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyddMM");

        Future<HttpResponse<JsonNode>> response =
                Unirest.get(configuration.apiUrl + "/{type}/{placeId}")
                        .routeParam("type", params.type)
                        .routeParam("placeId", params.placeId)
                        .queryString("v", fmt.print(warsawCurrent))
                        .queryString("client_id", configuration.clientId)
                        .queryString("client_secret", configuration.clientSecret)
                        .asJsonAsync();

        return executor.submit(() -> map(response.get().getBody().getObject()));
    }

    @Override
    protected FoursquarePlaceDetails map(JSONObject object) {
        JSONObject details = object.getJSONObject("response").getJSONObject("venue");
        ObjectMapper mapper = new ObjectMapper();
        FoursquarePlaceDetails mapped = null;
        try {
            mapped = mapper.readValue(details.toString(), FoursquarePlaceDetails.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mapped;
    }
}
