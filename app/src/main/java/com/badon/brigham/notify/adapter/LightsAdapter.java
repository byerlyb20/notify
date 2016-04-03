package com.badon.brigham.notify.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LightsAdapter extends RecyclerView.Adapter<LightsAdapter.LightViewHolder> {

    private JSONArray mCloudLights;
    private JSONArray mLights;
    private JSONArray mSelectedLights = new JSONArray();
    private OnLightCheckedListener mOnLightCheckedListener;
    private Context mContext;

    public LightsAdapter(JSONArray cloudLights, JSONArray lights, Context context) {
        mCloudLights = cloudLights;
        mLights = lights;
        mContext = context;

        mOnLightCheckedListener = new OnLightCheckedListener() {
            @Override
            public void onLightChecked(int position, boolean isChecked) {
                try {
                    JSONObject light = mCloudLights.getJSONObject(position);
                    String id = light.getString("id");

                    if (isChecked) {
                        mSelectedLights.put(id);
                    } else {
                        for (int i = 0; i < mSelectedLights.length(); i++) {
                            String tempId = mSelectedLights.getString(i);
                            if (id.equals(tempId)) {
                                mSelectedLights.remove(i);
                                break;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    public LightViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LightViewHolder(new CheckBox(mContext), mOnLightCheckedListener);
    }

    @Override
    public void onBindViewHolder(LightViewHolder holder, int position) {
        try {
            // get light JSONObject from LIFX Cloud
            JSONObject light = mCloudLights.getJSONObject(position);

            // set the label for the checkbox
            JSONObject location = light.getJSONObject("location");
            JSONObject group = light.getJSONObject("group");
            holder.mCheckbox.setHint(location.getString("name") + " - " + group.getString("name") + " - " + light.getString("label"));

            // check the checkbox, if the user has previously selected it
            if (mLights != null) {
                String cloudId = light.getString("id");
                for (int j = 0; j < mLights.length(); j++) {
                    String id = mLights.getString(j);
                    if (cloudId.equals(id)) {
                        holder.mCheckbox.setChecked(true);
                        break;
                    }
                }
            } else {
                holder.mCheckbox.setChecked(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mCloudLights.length();
    }

    public JSONArray getSelectedLights() {
        if (mSelectedLights.length() == mCloudLights.length()) {
            // If all the lights are selected, return null
            return null;
        }
        return mSelectedLights;
    }

    private interface OnLightCheckedListener {
        void onLightChecked(int position, boolean isChecked);
    }

    public static class LightViewHolder extends RecyclerView.ViewHolder {

        public CheckBox mCheckbox;

        public LightViewHolder(CheckBox v, final OnLightCheckedListener listener) {
            super(v);

            mCheckbox = v;
            mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    listener.onLightChecked(getAdapterPosition(), isChecked);
                }
            });
        }

    }

}
