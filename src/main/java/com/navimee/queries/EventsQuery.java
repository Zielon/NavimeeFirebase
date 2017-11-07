package com.navimee.queries;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.navimee.configuration.FacebookConfiguration;
import com.navimee.models.entities.Event;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class EventsQuery extends Query<Event, FacebookConfiguration> {

    private String id;

    public EventsQuery(FacebookConfiguration configuration) {
        super(configuration);
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Future<List<Event>> execute() {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        StringJoiner joiner = new StringJoiner(",");

        joiner.add("place.fields(id,name,location)");
        joiner.add("id");
        joiner.add("name");
        joiner.add("start_time");
        joiner.add("end_time");
        joiner.add("type");
        joiner.add("category");
        joiner.add("attending_count");
        joiner.add("maybe_count");
        joiner.add("picture.type(large)");

        DateTimeZone zone = DateTimeZone.forID("Europe/Warsaw");
        LocalDateTime warsawCurrent = LocalDateTime.now(zone);
        LocalDateTime warsaw1MonthLater = warsawCurrent.plusDays(14);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Future<HttpResponse<JsonNode>> response =
                Unirest.get(configuration.apiUrl)
                        .queryString("id", id)
                        .queryString("fields", String.format("name,category,events.fields(%s).since(%s).until(%s)",
                                joiner.toString(),
                                sdf.format(warsawCurrent.toDate()),
                                sdf.format(warsaw1MonthLater.toDate())))
                        .queryString("access_token", configuration.accessToken)
                        .asJsonAsync();

        return executor.submit(() -> map(response.get().getBody().getObject(), Event.class));
    }

    @Override
    protected List<Event> map(JSONObject object, Class<Event> type) {
        List<Event> list = new ArrayList<>();
        try {
            JSONObject obj = object.getJSONObject("events");
            list.addAll(convertNode(obj.getJSONArray("data"), type));
        } catch (JSONException _) {
        }

        return list
                .stream()
                .filter(e -> e.attending_count > 100)
                .filter(e -> e.place != null)
                .collect(Collectors.toList());
    }

    private List<Event> convertNode(JSONArray array, Class<Event> type) {
        List<Event> list = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        for (int n = 0; n < array.length(); n++) {
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
