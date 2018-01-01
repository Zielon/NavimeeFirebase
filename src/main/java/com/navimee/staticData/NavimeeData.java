package com.navimee.staticData;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navimee.models.entities.coordinates.City;
import com.navimee.models.entities.coordinates.Coordinate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NavimeeData {

    private JSONObject getJsonObject(StaticDataEnum mocks) {
        Resource selected = null;

        switch (mocks) {
            case Cities:
                selected = new ClassPathResource("navimeeData/availableCities.json");
                break;
            case Coordinates:
                selected = new ClassPathResource("navimeeData/coordinates.json");
                break;
            case Categories:
                selected = new ClassPathResource("navimeeData/forbiddenCategories.json");
                break;
        }

        BufferedReader streamReader = null;
        try {
            streamReader = new BufferedReader(
                    new InputStreamReader(selected.getInputStream(), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder responseStrBuilder = new StringBuilder();
        String inputStr;
        try {
            while ((inputStr = streamReader.readLine()) != null) responseStrBuilder.append(inputStr);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new JSONObject(responseStrBuilder.toString());
    }

    public List<City> getCities() {
        JSONObject object = getJsonObject(StaticDataEnum.Cities);
        ObjectMapper mapper = new ObjectMapper();
        List<City> cities = new ArrayList<>();
        object.keySet().forEach(e -> {
            City c = null;
            try {
                c = mapper.readValue(object.getJSONObject(e.toString()).toString(), City.class);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            cities.add(c);
        });

        return cities;
    }

    public List<String> getCategories() {
        JSONObject object = getJsonObject(StaticDataEnum.Categories);
        JSONArray array = object.getJSONArray("categories");
        List<String> list = new ArrayList<>();
        for (int n = 0; n < array.length(); n++) {
            JSONObject json = array.getJSONObject(n);
            list.add(json.get("id").toString().toUpperCase());
        }
        return list;
    }

    public Map<String, List<Coordinate>> getCoordinates() {
        JSONObject object = getJsonObject(StaticDataEnum.Coordinates);
        Map<String, List<Coordinate>> coordinates = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        object.keySet().forEach(city -> {
            try {
                final List<Coordinate> coords =
                        mapper.readValue(
                                object.getJSONObject(city.toString()).getJSONArray("points").toString(),
                                new TypeReference<List<Coordinate>>() {
                                });
                int i = 0;
                for (Coordinate c : coords) c.setId(Integer.toString(i++));
                coordinates.put(city.toString(), coords);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        return coordinates;
    }
}
