package com.badon.brigham.notify;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.View;

import com.badon.brigham.notify.adapter.ApplicationListAdapter;
import com.badon.brigham.notify.adapter.ClickAdapter;
import com.badon.brigham.notify.dialog.ColorPickerDialog;
import com.badon.brigham.notify.dialog.ColorWheelDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ApplicationPickerActivity extends AppCompatActivity {

    ApplicationListAdapter mAdapter;
    ArrayList<ApplicationInfo> mApplications = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_application_picker);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mAdapter = new ApplicationListAdapter(mApplications, this);
        mAdapter.setOnItemClickListener(new ClickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                boolean oldColorWheel = PreferenceManager.getDefaultSharedPreferences(ApplicationPickerActivity.this).getBoolean("oldColorWheel", false);
                if (oldColorWheel) {
                    new ColorPickerDialog(ApplicationPickerActivity.this).getDialog(mApplications.get(position)).show();
                } else {
                    new ColorWheelDialog(ApplicationPickerActivity.this).getDialog(mApplications.get(position)).show();
                }
            }
        });
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        mApplications = (ArrayList<ApplicationInfo>) this.getPackageManager().getInstalledApplications(0);
        final PackageManager manager = this.getPackageManager();
        Collections.sort(mApplications, new Comparator<ApplicationInfo>() {
            public int compare(ApplicationInfo a, ApplicationInfo b) {
                return a.loadLabel(manager).toString().compareTo(b.loadLabel(manager).toString());
            }
        });
        mAdapter.setApplications(mApplications);
        mAdapter.notifyDataSetChanged();
    }
}
