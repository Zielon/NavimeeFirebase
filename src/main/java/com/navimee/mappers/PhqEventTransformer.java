package com.navimee.mappers;

import com.navimee.models.bo.PhqEvent;
import com.navimee.models.entities.Event;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class PhqEventTransformer {

    public static Converter<PhqEvent, Event> get() {
        return new Converter<PhqEvent, Event>() {
            public Event convert(MappingContext<PhqEvent, Event> context) {
                PhqEvent bo = context.getSource();
                Event entity = context.getDestination();

                entity.setId(bo.getId());
                entity.setTitle(bo.getTitle());
                entity.setDescription(bo.getDescription());
                entity.setCategory(bo.getCategory());
                entity.setTimezone(bo.getTimezone());
                entity.setRank(bo.getRank());
                entity.setGeoPoint(bo.getGeoPoint());
                entity.setStartTime(bo.getStartTime());
                entity.setEndTime(bo.getEndTime());
                entity.setHotspotType(bo.getHotspotType());
                entity.setSource(bo.getSource());

                return entity;
            }
        };
    }
}
