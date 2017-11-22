package com.navimee.places.queries;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.navimee.configuration.specific.FacebookConfiguration;
import com.navimee.models.externalDto.places.FbPlaceDto;
import com.navimee.places.queries.params.PlacesParams;
import com.navimee.queries.Query;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FacebookPlacesQuery extends Query<List<FbPlaceDto>, FacebookConfiguration, PlacesParams> {

    public FacebookPlacesQuery(FacebookConfiguration configuration) {
        super(configuration);
    }

    @Async
    @Override
    public Future<List<FbPlaceDto>> execute(PlacesParams params) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<HttpResponse<JsonNode>> response =
                Unirest.get(configuration.apiUrl + "/search")
                        .queryString("q", "*")
                        .queryString("type", "place")
                        .queryString("center", params.lat + "," + params.lon)
                        .queryString("distance", "1000")
                        .queryString("fields", "name,category,location")
                        .queryString("limit", "300")
                        .queryString("access_token", configuration.accessToken)
                        .asJsonAsync();

        return executor.submit(() -> map(response.get().getBody().getObject()));
    }

    @Override
    protected List<FbPlaceDto> map(JSONObject object) {
        List<FbPlaceDto> list = new ArrayList<>();
        list.addAll(convertNode(object.getJSONArray("data")));

        if (!object.has("paging"))
            return list;

        JSONObject paging = object.getJSONObject("paging");
        String nextUrl = paging.getString("next");

        while (list.size() < 7000) {
            try {
                JSONObject nextObj = Unirest.get(nextUrl).asJson().getBody().getObject();
                list.addAll(convertNode(nextObj.getJSONArray("data")));
                if (!nextObj.has("paging")) break;
                nextUrl = nextObj.getJSONObject("paging").getString("next");
            } catch (UnirestException e) {
                break;
            }
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