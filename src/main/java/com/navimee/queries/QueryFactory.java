package com.navimee.queries;

import java.util.HashMap;

public class QueryFactory {

    private static HashMap<Class, Query> queries = new HashMap<>();

    static {
        queries.put(EventsQuery.class, new EventsQuery());
        queries.put(PlacesQuery.class, new PlacesQuery());
    }

    public static <T extends Query> T getQuery(Class<T> type){
        return (T)queries.get(type);
    }
}
