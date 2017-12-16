package com.navimee.places.queries;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navimee.configuration.specific.FoursquareConfiguration;
import com.navimee.contracts.services.HttpClient;
import com.navimee.models.dto.placeDetails.FsPlaceDetailsDto;
import com.navimee.places.queries.params.PlaceDetailsParams;
import com.navimee.queries.Query;
import org.apache.http.client.utils.URIBuilder;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class FoursquareDetailsQuery extends Query<FsPlaceDetailsDto, FoursquareConfiguration, PlaceDetailsParams> {


    public FoursquareDetailsQuery(FoursquareConfiguration configuration,
                                  ExecutorService executorService,
                                  HttpClient httpClient) {
        super(configuration, executorService, httpClient);
    }

    @Override
    public Callable<FsPlaceDetailsDto> execute(PlaceDetailsParams params) {

        LocalDateTime warsawCurrent = LocalDateTime.now(DateTimeZone.forID("Europe/Warsaw"));
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyyddMM");

        URI uri = null;

        try {
            URIBuilder builder = new URIBuilder(configuration.apiUrl);
            builder.setPath("v2/" + params.type + "/" + params.placeId);
            builder.setParameter("v", dtf.print(warsawCurrent));
            builder.setParameter("client_id", configuration.clientId);
            builder.setParameter("client_secret", configuration.clientSecret);
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        URI finalUri = uri;
        return () -> map(httpClient.GET(finalUri), params);
    }

    @Override
    protected FsPlaceDetailsDto map(Callable<JSONObject> task, PlaceDetailsParams params) {
        ObjectMapper mapper = new ObjectMapper();
        FsPlaceDetailsDto mapped = null;
        try {
            JSONObject object = task.call();
            JSONObject response = object.getJSONObject("response");
            if (!response.has("venue")) return mapped;
            JSONObject details = response.getJSONObject("venue");
            mapped = mapper.readValue(details.toString(), FsPlaceDetailsDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mapped;
    }
}
