package com.badon.brigham.notify;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.badon.brigham.notify.adapter.ApplicationListAdapter;
import com.badon.brigham.notify.adapter.ClickAdapter;
import com.badon.brigham.notify.anim.Fade;
import com.badon.brigham.notify.util.SettingsManager;
import com.badon.brigham.notify.util.SettingsManagerAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ApplicationPickerActivity extends AppCompatActivity {

    private ProgressBar mProgress;
    private RecyclerView mList;
    private ApplicationListAdapter mAdapter;
    private ArrayList<ApplicationInfo> mApplications = new ArrayList<>();
    private boolean mShowAll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_application_picker);

        mProgress = (ProgressBar) findViewById(R.id.progress);
        mList = (RecyclerView) findViewById(R.id.list);

        mAdapter = new ApplicationListAdapter(this);
        mAdapter.setOnItemClickListener(new ClickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(ApplicationPickerActivity.this, ConfigureActivity.class);
                intent.putExtra("package", mApplications.get(position).packageName);
                startActivity(intent);
            }
        });
        mAdapter.setOnItemEnableChangeListener(new ApplicationListAdapter.OnItemEnabledChangeListener() {
            @Override
            public void onItemEnableChange(final int position, final boolean value) {
                new SettingsManagerAsyncTask(ApplicationPickerActivity.this, new SettingsManagerAsyncTask.OnSettingsManagerCreated() {
                    @Override
                    public void onSettingsManagerCreated(SettingsManager settings) {
                        JSONObject object = settings.getPackagePreferences(mApplications
                                .get(position));
                        try {
                            object.put("enabled", value);
                            settings.addPackagePreferences(mApplications.get(position), object);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).execute();
            }
        });

        mList.setAdapter(mAdapter);
        mList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST,
                false));
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.setHasFixedSize(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert toolbar != null;
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_application_picker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_show_all:
                item.setChecked(!item.isChecked());
                mShowAll = item.isChecked();
                new LoadAppsTask().execute();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setLoading(boolean loading) {
        if (loading) {
            mProgress.setVisibility(View.VISIBLE);
            mProgress.setAlpha(1f);
            mList.setVisibility(View.GONE);
        } else {
            Fade.show(mList);
            Fade.hide(mProgress, true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        new LoadAppsTask().execute();

        SettingsManager.getSettings(this).resetLights();
    }

    class LoadAppsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            setLoading(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            final PackageManager pm = getPackageManager();
            ArrayList<ApplicationInfo> applications = (ArrayList<ApplicationInfo>) pm
                    .getInstalledApplications(0);
            mApplications.clear();
            for (ApplicationInfo app : applications) {
                Intent intent = pm.getLaunchIntentForPackage(app.packageName);
                if (mShowAll || intent != null) {
                    mApplications.add(app);
                }
            }
            Collections.sort(mApplications, new Comparator<ApplicationInfo>() {
                public int compare(ApplicationInfo a, ApplicationInfo b) {
                    return a.loadLabel(pm).toString().compareTo(b.loadLabel(pm)
                            .toString());
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            mAdapter.setApplications(mApplications);
            mAdapter.notifyDataSetChanged();
            setLoading(false);
        }
    }
}
