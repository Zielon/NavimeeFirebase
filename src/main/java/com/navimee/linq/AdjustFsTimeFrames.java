package com.navimee.linq;

import com.google.common.collect.Sets;
import com.navimee.models.entities.places.foursquare.FsPlaceDetails;
import com.navimee.models.entities.places.foursquare.FsTimeFrame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class AdjustFsTimeFrames {

    private static Set<String> days = Sets.newHashSet(Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));

    public static Function<FsPlaceDetails, FsPlaceDetails> adjust() {
        return fsPlaceDetails -> {
            List<FsTimeFrame> frames = new ArrayList<>();
            fsPlaceDetails.getPopularTimeframes().forEach(frame -> {
                String singleDay = frame.getDays();
                if (singleDay.contains("–")) {
                    Arrays.asList(singleDay.split("–")).forEach(day -> frames.add(new FsTimeFrame(day, frame.getOpen())));
                } else frames.add(frame);
            });

            String today = Sets.difference(days, Sets.newHashSet(frames.stream().map(f -> f.getDays()).collect(toList()))).toArray()[0].toString();
            frames.stream().filter(f -> f.getDays().equals("Today")).collect(toList()).get(0).setDays(today);
            fsPlaceDetails.setPopularTimeframes(frames);
            return fsPlaceDetails;
        };
    }
}
