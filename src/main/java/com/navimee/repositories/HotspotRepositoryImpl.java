package com.navimee.repositories;

import com.google.cloud.firestore.Firestore;
import com.navimee.contracts.repositories.HotspotRepository;
import com.navimee.firestore.Paths;
import com.navimee.firestore.operations.Add;
import com.navimee.models.entities.Hotspot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.Future;

@Repository
public class HotspotRepositoryImpl implements HotspotRepository {

    @Autowired
    Firestore db;

    @Override
    public Future setHotspot(List<Hotspot> hotspots) {
        return Add.toCollection(db.collection(Paths.HOTSPOT), hotspots);
    }
}
