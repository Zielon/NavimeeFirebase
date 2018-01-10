package com.navimee.linq;

import com.navimee.models.entities.places.foursquare.FsPlaceDetails;
import com.navimee.models.entities.places.foursquare.popularHours.FsTimeFrame;
import com.navimee.models.entities.places.foursquare.popularHours.FsTimeOpen;
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
        int dayBefore = currentDay == 1 ? 7 : currentDay - 1;

        FsTimeFrame currentTimeFrame = details.getPopular()
                .getTimeframes().stream()
                .filter(frame -> frame.getDays().contains(currentDay))
                .findFirst().orElse(null);

        FsTimeFrame beforeTimeFrame = details.getPopular()
                .getTimeframes().stream()
                .filter(frame -> frame.getDays().contains(dayBefore))
                .findFirst().orElse(null);

        boolean isNowPopular = false;

        if (currentTimeFrame != null)
            isNowPopular = currentTimeFrame.getOpen().stream().anyMatch(nowPopular(placeTime));

        boolean isFromDayBeforePopular = false;

        if (beforeTimeFrame != null)
            isFromDayBeforePopular = beforeTimeFrame.getOpen().stream().anyMatch(fromDayBeforePopular(placeTime));

        return isNowPopular || isFromDayBeforePopular;
    }

    private static Predicate<FsTimeOpen> nowPopular(DateTime placeTime) {
        return time -> {
            DateTime start = fmt.parseDateTime(time.getStart());
            DateTime end = fmt.parseDateTime(time.getEnd().contains("+") ? time.getEnd().substring(1) : time.getEnd());

            if (time.getEnd().contains("+"))
                end = end.plusDays(1);

            DateTime current = fmt.parseDateTime(fmt.print(placeTime));
            return start.getMillis() <= current.getMillis() && end.getMillis() >= current.getMillis();
        };
    }

    private static Predicate<FsTimeOpen> fromDayBeforePopular(DateTime placeTime) {
        return time -> {
            if (!time.getEnd().contains("+")) return false;

            DateTime end = fmt.parseDateTime(time.getEnd().contains("+") ? time.getEnd().substring(1) : time.getEnd());

            DateTime current = fmt.parseDateTime(fmt.print(placeTime));
            return end.getMillis() >= current.getMillis();
        };
    }
}