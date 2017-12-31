package com.navimee.linq;

import com.navimee.models.entities.places.foursquare.FsPlaceDetails;
import com.navimee.models.entities.places.foursquare.popularHours.FsTimeFrame;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.function.Predicate;

public class HotspotFilters {

    private static DateTimeFormatter fmt = DateTimeFormat.forPattern("HHmm");

    public static Predicate<FsPlaceDetails> filterFsPopular() {
        return HotspotFilters::isPopular;
    }

    private static boolean isPopular(FsPlaceDetails details) {
        DateTime placeTime = DateTime.now(DateTimeZone.forID(details.getTimeZone()));
        int currentDay = placeTime.getDayOfWeek();
        FsTimeFrame timeFrame = details.getPopular().getTimeframes().stream().filter(frame -> frame.getDays().contains(currentDay)).findFirst().get();

        return timeFrame.getOpen().stream().anyMatch(time -> {
            DateTime start = fmt.parseDateTime(time.getStart());
            DateTime end = fmt.parseDateTime(time.getEnd().contains("+") ? time.getEnd().substring(1) : time.getEnd());

            if (time.getEnd().contains("+"))
                end = end.plusDays(1);

            return isNowPopular(start, end, placeTime);
        });
    }

    private static boolean isNowPopular(DateTime start, DateTime end, DateTime warsaw) {
        DateTime current = fmt.parseDateTime(fmt.print(warsaw));
        return start.getMillis() <= current.getMillis() && end.getMillis() >= current.getMillis();
    }
}