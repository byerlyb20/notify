package com.badon.brigham.notify.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.badon.brigham.notify.R;

import java.util.ArrayList;

public class ApplicationListAdapter extends ClickAdapter<ApplicationListAdapter.ViewHolder> {

    private ArrayList<ApplicationInfo> mApplications;
    private PackageManager mManager;

    public ApplicationListAdapter(ArrayList<ApplicationInfo> applications, Context context) {
        mApplications = applications;
        mManager = context.getPackageManager();
    }

    @Override
    public ApplicationListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_application, parent, false);
        ViewHolder holder = new ApplicationListAdapter.ViewHolder(view);
        holder.attachToListener(mListener);
        holder.attachToLongListener(mLongListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ApplicationListAdapter.ViewHolder holder, int position) {
        ApplicationInfo applicationInfo = mApplications.get(position);
        holder.mPrimary.setText(applicationInfo.loadLabel(mManager).toString());
        holder.mSecondary.setText(applicationInfo.packageName);
        holder.mIcon.setImageDrawable(applicationInfo.loadIcon(mManager));
    }

    @Override
    public int getItemCount() {
        return mApplications.size();
    }

    public void setApplications(ArrayList<ApplicationInfo> applications) {
        mApplications = applications;
    }

    public static class ViewHolder extends ClickAdapter.ClickViewHolder {

        public TextView mPrimary;
        public TextView mSecondary;
        public ImageView mIcon;

        public ViewHolder(View v) {
            super(v);
            mPrimary = (TextView) v.findViewById(R.id.firstLine);
            mSecondary = (TextView) v.findViewById(R.id.secondLine);
            mIcon = (ImageView) v.findViewById(R.id.icon);
        }
    }
}
