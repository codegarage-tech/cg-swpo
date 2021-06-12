package com.meembusoft.safewaypharmaonline.viewholder;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;

import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.CategoryActivity;
import com.meembusoft.safewaypharmaonline.activity.CheckoutOrderActivity;
import com.meembusoft.safewaypharmaonline.adapter.CategoryListAdapter;
import com.meembusoft.safewaypharmaonline.adapter.ShippingChargeListAdapter;
import com.meembusoft.safewaypharmaonline.model.Category;
import com.meembusoft.safewaypharmaonline.model.ShippingCharge;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.reversecoder.library.event.OnSingleClickListener;

import java.util.List;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ShippingChargeViewHolder extends BaseViewHolder<ShippingCharge> {

    private TextView tviItemName,tvPrice;
    private LinearLayout linSelectDeliveryOption,llSelected;

    public ShippingChargeViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_delivery_options);

        tviItemName = (TextView) itemView.findViewById(R.id.tv_area_title);
        tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
        linSelectDeliveryOption = (LinearLayout) itemView.findViewById(R.id.lin_select_delivery_option);
        llSelected = (LinearLayout) itemView.findViewById(R.id.ll_selected);
    }

    @Override
    public void setData(final ShippingCharge data) {
        tviItemName.setText(data.getName());
        if (data.getId().equalsIgnoreCase("2")) {
            tvPrice.setText( data.getNote());
        } else {
            tvPrice.setText(getContext().getResources().getString(R.string.title_price) + ": " + data.getAmount() + " " + getContext().getResources().getString(R.string.title_tk));
        }
        if (data.isSelected()) {
//            linSelectDeliveryOption.setBackgroundResource(R.drawable.shape_rectangle_border_primary_bg_shade);
            llSelected.setVisibility(View.VISIBLE);
        } else {
//            linSelectDeliveryOption.setBackgroundResource(R.drawable.shape_rectangle_border_white_bg_shade);
            llSelected.setVisibility(View.GONE);
        }

        itemView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                setSelection(data);

            }
        });
    }

    private void setSelection(ShippingCharge data) {
        ShippingChargeListAdapter chargeListAdapter = ((ShippingChargeListAdapter) getOwnerAdapter());
        List<ShippingCharge> chargeList = chargeListAdapter.getAllData();

        if (chargeList != null && chargeList.size() > 0) {
            for (ShippingCharge shippingCharge : chargeList) {
                if (shippingCharge.getId() == data.getId()) {
                    shippingCharge.setSelected(true);
                    llSelected.setVisibility(View.VISIBLE);
                    Logger.d("shippingChargeId", shippingCharge.getId() + "");
                    ((CheckoutOrderActivity)getContext()).shippingCharge(shippingCharge);
                } else {
                    llSelected.setVisibility(View.GONE);
                    shippingCharge.setSelected(false);
                }
            }
            chargeListAdapter.notifyDataSetChanged();
        }
    }
}