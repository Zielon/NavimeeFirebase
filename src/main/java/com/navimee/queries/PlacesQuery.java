package com.navimee.queries;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.navimee.models.Place;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

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
    public Future<List<Place>> get(String accessToken) {

        Future<HttpResponse<JsonNode>> response = Unirest.get("https://graph.facebook.com/v2.10/search")
                .header("accept", "application/json")
                .queryString("q", "*")
                .queryString("type", "place")
                .queryString("center", String.format("%f,%f", lat,lon))
                .queryString("fields", "name,category,location")
                .queryString("limit", "100")
                .queryString("access_token", accessToken)
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
}