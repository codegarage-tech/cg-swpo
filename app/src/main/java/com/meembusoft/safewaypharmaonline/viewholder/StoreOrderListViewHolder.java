package com.meembusoft.safewaypharmaonline.viewholder;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.SupplierStoreDetailActivity;
import com.meembusoft.safewaypharmaonline.activity.SupplierOrderDetailsActivity;
import com.meembusoft.safewaypharmaonline.enumeration.DetailType;
import com.meembusoft.safewaypharmaonline.model.PlaceOrderByItem;
import com.meembusoft.safewaypharmaonline.util.AllConstants;
import com.meembusoft.safewaypharmaonline.util.AppUtil;

import org.parceler.Parcels;

import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_DETAIL_TYPE;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_REQUEST_CODE_STORE;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class StoreOrderListViewHolder extends BaseViewHolder<PlaceOrderByItem> {

    private static String TAG = StoreOrderListViewHolder.class.getSimpleName();
    private TextView tvfName,tvfullName;
    private ImageView ivStore;
    LinearLayout linRowMain;

    public StoreOrderListViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_store_item);

        tvfName = (TextView) $(R.id.tv_store_first_name);
        tvfullName = (TextView) $(R.id.tv_store_full_name);
        ivStore = (ImageView) $(R.id.iv_store);
        linRowMain = (LinearLayout) $(R.id.lin_row_main);
    }

    @Override
    public void setData(final PlaceOrderByItem data) {
        //Set data
        if (AppUtil.optStringNullCheckValue(data.getShiping_charge_id()).equalsIgnoreCase("3")){
            tvfName.setVisibility(View.VISIBLE);
        } else {
            tvfName.setVisibility(View.GONE);
        }
        tvfullName.setText(data.getCustomer_name());

        if (AppUtil.optStringNullCheckValue(data.getGender()).equalsIgnoreCase(AllConstants.GENDER_FEMALE)){
            ivStore.setImageResource(R.drawable.vector_ic_female_user);
        } else {
            ivStore.setImageResource(R.drawable.vector_ic_login_user_grey);
        }
      //  AppUtil.loadImage(getContext(), ivStore, "", false, false, true);



        linRowMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iStoreDetails = new Intent(view.getContext(), SupplierOrderDetailsActivity.class);
                iStoreDetails.putExtra(INTENT_KEY_DETAIL_TYPE, DetailType.REGULAR.name());
                iStoreDetails.putExtra(AllConstants.INTENT_KEY_STORE, Parcels.wrap(data));
                ((SupplierStoreDetailActivity) getContext()).startActivityForResult(iStoreDetails, INTENT_REQUEST_CODE_STORE);

            }
        });

    }
}