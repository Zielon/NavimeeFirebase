package com.navimee.general;

import org.json.JSONObject;

public class JSON {
    public static boolean hasPaging(JSONObject jsonObject) {
        return jsonObject.has("paging") && jsonObject.has("next");
    }
}
