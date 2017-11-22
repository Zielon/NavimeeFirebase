package com.navimee.models.entities.places;

import java.util.List;

public class FsPlaceDetails extends Place {
    public String id;
    public String name;
    public double rating;

    public int likesCount;
    public String likesSummary;

    public int statsCheckinsCount;
    public int statsUsersCount;
    public int statsTipCount;
    public int statsVisitsCount;

    public String locationAddress;
    public String locationCrossStreet;
    public String locationPostalCode;
    public String locationCity;
    public String locationCountry;
    public double locationLat;
    public double locationLng;

    public String popularStatus;
    public boolean popularIsOpen;
    public boolean popularIsLocalHoliday;
    public List<FsTimeFrame> popularTimeframes;
}
