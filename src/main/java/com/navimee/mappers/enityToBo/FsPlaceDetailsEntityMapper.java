package com.navimee.mappers.enityToBo;

import com.navimee.models.bussinesObjects.places.FsPlaceDetailsBo;
import com.navimee.models.entities.places.FsPlaceDetails;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FsPlaceDetailsEntityMapper {
    FsPlaceDetailsEntityMapper FS_PLACE_DETAILS_ENTITY_MAPPER = Mappers.getMapper(FsPlaceDetailsEntityMapper.class);

    FsPlaceDetails toFsPlaceDetails(FsPlaceDetailsBo fbEvent);
}
