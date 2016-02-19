package com.badon.brigham.notify.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;

import android.view.View;
import android.widget.RadioGroup;

import com.badon.brigham.notify.R;
import com.badon.brigham.notify.util.SettingsManager;

import java.util.Arrays;

public class ColorPickerDialog {

    private Activity mContext;

    public ColorPickerDialog(Activity context) {
        mContext = context;
    }

    public AlertDialog getDialog(final ApplicationInfo app) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.DialogTheme);
        final SettingsManager settings = new SettingsManager(mContext);
        final View view = View.inflate(mContext, R.layout.dialog_color_picker, null);
        int[] radioButtons = {R.id.color_1, R.id.color_2, R.id.color_3, R.id.color_4, R.id.color_5, R.id.color_6, R.id.color_7, R.id.color_8, R.id.color_9};
        final String[] colorOptions = {"#9A2EFE", "#1BA260", "#D44638", "#3B57A0", "#FFFC00", "#FFB300", "#00A478", "#00ACED", "#517FA4"};
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.color_options);
        int index = Arrays.asList(colorOptions).indexOf(settings.getPackageColor(app));
        if (index != -1) {
            radioGroup.check(radioButtons[index]);
        }
        builder.setView(view)
                .setTitle(R.string.setup_dialog_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        int selected = radioGroup.indexOfChild(view.findViewById(radioGroup.getCheckedRadioButtonId()));
                        String color = colorOptions[selected];
                        settings.addPackageColor(app, color);
                        CoordinatorLayout parent = (CoordinatorLayout) mContext.findViewById(R.id.parentLayout);
                        Snackbar.make(parent, "Preferences Saved", Snackbar.LENGTH_SHORT).show();
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
