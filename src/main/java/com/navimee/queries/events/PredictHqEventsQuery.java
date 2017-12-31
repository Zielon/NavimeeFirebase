package com.navimee.queries.events;

import com.navimee.configuration.specific.PredictHqConfiguration;
import com.navimee.contracts.services.HttpClient;
import com.navimee.general.JSON;
import com.navimee.models.dto.events.PhqEventDto;
import com.navimee.queries.Query;
import com.navimee.queries.events.params.PredictHqEventsParams;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class PredictHqEventsQuery extends Query<List<PhqEventDto>, PredictHqConfiguration, PredictHqEventsParams> {

    public PredictHqEventsQuery(PredictHqConfiguration configuration, ExecutorService executorService, HttpClient httpClient) {
        super(configuration, executorService, httpClient);
    }

    @Override
    public Callable<List<PhqEventDto>> execute(PredictHqEventsParams params) {

        DateTime warsawCurrent = DateTime.now(DateTimeZone.UTC);
        DateTime warsawLater = warsawCurrent.plusDays(14);
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");

        URI uri = null;
        try {
            URIBuilder builder = new URIBuilder(configuration.apiUrl + "/v1/events/");
            builder.setParameter("start.gte", dtf.print(warsawCurrent));
            builder.setParameter("start.lte", dtf.print(warsawLater));
            builder.setParameter("within", String.format("2km@%f,%f", params.lat, params.lon));
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
