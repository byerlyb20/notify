package com.badon.brigham.notify.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Html;

import com.badon.brigham.notify.R;

public class AboutDialog {

    private Context mContext;

    public AboutDialog(Context context) {
        mContext = context;
    }

    public AlertDialog getDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.DialogTheme);
        builder.setMessage(Html.fromHtml(mContext.getResources().getString(R.string.about)))
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }

}
