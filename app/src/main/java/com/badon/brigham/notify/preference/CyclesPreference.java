package com.badon.brigham.notify.preference;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.badon.brigham.notify.R;

public class CyclesPreference extends DialogPreference {

    private Context mContext;
    private NumberPicker mNumberPicker;

    public CyclesPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setDialogTitle(R.string.cycles_dialog_title);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        int pulseCycles = getPersistedInt(2);
        if (pulseCycles != 1) {
            setSummary("Pulse " + pulseCycles + " times");
        } else {
            setSummary("Pulse 1 time");
        }
        return super.onCreateView(parent);
    }

    @Override
    protected View onCreateDialogView() {
        mNumberPicker = new NumberPicker(mContext);
        mNumberPicker.setMaxValue(10);
        mNumberPicker.setMinValue(1);
        mNumberPicker.setValue(getPersistedInt(2));
        return mNumberPicker;
    }

    @Override
    public void onClick(DialogInterface dialog, int button) {
        if (button == DialogInterface.BUTTON_POSITIVE) {
            persistInt(mNumberPicker.getValue());
            int pulseCycles = getPersistedInt(2);
            if (pulseCycles != 1) {
                setSummary("Pulse " + pulseCycles + " times");
            } else {
                setSummary("Pulse 1 time");
            }
        }
    }
}
