package com.navimee.places.queries;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navimee.configuration.specific.FoursquareConfiguration;
import com.navimee.contracts.services.HttpClient;
import com.navimee.models.dto.timeframes.PopularDto;
import com.navimee.places.queries.params.PlaceDetailsParams;
import com.navimee.queries.Query;
import org.apache.http.client.utils.URIBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class FoursquareTimeFramesQuery extends Query<PopularDto, FoursquareConfiguration, PlaceDetailsParams> {

    public FoursquareTimeFramesQuery(FoursquareConfiguration configuration,
                                     ExecutorService executorService,
                                     HttpClient httpClient) {
        super(configuration, executorService, httpClient);
    }

    @Override
    public Callable<PopularDto> execute(PlaceDetailsParams params) {

        DateTime warsawCurrent = DateTime.now(DateTimeZone.UTC);
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyddMM");

        URI uri = null;

        try {
            URIBuilder builder = new URIBuilder(configuration.apiUrl);
            builder.setPath("v2/" + params.type + "/" + params.placeId + "/hours");
            builder.setParameter("v", fmt.print(warsawCurrent));
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
    protected PopularDto map(Callable<JSONObject> task, PlaceDetailsParams params) {
        ObjectMapper mapper = new ObjectMapper();
        PopularDto popular = null;
        try {
            JSONObject object = task.call();
            JSONObject json = object.getJSONObject("response").getJSONObject("popular");
            if (!json.has("timeframes")) return popular;
            popular = mapper.readValue(json.toString(), PopularDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        popular.setPlaceId(params.placeId);
        return popular;
    }
}