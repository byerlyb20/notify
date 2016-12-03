package com.badon.brigham.notify.lifx;

import android.content.Context;
import android.util.AttributeSet;

import com.badon.brigham.notify.widget.CheckboxGroup;

import java.util.ArrayList;

public class SelectLightView extends CheckboxGroup {

    private Context mContext;
    private ArrayList<Location> mLights;

    public SelectLightView(Context context) {
        super(context);
        mContext = context;
        if (mLights == null) {
            setLoading(true);
        } else {
            setLoading(false);
        }
        setLabel("All");
    }

    public SelectLightView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        if (mLights == null) {
            setLoading(true);
        } else {
            setLoading(false);
        }
        setLabel("All");
    }

    public void setLights(ArrayList<Location> lights) {
        clear();
        mLights = lights;
        for (Location location : mLights) {
            LocationCheckboxGroup locationGroup = new LocationCheckboxGroup(mContext, location);
            addChild(locationGroup);
        }
    }

    public String getSelector() {
        if (isChecked()) {
            return "all";
        }

        StringBuilder builder = new StringBuilder();
        for (CheckboxGroup group : mChildGroups) {
            LocationCheckboxGroup locationGroup = (LocationCheckboxGroup) group;
            String selector = locationGroup.getSelector();
            if (builder.length() > 0 && !selector.isEmpty()) {
                builder.append(",");
            }
            builder.append(selector);
        }

        return builder.toString();
    }

    public void setSelector(String selector) {
        if (selector.equals("all")) {
            setChecked(true, false);
            return;
        }

        String[] strings = selector.split("\\s*,\\s*");
        for (String string : strings) {
            for (CheckboxGroup group : mChildGroups) {
                LocationCheckboxGroup locationGroup = (LocationCheckboxGroup) group;
                if (locationGroup.checkLights(string)) {
                    break;
                }
            }
        }
    }

}
