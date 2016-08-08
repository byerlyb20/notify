package com.badon.brigham.notify.lifx;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.WorkerThread;
import android.support.v4.app.NotificationCompat;

import com.badon.brigham.notify.IntroActivity;
import com.badon.brigham.notify.R;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class LifxCloud {

    public static final int STATUS_OK = 200;
    public static final int STATUS_UNAUTHORIZED = 401;

    private Context mContext;
    private String mApiKey;

    public LifxCloud(Context context, String key) {
        mContext = context;
        mApiKey = key;
    }

    @WorkerThread
    public void breath(String color, int cycles, String selector) throws LifxCloudException {
        if (mApiKey.isEmpty()) {
            throw new InvalidTokenException();
        }
        try {
            URL obj = new URL("https://api.lifx.com/v1/lights/" + selector + "/effects/breathe");
            HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();
            conn.setRequestProperty("Authorization", " Bearer " + mApiKey);
            conn.setRequestMethod("POST");
            String urlParameters = "color=" + color + "&cycles=" + cycles;
            conn.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == STATUS_UNAUTHORIZED) {
                throw new InvalidTokenException();
            } else if (responseCode != STATUS_OK) {
                throw new LifxCloudException();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new LifxCloudException();
        }
    }

    @WorkerThread
    public void flash(String color, int cycles, String selector) throws LifxCloudException {
        if (mApiKey.isEmpty()) {
            throw new InvalidTokenException();
        }
        try {
            URL obj = new URL("https://api.lifx.com/v1/lights/" + selector + "/effects/pulse");
            HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();
            conn.setRequestProperty("Authorization", " Bearer " + mApiKey);
            conn.setRequestMethod("POST");
            String urlParameters = "color=" + color + "&cycles=" + cycles;
            conn.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == STATUS_UNAUTHORIZED) {
                throw new InvalidTokenException();
            } else if (responseCode != STATUS_OK) {
                throw new LifxCloudException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new LifxCloudException();
        }
    }

    @WorkerThread
    public JSONArray listLights() throws LifxCloudException {
        if (mApiKey.isEmpty()) {
            throw new InvalidTokenException();
        }
        try {
            URL obj = new URL("https://api.lifx.com/v1/lights/all");
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestProperty("Authorization", " Bearer " + mApiKey);
            conn.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == STATUS_UNAUTHORIZED) {
                throw new InvalidTokenException();
            } else if (responseCode != STATUS_OK) {
                throw new LifxCloudException();
            }

            return new JSONArray(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new LifxCloudException();
        }
    }

    public static class LifxCloudException extends Exception {

    }

    public class InvalidTokenException extends LifxCloudException {

        public InvalidTokenException() {
            super();
            Intent intent = new Intent(mContext, IntroActivity.class);
            intent.putExtra("page", 1);
            PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

            int color;
            Resources resources = mContext.getResources();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                color = resources.getColor(R.color.colorPrimary, null);
            } else {
                color = resources.getColor(R.color.colorPrimary);
            }

            // TODO: Add strings to strings.xml
            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                    .setSmallIcon(R.drawable.ic_warning)
                    .setColor(color)
                    .setContentIntent(contentIntent)
                    .setContentTitle("Invalid API Token")
                    .setContentText("Notify encountered a problem because your LIFX API key has become invalidated.")
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true);

            NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, builder.build());
        }

    }
}
