package com.navimee.queries;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.navimee.configuration.Configuration;
import com.navimee.models.Event;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.AsyncResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class EventsQuery extends Query<Event> {

    private String id;

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Future<List<Event>> get(Configuration configuration) {
        String eventsFields = "place.fields(id,name,location),id,name,start_time,end_time,type,category,attending_count,maybe_count,picture.type(large)";
        Future<HttpResponse<JsonNode>> response =
                Unirest.get(configuration.getJSONObject().getString("apiUrl"))
                        .queryString("id", id)
                        .queryString("fields", String.format("name,category,events.fields(%s).since(2017-10-10T10:10:10).until(2017-12-12T10:10:10)", eventsFields))
                        .queryString("access_token", configuration.getAccessToken())
                        .asJsonAsync();
        try {
            return new AsyncResult<>(map(response.get().getBody().getObject(), Event.class));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected List<Event> map(JSONObject object, Class<Event> type) {
        List<Event> list = new ArrayList<>();
        try {
            JSONObject obj = object.getJSONObject("events");
            list.addAll(convertNode(obj.getJSONArray("data"), type));
        }catch (JSONException e){ }

        return list.stream().filter(e -> e.attending_count > 100).collect(Collectors.toList());
    }

    private List<Event> convertNode(JSONArray array, Class<Event> type){
        List<Event> list = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for(int n = 0; n < array.length(); n++){
            JSONObject eventJson = array.getJSONObject(n);
            try {
                Event mapped = mapper.readValue(eventJson.toString(), type);
                list.add(mapped);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return list;
    }
}
