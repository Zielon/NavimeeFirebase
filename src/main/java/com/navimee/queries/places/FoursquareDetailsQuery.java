package com.navimee.queries.places;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navimee.configuration.specific.FoursquareConfiguration;
import com.navimee.contracts.services.HttpClient;
import com.navimee.models.dto.placeDetails.FsPlaceDetailsDto;
import com.navimee.queries.Query;
import com.navimee.queries.places.params.PlaceDetailsParams;
import org.apache.http.client.utils.URIBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class FoursquareDetailsQuery extends Query<FsPlaceDetailsDto, FoursquareConfiguration, PlaceDetailsParams> {


    public FoursquareDetailsQuery(FoursquareConfiguration configuration,
                                  ExecutorService executorService,
                                  HttpClient httpClient) {
        super(configuration, executorService, httpClient);
    }

    @Override
    public CompletableFuture<FsPlaceDetailsDto> execute(PlaceDetailsParams params) {

        DateTime warsawCurrent = DateTime.now(DateTimeZone.UTC);
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyyddMM");

        URI uri = null;
        try {
            URIBuilder builder = new URIBuilder(configuration.getApiUrl());
            builder.setPath("v2/" + params.type + "/" + params.placeId);
            builder.setParameter("v", dtf.print(warsawCurrent));
            builder.setParameter("client_id", configuration.getClientId());
            builder.setParameter("client_secret", configuration.getClientSecret());
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        URI finalUri = uri;
        return CompletableFuture.supplyAsync(() -> map(httpClient.GET(finalUri), params), executorService);
    }

    @Override
    protected FsPlaceDetailsDto map(CompletableFuture<JSONObject> task, PlaceDetailsParams params) {
        ObjectMapper mapper = new ObjectMapper();
        FsPlaceDetailsDto placeDto = null;
        try {
            JSONObject object = task.join();
            JSONObject response = object.getJSONObject("response");
            if (!response.has("venue")) return placeDto;
            JSONObject details = response.getJSONObject("venue");
            placeDto = mapper.readValue(details.toString(), FsPlaceDetailsDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return placeDto;
    }
}
