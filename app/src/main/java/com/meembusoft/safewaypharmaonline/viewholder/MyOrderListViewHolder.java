package com.meembusoft.safewaypharmaonline.viewholder;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatCheckBox;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.HomeActivity;
import com.meembusoft.safewaypharmaonline.activity.MyOrderActivity;
import com.meembusoft.safewaypharmaonline.adapter.MyOrderListAdapter;
import com.meembusoft.safewaypharmaonline.dialog.RemoveMedicineFromCartDialog;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.util.AllSettingsManager;

import cn.ymex.popup.controller.DialogControllable;
import cn.ymex.popup.dialog.PopupDialog;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class MyOrderListViewHolder extends BaseViewHolder<StaggeredMedicineByItem> {

    private static String TAG = MyOrderListViewHolder.class.getSimpleName();
    private TextView tvName, tvGenericName, tvAmount,tvDiscountAmount, tvQty;
    private ImageView ivProduct, ivProductDelete;
    private LinearLayout llMinus, llPlus;
    private EditText etQty;
    private AppCompatCheckBox cbMyOrder;
    Activity mActivity;
    float discountCost = 0.00f,originSellingPrice =  0.00f;
    // private int qty=0;
    public MyOrderListViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_my_orders_screen);
        mActivity = ((Activity) getContext());
        tvName = (TextView) $(R.id.tv_name);
        tvGenericName = (TextView) $(R.id.tv_generic_name);
        tvAmount = (TextView) $(R.id.tv_amount);
        tvDiscountAmount = (TextView) $(R.id.tv_discount_amount);
        tvQty = (TextView) $(R.id.tv_qty);
       // etQty = (EditText) $(R.id.et_qty);
        ivProduct = (ImageView) $(R.id.iv_product);
        llMinus = (LinearLayout) $(R.id.ll_minus);
        llPlus = (LinearLayout) $(R.id.ll_plus);
        ivProductDelete = (ImageView) $(R.id.iv_product_delete);
        cbMyOrder = (AppCompatCheckBox) $(R.id.cb_myorder);

    }

    @Override
    public void setData(final StaggeredMedicineByItem medicineByItem) {
        //Set data

        tvName.setText(AppUtil.optStringNullCheckValue(medicineByItem.getName()) + " " + AppUtil.optStringNullCheckValue(medicineByItem.getForm_name()));
        tvGenericName.setText(AppUtil.optStringNullCheckValue(medicineByItem.getGeneric_name()));

        // Discount Calculation
        float discountPercent = (((!AllSettingsManager.isNullOrEmpty(medicineByItem.getDiscount_percent()) && (Float.parseFloat(medicineByItem.getDiscount_percent()) > 0))) ? (Float.parseFloat(medicineByItem.getDiscount_percent())) :  0.00f);

        if (!AllSettingsManager.isNullOrEmpty(medicineByItem.getDiscount_percent()) && discountPercent > 0){
            try {
            discountCost = (((!AllSettingsManager.isNullOrEmpty(medicineByItem.getDiscount_percent()) && (Float.parseFloat(medicineByItem.getDiscount_percent()) > 0))) ? (Float.parseFloat(medicineByItem.getDiscount_percent())) : 0.00f);
            originSellingPrice = (((!AllSettingsManager.isNullOrEmpty(medicineByItem.getSelling_price()) && (Float.parseFloat(medicineByItem.getSelling_price()) > 0))) ? (Float.parseFloat(medicineByItem.getSelling_price())) : 0.00f);
            float subtotalToDiscountCost= (discountCost/100) * originSellingPrice;
            float minusSubtotalToDiscountCost = originSellingPrice - subtotalToDiscountCost;
                Log.e("subtotalToDiscountCost", subtotalToDiscountCost + "llMinus");
                Log.e("minusToDiscountCost", minusSubtotalToDiscountCost + "llMinus");
                String finalSubtotalToDiscountCost = AppUtil.formatDoubleString(minusSubtotalToDiscountCost);
                medicineByItem.setDiscount_percent_price(""+finalSubtotalToDiscountCost);
                tvDiscountAmount.setVisibility(View.VISIBLE);
                tvAmount.setText(getContext().getString(R.string.title_tk)+" " +"" +finalSubtotalToDiscountCost);
                tvDiscountAmount.setText(getContext().getString(R.string.title_tk)+" " +"" +AppUtil.formatDoubleString(originSellingPrice));
                tvDiscountAmount .setPaintFlags(tvDiscountAmount .getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } catch (NumberFormatException nfe) {
                System.out.println("NumberFormatException: " + nfe.getMessage());
            }
        } else {
            tvDiscountAmount.setVisibility(View.GONE);
            tvAmount.setText(getContext().getString(R.string.title_tk)+" " +AppUtil.optStringNullCheckValue(medicineByItem.getSelling_price()));
        }
        tvQty.setText(medicineByItem.getItem_quantity() + "");
        AppUtil.loadImage(getContext(), ivProduct, medicineByItem.getProduct_image(), false, false, true);
        if (medicineByItem.getIsCheckedItem()) {
            cbMyOrder.setChecked(true);
        } else {
            cbMyOrder.setChecked(false);
        }

        cbMyOrder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    medicineByItem.setIsCheckedItem(true);
                    onOrderNowClick(medicineByItem);
                } else {
                    medicineByItem.setIsCheckedItem(false);
                    onOrderNowClick(medicineByItem);
                }
            }
        });

        llMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int qty = 0;
                    qty = Integer.valueOf(tvQty.getText().toString());

                    if (qty > 1) {
                        qty = qty - 1;
                        Log.e("qty", qty + "llMinus");
                        tvQty.setText(qty+"");
                        Logger.d(TAG, "@=> " + "minus.......num=> " + qty);
                        medicineByItem.setItem_quantity(qty);
                        onOrderNowClick(medicineByItem);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });

        llPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int qty = Integer.valueOf(tvQty.getText().toString());
                    qty = qty + 1;
                    tvQty.setText(qty+"");
                    Logger.d(TAG, "@=> " + "add.......num=> " + qty);
                    medicineByItem.setItem_quantity(qty);
                    onOrderNowClick(medicineByItem);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });

        tvQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(v.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_view_quantity);
                dialog.setCanceledOnTouchOutside(false);
                final EditText etQty = (EditText) dialog.findViewById(R.id.et_qty);
                final Button btnSubmit = (Button) dialog.findViewById(R.id.btn_dialog_submit);
                final TextView tvCancel = (TextView) dialog.findViewById(R.id.tv_dialog_cancel);
                etQty.setText( tvQty.getText().toString());
                dialog.show();

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int qtyValue = (((!AllSettingsManager.isNullOrEmpty(etQty.getText().toString().trim()) && (Integer.parseInt(etQty.getText().toString().trim()) > 0))) ? (Integer.parseInt(etQty.getText().toString().trim())) : 0);
                        if (qtyValue>0) {
                            tvQty.setText(etQty.getText().toString().trim() + "");
                            Logger.d(TAG, "@=> " + "add.......num=> " + qtyValue);
                            medicineByItem.setItem_quantity(qtyValue);
                            onOrderNowClick(medicineByItem);
                            dialog.dismiss();

                        }
                    }
                });

                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        ivProductDelete.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                //  final Activity mActivity = ((Activity) getContext());

                RemoveMedicineFromCartDialog removeMedicineFromCartDialog = new RemoveMedicineFromCartDialog(mActivity, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                // final MyOrderActivity mActivity = ((MyOrderActivity) getContext());
                                //Delete the Item from database
                                if (AppUtil.isMedicineByItemStored(mActivity, medicineByItem)) {
                                    AppUtil.deleteSelectedMedicineByItem(mActivity, medicineByItem);
                                }
                                //Remove from list
                                ((MyOrderListAdapter) getOwnerAdapter()).remove(getAdapterPosition());

//                                MyOrderListAdapter myOrderListAdapter = ((MyOrderListAdapter) getOwnerAdapter());
//
//                                if (myOrderListAdapter != null) {
//                                    myOrderListAdapter.removeItem(medicineByItem);
//                                }

                                if (mActivity instanceof MyOrderActivity) {
                                    ((MyOrderActivity) mActivity).subTotals();
                                    // ((MyOrderActivity)mActivity).initActivityViews();
                                } else if (mActivity instanceof HomeActivity) {
                                    ((HomeActivity) mActivity).updateSubTotals();
                                    // ((HomeActivity)mActivity).resetCounterView();
                                }


                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                            case DialogInterface.BUTTON_NEUTRAL:
                                break;
                        }
                    }
                });
                removeMedicineFromCartDialog.initView().show();
            }
        });
    }


    public StaggeredMedicineByItem onOrderNowClick(final StaggeredMedicineByItem item) {
        Logger.d(TAG, "onOrderNowClick: " + "count: " + item.getItem_quantity());
//        MyOrderActivity mActivity = ((MyOrderActivity) getContext());

        if (item.getItem_quantity() == 0) {
            if (AppUtil.isMedicineByItemStored(mActivity, item)) {
                //Delete the food from database
                AppUtil.deleteSelectedMedicineByItem(mActivity, item);
            }
        } else if (item.getItem_quantity() == 1) {
            if (AppUtil.isMedicineByItemStored(mActivity, item)) {
                //Update data into database
                AppUtil.storeSelectedMedicineByItem(mActivity, item);
            } else {
                //Add item into database
                AppUtil.storeSelectedMedicineByItem(mActivity, item);
            }
        } else {
            //Update data into database
            AppUtil.storeSelectedMedicineByItem(mActivity, item);
        }
        //   mActivity.updateTotalAmount();

        if (mActivity instanceof MyOrderActivity) {
            //((MyOrderActivity)mActivity).resetCounterView();
            ((MyOrderActivity) mActivity).subTotals();

        } else if (mActivity instanceof HomeActivity) {
            ((HomeActivity) mActivity).updateSubTotals();
            // ((HomeActivity)mActivity).resetCounterView();
        }

        return item;

    }



}