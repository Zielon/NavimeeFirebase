package com.navimee.mappers.boToEntity;

import com.navimee.models.bussinesObjects.places.PlaceBo;
import com.navimee.models.entities.places.Place;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlaceBoMapper {
    PlaceBoMapper PLACE_BO_MAPPER = Mappers.getMapper(PlaceBoMapper.class);

    Place toPlace(PlaceBo placeBo);
}
