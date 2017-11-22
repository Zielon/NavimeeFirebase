package com.navimee.mappers.enityToBo;

import com.navimee.models.bussinesObjects.places.FsPlaceDetailsBo;
import com.navimee.models.entities.places.FsPlaceDetails;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FsPlaceDetailsEntityMapper {
    FsPlaceDetailsEntityMapper INSTANCE = Mappers.getMapper(FsPlaceDetailsEntityMapper.class);

    FsPlaceDetails toFsPlaceDetails(FsPlaceDetailsBo fbEvent);
}
