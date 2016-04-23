package com.badon.brigham.notify.preference;

import android.content.Context;
import android.content.Intent;
import android.preference.Preference;
import android.util.AttributeSet;

public class NRPPreference extends Preference {

    private Context mContext;

    public NRPPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    @Override
    public void onClick() {
        mContext.startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
    }
}
