package com.badon.brigham.notify.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.badon.brigham.notify.R;
import com.badon.brigham.notify.intro.IntroFragment;

public class PermissionFragment extends IntroFragment {

    @Override
    public int getPosition() {
        return IntroFragment.POSITION_END;
    }

    @Override
    public int getTitleResource() {
        return R.string.preference_nrp;
    }

    @Override
    public int getMessageResource() {
        return R.string.intro_nrp_detail;
    }

    @Override
    public int getIconResource() {
        return R.drawable.ic_notifications_none_white_24dp;
    }

    @Override
    public View onCreateActionView(LayoutInflater inflater, ViewGroup container) {
        Button settingsShortcut = (Button) inflater.inflate(R.layout.intro_action_3, container, false);
        settingsShortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings();
            }
        });

        return settingsShortcut;
    }

    private void openSettings() {
        startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
    }
}