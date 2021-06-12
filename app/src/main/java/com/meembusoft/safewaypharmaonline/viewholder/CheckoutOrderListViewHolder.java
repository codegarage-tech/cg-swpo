package com.meembusoft.safewaypharmaonline.viewholder;

import android.content.DialogInterface;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.CheckoutOrderActivity;
import com.meembusoft.safewaypharmaonline.activity.MyOrderActivity;
import com.meembusoft.safewaypharmaonline.adapter.CheckoutOrderAdapter;
import com.meembusoft.safewaypharmaonline.dialog.RemoveMedicineFromCartDialog;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.util.AllSettingsManager;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class CheckoutOrderListViewHolder extends BaseViewHolder<StaggeredMedicineByItem> {

    private static String TAG = CheckoutOrderListViewHolder.class.getSimpleName();
    private TextView tvName, tvGenericName, tvAmount, tvTotalAmount, tvQty,tvQtyTotal;
    private ImageView ivProduct, ivProductDelete;

    // private int qty=0;
    public CheckoutOrderListViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_checkout_orders_list_screen);

        tvName = (TextView) $(R.id.tv_name);
        tvGenericName = (TextView) $(R.id.tv_generic_name);
        tvAmount = (TextView) $(R.id.tv_amount);
        tvTotalAmount = (TextView) $(R.id.tv_totals_amount);
        tvQty = (TextView) $(R.id.tv_qty);
        tvQtyTotal = (TextView) $(R.id.tv_qty_total);
        ivProduct = (ImageView) $(R.id.iv_product);
        ivProductDelete = (ImageView) $(R.id.iv_product_delete);
        ivProductDelete.setVisibility(View.VISIBLE);
    }

    @Override
    public void setData(final StaggeredMedicineByItem medicineByItem) {
        //Set data
        final CheckoutOrderActivity mActivity = ((CheckoutOrderActivity) getContext());
        try {
            tvName.setText(AppUtil.optStringNullCheckValue(medicineByItem.getName()) + " " +AppUtil.optStringNullCheckValue(medicineByItem.getForm_name()));
            tvGenericName.setText(AppUtil.optStringNullCheckValue(medicineByItem.getGeneric_name()));

            // Discount Calculation
            float discountPercent = (((!AllSettingsManager.isNullOrEmpty(medicineByItem.getDiscount_percent()) && (Float.parseFloat(medicineByItem.getDiscount_percent()) > 0))) ? (Float.parseFloat(medicineByItem.getDiscount_percent())) :  0.00f);

            if (!AllSettingsManager.isNullOrEmpty(medicineByItem.getDiscount_percent()) && discountPercent > 0){
                tvAmount.setText(medicineByItem.getDiscount_percent_price());
            } else {
                tvAmount.setText(medicineByItem.getSelling_price());
            }
            String tvItemQty = " "
                    + " <font color='" + getContext().getResources().getColor(R.color.color_grey_500) + "'>"
                    + "  X  " + "</font>"
                    + " <font color='" + getContext().getResources().getColor(R.color.color_pink) + "'>"
                    + " "+medicineByItem.getItem_quantity()+" " + "</font>";
            tvQtyTotal.setText(Html.fromHtml(tvItemQty) + "");
            tvQty.setText(medicineByItem.getItem_quantity() + "");
            AppUtil.loadImage(getContext(), ivProduct, medicineByItem.getProduct_image(), false, false, true);
            Log.e("medicineByItem", medicineByItem.toString() + "medicineByItem");

            float itemPrice = AppUtil.getItemTotalPrice(medicineByItem);
            String itemPriceFinal = AppUtil.formatDoubleString(itemPrice);
            tvTotalAmount.setText(itemPriceFinal + "");
            ivProductDelete.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    RemoveMedicineFromCartDialog removeMedicineFromCartDialog = new RemoveMedicineFromCartDialog(((CheckoutOrderActivity) getContext()), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Delete the Medicine from database
                                    if (AppUtil.isMedicineByItemStored(mActivity, medicineByItem)) {
                                        AppUtil.deleteSelectedMedicineByItem(mActivity, medicineByItem);
                                    }

                                    //Remove from list
                                    ((CheckoutOrderAdapter) getOwnerAdapter()).remove(getAdapterPosition());

//                                CheckoutOrderAdapter checkoutAdapter = ((CheckoutOrderAdapter) getOwnerAdapter());
//                                if (checkoutAdapter != null) {
//                                    checkoutAdapter.removeItem(medicineByItem);
//                                }
                                    mActivity.checkoutTotalAmount();
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
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.d(TAG, TAG + " >>> " + "Exception: " + ex.toString());

        }
    }


    public StaggeredMedicineByItem onOrderNowClick(final StaggeredMedicineByItem item) {
        Logger.d(TAG, "onOrderNowClick: " + "count: " + item.getItem_quantity());
        MyOrderActivity mActivity = ((MyOrderActivity) getContext());

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

        return item;

    }

}