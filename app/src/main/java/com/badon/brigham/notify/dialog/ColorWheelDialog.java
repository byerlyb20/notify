package com.badon.brigham.notify.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.badon.brigham.notify.R;
import com.badon.brigham.notify.adapter.LightsAdapter;
import com.badon.brigham.notify.util.ListLightsAsyncTask;
import com.badon.brigham.notify.util.SettingsManager;
import com.badon.brigham.notify.util.SettingsManagerAsyncTask;
import com.larswerkman.lobsterpicker.LobsterPicker;
import com.larswerkman.lobsterpicker.OnColorListener;
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ColorWheelDialog {

    private Activity mContext;
    private JSONObject mAppPrefs;
    private LobsterPicker mPicker;
    private LobsterShadeSlider mShader;
    private CheckBox mDefaultColor;
    private RecyclerView mLightsList;
    private LightsAdapter mLightsAdapter;

    public ColorWheelDialog(Activity context) {
        mContext = context;
    }

    public AlertDialog getDialog(final ApplicationInfo app) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.DialogTheme);
        View view = View.inflate(mContext, R.layout.dialog_color_wheel, null);
        mPicker = (LobsterPicker) view.findViewById(R.id.picker);
        mShader = (LobsterShadeSlider) view.findViewById(R.id.shader);
        mDefaultColor = (CheckBox) view.findViewById(R.id.defaultColor);
        mLightsList = (RecyclerView) view.findViewById(R.id.lights);
        mPicker.setColorHistoryEnabled(true);

        new SettingsManagerAsyncTask(mContext, new SettingsManagerAsyncTask.OnSettingsManagerCreated() {
            @Override
            public void onTimeManagerCreated(SettingsManager settings) {
                PackageManager manager = mContext.getPackageManager();
                mAppPrefs = settings.getPackagePreferences(app);
                String color = mAppPrefs.optString("color", "default");
                if (color.equals("default")) {
                    color = settings.getDefaultColor(app.loadIcon(manager));
                    mDefaultColor.setChecked(true);
                }
                int colorInt = Color.parseColor(color);
                mPicker.setHistory(colorInt);
                mPicker.addDecorator(mShader);
                mPicker.addOnColorListener(new OnColorListener() {
                    @Override
                    public void onColorChanged(int color) {
                        mDefaultColor.setChecked(false);
                    }

                    @Override
                    public void onColorSelected(int color) {

                    }
                });

                new ListLightsAsyncTask(settings, new ListLightsAsyncTask.OnLightsReceived() {
                    @Override
                    public void onLightsReceived(JSONArray cloudLights) {
                        mLightsAdapter = new LightsAdapter(cloudLights, mAppPrefs.optJSONArray("lights"), mContext);
                        mLightsList.setAdapter(mLightsAdapter);
                        mLightsList.setLayoutManager(new LinearLayoutManager(mContext));
                        mLightsList.setHasFixedSize(true);
                    }
                }).execute();
            }
        }).execute();

        builder.setView(view)
                .setTitle(R.string.setup_dialog_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        new SettingsManagerAsyncTask(mContext, new SettingsManagerAsyncTask.OnSettingsManagerCreated() {
                            @Override
                            public void onTimeManagerCreated(SettingsManager settings) {
                                String color = String.format("#%06X", 0xFFFFFF & mPicker.getColor());
                                try {
                                    mAppPrefs.put("color", (mDefaultColor.isChecked() ? "default" : color));
                                    if (mLightsAdapter != null) {
                                        mAppPrefs.put("lights", mLightsAdapter.getSelectedLights());
                                    }
                                    settings.addPackagePreferences(app, mAppPrefs);
                                    CoordinatorLayout parent = (CoordinatorLayout) mContext.findViewById(R.id.parentLayout);
                                    Snackbar.make(parent, "Preferences Saved", Snackbar.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).execute();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .setMessage(R.string.setup_dialog);
        return builder.create();
    }
}
