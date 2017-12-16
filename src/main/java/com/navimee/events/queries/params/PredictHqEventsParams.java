package com.navimee.events.queries.params;

import com.navimee.models.entities.coordinates.Coordinate;
import com.navimee.queries.QueryParams;

import java.util.List;

public class PredictHqEventsParams implements QueryParams {
    public List<Coordinate> coordinates;

    public PredictHqEventsParams(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }
}
