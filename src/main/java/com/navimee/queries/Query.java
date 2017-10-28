package com.navimee.queries;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public abstract class Query<T> {

    public abstract Future<List<T>> get(String accessToken);

    public <T> List<T> map(JSONObject object, Class<T> type){
        ObjectMapper mapper = new ObjectMapper();
        List<T> list = new ArrayList<>();
        for(Object obj : object.keySet()){
            String key = obj.toString();
            T mapped = null;
            try {
                mapped = mapper.readValue(object.get(key).toString(), type);
            } catch (IOException e) {
                e.printStackTrace();
            }
            list.add(mapped);
        }
        return list;
    }
}
