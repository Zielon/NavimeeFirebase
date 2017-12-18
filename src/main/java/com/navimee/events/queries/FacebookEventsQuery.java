package com.navimee.events.queries;

import com.navimee.configuration.specific.FacebookConfiguration;
import com.navimee.contracts.services.HttpClient;
import com.navimee.events.queries.params.FacebookEventsParams;
import com.navimee.general.JSON;
import com.navimee.models.dto.events.FbEventDto;
import com.navimee.models.entities.places.Place;
import com.navimee.models.entities.places.facebook.FbPlace;
import com.navimee.queries.Query;
import org.apache.http.client.utils.URIBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class FacebookEventsQuery extends Query<List<FbEventDto>, FacebookConfiguration, FacebookEventsParams> {

    public FacebookEventsQuery(FacebookConfiguration configuration,
                               ExecutorService executorService,
                               HttpClient httpClient) {
        super(configuration, executorService, httpClient);
    }

    @Override
    public Callable<List<FbEventDto>> execute(FacebookEventsParams params) {

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

        DateTime warsawCurrent = DateTime.now(DateTimeZone.UTC);
        DateTime warsawLater = warsawCurrent.plusDays(14);
        DateTimeFormatter dtf = ISODateTimeFormat.dateTime();

        String fields =
                String.format("name,category,events.fields(%s).since(%s).until(%s)",
                        joiner.toString(),
                        dtf.print(warsawCurrent),
                        dtf.print(warsawLater));

        URI uri = null;
        String ids = params.places.stream().map(Place::getId).collect(Collectors.joining(","));
        try {
            URIBuilder builder = new URIBuilder(configuration.apiUrl);
            builder.setParameter("ids", ids);
            builder.setParameter("fields", fields);
            builder.setParameter("access_token", configuration.accessToken);
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        URI finalUri = uri;
        return () -> map(httpClient.GET(finalUri), params);
    }

    @Override
    protected List<FbEventDto> map(Callable<JSONObject> task, FacebookEventsParams params) {
        List<FbEventDto> events = new ArrayList<>();

        try {
            JSONObject object = task.call();
            for (Object key : object.keySet()) {
                JSONObject event = object.getJSONObject(key.toString());
                if (!event.has("events")) continue;
                List<FbEventDto> dto = JSON.arrayMapper(event.getJSONObject("events").getJSONArray("data"), FbEventDto.class);
                FbPlace searchPlace = params.places.stream().filter(p -> p.getId().equals(key.toString())).findFirst().get();
                dto.forEach(d -> d.setSearchPlace(searchPlace));
                events.addAll(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return events.stream().filter(e -> e.getAttendingCount() > 50).collect(toList());
    }
}
