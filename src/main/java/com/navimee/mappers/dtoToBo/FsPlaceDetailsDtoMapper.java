package com.navimee.mappers.dtoToBo;

import com.navimee.models.bussinesObjects.places.FsPlaceDetailsBo;
import com.navimee.models.externalDto.placeDetails.FsPlaceDetailsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FsPlaceDetailsDtoMapper {

    FsPlaceDetailsDtoMapper INSTANCE = Mappers.getMapper(FsPlaceDetailsDtoMapper.class);

    @Mappings({
            // LOCATION
            @Mapping(source = "location.address", target = "locationAddress"),
            @Mapping(source = "location.crossStreet", target = "locationCrossStreet"),
            @Mapping(source = "location.postalCode", target = "locationPostalCode"),
            @Mapping(source = "location.city", target = "locationCity"),
            @Mapping(source = "location.country", target = "locationCountry"),
            @Mapping(source = "location.lat", target = "locationLat"),
            @Mapping(source = "location.lng", target = "locationLng"),
            // STATS
            @Mapping(source = "stats.checkinsCount", target = "statsCheckinsCount"),
            @Mapping(source = "stats.usersCount", target = "statsUsersCount"),
            @Mapping(source = "stats.tipCount", target = "statsTipCount"),
            @Mapping(source = "stats.visitsCount", target = "statsVisitsCount"),
    })
    FsPlaceDetailsBo toFsPlaceDetailsBo(FsPlaceDetailsDto fsPlaceDetailsDto);
}
