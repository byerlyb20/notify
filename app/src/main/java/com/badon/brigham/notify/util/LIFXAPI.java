package com.badon.brigham.notify.util;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class LIFXAPI {

    String apiKey;

    public LIFXAPI(String key) {
        apiKey = key;
    }

    public void breath(String color, int cycles, String selector) {
        try {
            URL obj = new URL("https://api.lifx.com/v1/lights/" + selector + "/effects/breathe");
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

    public void flash(String color, int cycles, String selector) {
        try {
            URL obj = new URL("https://api.lifx.com/v1/lights/" + selector + "/effects/pulse");
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

    public JSONArray listLights() {
        try {
            URL obj = new URL("https://api.lifx.com/v1/lights/all");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("Authorization", " Bearer " + apiKey);
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return new JSONArray(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new JSONArray();
    }
}
