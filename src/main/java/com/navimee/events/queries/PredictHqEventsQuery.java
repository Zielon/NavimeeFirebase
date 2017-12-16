package com.navimee.events.queries;

import com.navimee.configuration.specific.PredictHqConfiguration;
import com.navimee.contracts.services.HttpClient;
import com.navimee.events.queries.params.PredictHqEventsParams;
import com.navimee.general.JSON;
import com.navimee.models.dto.events.PhqEventDto;
import com.navimee.queries.Query;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class PredictHqEventsQuery extends Query<List<PhqEventDto>, PredictHqConfiguration, PredictHqEventsParams> {

    public PredictHqEventsQuery(PredictHqConfiguration configuration, ExecutorService executorService, HttpClient httpClient) {
        super(configuration, executorService, httpClient);
    }

    @Override
    public Callable<List<PhqEventDto>> execute(PredictHqEventsParams params) {

        LocalDateTime warsawCurrent = LocalDateTime.now(DateTimeZone.forID("Europe/Warsaw"));
        LocalDateTime warsawLater = warsawCurrent.plusDays(14);
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyyddMM");

        URI uri = null;
        String within = params.coordinates.stream()
                .map(c -> String.format("1km@%f,%f", c.getLatitude(), c.getLongitude()))
                .collect(Collectors.joining("+"));

        try {
            URIBuilder builder = new URIBuilder(configuration.apiUrl + "/v1/events/");
            builder.setParameter("date.gte", dtf.print(warsawCurrent));
            builder.setParameter("date.lte", dtf.print(warsawLater));
            builder.setParameter("date.tz", "Europe/Warsaw");
            builder.setParameter("within", within);
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HttpGet request = new HttpGet(uri);
        request.setHeader("Authorization", String.format("Bearer %s", configuration.accessToken));

        return () -> map(httpClient.GET(request), params);
    }

    @Override
    protected List<PhqEventDto> map(Callable<JSONObject> task, PredictHqEventsParams params) {
        List<PhqEventDto> list = new ArrayList<>();

        try {
            JSONObject object = task.call();
            list.addAll(JSON.arrayMapper(object.getJSONArray("results"), PhqEventDto.class));

            if (object.get("next").toString().equals("null")) return list;
            String nextUrl = object.get("next").toString();

            while (true) {
                try {
                    HttpGet request = new HttpGet(nextUrl);
                    request.setHeader("Authorization", String.format("Bearer %s", configuration.accessToken));

                    object = httpClient.GET(request).call();
                    if (!object.has("results")) break;

                    list.addAll(JSON.arrayMapper(object.getJSONArray("results"), PhqEventDto.class));

                    if (object.get("next").toString().equals("null")) break;
                    nextUrl = object.get("next").toString();

                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
