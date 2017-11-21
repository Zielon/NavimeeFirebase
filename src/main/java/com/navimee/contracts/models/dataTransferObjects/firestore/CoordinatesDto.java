package com.navimee.contracts.models.dataTransferObjects.firestore;

import com.navimee.contracts.models.dataTransferObjects.BaseDto;
import com.navimee.contracts.models.dataTransferObjects.places.subelement.CoordinateDto;

import java.util.List;

public class CoordinatesDto extends BaseDto {
    public List<CoordinateDto> points;
}
