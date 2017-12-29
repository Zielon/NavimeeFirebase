package com.navimee.mappers;

import com.navimee.models.dto.placeDetails.FsPlaceDetailsDto;
import com.navimee.models.entities.places.foursquare.FsPlaceDetails;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import static java.util.stream.Collectors.toList;

public class FsPlacesDetailsTransformer {

    public static Converter<FsPlaceDetailsDto, FsPlaceDetails> get() {
        return new Converter<FsPlaceDetailsDto, FsPlaceDetails>() {
            public FsPlaceDetails convert(MappingContext<FsPlaceDetailsDto, FsPlaceDetails> context) {
                FsPlaceDetailsDto dto = context.getSource();
                FsPlaceDetails entity = context.getDestination();

                // General
                entity.setId(dto.id);
                entity.setName(dto.name);
                entity.setRating(dto.rating);
                entity.setTimeZone(dto.timeZone);

                // Likes
                if (dto.likes != null) {
                    entity.setLikesCount(dto.likes.count);
                    entity.setLikesSummary(dto.likes.summary);
                }

                // Location
                if (dto.location != null) {
                    entity.setLocationAddress(dto.location.address);
                    entity.setLocationCity(dto.location.city);
                    entity.setLocationCountry(dto.location.country);
                    entity.setLocationCrossStreet(dto.location.crossStreet);
                    entity.setLocationPostalCode(dto.location.postalCode);
                    entity.setLocationLat(dto.location.lat);
                    entity.setLocationLng(dto.location.lng);
                }

                // Stats
                if (dto.stats != null) {
                    entity.setStatsCheckinsCount(dto.stats.checkinsCount);
                    entity.setStatsTipCount(dto.stats.tipCount);
                    entity.setStatsUsersCount(dto.stats.usersCount);
                    entity.setStatsVisitsCount(dto.stats.visitsCount);
                }

                // Category
                if (dto.categories != null) {
                    entity.setCategories(dto.categories.stream().map(c -> c.name).collect(toList()));
                }

                return entity;
            }
        };
    }

}
