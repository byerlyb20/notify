package com.badon.brigham.notify.util;

import java.io.DataOutputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class LIFXAPI {

    String apiKey;

    public LIFXAPI(String key) {
        apiKey = key;
    }

    public void breath(String color, int cycles) {
        try {
            URL obj = new URL("https://api.lifx.com/v1/lights/all/effects/breathe");
            HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();
            conn.setRequestProperty("Authorization", " Bearer " + apiKey);
            conn.setRequestMethod("POST");
            String urlParameters = "color=" + color + "&cycles=" + cycles;
            conn.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            conn.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void flash(String color, int cycles) {
        try {
            URL obj = new URL("https://api.lifx.com/v1/lights/all/effects/pulse");
            HttpsURLConnection conn = (HttpsURLConnection) obj.openConnection();
            conn.setRequestProperty("Authorization", " Bearer " + apiKey);
            conn.setRequestMethod("POST");
            String urlParameters = "color=" + color + "&cycles=" + cycles;
            conn.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            conn.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
