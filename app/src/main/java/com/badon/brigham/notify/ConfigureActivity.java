package com.badon.brigham.notify;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

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
            // TODO: Show error message
            finish();
        }

        new SettingsManagerAsyncTask(this, new SettingsManagerAsyncTask.OnSettingsManagerCreated() {

            @Override
            public void onSettingsManagerCreated(SettingsManager settings) {
                mAppPrefs = settings.getPackagePreferences(mApp);

                new ListLightsAsyncTask(settings, new ListLightsAsyncTask.OnLightsReceived() {
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
                    }
                }).execute();

                String color = mAppPrefs.optString("color", "default");
                if (color.equals("default")) {
                    PackageManager manager = ConfigureActivity.this.getPackageManager();
                    color = settings.getDefaultColor(mApp.loadIcon(manager));
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
