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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class FoursquareCategoryQuery extends Query<List<FsCategoriesDto>, FoursquareConfiguration, QueryParams> {

    public FoursquareCategoryQuery(FoursquareConfiguration configuration,
                                   ExecutorService executorService,
                                   HttpClient httpClient) {
        super(configuration, executorService, httpClient);
    }

    @Override
    public CompletableFuture<List<FsCategoriesDto>> execute(QueryParams params) {

        DateTime warsawCurrent = DateTime.now(DateTimeZone.UTC);
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyyddMM");

        URI uri = null;
        try {
            URIBuilder builder = new URIBuilder(configuration.getApiUrl());
            builder.setPath("v2/venues/categories");
            builder.setParameter("v", dtf.print(warsawCurrent));
            builder.setParameter("client_id", configuration.getClientId());
            builder.setParameter("client_secret", configuration.getClientSecret());
            uri = builder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        URI finalUri = uri;
        return CompletableFuture.supplyAsync(() -> map(httpClient.GET(finalUri), params), executorService);
    }

    @Override
    protected List<FsCategoriesDto> map(CompletableFuture<JSONObject> task, QueryParams params) {
        List<FsCategoriesDto> output = null;
        try {
            JSONObject object = task.join();
            output = JSON.arrayMapper(object.getJSONObject("response").getJSONArray("categories"), FsCategoriesDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}
