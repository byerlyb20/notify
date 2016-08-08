package com.badon.brigham.notify.lifx;

import android.content.Context;

import com.badon.brigham.notify.view.CheckboxGroup;

class LocationCheckboxGroup extends CheckboxGroup {

    private Location mLocation;

    public LocationCheckboxGroup(Context context, Location location) {
        super(context);
        mLocation = location;
        setLabel(location.getName());
        for (Group group : location.getGroups()) {
            GroupCheckboxGroup groupGroup = new GroupCheckboxGroup(context, group);
            addChild(groupGroup);
        }
    }

    public boolean checkLights(String selector) {
        if (selector.equals(mLocation.getSelector())) {
            setChecked(true, false);
            return true;
        }

        for (CheckboxGroup group : mChildGroups) {
            GroupCheckboxGroup lifxGroup = (GroupCheckboxGroup) group;
            if (lifxGroup.checkLights(selector)) {
                return true;
            }
        }

        return false;
    }

    public String getSelector() {
        if (isChecked()) {
            return mLocation.getSelector();
        }

        StringBuilder builder = new StringBuilder();
        for (CheckboxGroup group : mChildGroups) {
            GroupCheckboxGroup lifxGroup = (GroupCheckboxGroup) group;
            String selector = lifxGroup.getSelector();
            if (builder.length() > 0 && !selector.isEmpty()) {
                builder.append(",");
            }
            builder.append(selector);
        }

        return builder.toString();
    }

    public Location getLocation() {
        return mLocation;
    }
}
