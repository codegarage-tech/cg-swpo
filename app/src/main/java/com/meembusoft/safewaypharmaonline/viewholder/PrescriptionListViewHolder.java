package com.meembusoft.safewaypharmaonline.viewholder;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.FullScreenActivity;
import com.meembusoft.safewaypharmaonline.activity.PlaceOrderDetailsActivity;
import com.meembusoft.safewaypharmaonline.activity.ProductDetailsActivity;
import com.meembusoft.safewaypharmaonline.model.PlaceOrderByItem;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.reversecoder.library.event.OnSingleClickListener;

import org.parceler.Parcels;

import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_CATEGORY_ID;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_FULL_SCREEN_IMAGE_URL;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_ORDER_ITEM;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_PRODUCT_ITEM;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class PrescriptionListViewHolder extends BaseViewHolder<PlaceOrderByItem> {

    private static String TAG = PrescriptionListViewHolder.class.getSimpleName();
    private TextView tvPlaceOrderName;
    private ImageView ivProduct;
    public PrescriptionListViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_prescription_item);

        tvPlaceOrderName = (TextView) $(R.id.tv_place_order_name);
        ivProduct = (ImageView) $(R.id.iv_product);

    }

    @Override
    public void setData(final PlaceOrderByItem data) {
        //Set data

       // tvPlaceOrderName.setText(data.getCustomer_name());
        AppUtil.loadImage(getContext(), ivProduct, data.getImage(), false, false, true);


        itemView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent iFullImageActivity = new Intent(getContext(), FullScreenActivity.class);
                iFullImageActivity.putExtra(INTENT_KEY_FULL_SCREEN_IMAGE_URL, data.getImage());
                getContext().startActivity(iFullImageActivity);
            }
        });

    }


}