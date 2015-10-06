package com.badon.brigham.notify;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;


public class ApplicationPickerActivity extends AppCompatActivity {

    ApplicationInfo[] array;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_application_picker);

        List<ApplicationInfo> list = this.getPackageManager().getInstalledApplications(0);
        array = new ApplicationInfo[list.size()];
        for (int i = 0; i < list.size(); i++) array[i] = list.get(i);
        ArrayAdapter adapter = new CustomArrayAdapter(this, array);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                colorPicker(array[position].packageName);
            }
        });

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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_application_picker, menu);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    public void colorPicker(final String packageName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final SettingsManager settings = new SettingsManager(getApplicationContext());
        final View view = this.getLayoutInflater().inflate(R.layout.colorpicker_dialog_layout, null);
        int[] radioButtons = {R.id.color_1, R.id.color_2, R.id.color_3, R.id.color_4, R.id.color_5, R.id.color_6, R.id.color_7, R.id.color_8, R.id.color_9};
        final String[] colorOptions = {"#9A2EFE", "#1BA260", "#D44638", "#3B57A0", "#3B5998", "#FFB300", "#00A478", "#00ACED", "#517FA4"};
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.color_options);
        radioGroup.check(radioButtons[Arrays.asList(colorOptions).indexOf(settings.getPackageColor(packageName))]);
        builder.setView(view)
                .setTitle(R.string.setup_dialog_title)
                .setPositiveButton(R.string.save_setup, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        int selected = radioGroup.indexOfChild(view.findViewById(radioGroup.getCheckedRadioButtonId()));
                        Log.i("ApplicationPicker", "Selected " + selected);
                        String color = colorOptions[selected];
                        settings.addPackageColor(packageName, color);
                        Log.i("ApplicationPicker", "Added color " + color);
                        Toast.makeText(getApplicationContext(), "Preferences Saved", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .setMessage(R.string.setup_dialog);
        builder.create().show();
    }

    public class CustomArrayAdapter extends ArrayAdapter<ApplicationInfo> {
        private final Context context;
        private final ApplicationInfo[] packages;

        public CustomArrayAdapter(Context context, ApplicationInfo[] applicationInfo) {
            super(context, R.layout.listview_layout, applicationInfo);
            this.context = context;
            this.packages = applicationInfo;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ApplicationInfo applicationInfo = packages[position];
            PackageManager packageManager = context.getPackageManager();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.listview_layout, parent, false);
            TextView label = (TextView) rowView.findViewById(R.id.firstLine);
            TextView packageName = (TextView) rowView.findViewById(R.id.secondLine);
            ImageView applicationIcon = (ImageView) rowView.findViewById(R.id.icon);
            label.setText(applicationInfo.loadLabel(packageManager).toString());
            packageName.setText(applicationInfo.packageName);
            applicationIcon.setImageDrawable(applicationInfo.loadIcon(packageManager));
            return rowView;
        }
    }
}
