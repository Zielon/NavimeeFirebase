package com.navimee.queries;

import com.navimee.configuration.Configuration;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.Future;

public abstract class Query<T> {
    public abstract Future<List<T>> get(Configuration configuration);
    protected abstract List<T> map(JSONObject object, Class<T> type);
}
