package com.navimee;

import com.navimee.linq.HotspotFilters;
import com.navimee.models.entities.places.foursquare.FsPlaceDetails;
import com.navimee.models.entities.places.foursquare.popularHours.FsPopular;
import com.navimee.models.entities.places.foursquare.popularHours.FsTimeFrame;
import com.navimee.models.entities.places.foursquare.popularHours.FsTimeOpen;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FsTimeFramesTests {

    public List<FsPlaceDetails> getPlacesDetailsList(){

        List<FsPlaceDetails> placesDetails = new ArrayList<>();

        // [START] PLACE A
        FsPlaceDetails details = new FsPlaceDetails();
        details.setTimeZone("Europe/Warsaw");

        FsPopular popular = new FsPopular();
        List<FsTimeFrame> timeFrames = new ArrayList<>();

        // FIRST TIMEFRAME

        FsTimeFrame timeFrame = new FsTimeFrame();
        List<Integer> days = new ArrayList<>();

        days.add(1);

        List<FsTimeOpen> opens = new ArrayList<>();

        FsTimeOpen timeOpen = new FsTimeOpen();
        timeOpen.setStart("2300");
        timeOpen.setEnd("2330");

        opens.add(timeOpen);

        timeFrame.setDays(days);
        timeFrame.setOpen(opens);
        timeFrames.add(timeFrame);

        // SECOND TIMEFRAME

        timeFrame = new FsTimeFrame();
        days = new ArrayList<>();
        days.add(7);

        opens = new ArrayList<>();

        timeOpen = new FsTimeOpen();
        timeOpen.setStart("1700");
        timeOpen.setEnd("+2200");

        opens.add(timeOpen);

        timeFrame.setDays(days);
        timeFrame.setOpen(opens);
        timeFrames.add(timeFrame);

        // END OF TIMEFRAMES

        popular.setTimeframes(timeFrames);

        details.setPopular(popular);
        placesDetails.add(details);

        // [END] PLACE A

        return placesDetails;
    }


    @Test
    public void FsPopularPlacesOverlappingOpenHoursTest(){
        boolean isPopular = getPlacesDetailsList().stream().filter(HotspotFilters.filterFsPopular()).count() > 0;
        assertThat(isPopular).isEqualTo(true);
    }
}
