package com.badon.brigham.notify;

import android.app.Notification;
import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;


public class LIFXNotify extends NotificationListenerService {
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
        Log.i("LIFXNotify", "Notification Recieved");
        Log.i("LIFXNotify", "Package: " + sbn.getPackageName());
        SettingsManager settings = new SettingsManager(this);
            Log.i("LIFXNotify", "App not disabled, executing notification");
            Notification notification = sbn.getNotification();
            NotificationListenerService.RankingMap rankingMap = this.getCurrentRanking();
            NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
            rankingMap.getRanking(sbn.getKey(), ranking);
            if (ranking.matchesInterruptionFilter() && notification.priority != Notification.PRIORITY_MIN && notification.priority != Notification.PRIORITY_LOW && sbn.isClearable()) {
                Log.i("LIFXNotify", "Real Notification, priority: " + notification.priority);
                String apiKey = settings.getAPIKey();
                String color = settings.getPackageColor(sbn.getPackageName());
                Log.i("LIFXNotify", "Displaying Notification with color " + color + " and apiKey " + apiKey);
                LIFXAPI lifxApi = new LIFXAPI(apiKey);
                lifxApi.breath("all", color, settings.getBreathCycles());
            }
    }
}
