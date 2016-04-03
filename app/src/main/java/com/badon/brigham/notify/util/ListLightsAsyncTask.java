package com.badon.brigham.notify.util;

import android.os.AsyncTask;

import org.json.JSONArray;

public class ListLightsAsyncTask extends AsyncTask<Void, Void, JSONArray> {

    protected SettingsManager mSettings;
    protected OnLightsReceived mListener;

    public ListLightsAsyncTask(SettingsManager settings, OnLightsReceived listener) {
        mSettings = settings;
        mListener = listener;
    }

    @Override
    protected JSONArray doInBackground(Void... params) {
        return mSettings.getLights();
    }

    @Override
    protected void onPostExecute(JSONArray result) {
        mListener.onLightsReceived(result);
    }

    public interface OnLightsReceived {
        void onLightsReceived(JSONArray lights);
    }
}