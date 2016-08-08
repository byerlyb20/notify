package com.badon.brigham.notify.lifx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LocationBuilder {

    private JSONArray mRawLights;
    private ArrayList<Location> mLights = new ArrayList<>();

    public LocationBuilder(JSONArray rawLights) {
        mRawLights = rawLights;
    }

    public ArrayList<Location> getLights() throws LifxCloud.LifxCloudException {
        try {
            for (int i = 0; i < mRawLights.length(); i++) {
                JSONObject obj = mRawLights.getJSONObject(i);
                processRawLight(obj);
            }
        } catch (JSONException e) {
            throw new LifxCloud.LifxCloudException();
        }
        return mLights;
    }

    private void processRawLight(JSONObject rawLight) throws JSONException {
        Location location = new Location(rawLight.getJSONObject("location"));
        Group group = new Group(rawLight.getJSONObject("group"));
        Light light = new Light(rawLight);
        Location foundLocation = containsLocation(location);
        if (foundLocation != null) {
            Group foundGroup = containsGroup(foundLocation, group);
            if (foundGroup != null) {
                foundGroup.addLight(light);
            } else {
                group.addLight(light);
                foundLocation.addGroup(group);
            }
        } else {
            group.addLight(light);
            location.addGroup(group);
            mLights.add(location);
        }
    }

    private Location containsLocation(Location a) {
        for (Location b : mLights) {
            if (b.getId().equals(a.getId())) {
                return b;
            }
        }
        return null;
    }

    private Group containsGroup(Location location, Group a) {
        for (Group b : location.getGroups()) {
            if (b.getId().equals(a.getId())) {
                return b;
            }
        }
        return null;
    }
}
