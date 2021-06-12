package com.meembusoft.safewaypharmaonline.dialog;

import android.app.Activity;

import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.base.BaseAlertDialog;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ReminderStatusDialog extends BaseAlertDialog {

    private OnClickListener mOnClickListener;

    public ReminderStatusDialog(Activity activity, OnClickListener onClickListener) {
        super(activity);
        mOnClickListener = onClickListener;
    }

    @Override
    public Builder initView() {
        Builder builder = prepareView("", getActivity().getString(R.string.dialog_reminder_status_label), R.string.dialog_cancel, R.string.dialog_ok, -1, mOnClickListener);

        return builder;
    }
}