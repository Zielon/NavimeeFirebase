package com.navimee.contracts.models.placeDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.navimee.contracts.models.placeDetails.pojo.*;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FoursquarePlaceDetails {

    public String id;
    public String name;
    public double rating;
    public Location location;
    public Stats stats;
    public Popular popular;
    public Likes likes;
    public List<Category> categories;
}
