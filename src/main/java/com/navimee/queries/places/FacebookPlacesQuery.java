package com.navimee.queries.places;

import com.navimee.configuration.specific.FacebookConfiguration;
import com.navimee.contracts.services.HttpClient;
import com.navimee.general.JSON;
import com.navimee.models.dto.places.facebook.FbPlaceDto;
import com.navimee.queries.Query;
import com.navimee.queries.places.params.PlacesParams;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class FacebookPlacesQuery extends Query<List<FbPlaceDto>, FacebookConfiguration, PlacesParams> {

    public FacebookPlacesQuery(FacebookConfiguration configuration,
                               ExecutorService executorService,
                               HttpClient httpClient) {
        super(configuration, executorService, httpClient);
    }

    @Override
    public CompletableFuture<List<FbPlaceDto>> execute(PlacesParams params) {

        URI uri = null;
        try {
            URIBuilder builder = new URIBuilder(configuration.getApiUrl());
            builder.setPath("v2.10/search");
            builder.setParameter("type", "place");
            builder.setParameter("center", params.lat + "," + params.lon);
            builder.setParameter("distance", "1000");
            builder.setParameter("fields", "name,category,location");
            builder.setParameter("limit", "500");
            builder.setParameter("access_token", configuration.getAccessToken());
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        URI finalUri = uri;
        return CompletableFuture.supplyAsync(() -> map(httpClient.GET(finalUri), params), executorService);
    }

    @Override
    protected List<FbPlaceDto> map(CompletableFuture<JSONObject> task, PlacesParams params) {
        List<FbPlaceDto> list = new ArrayList<>();

        try {
            JSONObject object = task.join();
            list.addAll(JSON.arrayMapper(object.getJSONArray("data"), FbPlaceDto.class));

            if (!JSON.hasPaging(object)) return list;
            String nextUrl = JSON.getNext(object);

            // Read all places from paging
            while (true) {
                try {
                    object = httpClient.GET(new URI(nextUrl)).join();
                    list.addAll(JSON.arrayMapper(object.getJSONArray("data"), FbPlaceDto.class));

                    if (!JSON.hasPaging(object)) break;
                    nextUrl = JSON.getNext(object);

                } catch (Exception e) {
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