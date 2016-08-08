package com.badon.brigham.notify.util;

import android.os.AsyncTask;

import com.badon.brigham.notify.lifx.LifxCloud;
import com.badon.brigham.notify.lifx.Location;
import com.badon.brigham.notify.lifx.LocationBuilder;

import java.util.ArrayList;

public class ListLightsAsyncTask extends AsyncTask<Void, Void, ArrayList<Location>> {

    protected SettingsManager mSettings;
    protected boolean mUpdate;
    protected OnLightsReceived mListener;

    public ListLightsAsyncTask(SettingsManager settings, boolean update, OnLightsReceived listener) {
        mSettings = settings;
        mUpdate = update;
        mListener = listener;
    }

    @Override
    protected ArrayList<Location> doInBackground(Void... params) {
        if (mUpdate) {
            mSettings.resetLights();
        }
        LocationBuilder builder = new LocationBuilder(mSettings.getLights());
        try {
            return builder.getLights();
        } catch (LifxCloud.LifxCloudException e) {
            return new ArrayList<>();
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Location> result) {
        mListener.onLightsReceived(result);
    }

    public interface OnLightsReceived {
        void onLightsReceived(ArrayList<Location> lights);
    }
}