package com.navimee.contracts.models.dataTransferObjects.firestore;

import com.navimee.contracts.models.dataTransferObjects.BaseDto;

public class CityDto extends BaseDto {

    public String id;
    public String name;

    public CityDto() {
    }

    public CityDto(String name) {
        this.name = name;
    }
}
