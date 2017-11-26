package com.navimee.places.queries;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.navimee.configuration.specific.FoursquareConfiguration;
import com.navimee.models.dto.placeDetails.FsPlaceDetailsDto;
import com.navimee.places.queries.params.PlaceDetailsParams;
import com.navimee.queries.Query;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import java.util.concurrent.*;

public class FoursquareDetailsQuery extends Query<FsPlaceDetailsDto, FoursquareConfiguration, PlaceDetailsParams> {

    public FoursquareDetailsQuery(FoursquareConfiguration configuration, ExecutorService executorService) {
        super(configuration, executorService);
    }

    @Override
    public Future<FsPlaceDetailsDto> execute(PlaceDetailsParams params) {
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

        return executorService.submit(() -> map(response));
    }

    @Override
    protected FsPlaceDetailsDto map(Future<HttpResponse<JsonNode>> future) {
        FsPlaceDetailsDto mapped = null;
        try {
            JSONObject object =  future.get().getBody().getObject();
            JSONObject details = object.getJSONObject("response").getJSONObject("venue");
            ObjectMapper mapper = new ObjectMapper();
            mapped = mapper.readValue(details.toString(), FsPlaceDetailsDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapped;
    }
}
