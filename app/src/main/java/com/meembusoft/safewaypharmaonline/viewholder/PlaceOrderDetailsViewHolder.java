package com.meembusoft.safewaypharmaonline.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.model.OrderItems;
import com.meembusoft.safewaypharmaonline.util.AppUtil;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class PlaceOrderDetailsViewHolder extends BaseViewHolder<OrderItems> {

    private static String TAG = PlaceOrderDetailsViewHolder.class.getSimpleName();
    private TextView tvName,tvGenericName,tvAmount,tvQty,tvTotalAmount;
    private ImageView ivProduct;
    public PlaceOrderDetailsViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_checkout_orders_list_screen);

        tvName = (TextView) $(R.id.tv_name);
        tvGenericName = (TextView) $(R.id.tv_generic_name);
        tvAmount = (TextView) $(R.id.tv_amount);
        tvTotalAmount = (TextView) $(R.id.tv_totals_amount);
        tvQty = (TextView) $(R.id.tv_qty);
        ivProduct = (ImageView) $(R.id.iv_product);

        // tvGenericName.setVisibility(View.GONE);
    }

    @Override
    public void setData(final OrderItems item) {
        //Set data
        tvName.setText(AppUtil.optStringNullCheckValue(item.getName()) + " " + AppUtil.optStringNullCheckValue(item.getForm_name()));
        tvGenericName.setText(AppUtil.optStringNullCheckValue(item.getGeneric_name())+"");
        tvQty.setText(AppUtil.optStringNullCheckValue(item.getQuantitys())+"");
        tvAmount.setText(getContext().getResources().getString(R.string.title_tk)+" "+ AppUtil.optStringNullCheckValue(item.getPrices())+"");
        tvTotalAmount.setText(AppUtil.optStringNullCheckValue(item.getSub_total_prices())+"");
        AppUtil.loadImage(getContext(), ivProduct, item.getProduct_image(), false, false, true);

    }


}