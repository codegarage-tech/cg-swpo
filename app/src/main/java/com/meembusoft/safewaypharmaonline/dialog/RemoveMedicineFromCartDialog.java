package com.meembusoft.safewaypharmaonline.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.base.BaseAlertDialog;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class RemoveMedicineFromCartDialog extends BaseAlertDialog {

    private DialogInterface.OnClickListener mOnClickListener;

    public RemoveMedicineFromCartDialog(Activity activity, DialogInterface.OnClickListener onClickListener) {
        super(activity);
        mOnClickListener = onClickListener;
    }

    @Override
    public AlertDialog.Builder initView() {
        AlertDialog.Builder builder = prepareView("", getActivity().getString(R.string.dialog_the_item_will_be_removed_from_cart), R.string.dialog_cancel, R.string.dialog_ok, -1, mOnClickListener);

        return builder;
    }
}