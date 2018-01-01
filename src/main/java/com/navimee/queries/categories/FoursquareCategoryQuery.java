package com.navimee.queries.categories;

import com.navimee.configuration.specific.FoursquareConfiguration;
import com.navimee.contracts.services.HttpClient;
import com.navimee.general.JSON;
import com.navimee.models.dto.categories.FsCategoriesDto;
import com.navimee.queries.Query;
import com.navimee.queries.QueryParams;
import org.apache.http.client.utils.URIBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class FoursquareCategoryQuery extends Query<List<FsCategoriesDto>, FoursquareConfiguration, QueryParams> {

    public FoursquareCategoryQuery(FoursquareConfiguration configuration, ExecutorService executorService, HttpClient httpClient) {
        super(configuration, executorService, httpClient);
    }

    @Override
    public Callable<List<FsCategoriesDto>> execute(QueryParams params) {

        DateTime warsawCurrent = DateTime.now(DateTimeZone.UTC);
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyyddMM");

        URI uri = null;

        try {
            URIBuilder builder = new URIBuilder(configuration.apiUrl);
            builder.setPath("v2/venues/categories");
            builder.setParameter("v", dtf.print(warsawCurrent));
            builder.setParameter("client_id", configuration.clientId);
            builder.setParameter("client_secret", configuration.clientSecret);
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        URI finalUri = uri;
        return () -> map(httpClient.GET(finalUri), params);
    }

    @Override
    protected List<FsCategoriesDto> map(Callable<JSONObject> task, QueryParams params) {
        List<FsCategoriesDto> output = null;
        try {
            JSONObject object = task.call();
            output = JSON.arrayMapper(object.getJSONObject("response").getJSONArray("categories"), FsCategoriesDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}
