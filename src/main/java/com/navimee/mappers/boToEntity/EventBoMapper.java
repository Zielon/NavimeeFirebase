package com.navimee.mappers.boToEntity;

import com.navimee.models.bussinesObjects.events.FbEventBo;
import com.navimee.models.entities.events.FbEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EventBoMapper {
    EventBoMapper EVENT_BO_MAPPER = Mappers.getMapper(EventBoMapper.class);

    FbEvent toEvent(FbEventBo fbEventBo);
}
