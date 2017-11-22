package com.navimee.mappers.dtoToBo;

import com.navimee.models.bussinesObjects.events.FbEventBo;
import com.navimee.models.externalDto.events.FbEventDto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EventDtoMapper {
    EventDtoMapper EVENT_DTO_MAPPER = Mappers.getMapper(EventDtoMapper.class);

    FbEventBo toEventBo(FbEventDto eventDto);
}
