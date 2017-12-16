package com.navimee.configuration.mappers;

import com.navimee.models.bo.FbEvent;
import com.navimee.models.entities.HotspotEvent;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class FbEventTransformer {

    public static Converter<FbEvent, HotspotEvent> get() {
        return new Converter<FbEvent, HotspotEvent>() {
            public HotspotEvent convert(MappingContext<FbEvent, HotspotEvent> context) {
                FbEvent bo = context.getSource();
                HotspotEvent entity = context.getDestination();

                DateTimeZone tz = DateTimeZone.forID("Europe/Warsaw");

                DateTime startDate = new DateTime(bo.getStartTime(), DateTimeZone.UTC).withZone(tz);
                DateTime endDate = new DateTime(bo.getEndTime(), DateTimeZone.UTC).withZone(tz);

                entity.setId(bo.getId());
                entity.setTitle(bo.getName());
                entity.setDescription(bo.getCategory());
                entity.setCategory(bo.getCategory());
                entity.setTimezone(bo.getTimezone());
                entity.setRank(estimateRanking(bo));
                entity.setGeoPoint(bo.getPlace().getGeoPoint());
                entity.setStartTime(startDate.toDate());
                entity.setEndTime(endDate.toDate());
                entity.setHotspotType(bo.getHotspotType());
                entity.setSource(bo.getSource());

                return entity;
            }
        };
    }

    private static int estimateRanking(FbEvent event) {
        double z = (0.8 * event.getAttendingCount() + 0.2 * event.getMaybeCount()) / 1000.0;
        return (int) ((1 / (1 + Math.pow(Math.E, -z))) * 100.0);
    }
}
