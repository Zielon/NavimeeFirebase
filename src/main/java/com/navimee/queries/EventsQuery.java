package com.navimee.queries;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.navimee.configuration.Configuration;
import com.navimee.models.Event;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.AsyncResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class EventsQuery extends Query<Event> {

    private String id;

    public void setId(String id){
        this.id = id;
    }

    @Override
    public Future<List<Event>> get(Configuration configuration) {
        String eventsFields =  "place,id,name,start_time,end_time,type,category,attending_count,maybe_count,picture.type(large)";
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

        return list;
    }
}
