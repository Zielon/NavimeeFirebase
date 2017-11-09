package com.navimee.queries;

import com.navimee.configuration.Configuration;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.Future;

public abstract class Query<T, C extends Configuration, P extends QueryParams> {

    protected C configuration;

    public Query(C configuration) {
        this.configuration = configuration;
    }

    public abstract Future<List<T>> execute(P params);

    protected abstract List<T> map(JSONObject object, Class<T> type);
}
