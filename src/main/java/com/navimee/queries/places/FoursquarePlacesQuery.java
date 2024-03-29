package com.navimee.queries.places;

import com.navimee.configuration.specific.FoursquareConfiguration;
import com.navimee.contracts.services.HttpClient;
import com.navimee.general.JSON;
import com.navimee.models.dto.places.foursquare.FsPlaceDto;
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
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class FoursquarePlacesQuery extends Query<List<FsPlaceDto>, FoursquareConfiguration, PlaceDetailsParams> {

    public FoursquarePlacesQuery(FoursquareConfiguration configuration,
                                 ExecutorService executorService,
                                 HttpClient httpClient) {
        super(configuration, executorService, httpClient);
    }

    @Override
    public CompletableFuture<List<FsPlaceDto>> execute(PlaceDetailsParams params) {

        DateTime warsawCurrent = DateTime.now(DateTimeZone.UTC);
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyddMM");

        URI uri = null;
        try {
            URIBuilder builder = new URIBuilder(configuration.getApiUrl());
            builder.setPath("v2/" + params.type);
            builder.setParameter("v", fmt.print(warsawCurrent));
            builder.setParameter("client_id", configuration.getClientId());
            builder.setParameter("client_secret", configuration.getClientSecret());
            builder.setParameter("ll", params.lat + "," + params.lon);
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        URI finalUri = uri;
        return CompletableFuture.supplyAsync(() -> map(httpClient.GET(finalUri), params), executorService);
    }

    @Override
    protected List<FsPlaceDto> map(CompletableFuture<JSONObject> task, PlaceDetailsParams params) {
        List<FsPlaceDto> output = null;
        try {
            JSONObject object = task.join();
            output = JSON.arrayMapper(object.getJSONObject("response").getJSONArray("venues"), FsPlaceDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}
