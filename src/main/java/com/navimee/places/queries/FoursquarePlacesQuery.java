package com.navimee.places.queries;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navimee.configuration.specific.FoursquareConfiguration;
import com.navimee.contracts.services.HttpClient;
import com.navimee.models.dto.places.foursquare.FsPlaceDto;
import com.navimee.places.queries.params.PlaceDetailsParams;
import com.navimee.queries.Query;
import org.apache.http.client.utils.URIBuilder;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class FoursquarePlacesQuery extends Query<List<FsPlaceDto>, FoursquareConfiguration, PlaceDetailsParams> {

    public FoursquarePlacesQuery(FoursquareConfiguration configuration,
                                 ExecutorService executorService,
                                 HttpClient httpClient) {
        super(configuration, executorService, httpClient);
    }

    @Override
    public Callable<List<FsPlaceDto>> execute(PlaceDetailsParams params) {

        LocalDateTime warsawCurrent = LocalDateTime.now(DateTimeZone.forID("Europe/Warsaw"));
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyddMM");

        URI uri = null;

        try {
            URIBuilder builder = new URIBuilder(configuration.apiUrl);
            builder.setPath("v2/" + params.type);
            builder.setParameter("v", fmt.print(warsawCurrent));
            builder.setParameter("client_id", configuration.clientId);
            builder.setParameter("client_secret", configuration.clientSecret);
            builder.setParameter("ll", params.lat + "," + params.lon);
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        URI finalUri = uri;
        return () -> map(httpClient.GET(finalUri), null);
    }

    @Override
    protected List<FsPlaceDto> map(Callable<JSONObject> task, Consumer<List<FsPlaceDto>> consumer) {
        List<FsPlaceDto> list = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JSONObject object = task.call();
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
