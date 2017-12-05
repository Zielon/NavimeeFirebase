package com.navimee.contracts.repositories;

import com.navimee.models.entities.Hotspot;

import java.util.List;
import java.util.concurrent.Future;

public interface HotspotRepository {
    Future setHotspot(List<Hotspot> hotspots);
}
