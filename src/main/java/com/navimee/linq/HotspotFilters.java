package com.navimee.linq;

import com.navimee.models.entities.events.FbEvent;
import com.navimee.models.entities.places.foursquare.FsPlaceDetails;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;

import java.util.function.Predicate;

public class HotspotFilters {

    public static Predicate<FsPlaceDetails> filterFsPopular() {
        return fsPlaceDetails -> {
            return true;
        };
    }

    public static Predicate<FbEvent> filterFbEvents() {
        DateTime warsaw = LocalDateTime.now(DateTimeZone.forID("Europe/Warsaw")).toDateTime();
        return event -> new DateTime(event.getEndTime()).isBefore(warsaw.plusMinutes(30));
    }
}
