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
        DateTime warsaw = DateTime.now(DateTimeZone.UTC);
        int currentDay = warsaw.getDayOfWeek();
        FsTimeFrame timeFrame = popular.getTimeframes().stream().filter(frame -> frame.getDays().contains(currentDay)).findFirst().get();

        return timeFrame.getOpen().stream().anyMatch(time -> {
            DateTime start = fmt.parseDateTime(time.getStart());
            DateTime end = fmt.parseDateTime(time.getEnd().contains("+") ? time.getEnd().substring(1) : time.getEnd());
            
            if(time.getEnd().contains("+"))
                end = end.plusDays(1);

            return isNowPopular(start, end, warsaw);
        });
    }

    private static boolean isNowPopular(DateTime start, DateTime end, DateTime warsaw) {
        DateTime current = fmt.parseDateTime(fmt.print(warsaw));
        return start.getMillis() <= current.getMillis() && end.getMillis() >= current.getMillis();
    }
}
