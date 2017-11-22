package com.navimee.mappers.dtoToBo;

import com.navimee.models.bussinesObjects.places.FsPlaceBo;
import com.navimee.models.externalDto.places.FsPlaceDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FsPlaceDtoMapper {
    FsPlaceDtoMapper FS_PLACE_DTO_MAPPER = Mappers.getMapper(FsPlaceDtoMapper.class);

    FsPlaceBo toFsPlaceBo(FsPlaceDto fsPlaceDto);
}
