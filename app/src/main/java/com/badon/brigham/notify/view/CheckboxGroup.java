package com.badon.brigham.notify.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.badon.brigham.notify.R;

import java.util.ArrayList;

public class CheckboxGroup extends LinearLayout {

    protected ArrayList<CheckboxGroup> mChildGroups = new ArrayList<>();
    protected ArrayList<CheckBox> mChildCheckboxes = new ArrayList<>();
    private CheckBox mMasterCheckbox;
    private LinearLayout mChildren;
    private ProgressBar mProgress;
    private String mLabel;

    public CheckboxGroup(Context context) {
        super(context);
        initializeViews(context);
    }

    public CheckboxGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        setOrientation(VERTICAL);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_checkbox_group, this);

        mMasterCheckbox = (CheckBox) view.findViewById(R.id.masterCheckbox);
        mChildren = (LinearLayout) view.findViewById(R.id.children);
        mProgress = (ProgressBar) view.findViewById(R.id.progress);
        mMasterCheckbox.setChecked(false);
        if (mLabel != null) {
            mMasterCheckbox.setText(mLabel);
        }
        mMasterCheckbox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = mMasterCheckbox.isChecked();
                checkChildren(isChecked);
            }
        });
    }

    public void setLoading(boolean isLoading) {
        if (isLoading) {
            mMasterCheckbox.setVisibility(GONE);
            mChildren.setVisibility(GONE);
            mProgress.setVisibility(VISIBLE);
        } else {
            mMasterCheckbox.setVisibility(VISIBLE);
            mChildren.setVisibility(VISIBLE);
            mProgress.setVisibility(GONE);
        }
    }

    private boolean isAllChecked() {
        for (CheckboxGroup group : mChildGroups) {
            if (!group.isChecked()) {
                return false;
            }
        }
        for (CheckBox checkBox : mChildCheckboxes) {
            if (!checkBox.isChecked()) {
                return false;
            }
        }
        return true;
    }

    private void checkChildren(boolean checked) {
        for (CheckboxGroup group : mChildGroups) {
            group.setChecked(checked);
        }
        for (CheckBox checkBox : mChildCheckboxes) {
            checkBox.setChecked(checked);
        }
    }

    public boolean isChecked() {
        return mMasterCheckbox.isChecked();
    }

    public void setChecked(boolean checked) {
        mMasterCheckbox.setChecked(checked);
        checkChildren(checked);
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        mMasterCheckbox.setOnCheckedChangeListener(listener);
    }

    public void addChild(CheckboxGroup group) {
        group.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mMasterCheckbox.setChecked(isAllChecked());
            }
        });
        mChildGroups.add(group);
        mChildren.addView(group);
    }

    public void addChild(CheckBox checkBox) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mMasterCheckbox.setChecked(isAllChecked());
            }
        });
        mChildCheckboxes.add(checkBox);
        mChildren.addView(checkBox);
    }

    public void setLabel(String label) {
        mLabel = label;
        mMasterCheckbox.setHint(label);
    }

    public void clear() {
        mChildren.removeAllViews();
        mChildCheckboxes.clear();
        mChildGroups.clear();
        mMasterCheckbox.setChecked(false);
    }

}
