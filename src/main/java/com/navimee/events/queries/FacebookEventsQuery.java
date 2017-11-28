package com.navimee.events.queries;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.navimee.configuration.specific.FacebookConfiguration;
import com.navimee.contracts.services.HttpClient;
import com.navimee.events.queries.params.EventsParams;
import com.navimee.models.dto.events.FbEventDto;
import com.navimee.queries.Query;
import org.apache.http.client.utils.URIBuilder;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

public class FacebookEventsQuery extends Query<List<FbEventDto>, FacebookConfiguration, EventsParams> {

    public FacebookEventsQuery(FacebookConfiguration configuration,
                               ExecutorService executorService,
                               HttpClient httpClient) {
        super(configuration, executorService, httpClient);
    }

    @Override
    public Callable<List<FbEventDto>> execute(EventsParams params) {

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

        LocalDateTime warsawCurrent = LocalDateTime.now(DateTimeZone.forID("Europe/Warsaw"));
        LocalDateTime warsawLater = warsawCurrent.plusDays(14);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String fields =
                String.format("name,category,events.fields(%s).since(%s).until(%s)",
                        joiner.toString(),
                        sdf.format(warsawCurrent.toDate()),
                        sdf.format(warsawLater.toDate()));

        URI uri = null;

        try {
            URIBuilder builder = new URIBuilder(configuration.apiUrl);
            builder.setPath("v2.10");
            builder.setParameter("id", params.place.getId());
            builder.setParameter("fields", fields);
            builder.setParameter("access_token", configuration.accessToken);
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        URI finalUri = uri;
        return () -> map(httpClient.GET(finalUri), events -> events.forEach(event -> event.setSearchPlace(params.place)));
    }

    @Override
    protected List<FbEventDto> map(Callable<JSONObject> task, Consumer<List<FbEventDto>> consumer) {
        List<FbEventDto> events = new ArrayList<>();

        try {
            JSONObject object = task.call();
            if (!object.has("events")) return events;
            JSONObject obj = object.getJSONObject("events");
            events.addAll(convertNode(obj.getJSONArray("data")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        consumer.accept(events);

        return events.stream().filter(e -> e.getAttendingCount() > 20).collect(toList());
    }

    private List<FbEventDto> convertNode(JSONArray array) {
        List<FbEventDto> list = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        for (int n = 0; n < array.length(); n++) {
            JSONObject eventJson = array.getJSONObject(n);
            try {
                FbEventDto event = mapper.readValue(eventJson.toString(), FbEventDto.class);
                list.add(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return list;
    }
}
