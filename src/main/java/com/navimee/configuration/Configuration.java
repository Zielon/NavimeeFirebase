package com.navimee.configuration;

import org.json.JSONObject;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class Configuration {

    protected JSONObject transformConfig(Resource configuration) throws IOException {

        BufferedReader streamReader = new BufferedReader(
                new InputStreamReader(configuration.getInputStream(), "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();
        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) responseStrBuilder.append(inputStr);

        return new JSONObject(responseStrBuilder.toString());
    }
}
