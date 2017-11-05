package com.navimee.queries;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.navimee.configuration.FacebookConfiguration;
import com.navimee.models.Place;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PlacesQuery extends Query<Place, FacebookConfiguration> {

    private double lat;
    private double lon;

    public PlacesQuery(FacebookConfiguration configuration) {
        super(configuration);
    }

    public void setCoordinates(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Async
    @Override
    public Future<List<Place>> execute() {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<HttpResponse<JsonNode>> response =
                Unirest.get(configuration.apiUrl + "/search")
                        .queryString("q", "*")
                        .queryString("type", "place")
                        .queryString("center", lat + "," + lon)
                        .queryString("distance", "2000")
                        .queryString("fields", "name,category,location")
                        .queryString("limit", "200")
                        .queryString("access_token", configuration.accessToken)
                        .asJsonAsync();

        return executor.submit(() -> map(response.get().getBody().getObject(), Place.class));
    }

    @Override
    protected List<Place> map(JSONObject object, Class<Place> type) {
        List<Place> list = new ArrayList<>();
        list.addAll(convertNode(object.getJSONArray("data")));
        JSONObject paging = object.getJSONObject("paging");
        String nextUrl = paging.getString("next");
        while (list.size() < 3000) {
            try {
                JSONObject nextObj = Unirest.get(nextUrl).asJson().getBody().getObject();
                list.addAll(convertNode(nextObj.getJSONArray("data")));
                nextUrl = object.getJSONObject("paging").getString("next");
                if (nextUrl == null || nextUrl == "") break;
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private List<Place> convertNode(JSONArray array) {
        List<Place> list = new ArrayList<>();
        for (int n = 0; n < array.length(); n++) {
            JSONObject placeJson = array.getJSONObject(n);
            Place place = new Place();
            place.id = placeJson.getString("id");
            place.name = placeJson.getString("name");
            place.category = placeJson.getString("category");
            list.add(place);
        }
        return list;
    }
}