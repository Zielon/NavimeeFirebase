package com.navimee.mappers.dtoToBo;

import com.navimee.models.bussinesObjects.places.PlaceBo;
import com.navimee.models.externalDto.places.FbPlaceDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FbPlaceDtoMapper {
    FbPlaceDtoMapper FB_PLACE_DTO_MAPPER = Mappers.getMapper(FbPlaceDtoMapper.class);

    PlaceBo toPlaceBo(FbPlaceDto placeDto);

    PlaceBo [] toPlacesBoList(List<FbPlaceDto> placeDtoList);
}
