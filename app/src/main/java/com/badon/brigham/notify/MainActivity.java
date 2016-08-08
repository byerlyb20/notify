package com.badon.brigham.notify;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.badon.brigham.notify.dialog.AboutDialog;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String apiKey = prefs.getString("apiKey", "");
        if (!NotificationHandler.NOTIFICATION_ACCESS || apiKey.isEmpty()) {
            startActivity(new Intent(getApplicationContext(), IntroActivity.class));
        }

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getFragmentManager().beginTransaction()
                .add(R.id.settingsFragment, new SettingsFragment())
                .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_about:
                new AboutDialog(this).getDialog().show();
                break;
            case R.id.action_intro:
                startActivity(new Intent(this, IntroActivity.class));
                break;
            case R.id.action_notification:
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                int priority = Integer.valueOf(prefs.getString("priority", "0"));
                int color;

                Resources resources = getResources();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    color = resources.getColor(R.color.colorPrimary, null);
                } else {
                    color = resources.getColor(R.color.colorPrimary);
                }

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setColor(color)
                        .setContentTitle("Test Notification")
                        .setContentText("This is a test notification from Notify. You can ignore it.")
                        .setPriority(priority);

                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, builder.build());
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefs);
        }

    }
}
