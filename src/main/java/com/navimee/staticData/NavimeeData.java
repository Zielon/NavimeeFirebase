package com.navimee.staticData;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navimee.firestore.FirebasePaths;
import com.navimee.general.JSON;
import com.navimee.models.entities.chat.Room;
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
import static com.navimee.general.StringNormalizer.stripAccents;

public class NavimeeData {

    private String COUNTRY = String.format("navimeeData/%s.json", System.getenv().get("COUNTRY").toLowerCase());
    private String CATEGORIES = "navimeeData/foursquare.json";

    private ObjectMapper mapper = new ObjectMapper();

    public List<City> getCities() {
        JSONArray array = getJsonObject(COUNTRY).getJSONArray(AVAILABLE_CITIES);
        return JSON.arrayMapper(array, City.class);
    }

    public List<String> getEventsDistributors() {
        return getStringList("EVENTS_DISTRIBUTORS", getJsonObject(COUNTRY));
    }

    public List<String> getPlacesBlackList() {
        return getStringList("PLACES_BLACKLIST", getJsonObject(COUNTRY));
    }

    public List<Room> getChatDefault() {
        JSONArray array = getJsonObject(COUNTRY).getJSONArray("CHAT_DEFAULT");
        return JSON.arrayMapper(array, Room.class);
    }

    public List<String> getCategories() {
        return getStringList("FOURSQUARE_FORBIDDEN_CATEGORIES", getJsonObject(CATEGORIES));
    }

    public Map<String, List<Coordinate>> getCoordinates() {
        JSONObject object = getJsonObject(COUNTRY).getJSONObject(FirebasePaths.COORDINATES);
        Map<String, List<Coordinate>> coordinates = new HashMap<>();

        object.keySet().forEach(city -> {
            JSONArray array = object.getJSONArray(city.toString());
            coordinates.put(stripAccents(city.toString()), JSON.arrayMapper(array, Coordinate.class));
        });

        return coordinates;
    }

    private List<String> getStringList(String path, JSONObject object) {
        JSONArray array = object.getJSONArray(path);
        List<String> list = new ArrayList<>();
        for (int n = 0; n < array.length(); n++) {
            list.add(array.get(n).toString());
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
