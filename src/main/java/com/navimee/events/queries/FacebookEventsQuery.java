package com.navimee.events.queries;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.navimee.configuration.specific.FacebookConfiguration;
import com.navimee.contracts.models.events.Event;
import com.navimee.contracts.models.places.Place;
import com.navimee.events.queries.params.EventsParams;
import com.navimee.queries.Query;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class FacebookEventsQuery extends Query<List<Event>, FacebookConfiguration, EventsParams> {

    public FacebookEventsQuery(FacebookConfiguration configuration) {
        super(configuration);
    }

    private Place searchPlace;

    @Async
    @Override
    public Future<List<Event>> execute(EventsParams params) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        StringJoiner joiner = new StringJoiner(",");
        searchPlace = params.place;

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
        LocalDateTime warsawLater = warsawCurrent.plusDays(14);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Future<HttpResponse<JsonNode>> response =
                Unirest.get(configuration.apiUrl)
                        .queryString("id", searchPlace.id)
                        .queryString("fields", String.format("name,category,events.fields(%s).since(%s).until(%s)",
                                joiner.toString(),
                                sdf.format(warsawCurrent.toDate()),
                                sdf.format(warsawLater.toDate())))
                        .queryString("access_token", configuration.accessToken)
                        .asJsonAsync();

        return executor.submit(() -> map(response.get().getBody().getObject()));
    }

    @Override
    protected List<Event> map(JSONObject object) {
        List<Event> events = new ArrayList<>();

        if (!object.has("events"))
            return events;

        JSONObject obj = object.getJSONObject("events");
        events.addAll(convertNode(obj.getJSONArray("data")));

        return events
                .stream()
                .filter(e -> e.attending_count > 50)
                .collect(Collectors.toList());
    }

    private List<Event> convertNode(JSONArray array) {
        List<Event> list = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        for (int n = 0; n < array.length(); n++) {
            JSONObject eventJson = array.getJSONObject(n);
            try {
                Event event = mapper.readValue(eventJson.toString(), Event.class);
                event.searchPlace = searchPlace;
                list.add(event);
            } catch (IOException e) {
            }
        }

        return list;
    }
}
