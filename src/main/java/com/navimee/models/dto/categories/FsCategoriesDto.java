package com.navimee.models.dto.categories;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.navimee.models.dto.Dto;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FsCategoriesDto implements Dto {
    private String id;
    private String name;
    private List<FsCategoriesDto> categories;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toUpperCase();
    }

    public List<FsCategoriesDto> getCategories() {
        return categories;
    }

    public void setCategories(List<FsCategoriesDto> categories) {
        this.categories = categories;
    }
}
