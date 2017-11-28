package com.navimee.places.queries;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.navimee.configuration.specific.FacebookConfiguration;
import com.navimee.contracts.services.HttpClient;
import com.navimee.models.dto.places.facebook.FbPlaceDto;
import com.navimee.places.queries.params.PlacesParams;
import com.navimee.queries.Query;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

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
        return () -> map(httpClient.GET(finalUri), null);
    }

    @Override
    protected List<FbPlaceDto> map(Callable<JSONObject> task, Consumer<List<FbPlaceDto>> consumer) {
        List<FbPlaceDto> list = new ArrayList<>();

        try {
            JSONObject object = task.call();
            list.addAll(convertNode(object.getJSONArray("data")));

            if (!object.has("paging")) return list;
            JSONObject paging = object.getJSONObject("paging");

            if (!paging.has("next")) return list;
            String nextUrl = paging.getString("next");

            // Read all places from paging
            while (true) {
                try {
                    JSONObject nextObj = httpClient.GET(new URI(nextUrl)).call();
                    list.addAll(convertNode(nextObj.getJSONArray("data")));

                    if (!nextObj.has("paging")) break;
                    paging = nextObj.getJSONObject("paging");

                    if (!paging.has("next")) break;
                    nextUrl = paging.getString("next");

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

    private List<FbPlaceDto> convertNode(JSONArray array) {
        List<FbPlaceDto> list = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for (int n = 0; n < array.length(); n++) {
            JSONObject placeJson = array.getJSONObject(n);
            try {
                FbPlaceDto mapped = mapper.readValue(placeJson.toString(), FbPlaceDto.class);
                list.add(mapped);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}