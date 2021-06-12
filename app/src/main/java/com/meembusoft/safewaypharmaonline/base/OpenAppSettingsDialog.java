package com.meembusoft.safewaypharmaonline.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class OpenAppSettingsDialog extends BaseAlertDialog {

    private DialogInterface.OnClickListener mOnClickListener;
    private String mMessage = "";

    public OpenAppSettingsDialog(Activity activity, String message, DialogInterface.OnClickListener onClickListener) {
        super(activity);
        mOnClickListener = onClickListener;
        mMessage = message;
    }

    @Override
    public AlertDialog.Builder initView() {
        AlertDialog.Builder builder = prepareView("", mMessage, "CANCELLED", "I AGREE", "", mOnClickListener);

        return builder;
    }
}