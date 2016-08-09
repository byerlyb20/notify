package com.badon.brigham.notify;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.badon.brigham.notify.lifx.Location;
import com.badon.brigham.notify.lifx.SelectLightView;
import com.badon.brigham.notify.util.ListLightsAsyncTask;
import com.badon.brigham.notify.util.SettingsManager;
import com.badon.brigham.notify.util.SettingsManagerAsyncTask;
import com.larswerkman.lobsterpicker.LobsterPicker;
import com.larswerkman.lobsterpicker.OnColorListener;
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConfigureActivity extends AppCompatActivity {

    private ImageView mIcon;
    private TextView mAppName;
    private TextView mPackage;
    private LobsterPicker mPicker;
    private LobsterShadeSlider mShader;
    private CheckBox mDefaultColor;
    private SelectLightView mSelectLightView;
    private JSONObject mAppPrefs;
    private ApplicationInfo mApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_configure);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_action_arrow_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

        mIcon = (ImageView) findViewById(R.id.icon);
        mAppName = (TextView) findViewById(R.id.appName);
        mPackage = (TextView) findViewById(R.id.packageName);
        mPicker = (LobsterPicker) findViewById(R.id.picker);
        mShader = (LobsterShadeSlider) findViewById(R.id.shader);
        mDefaultColor = (CheckBox) findViewById(R.id.defaultColor);
        mSelectLightView = (SelectLightView) findViewById(R.id.selectLightView);
        mPicker.setColorHistoryEnabled(true);


        Bundle in = getIntent().getExtras();
        String packageName = in.getString("package");

        try {
            mApp = getPackageManager().getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            finish();
        }

        new SettingsManagerAsyncTask(this, new SettingsManagerAsyncTask.OnSettingsManagerCreated() {

            @Override
            public void onSettingsManagerCreated(SettingsManager settings) {
                mAppPrefs = settings.getPackagePreferences(mApp);
                PackageManager manager = ConfigureActivity.this.getPackageManager();
                Drawable appIcon = mApp.loadIcon(manager);

                mIcon.setImageDrawable(appIcon);
                mAppName.setText(mApp.loadLabel(manager).toString());
                mPackage.setText(mApp.packageName);

                new ListLightsAsyncTask(settings, false, new ListLightsAsyncTask.OnLightsReceived() {
                    @Override
                    public void onLightsReceived(ArrayList<Location> cloudLights) {
                        mSelectLightView.setLights(cloudLights);
                        String selector = null;
                        try {
                            selector = mAppPrefs.getString("selector");
                        } catch (JSONException e) {
                            try {
                                JSONArray lights = mAppPrefs.optJSONArray("lights");
                                mAppPrefs.remove("lights");
                                selector = SettingsManager.getSelector(lights);
                            } catch (JSONException f) {
                                f.printStackTrace();
                            }
                        }
                        if (selector != null) {
                            mSelectLightView.setSelector(selector);
                        }
                        mSelectLightView.setLoading(false);
                    }
                }).execute();

                String color = mAppPrefs.optString("color", "default");
                if (color.equals("default")) {
                    color = settings.getDefaultColor(appIcon);
                    mDefaultColor.setChecked(true);
                    mDefaultColor.jumpDrawablesToCurrentState();
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
            }

        }).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_configure, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_done:
                save();
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateSelectLightView(View view) {
        if (mSelectLightView.isLoading()) {
            return;
        }
        mSelectLightView.setLoading(true);
        final String selector = mSelectLightView.getSelector();
        new SettingsManagerAsyncTask(this, new SettingsManagerAsyncTask.OnSettingsManagerCreated() {
            @Override
            public void onSettingsManagerCreated(SettingsManager settings) {
                new ListLightsAsyncTask(settings, true, new ListLightsAsyncTask.OnLightsReceived() {
                    @Override
                    public void onLightsReceived(ArrayList<Location> cloudLights) {
                        mSelectLightView.setLights(cloudLights);
                        mSelectLightView.setSelector(selector);
                        mSelectLightView.setLoading(false);
                    }
                }).execute();
            }
        }).execute();
    }

    private void save() {
        new SettingsManagerAsyncTask(this, new SettingsManagerAsyncTask.OnSettingsManagerCreated() {
            @Override
            public void onSettingsManagerCreated(SettingsManager settings) {
                String color = String.format("#%06X", 0xFFFFFF & mPicker.getColor());
                try {
                    mAppPrefs.put("color", (mDefaultColor.isChecked() ? "default" : color));
                    mAppPrefs.put("selector", mSelectLightView.getSelector());
                    settings.addPackagePreferences(mApp, mAppPrefs);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute();
    }
}
