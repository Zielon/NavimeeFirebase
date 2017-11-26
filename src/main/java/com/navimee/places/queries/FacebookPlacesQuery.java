package com.navimee.places.queries;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.navimee.configuration.specific.FacebookConfiguration;
import com.navimee.models.dto.places.facebook.FbPlaceDto;
import com.navimee.places.queries.params.PlacesParams;
import com.navimee.queries.Query;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class FacebookPlacesQuery extends Query<List<FbPlaceDto>, FacebookConfiguration, PlacesParams> {

    public FacebookPlacesQuery(FacebookConfiguration configuration, ExecutorService executorService) {
        super(configuration, executorService);
    }

    @Override
    public Future<List<FbPlaceDto>> execute(PlacesParams params) {

        Future<HttpResponse<JsonNode>> response =
                Unirest.get(configuration.apiUrl + "/search")
                        .queryString("q", "*")
                        .queryString("type", "place")
                        .queryString("center", params.lat + "," + params.lon)
                        .queryString("distance", "1000")
                        .queryString("fields", "name,category,location")
                        .queryString("limit", "200")
                        .queryString("access_token", configuration.accessToken)
                        .asJsonAsync();

        return executorService.submit(() -> map(response));
    }

    @Override
    protected List<FbPlaceDto> map(Future<HttpResponse<JsonNode>> future) {
        List<FbPlaceDto> list = new ArrayList<>();

        try{
            JSONObject object =  future.get().getBody().getObject();
            list.addAll(convertNode(object.getJSONArray("data")));

            if (!object.has("paging")) return list;
            JSONObject paging = object.getJSONObject("paging");
            String nextUrl = paging.getString("next");

            while (list.size() < 50000) {
                try {
                    JSONObject nextObj = Unirest.get(nextUrl).asJson().getBody().getObject();
                    list.addAll(convertNode(nextObj.getJSONArray("data")));
                    if (!nextObj.has("paging")) break;
                    nextUrl = nextObj.getJSONObject("paging").getString("next");
                } catch (UnirestException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }catch (Exception e){
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