package com.navimee.mappers.dtoToBo;

import com.navimee.models.bussinesObjects.places.PlaceBo;
import com.navimee.models.externalDto.places.PlaceDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PlaceDtoMapper {
    PlaceDtoMapper PLACE_DTO_MAPPER = Mappers.getMapper(PlaceDtoMapper.class);

    PlaceBo toPlaceBo(PlaceDto placeDto);

    List<PlaceBo> toPlacesBoList(List<PlaceDto> placeDtoList);
}
