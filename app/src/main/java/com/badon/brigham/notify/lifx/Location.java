package com.badon.brigham.notify.lifx;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Location {

    private String mId;
    private String mName;
    private ArrayList<Group> mGroups = new ArrayList<>();

    public Location(String id, String name) {
        mId = id;
        mName = name;
    }

    public Location(JSONObject object) throws JSONException {
        mId = object.getString("id");
        mName = object.getString("name");
    }

    public void addGroup(Group group) {
        mGroups.add(group);
    }

    public ArrayList<Group> getGroups() {
        return mGroups;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getSelector() {
        return "location_id:" + mId;
    }

}
