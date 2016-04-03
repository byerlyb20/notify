package com.badon.brigham.notify.util;

import android.content.Context;
import android.os.AsyncTask;

public class SettingsManagerAsyncTask extends AsyncTask<Void, Void, SettingsManager> {

    protected Context mContext;
    protected OnSettingsManagerCreated mListener;

    public SettingsManagerAsyncTask(Context context, OnSettingsManagerCreated listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    protected SettingsManager doInBackground(Void... params) {
        return SettingsManager.getSettings(mContext);
    }

    @Override
    protected void onPostExecute(SettingsManager result) {
        mListener.onTimeManagerCreated(result);
    }

    public interface OnSettingsManagerCreated {
        void onTimeManagerCreated(SettingsManager settings);
    }
}
