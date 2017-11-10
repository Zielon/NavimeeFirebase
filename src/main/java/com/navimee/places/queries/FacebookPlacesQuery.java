package com.navimee.places.queries;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.navimee.configuration.specific.FacebookConfiguration;
import com.navimee.contracts.models.places.FacebookPlace;
import com.navimee.queries.Query;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FacebookPlacesQuery extends Query<FacebookPlace, FacebookConfiguration, PlacesParams> {

    public FacebookPlacesQuery(FacebookConfiguration configuration) {
        super(configuration);
    }

    @Async
    @Override
    public Future<List<FacebookPlace>> execute(PlacesParams params) {
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

        return executor.submit(() -> map(response.get().getBody().getObject(), FacebookPlace.class));
    }

    @Override
    protected List<FacebookPlace> map(JSONObject object, Class<FacebookPlace> type) {
        List<FacebookPlace> list = new ArrayList<>();
        list.addAll(convertNode(object.getJSONArray("data")));

        if (!object.has("paging"))
            return list;

        JSONObject paging = object.getJSONObject("paging");
        String nextUrl = paging.getString("next");

        while (list.size() < 5000) {
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

    private List<FacebookPlace> convertNode(JSONArray array) {
        List<FacebookPlace> list = new ArrayList<>();
        for (int n = 0; n < array.length(); n++) {
            JSONObject placeJson = array.getJSONObject(n);
            FacebookPlace place = new FacebookPlace();
            place.id = placeJson.getString("id");
            place.name = placeJson.getString("name");
            place.category = placeJson.getString("category");
            list.add(place);
        }
        return list;
    }
}