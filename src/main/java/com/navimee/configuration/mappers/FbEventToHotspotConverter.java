package com.navimee.configuration.mappers;

import com.navimee.models.entities.Hotspot;
import com.navimee.models.entities.events.FbEvent;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class FbEventToHotspotConverter {

    public static Converter<FbEvent, Hotspot> getConverter() {
        return new Converter<FbEvent, Hotspot>() {
            public Hotspot convert(MappingContext<FbEvent, Hotspot> context) {
                FbEvent event = context.getSource();
                Hotspot entity = context.getDestination();

                entity.setId(event.getId());
                entity.setReference(event.getReference());

                return entity;
            }
        };
    }
}
