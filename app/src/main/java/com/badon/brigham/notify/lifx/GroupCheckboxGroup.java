package com.badon.brigham.notify.lifx;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.CheckBox;

import com.badon.brigham.notify.widget.CheckboxGroup;

@SuppressLint("ViewConstructor")
class GroupCheckboxGroup extends CheckboxGroup {

    private Group mGroup;

    public GroupCheckboxGroup(Context context, Group group) {
        super(context);
        mGroup = group;
        setLabel(group.getName());
        for (Light light : group.getLights()) {
            LightCheckbox lightCheckbox = new LightCheckbox(context, light);
            addChild(lightCheckbox);
        }
    }

    public boolean checkLights(String selector) {
        if (selector.equals(mGroup.getSelector())) {
            setChecked(true, false);
            return true;
        }

        for (CheckBox checkbox : mChildCheckboxes) {
            LightCheckbox light = (LightCheckbox) checkbox;
            if (selector.equals(light.getLight().getSelector())) {
                light.setChecked(true);
                return true;
            }
        }
        return false;
    }

    public String getSelector() {
        if (isChecked()) {
            return mGroup.getSelector();
        }

        StringBuilder builder = new StringBuilder();
        for (CheckBox checkbox : mChildCheckboxes) {
            LightCheckbox light = (LightCheckbox) checkbox;
            if (light.isChecked()) {
                builder.append(light.getLight().getSelector());
            }
        }

        return builder.toString();
    }

}
