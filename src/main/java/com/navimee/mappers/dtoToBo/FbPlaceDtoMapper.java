package com.navimee.mappers.dtoToBo;

import com.navimee.models.bussinesObjects.places.PlaceBo;
import com.navimee.models.externalDto.places.FbPlaceDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FbPlaceDtoMapper {
    FbPlaceDtoMapper INSTANCE = Mappers.getMapper(FbPlaceDtoMapper.class);

    PlaceBo toPlaceBo(FbPlaceDto placeDto);

    List<PlaceBo> toPlacesBoList(List<FbPlaceDto> placeDtoList);
}
