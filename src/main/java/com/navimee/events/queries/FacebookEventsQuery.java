package com.navimee.events.queries;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.navimee.configuration.specific.FacebookConfiguration;
import com.navimee.events.queries.params.EventsParams;
import com.navimee.models.dto.events.FbEventDto;
import com.navimee.models.entities.places.Place;
import com.navimee.queries.Query;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class FacebookEventsQuery extends Query<List<FbEventDto>, FacebookConfiguration, EventsParams> {

    private Place searchPlace;

    public FacebookEventsQuery(FacebookConfiguration configuration, ExecutorService executorService) {
        super(configuration, executorService);
    }

    @Override
    public Future<List<FbEventDto>> execute(EventsParams params) {

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Future<HttpResponse<JsonNode>> response =
                Unirest.get(configuration.apiUrl)
                        .queryString("id", searchPlace.getId())
                        .queryString("fields", String.format("name,category,events.fields(%s).since(%s).until(%s)",
                                joiner.toString(),
                                sdf.format(warsawCurrent.toDate()),
                                sdf.format(warsawLater.toDate())))
                        .queryString("access_token", configuration.accessToken)
                        .asJsonAsync();

        return executorService.submit(() -> map(response));
    }

    @Override
    protected List<FbEventDto> map(Future<HttpResponse<JsonNode>> future) {
        List<FbEventDto> events = new ArrayList<>();
        try{
            JSONObject object = future.get().getBody().getObject();
            if (!object.has("events")) return events;
            JSONObject obj = object.getJSONObject("events");
            events.addAll(convertNode(obj.getJSONArray("data")));
        }catch (Exception e){
            e.printStackTrace();
        }

        return events.stream().filter(e -> e.getAttendingCount() > 20).collect(Collectors.toList());
    }

    private List<FbEventDto> convertNode(JSONArray array) {
        List<FbEventDto> list = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        for (int n = 0; n < array.length(); n++) {
            JSONObject eventJson = array.getJSONObject(n);
            try {
                FbEventDto event = mapper.readValue(eventJson.toString(), FbEventDto.class);
                event.setSearchPlace(searchPlace);
                list.add(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return list;
    }
}
