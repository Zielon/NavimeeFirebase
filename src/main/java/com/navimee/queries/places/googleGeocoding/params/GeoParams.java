package com.navimee.queries.places.googleGeocoding.params;

import com.google.firebase.database.utilities.Pair;
import com.navimee.queries.QueryParams;

import java.util.ArrayList;
import java.util.List;

public class GeoParams implements QueryParams {
    public List<Pair<String, String>> paramsList;

    public GeoParams(){
        paramsList = new ArrayList<>();
    }

    public GeoParams add(String param, String value){
        paramsList.add(new Pair<>(param, value));
        return this;
    }
}
