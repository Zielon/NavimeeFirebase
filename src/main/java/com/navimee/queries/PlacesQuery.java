package com.navimee.queries;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.navimee.configuration.Configuration;
import com.navimee.models.Place;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class PlacesQuery extends Query<Place> {

    private double lat;
    private double lon;

    public void setCoordinates(double lat, double lon){
        this.lat = lat;
        this.lon = lon;
    }

    @Async
    @Override
    public Future<List<Place>> get(Configuration configuration) {
        Future<HttpResponse<JsonNode>> response =
                Unirest.get(configuration.getJSONObject().getString("apiUrl") + "/search")
                    .queryString("q", "*")
                    .queryString("type", "place")
                    .queryString("center", lat + "," + lon)
                    .queryString("distance", "3000")
                    .queryString("fields", "name,category,location")
                    .queryString("limit", "100")
                    .queryString("access_token", configuration.getAccessToken())
                    .asJsonAsync();
        try {
            return new AsyncResult<>(map(response.get().getBody().getObject(), Place.class));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected List<Place> map(JSONObject object, Class<Place> type) {
        List<Place> list = new ArrayList<>();
        list.addAll(convertNode(object.getJSONArray("data")));
        JSONObject paging = object.getJSONObject("paging");
        String nextUrl = paging.getString("next");
        while (list.size() < 10000){
            try {
                JSONObject nextObj = Unirest.get(nextUrl).asJson().getBody().getObject();
                list.addAll(convertNode(nextObj.getJSONArray("data")));
                nextUrl = object.getJSONObject("paging").getString("next");
                if(nextUrl != null || nextUrl != "") break;
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private List<Place> convertNode(JSONArray array){
        List<Place> list = new ArrayList<>();
        for(int n = 0; n < array.length(); n++){
            JSONObject placeJson = array.getJSONObject(n);
            String id = placeJson.getString("id");
            String name = placeJson.getString("name");
            Place place = new Place();
            place.id = id;
            place.name = name;
            list.add(place);
        }
        return list;
    }
}