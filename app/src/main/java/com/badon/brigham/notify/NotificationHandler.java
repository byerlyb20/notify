package com.badon.brigham.notify;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.badon.brigham.notify.lifx.LifxCloud;
import com.badon.brigham.notify.util.SettingsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class NotificationHandler extends NotificationListenerService {

    public static boolean NOTIFICATION_ACCESS = false;

    @Override
    public IBinder onBind(Intent mIntent) {
        IBinder mIBinder = super.onBind(mIntent);
        NOTIFICATION_ACCESS = true;
        return mIBinder;
    }

    @Override
    public boolean onUnbind(Intent mIntent) {
        boolean mOnUnbind = super.onUnbind(mIntent);
        NOTIFICATION_ACCESS = false;
        return mOnUnbind;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        SettingsManager settings = SettingsManager.getSettings(this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Notification notification = sbn.getNotification();
        NotificationListenerService.RankingMap rankingMap = this.getCurrentRanking();
        NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
        rankingMap.getRanking(sbn.getKey(), ranking);
        if (ranking.matchesInterruptionFilter() && notification.priority >= Integer.valueOf(prefs.getString("priority", "0")) && sbn.isClearable()) {
            try {
                String apiKey = prefs.getString("apiKey", "");
                PackageManager manager = getPackageManager();
                ApplicationInfo app = manager.getApplicationInfo(sbn.getPackageName(), 0);

                JSONObject appPrefs = settings.getPackagePreferences(app);
                if (appPrefs.optBoolean("enabled", true)) {
                    String color = appPrefs.optString("color", "default");
                    if (color.equals("default")) {
                        color = settings.getDefaultColor(app.loadIcon(manager));
                    }

                    String selector = null;
                    try {
                        selector = appPrefs.getString("selector");
                    } catch (JSONException e) {
                        JSONArray lights = appPrefs.optJSONArray("lights");
                        selector = SettingsManager.getSelector(lights);
                    }
                    if (selector == null) {
                        selector = "all";
                    }

                    LifxCloud lifxCloud = new LifxCloud(this, apiKey);
                    if (!selector.isEmpty()) {
                        try {
                            switch (prefs.getString("pulseType", "breath")) {
                                case "Breath":
                                    lifxCloud.breath(color, prefs.getInt("cycles", 2), selector);
                                    break;
                                case "Flash":
                                    lifxCloud.flash(color, prefs.getInt("cycles", 2), selector);
                                    break;
                            }
                        } catch (LifxCloud.LifxCloudException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (PackageManager.NameNotFoundException | JSONException e) {
                e.printStackTrace();
            }
        }
    }
}