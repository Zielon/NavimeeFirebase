package com.navimee.linq;

import com.google.common.collect.Sets;
import com.navimee.models.entities.places.foursquare.FsPlaceDetails;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;

public class AdjustFsTimeFrames {

    private static Set<String> days = Sets.newHashSet(Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));

    public static Function<FsPlaceDetails, FsPlaceDetails> adjust() {
        return fsPlaceDetails -> {
            return fsPlaceDetails;
        };
    }
}
