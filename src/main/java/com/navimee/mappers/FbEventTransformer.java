package com.navimee.mappers;

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
        int z = (int)(1.25 * event.getAttendingCount() + 1.25 * event.getMaybeCount());

        if(z >= 4000) return 5;
        if(z >= 3000) return 4;
        if(z >= 2000) return 3;
        if(z >= 1000) return 2;

        return 1;
    }
}
