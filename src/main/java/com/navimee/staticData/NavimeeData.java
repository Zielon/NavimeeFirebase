package com.navimee.staticData;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navimee.firestore.FirebasePaths;
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

import static com.navimee.firestore.FirebasePaths.AVAILABLE_CITIES;

public class NavimeeData {

    private String COUNTRY = String.format("navimeeData/%s.json", System.getenv().get("COUNTRY").toLowerCase());
    private String CATEGORIES = "navimeeData/foursquare.json";

    private ObjectMapper mapper = new ObjectMapper();

    public List<City> getCities() {
        JSONArray array = getJsonObject(COUNTRY).getJSONArray(AVAILABLE_CITIES);
        return getList(array, City.class);
    }

    public List<String> getEventsDistributors() {
        return getStringList("EVENTS_DISTRIBUTORS", getJsonObject(COUNTRY));
    }

    public List<String> getPlacesBlackList() {
        return getStringList("PLACES_BLACKLIST", getJsonObject(COUNTRY));
    }

    public List<String> getCategories() {
        return getStringList("FOURSQUARE_FORBIDDEN_CATEGORIES", getJsonObject(CATEGORIES));
    }

    public Map<String, List<Coordinate>> getCoordinates() {
        JSONObject object = getJsonObject(COUNTRY).getJSONObject(FirebasePaths.COORDINATES);
        Map<String, List<Coordinate>> coordinates = new HashMap<>();

        object.keySet().forEach(city -> {
            JSONArray array = object.getJSONArray(city.toString());
            coordinates.put(city.toString(), getList(array, Coordinate.class));
        });

        return coordinates;
    }

    private List<String> getStringList(String path, JSONObject object) {
        JSONArray array = object.getJSONArray(path);
        return getList(array, String.class);
    }

    private <T> List<T> getList(JSONArray array, Class<T> type) {
        List<T> list = new ArrayList<>();
        for (int n = 0; n < array.length(); n++) {
            try {
                list.add(mapper.readValue(array.get(n).toString(), type));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private JSONObject getJsonObject(String path) {
        Resource selected = new ClassPathResource(path);

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
}
