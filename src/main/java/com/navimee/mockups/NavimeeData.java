package com.navimee.mockups;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navimee.contracts.models.dataTransferObjects.firestore.CityDto;
import com.navimee.contracts.models.dataTransferObjects.firestore.CoordinatesDto;
import com.navimee.contracts.models.dataTransferObjects.places.subelement.CoordinateDto;
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

    private JSONObject getJsonObject(Mocks mocks) {
        Resource seleted = mocks == Mocks.Cities ? new ClassPathResource("navimeeData/availableCities.json")
                : new ClassPathResource("navimeeData/coordinates.json");
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

    public List<CityDto> getCities() {
        JSONObject object = getJsonObject(Mocks.Cities);
        ObjectMapper mapper = new ObjectMapper();
        List<CityDto> cities = new ArrayList<>();
        object.keySet().stream().forEach(e -> {
            CityDto c = null;
            try {
                c = mapper.readValue(object.getJSONObject(e.toString()).toString(), CityDto.class);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            cities.add(c);
        });

        return cities;
    }

    public Map<String, List<CoordinateDto>> getCoordinates() {
        JSONObject object = getJsonObject(Mocks.Coordinates);
        Map<String, List<CoordinateDto>> coordinates = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        object.keySet().stream().forEach(e -> {
            try {
                final CoordinatesDto c = mapper.readValue(object.getJSONObject(e.toString()).toString(), CoordinatesDto.class);
                coordinates.put(e.toString(), c.points);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        return coordinates;
    }
}
