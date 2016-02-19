package com.badon.brigham.notify.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;

import android.view.View;

import com.badon.brigham.notify.R;
import com.badon.brigham.notify.util.SettingsManager;
import com.larswerkman.lobsterpicker.LobsterPicker;
import com.larswerkman.lobsterpicker.sliders.LobsterShadeSlider;

public class ColorWheelDialog {

    private Activity mContext;

    public ColorWheelDialog(Activity context) {
        mContext = context;
    }

    public AlertDialog getDialog(final ApplicationInfo app) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.DialogTheme);
        final SettingsManager settings = new SettingsManager(mContext);
        final View view = View.inflate(mContext, R.layout.dialog_color_wheel, null);
        final LobsterPicker picker = (LobsterPicker) view.findViewById(R.id.picker);
        LobsterShadeSlider shader = (LobsterShadeSlider) view.findViewById(R.id.shader);
        picker.setColorHistoryEnabled(true);
        int color = Color.parseColor(settings.getPackageColor(app));
        picker.setHistory(color);
        picker.addDecorator(shader);
        builder.setView(view)
                .setTitle(R.string.setup_dialog_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String color = String.format("#%06X", 0xFFFFFF & picker.getColor());
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
                .setNeutralButton(R.string.auto, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        settings.addPackageColor(app, "default");
                        CoordinatorLayout parent = (CoordinatorLayout) mContext.findViewById(R.id.parentLayout);
                        Snackbar.make(parent, "Preferences Saved", Snackbar.LENGTH_SHORT).show();
                    }
                })
                .setMessage(R.string.setup_dialog);
        return builder.create();
    }

}
