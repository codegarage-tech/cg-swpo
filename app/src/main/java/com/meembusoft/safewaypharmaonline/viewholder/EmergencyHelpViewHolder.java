package com.meembusoft.safewaypharmaonline.viewholder;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.CategoryActivity;
import com.meembusoft.safewaypharmaonline.activity.EmergencyHelpActivity;
import com.meembusoft.safewaypharmaonline.activity.ProductDetailsActivity;
import com.meembusoft.safewaypharmaonline.adapter.CategoryListAdapter;
import com.meembusoft.safewaypharmaonline.model.Category;
import com.meembusoft.safewaypharmaonline.model.Emergency;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.reversecoder.library.event.OnSingleClickListener;

import org.parceler.Parcels;

import java.util.List;

import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_PRODUCT_ITEM;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class EmergencyHelpViewHolder extends BaseViewHolder<Emergency> {

    private static String TAG = EmergencyHelpViewHolder.class.getSimpleName();
    private TextView titleView;
    private ImageView ivProduct;

    public EmergencyHelpViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_emergency_help);
        ivProduct = (ImageView) $(R.id.iv_product);

        titleView = (TextView) $(R.id.tv_title);
    }

    @Override
    public void setData(final Emergency emergency) {
        //Set data
        titleView.setText(emergency.getTitle());
        AppUtil.loadImage(getContext(), ivProduct, emergency.getImage(), false, false, true);

        itemView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                EmergencyHelpActivity mActivity = ((EmergencyHelpActivity) getContext());
                mActivity.emergencyHelpList(emergency);
            }
        });
    }

}