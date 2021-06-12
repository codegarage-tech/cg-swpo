package com.meembusoft.safewaypharmaonline.viewholder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.CategoryActivity;
import com.meembusoft.safewaypharmaonline.activity.ProductDetailsActivity;
import com.meembusoft.safewaypharmaonline.activity.ShippingAddressActivity;
import com.meembusoft.safewaypharmaonline.adapter.ShippingAddressListAdapter;
import com.meembusoft.safewaypharmaonline.adapter.ShopStaggeredListAdapter;
import com.meembusoft.safewaypharmaonline.model.RelatedMedicine;
import com.meembusoft.safewaypharmaonline.model.ShippingAddress;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.reversecoder.library.event.OnSingleClickListener;

import org.parceler.Parcels;

import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_PRODUCT_ITEM;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ShippingAddressViewHolder extends BaseViewHolder<ShippingAddress> {

    private static String TAG = ShippingAddressViewHolder.class.getSimpleName();
    private TextView tvName,tvShippingAddress,tvPhone;
    private ImageView ivUser;
    private CardView cvShippingAddress;
    private LinearLayout llAddressTick;
    public ShippingAddressViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_item_shipping_address);

        cvShippingAddress = (CardView) $(R.id.cv_shipping_address);
        tvName = (TextView) $(R.id.tv_name);
        tvShippingAddress = (TextView) $(R.id.tv_shipping_address);
        tvPhone = (TextView) $(R.id.tv_phone);
        ivUser = (ImageView) $(R.id.iv_user);
        llAddressTick = (LinearLayout) $(R.id.ll_address_tick);
    }

    @Override
    public void setData(final ShippingAddress data) {
        //Set data
        tvShippingAddress.setText(data.getAddress());
        tvName.setText(AppUtil.optStringNullCheckValue(data.getName()));
        if (AppUtil.optStringNullCheckValue(data.getPhone()).equalsIgnoreCase("")){
            tvPhone.setVisibility(View.GONE);
        } else {
            tvPhone.setVisibility(View.VISIBLE);
            tvPhone.setText(data.getPhone());
        }
        AppUtil.loadImage(getContext(), ivUser, R.drawable.avatar_placeholder_circular, false, true, false);
        if (data.isSelected()) {
            llAddressTick.setVisibility(View.VISIBLE);
        } else {
            llAddressTick.setVisibility(View.GONE);
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShippingAddressListAdapter addressListAdapter = (ShippingAddressListAdapter) getOwnerAdapter();
                for (ShippingAddress shippingResult : addressListAdapter.getAllData()) {
                    Logger.d("<<<mShippingAddressdata", data.toString());

                    if (shippingResult.getId().equalsIgnoreCase(data.getId())) {
                        llAddressTick.setVisibility(View.VISIBLE);
                        cvShippingAddress.setCardBackgroundColor(Color.parseColor("#407EBF2F"));
                        shippingResult.setSelected(true);
                        ((ShippingAddressActivity)getContext()).updateShippingAddress(shippingResult);

                    } else {
                        llAddressTick.setVisibility(View.GONE);
                        shippingResult.setSelected(false);
                    }
                }
                addressListAdapter.notifyDataSetChanged();
            }
        });

    }
}