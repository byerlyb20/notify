package com.badon.brigham.notify;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsManager {
    SharedPreferences sharedPref;

    public SettingsManager(Context context) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getAPIKey() {
        return sharedPref.getString("apiKey", "");
    }

    public void setAPIKey(String apiKey) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("apiKey", apiKey);
        editor.apply();
    }

    public Integer getBreathCycles() {
        return sharedPref.getInt("Cycles", 2);
    }

    public void setBreathCycles(Integer cycles) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("Cycles", cycles);
        editor.apply();
    }

    public String getPackageColor(String packageName) {
        String[] packages = sharedPref.getString("packages", "").split(",");
        String[] colors = sharedPref.getString("colors", "").split(",");
        int index = Arrays.asList(packages).indexOf(packageName);
        Log.i("SettingsManager", "Index of " + packageName + ": " + index);
        if (index != -1) {
            return colors[index];
        } else {
            return "#9A2EFE";
        }
    }

    public void addPackageColor(String packageName, String color) {
        SharedPreferences.Editor editor = sharedPref.edit();
        Log.i("SettingsManager", "Received color is " + color);

        List<String> packages = new ArrayList<String> ();
        List<String> colors = new ArrayList<String>();
        if (!sharedPref.getString("packages", "").isEmpty()) {
            packages = new ArrayList<String>(Arrays.asList(sharedPref.getString("packages", "").split(",")));
            colors = new ArrayList<String>(Arrays.asList(sharedPref.getString("colors", "").split(",")));
        }

        int index = packages.indexOf(packageName);
        Log.i("SettingsManager", "Index of " + packageName + " is " + index);

        if (color != "#9A2EFE") {
            if (index != -1) {
                // Modify existing package entry
                Log.i("SettingsManager", "Modifying a package entry");
                colors.remove(index);
                colors.add(index, color);
            } else {
                // Create new package entry
                Log.i("SettingsManager", "Creating a new package entry");
                packages.add(packageName);
                colors.add(color);
            }
        } else {
            if (index != -1) {
                Log.i("SettingsManager", "Removing a package entry");
                packages.remove(index);
                colors.remove(index);
            }
        }

        if (color != "#9A2EFE" || index != -1) {
            StringBuilder packageBuilder = new StringBuilder();
            for (String n : packages) {
                packageBuilder.append(n).append(",");
                Log.i("SettingsManager", "Placing package in StringBuilder: " + n);
            }
            packageBuilder.deleteCharAt(packageBuilder.length() - 1);

            String finalPackages = packageBuilder.toString();
            Log.i("SettingsManager", "Posted Packages: " + finalPackages);
            editor.putString("packages", finalPackages);

            StringBuilder colorBuilder = new StringBuilder();
            for (String n : colors) {
                colorBuilder.append(n).append(",");
                Log.i("SettingsManager", "Placing color in StringBuilder: " + n);
            }
            colorBuilder.deleteCharAt(colorBuilder.length() - 1);

            String finalColors = colorBuilder.toString();
            Log.i("SettingsManager", "Posted Colors: " + finalColors);
            editor.putString("colors", finalColors);
            editor.apply();
        }
    }
}
