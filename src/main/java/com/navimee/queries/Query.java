package com.navimee.queries;

import com.navimee.configuration.Configuration;
import org.json.JSONObject;

import java.util.concurrent.Future;

public abstract class Query<T, C extends Configuration, P extends QueryParams> {

    protected C configuration;

    public Query(C configuration) {
        this.configuration = configuration;
    }

    public abstract Future<T> execute(P params);

    protected abstract T map(JSONObject object);
}
