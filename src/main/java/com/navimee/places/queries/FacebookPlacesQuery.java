package com.navimee.places.queries;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.navimee.configuration.specific.FacebookConfiguration;
import com.navimee.contracts.services.HttpClient;
import com.navimee.general.JSON;
import com.navimee.models.dto.places.facebook.FbPlaceDto;
import com.navimee.places.queries.params.PlacesParams;
import com.navimee.queries.Query;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class FacebookPlacesQuery extends Query<List<FbPlaceDto>, FacebookConfiguration, PlacesParams> {

    public FacebookPlacesQuery(FacebookConfiguration configuration,
                               ExecutorService executorService,
                               HttpClient httpClient) {
        super(configuration, executorService, httpClient);
    }

    @Override
    public Callable<List<FbPlaceDto>> execute(PlacesParams params) {

        URI uri = null;

        try {
            URIBuilder builder = new URIBuilder(configuration.apiUrl);
            builder.setPath("v2.10/search");
            builder.setParameter("type", "place");
            builder.setParameter("center", params.lat + "," + params.lon);
            builder.setParameter("distance", "1000");
            builder.setParameter("fields", "name,category,location");
            builder.setParameter("limit", "500");
            builder.setParameter("access_token", configuration.accessToken);
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        URI finalUri = uri;
        return () -> map(httpClient.GET(finalUri), params);
    }

    @Override
    protected List<FbPlaceDto> map(Callable<JSONObject> task, PlacesParams params) {
        List<FbPlaceDto> list = new ArrayList<>();

        try {
            JSONObject object = task.call();
            list.addAll(JSON.arrayMapper(object.getJSONArray("data"), FbPlaceDto.class));

            if (!JSON.hasPaging(object)) return list;
            String nextUrl = object.getJSONObject("paging").getString("next");

            // Read all places from paging
            while (true) {
                try {
                    JSONObject nextObj = httpClient.GET(new URI(nextUrl)).call();
                    list.addAll(JSON.arrayMapper(nextObj.getJSONArray("data"), FbPlaceDto.class));

                    if (!JSON.hasPaging(object)) break;

                } catch (UnirestException e) {
                    e.printStackTrace();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}