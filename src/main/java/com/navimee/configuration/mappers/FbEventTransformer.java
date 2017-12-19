package com.navimee.configuration.mappers;

import com.navimee.models.bo.FbEvent;
import com.navimee.models.entities.Event;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class FbEventTransformer {

    public static Converter<FbEvent, Event> get() {
        return new Converter<FbEvent, Event>() {
            public Event convert(MappingContext<FbEvent, Event> context) {
                FbEvent bo = context.getSource();
                Event entity = context.getDestination();

                entity.setId(bo.getId());
                entity.setTitle(bo.getName());
                entity.setDescription(bo.getCategory());
                entity.setCategory(bo.getCategory());
                entity.setTimezone(bo.getTimezone());
                entity.setRank(estimateRanking(bo));
                entity.setGeoPoint(bo.getPlace().getGeoPoint());
                entity.setStartTime(bo.getStartTime());
                entity.setEndTime(bo.getEndTime());
                entity.setHotspotType(bo.getHotspotType());
                entity.setSource(bo.getSource());
                entity.setPlace(bo.getPlace());

                return entity;
            }
        };
    }

    private static int estimateRanking(FbEvent event) {
        double z = (0.8 * event.getAttendingCount() + 0.2 * event.getMaybeCount()) / 10000.0;
        return (int) ((1 / (1 + Math.pow(Math.E, -z))) * 100.0);
    }
}
