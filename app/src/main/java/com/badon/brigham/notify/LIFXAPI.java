package com.badon.brigham.notify;

import android.util.Log;

import java.io.DataOutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class LIFXAPI {

    private final String TAG = "LIFXAPI";
    String apiKey;

    public LIFXAPI(String key) {
        apiKey = key;
    }

    public void breath(String selector, String color, int cycles) {
        try {
            Log.i(TAG, "LIFX API recieved request, trying");
            URL obj = new URL("https://api.lifx.com/v1.0-beta1/lights/all/effects/breathe");
            HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();
            conn.setRequestProperty("Authorization", " Bearer " + apiKey);
            conn.setRequestMethod("POST");
            String urlParameters = "selector=" + selector + "&color=" + color + "&cycles=" + cycles;
            conn.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            Log.i(TAG, "LIFX API finished with response " + conn.getResponseCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
