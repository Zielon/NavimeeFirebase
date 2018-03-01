package com.navimee.contracts.services.places;

import com.navimee.foursquareCategories.CategoryTree;

import java.util.concurrent.CompletableFuture;

public interface PlacesDetailsService extends PlacesService {

    CompletableFuture<Void> savePlacesDetails(String city);

    CompletableFuture<CategoryTree> getCategoryTree();
}
