package com.navimee.linq;

import com.navimee.models.entities.places.foursquare.FsPlaceDetails;
import com.navimee.models.entities.places.foursquare.popularHours.FsPopular;
import com.navimee.models.entities.places.foursquare.popularHours.FsTimeFrame;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.function.Predicate;

public class HotspotFilters {

    private static DateTimeFormatter fmt = DateTimeFormat.forPattern("HHmm");

    public static Predicate<FsPlaceDetails> filterFsPopular() {
        return fsPlaceDetails -> isPopular(fsPlaceDetails.getPopular());
    }

    private static boolean isPopular(FsPopular popular) {
        DateTime warsaw = LocalDateTime.now(DateTimeZone.forID("Europe/Warsaw")).toDateTime();
        int currentDay = warsaw.getDayOfWeek();
        FsTimeFrame timeFrame = popular.getTimeframes().stream().filter(frame -> frame.getDays().contains(currentDay)).findFirst().get();

        return timeFrame.getOpen().stream().anyMatch(time -> {
            String s = time.getStart().length() > 4 ? time.getStart().substring(1) : time.getStart();
            String e = time.getEnd().length() > 4 ? time.getEnd().substring(1) : time.getEnd();
            DateTime start = fmt.parseDateTime(s);
            DateTime end = fmt.parseDateTime(e);
            return isNowPopular(start, end, warsaw);
        });
    }

    private static boolean isNowPopular(DateTime start, DateTime end, DateTime warsaw) {
        DateTime current = fmt.parseDateTime(fmt.print(warsaw));
        DateTime earlier = start.isBefore(end) ? start : end;
        DateTime later = start.isBefore(end) ? end : start;
        return earlier.getMinuteOfDay() <= current.getMinuteOfDay() && later.getMinuteOfDay() >= current.getMinuteOfDay();
    }
}
