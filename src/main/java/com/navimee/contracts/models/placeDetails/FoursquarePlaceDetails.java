package com.navimee.contracts.models.placeDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.navimee.contracts.models.pojos.placeDetails.Popular;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FoursquarePlaceDetails {

    public int checkinsCount;
    public int usersCount;
    public int tipCount;
    public int visitsCount;
    public Popular popular;

    @JsonProperty("stats")
    private void stats(Map<String, String> json) {
        checkinsCount = Integer.parseInt(json.get("checkinsCount"));
        usersCount = Integer.parseInt(json.get("usersCount"));
        tipCount = Integer.parseInt(json.get("tipCount"));
        visitsCount = Integer.parseInt(json.get("visitsCount"));
    }
}
