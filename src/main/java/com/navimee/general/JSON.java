package com.navimee.general;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSON {
    public static boolean hasPaging(JSONObject jsonObject) {
        return jsonObject.has("paging") && jsonObject.has("next");
    }

    public static String getNext(JSONObject jsonObject) {
        return jsonObject.getJSONObject("paging").getString("next");
    }

    public static <T> List<T> arrayMapper(JSONArray array, Class<T> type) {
        List<T> list = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());

        for (int n = 0; n < array.length(); n++) {
            JSONObject json = array.getJSONObject(n);
            try {
                T mapped = mapper.readValue(json.toString(), type);
                list.add(mapped);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
