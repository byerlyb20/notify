package com.badon.brigham.notify.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.graphics.Palette;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class SettingsManager {

    private static final String NOTIFICATIONS_STORE = "prefs.json";
    private static SettingsManager mSettingsManager;
    private SharedPreferences mSharedPref;
    private Context mContext;
    private JSONArray mNotificationPreferences;
    private JSONArray mLights;

    private SettingsManager(Context context) {
        mContext = context;

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        File file = new File(mContext.getFilesDir(), NOTIFICATIONS_STORE);
        String times = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            times = response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!times.isEmpty()) {
            try {
                mNotificationPreferences = new JSONArray(times);
            } catch (JSONException e) {
                e.printStackTrace();
                mNotificationPreferences = new JSONArray();
            }
        } else {
            mNotificationPreferences = new JSONArray();
            String[] packages = mSharedPref.getString("packages", "").split(",");
            String[] colors = mSharedPref.getString("colors", "").split(",");

            int i = 0;
            for (String string : packages) {
                JSONObject object = new JSONObject();
                try {
                    object.put("color", colors[i]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                addPackagePreferences(string, object);
                i++;
            }

            mSharedPref.edit().putString("packages", null).putString("colors", null).apply();
        }
    }

    public synchronized static SettingsManager getSettings(Context context) {

        if (mSettingsManager == null) mSettingsManager = new SettingsManager(context);

        return mSettingsManager;
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public JSONObject getPackagePreferences(ApplicationInfo app) {
        for (int i = 0; i < mNotificationPreferences.length(); i++) {
            try {
                JSONObject object = mNotificationPreferences.getJSONObject(i);
                if (object.getString("package").equals(app.packageName)) {
                    return object;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JSONObject object = new JSONObject();
        try {
            object.put("package", app.packageName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public void addPackagePreferences(ApplicationInfo app, JSONObject object) {
        addPackagePreferences(app.packageName, object);
    }

    public void addPackagePreferences(String packageName, JSONObject object) {
        try {
            if (!object.optString("color").equals("default") || !object.optBoolean("enabled", true)
                    || object.optJSONArray("lights") != null) {
                object.put("package", packageName);
                mNotificationPreferences.put(object);
                save();
            } else {
                removePackagePreferences(packageName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void removePackagePreferences(String packageName) {
        for (int i = 0; i < mNotificationPreferences.length(); i++) {
            try {
                JSONObject object = mNotificationPreferences.getJSONObject(i);
                if (object.getString("package").equals(packageName)) {
                    mNotificationPreferences.remove(i);
                }
                save();
                break;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public JSONArray getLights() {
        if (mLights == null || mLights.length() == 0) {
            resetLights();
        }

        return mLights;
    }

    public void resetLights() {
        String apiKey = mSharedPref.getString("apiKey", "");

        LifxCloud api = new LifxCloud(mContext, apiKey);
        try {
            mLights = api.listLights();
        } catch (LifxCloud.LifxCloudException e) {
            e.printStackTrace();
            mLights = new JSONArray();
        }
    }

    public String getDefaultColor(Drawable drawable) {
        Bitmap bitmap = drawableToBitmap(drawable);
        if (bitmap != null && !bitmap.isRecycled()) {
            Palette palette = Palette.from(bitmap).generate();
            int vibrant = palette.getVibrantColor(0x000000);
            return String.format("#%06X", 0xFFFFFF & vibrant);
        }

        return "#9A2EFE";
    }

    private void save() {
        try {
            FileOutputStream outputStream = mContext.openFileOutput(NOTIFICATIONS_STORE,
                    Context.MODE_PRIVATE);
            outputStream.write(mNotificationPreferences.toString().getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
