package com.navimee.mockups;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navimee.contracts.models.firestore.City;
import com.navimee.contracts.models.firestore.Coordinates;
import com.navimee.contracts.models.places.Coordinate;
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

    private JSONObject getJsonObject(Mocks mocks){
        Resource seleted = mocks == Mocks.Cities ? new ClassPathResource("NavimeeData/availableCities.json")
                : new ClassPathResource("NavimeeData/coordinates.json");
        BufferedReader streamReader = null;
        try {
            streamReader = new BufferedReader(
                    new InputStreamReader(seleted.getInputStream(), "UTF-8"));
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

    public List<City> getCities(){
        JSONObject object = getJsonObject(Mocks.Cities);
        ObjectMapper mapper = new ObjectMapper();
        List<City> cities = new ArrayList<>();
        object.keySet().stream().forEach(e -> {
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

    public Map<String, List<Coordinate>> getCoordinates(){
        JSONObject object = getJsonObject(Mocks.Coordinates);
        Map<String, List<Coordinate>> coordinates = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        object.keySet().stream().forEach(e -> {
            Coordinates c = null;
            try {
                c = mapper.readValue(object.getJSONObject(e.toString()).toString(), Coordinates.class);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            coordinates.put(e.toString(), c.points);
        });
        return coordinates;
    }
}
