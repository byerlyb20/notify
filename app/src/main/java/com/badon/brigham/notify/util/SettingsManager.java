package com.badon.brigham.notify.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.graphics.Palette;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsManager {

    private SharedPreferences sharedPref;
    private Context mContext;

    public SettingsManager(Context context) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        mContext = context;
    }

    public String getPackageColor(ApplicationInfo app) {
        String[] packages = sharedPref.getString("packages", "").split(",");
        String[] colors = sharedPref.getString("colors", "").split(",");
        int index = Arrays.asList(packages).indexOf(app.packageName);
        if (index != -1) {
            return colors[index];
        } else {
            Drawable icon = mContext.getPackageManager().getApplicationIcon(app);
            return getDefaultColor(icon);
        }
    }

    public void addPackageColor(ApplicationInfo app, String color) {
        SharedPreferences.Editor editor = sharedPref.edit();

        ArrayList<String> packages = new ArrayList<>();
        ArrayList<String> colors = new ArrayList<>();
        if (!sharedPref.getString("packages", "").isEmpty()) {
            packages = new ArrayList<>(Arrays.asList(sharedPref.getString("packages", "").split(",")));
            colors = new ArrayList<>(Arrays.asList(sharedPref.getString("colors", "").split(",")));
        }

        int index = packages.indexOf(app.packageName);

        if (!color.equals("default")) {
            if (index != -1) {
                // Modify existing package entry
                colors.remove(index);
                colors.add(index, color);
            } else {
                // Create new package entry
                packages.add(app.packageName);
                colors.add(color);
            }
        } else {
            if (index != -1) {
                packages.remove(index);
                colors.remove(index);
            }
        }

        StringBuilder packageBuilder = new StringBuilder();
        for (String n : packages) {
            packageBuilder.append(n).append(",");
        }

        String finalPackages = packageBuilder.toString();
        editor.putString("packages", finalPackages);

        StringBuilder colorBuilder = new StringBuilder();
        for (String n : colors) {
            colorBuilder.append(n).append(",");
        }

        String finalColors = colorBuilder.toString();
        editor.putString("colors", finalColors);
        editor.apply();
    }

    private String getDefaultColor(Drawable drawable) {
        Bitmap bitmap = drawableToBitmap(drawable);
        if (bitmap != null && !bitmap.isRecycled()) {
            Palette palette = Palette.from(bitmap).generate();
            int vibrant = palette.getVibrantColor(0x000000);
            return String.format("#%06X", 0xFFFFFF & vibrant);
        }

        return "#9A2EFE";
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
