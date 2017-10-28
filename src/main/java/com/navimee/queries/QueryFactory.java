package com.navimee.queries;

import java.util.HashMap;

public class QueryFactory {

    private HashMap<Class, Query> queries = new HashMap<>();

    public QueryFactory(){
        queries.put(EventsQuery.class, new EventsQuery());
        queries.put(PlacesQuery.class, new PlacesQuery());
    }

    public <T extends Query> T getQuery(Class<T> type){
        return (T)queries.get(type);
    }
}
