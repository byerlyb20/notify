package com.badon.brigham.notify.lifx;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.CheckBox;

@SuppressLint("ViewConstructor")
class LightCheckbox extends CheckBox {

    private Light mLight;

    public LightCheckbox(Context context, Light light) {
        super(context);
        mLight = light;
        setHint(light.getName());
    }

    public Light getLight() {
        return mLight;
    }
}
