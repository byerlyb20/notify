package com.badon.brigham.notify.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.badon.brigham.notify.R;
import com.badon.brigham.notify.util.SettingsManager;
import com.badon.brigham.notify.util.SettingsManagerAsyncTask;

import org.json.JSONObject;

import java.util.ArrayList;

public class ApplicationListAdapter extends ClickAdapter<ApplicationListAdapter.ViewHolder> {

    protected OnItemEnabledChangeListener mEnabledListener;
    private ArrayList<ApplicationInfo> mApplications;
    private PackageManager mManager;
    private Context mContext;

    public ApplicationListAdapter(ArrayList<ApplicationInfo> applications, Context context) {
        mApplications = applications;
        mContext = context;
        mManager = context.getPackageManager();
    }

    @Override
    public ApplicationListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_application, parent, false);
        ViewHolder holder = new ApplicationListAdapter.ViewHolder(view);
        holder.attachToListener(mListener);
        holder.attachToEnabledListener(mEnabledListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ApplicationListAdapter.ViewHolder holder, int position) {
        new SettingsManagerAsyncTask(mContext, new SettingsManagerAsyncTask.OnSettingsManagerCreated() {
            @Override
            public void onSettingsManagerCreated(SettingsManager settings) {
                ApplicationInfo applicationInfo = mApplications.get(holder.getAdapterPosition());
                JSONObject object = settings.getPackagePreferences(applicationInfo);
                holder.mPrimary.setText(applicationInfo.loadLabel(mManager).toString());
                holder.mSecondary.setText(applicationInfo.packageName);
                holder.mIcon.setImageDrawable(applicationInfo.loadIcon(mManager));
                holder.mEnabled.setChecked(object.optBoolean("enabled", true));
                holder.mEnabled.jumpDrawablesToCurrentState();
            }
        }).execute();
    }

    @Override
    public int getItemCount() {
        return mApplications.size();
    }

    public void setApplications(ArrayList<ApplicationInfo> applications) {
        mApplications = applications;
    }

    public void setOnItemEnableChangeListener(OnItemEnabledChangeListener listener) {
        mEnabledListener = listener;
    }

    public interface OnItemEnabledChangeListener {
        void onItemEnableChange(int position, boolean value);
    }

    public static class ViewHolder extends ClickAdapter.ClickViewHolder {

        public TextView mPrimary;
        public TextView mSecondary;
        public ImageView mIcon;
        public Switch mEnabled;
        private OnItemEnabledChangeListener mEnabledListener;

        public ViewHolder(View v) {
            super(v);
            mPrimary = (TextView) v.findViewById(R.id.firstLine);
            mSecondary = (TextView) v.findViewById(R.id.secondLine);
            mIcon = (ImageView) v.findViewById(R.id.icon);
            mEnabled = (Switch) v.findViewById(R.id.enabled);
            mEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (mEnabledListener != null) {
                        mEnabledListener.onItemEnableChange(getAdapterPosition(), isChecked);
                    }
                }
            });
        }

        public void attachToEnabledListener(OnItemEnabledChangeListener listener) {
            mEnabledListener = listener;
        }
    }
}
