package com.navimee.mappers.boToEntity;

import com.navimee.models.bussinesObjects.places.FsPlaceBo;
import com.navimee.models.entities.places.Place;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FsPlaceBoMapper {
    FsPlaceBoMapper FS_PLACE_BO_MAPPER = Mappers.getMapper(FsPlaceBoMapper.class);

    Place toPlace(FsPlaceBo fsPlaceBo);
}