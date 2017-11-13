package com.navimee.places.queries;

import com.navimee.configuration.specific.FoursquareConfiguration;
import com.navimee.contracts.models.places.FoursquareHotPlace;
import com.navimee.places.queries.params.PlacesParams;
import com.navimee.queries.Query;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.Future;

public class FoursquareHotPlacesQuery extends Query<FoursquareHotPlace, FoursquareConfiguration, PlacesParams> {

    public FoursquareHotPlacesQuery(FoursquareConfiguration configuration) {
        super(configuration);
    }

    @Override
    public Future<List<FoursquareHotPlace>> execute(PlacesParams params) {
        return null;
    }

    @Override
    protected List<FoursquareHotPlace> map(JSONObject object) {
        return null;
    }
}
