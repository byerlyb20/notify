package com.badon.brigham.notify;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;

import com.badon.brigham.notify.fragment.KeyFragment;
import com.badon.brigham.notify.fragment.PermissionFragment;
import com.badon.brigham.notify.fragment.SplashFragment;
import com.badon.brigham.notify.intro.IntroBaseActivity;

public class IntroActivity extends IntroBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int page = intent.getIntExtra("page", 0);
        setCurrentPage(page);
    }

    @Override
    protected void onAddTabs() {
        addTab(new SplashFragment());
        addTab(new KeyFragment());
        addTab(new PermissionFragment());
    }

    @Override
    public void introFinish() {
        super.introFinish();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String apiKey = prefs.getString("apiKey", "");
        if (NotificationHandler.NOTIFICATION_ACCESS && !apiKey.isEmpty()) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final int currentItem;
            if (!NotificationHandler.NOTIFICATION_ACCESS && apiKey.isEmpty()) {
                // Notify is missing both Notification Read Access and an API Key
                currentItem = 1;
                builder.setMessage(R.string.intro_setup_incomplete_both);
            } else if (!NotificationHandler.NOTIFICATION_ACCESS) {
                // Notify is only missing Notification Read Access
                currentItem = 2;
                builder.setMessage(R.string.intro_setup_incomplete_nrp);
            } else if (apiKey.isEmpty()) {
                // Notify is only missing an API Key
                currentItem = 1;
                builder.setMessage(R.string.intro_setup_incomplete_key);
            } else {
                currentItem = 0;
            }
            builder.setTitle(R.string.setup_incomplete_dialog_title)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ViewPager mPager = (ViewPager) findViewById(R.id.pager);
                            mPager.setCurrentItem(currentItem);
                        }
                    });
            builder.create().show();
        }
    }
}