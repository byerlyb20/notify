package com.badon.brigham.notify.util;

import android.content.Context;
import android.os.AsyncTask;

public class SettingsManagerAsyncTask extends AsyncTask<Void, Void, SettingsManager> {

    private Context mContext;
    private OnSettingsManagerCreated mListener;

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
        mListener.onSettingsManagerCreated(result);
    }

    public interface OnSettingsManagerCreated {
        void onSettingsManagerCreated(SettingsManager settings);
    }
}
