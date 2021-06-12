package com.meembusoft.safewaypharmaonline.viewholder;

import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.CheckoutOrderActivity;
import com.meembusoft.safewaypharmaonline.activity.MyOrderActivity;
import com.meembusoft.safewaypharmaonline.adapter.CheckoutOrderAdapter;
import com.meembusoft.safewaypharmaonline.dialog.RemoveMedicineFromCartDialog;
import com.meembusoft.safewaypharmaonline.model.OrderItems;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.reversecoder.library.event.OnSingleClickListener;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class StoreDetailsOrderListViewHolder extends BaseViewHolder<OrderItems> {

    private static String TAG = StoreDetailsOrderListViewHolder.class.getSimpleName();
    private TextView tvName,tvGenericName,tvAmount,tvTotalAmount,tvQty;
    private ImageView ivProduct,ivProductDelete;
   // private int qty=0;
    public StoreDetailsOrderListViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_checkout_orders_list_screen);

        tvName = (TextView) $(R.id.tv_name);
        tvGenericName = (TextView) $(R.id.tv_generic_name);
        tvAmount = (TextView) $(R.id.tv_amount);
        tvTotalAmount = (TextView) $(R.id.tv_totals_amount);
        tvQty = (TextView) $(R.id.tv_qty);
        ivProduct = (ImageView) $(R.id.iv_product);
        ivProductDelete = (ImageView) $(R.id.iv_product_delete);
        ivProductDelete.setVisibility(View.GONE);
    }

    @Override
    public void setData(final OrderItems items) {
        //Set data
        tvName.setText(AppUtil.optStringNullCheckValue(items.getName()) + " " + AppUtil.optStringNullCheckValue(items.getForm_name()));
        tvGenericName.setText(AppUtil.optStringNullCheckValue(items.getGeneric_name()));
        tvAmount.setText(AppUtil.optStringNullCheckValue(items.getPrices()));
        tvQty.setText(AppUtil.optStringNullCheckValue(items.getQuantitys())+"");
        AppUtil.loadImage(getContext(), ivProduct, items.getProduct_image(), false, false, true);
        tvTotalAmount.setText(AppUtil.optStringNullCheckValue(items.getSub_total_prices())+"");

    }

}