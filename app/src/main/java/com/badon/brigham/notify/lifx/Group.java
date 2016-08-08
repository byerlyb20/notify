package com.badon.brigham.notify.lifx;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Group {

    private String mId;
    private String mName;
    private ArrayList<Light> mLights = new ArrayList<>();

    public Group(String id, String name) {
        mId = id;
        mName = name;
    }

    public Group(JSONObject object) throws JSONException {
        mId = object.getString("id");
        mName = object.getString("name");
    }

    public void addLight(Light light) {
        mLights.add(light);
    }

    public ArrayList<Light> getLights() {
        return mLights;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getSelector() {
        return "group_id:" + mId;
    }

}
