package com.badon.brigham.notify.lifx;

import org.json.JSONException;
import org.json.JSONObject;

public class Light {

    private String mId;
    private String mName;

    public Light(String id, String name) {
        mId = id;
        mName = name;
    }

    public Light(JSONObject object) throws JSONException {
        mId = object.getString("id");
        mName = object.getString("label");
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getSelector() {
        return "id:" + mId;
    }
}
