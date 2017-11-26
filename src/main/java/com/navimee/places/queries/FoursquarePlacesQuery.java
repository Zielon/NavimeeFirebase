package com.navimee.places.queries;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.navimee.configuration.specific.FoursquareConfiguration;
import com.navimee.models.dto.places.foursquare.FsPlaceDto;
import com.navimee.places.queries.params.PlaceDetailsParams;
import com.navimee.queries.Query;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class FoursquarePlacesQuery extends Query<List<FsPlaceDto>, FoursquareConfiguration, PlaceDetailsParams> {

    public FoursquarePlacesQuery(FoursquareConfiguration configuration, ExecutorService executorService) {
        super(configuration, executorService);
    }

    @Override
    public Future<List<FsPlaceDto>> execute(PlaceDetailsParams params) {
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

        return executorService.submit(() -> map(response));
    }

    @Override
    protected List<FsPlaceDto> map(Future<HttpResponse<JsonNode>> future) {
        List<FsPlaceDto> list = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JSONObject object =  future.get().getBody().getObject();
            JSONArray array = object.getJSONObject("response").getJSONArray("venues");
            for (int n = 0; n < array.length(); n++) {
                JSONObject placeJson = array.getJSONObject(n);
                FsPlaceDto mapped = mapper.readValue(placeJson.toString(), FsPlaceDto.class);
                list.add(mapped);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
