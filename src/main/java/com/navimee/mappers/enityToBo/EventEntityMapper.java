package com.navimee.mappers.enityToBo;

import com.navimee.models.bussinesObjects.events.FbEventBo;
import com.navimee.models.entities.events.FbEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EventEntityMapper {
    EventEntityMapper INSTANCE = Mappers.getMapper(EventEntityMapper.class);

    FbEventBo toEvent(FbEvent fbEvent);
}