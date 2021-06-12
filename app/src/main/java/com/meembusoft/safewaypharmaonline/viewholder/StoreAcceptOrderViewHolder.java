package com.meembusoft.safewaypharmaonline.viewholder;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.SupplierOrderDetailsActivity;
import com.meembusoft.safewaypharmaonline.activity.SupplierStoreDetailActivity;
import com.meembusoft.safewaypharmaonline.enumeration.DetailType;
import com.meembusoft.safewaypharmaonline.enumeration.OrderStatusType;
import com.meembusoft.safewaypharmaonline.model.PlaceOrderByItem;
import com.meembusoft.safewaypharmaonline.util.AllConstants;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.reversecoder.library.event.OnSingleClickListener;

import org.parceler.Parcels;

import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_DETAIL_TYPE;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_REQUEST_CODE_STORE;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class StoreAcceptOrderViewHolder extends BaseViewHolder<PlaceOrderByItem> {

    private static String TAG = StoreAcceptOrderViewHolder.class.getSimpleName();
    private TextView tvfName, tvfullName, tvShopName, tvReviewCount;
    private ImageView ivStore;
    private LinearLayout linAllStatusView, linOrderProgress, linOrderDeliveryOnTheWay, linOrderRecievedByUser, linOrderCompletedByStore;

    public StoreAcceptOrderViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_supplier_list);

        tvfName = (TextView) $(R.id.tv_store_first_name);
        tvfullName = (TextView) $(R.id.tv_store_full_name);
        tvShopName = (TextView) $(R.id.tv_store_shop_name);
        tvReviewCount = (TextView) $(R.id.tv_review_count);
        ivStore = (ImageView) $(R.id.iv_store);
        linAllStatusView = (LinearLayout) $(R.id.lin_all_status_view);
        linOrderProgress = (LinearLayout) $(R.id.lin_order_progress);
        linOrderDeliveryOnTheWay = (LinearLayout) $(R.id.lin_order_delivery_on_the_way);
        linOrderRecievedByUser = (LinearLayout) $(R.id.lin_order_received_by_user);
        linOrderCompletedByStore = (LinearLayout) $(R.id.lin_order_comleted);
    }

    @Override
    public void setData(final PlaceOrderByItem data) {
        //Set data
//        if (data.getCustomer_name() != null) {
//            String fName = data.getCustomer_name();
//            char[] firstChar = fName.toCharArray();
//            char fChar = firstChar[0];
//            tvfName.setText(fChar + "");
//        }

        if (AppUtil.optStringNullCheckValue(data.getShiping_charge_id()).equalsIgnoreCase("3")){
            tvfName.setVisibility(View.VISIBLE);
        } else {
            tvfName.setVisibility(View.GONE);
        }
        tvfullName.setText(AppUtil.optStringNullCheckValue(data.getCustomer_name()));

        if (AppUtil.optStringNullCheckValue(data.getGender()).equalsIgnoreCase(AllConstants.GENDER_FEMALE)){
            ivStore.setImageResource(R.drawable.vector_ic_female_user);
        } else {
            ivStore.setImageResource(R.drawable.vector_ic_login_user_grey);
        }
       // AppUtil.loadImage(getContext(), ivStore, "", false, false, true);

        tvShopName.setText(OrderStatusType.getMessage(data.getStatus()));

        if (data.getStatus().equalsIgnoreCase(OrderStatusType.ACCEPTED.toString())) {
            linOrderProgress.setVisibility(View.VISIBLE);
            linOrderDeliveryOnTheWay.setVisibility(View.GONE);
            tvReviewCount.setVisibility(View.GONE);
            linOrderRecievedByUser.setVisibility(View.GONE);
            linOrderCompletedByStore.setVisibility(View.GONE);
        } else if (data.getStatus().equalsIgnoreCase(OrderStatusType.DELIVERED_TO_DELIVERY_MAN.toString())) {
            linOrderProgress.setVisibility(View.VISIBLE);
            linOrderDeliveryOnTheWay.setVisibility(View.VISIBLE);
            tvReviewCount.setVisibility(View.GONE);
            linOrderRecievedByUser.setVisibility(View.GONE);
            linOrderCompletedByStore.setVisibility(View.GONE);
        } else if (data.getStatus().equalsIgnoreCase(OrderStatusType.RECEIVED_BY_USER.toString())) {
            linOrderProgress.setVisibility(View.VISIBLE);
            linOrderDeliveryOnTheWay.setVisibility(View.VISIBLE);
            tvReviewCount.setVisibility(View.GONE);
            linOrderRecievedByUser.setVisibility(View.VISIBLE);
            linOrderCompletedByStore.setVisibility(View.GONE);
        } else if (data.getStatus().equalsIgnoreCase(OrderStatusType.DONE.toString())) {
            linOrderProgress.setVisibility(View.VISIBLE);
            linOrderDeliveryOnTheWay.setVisibility(View.VISIBLE);
            tvReviewCount.setVisibility(View.GONE);
            linOrderRecievedByUser.setVisibility(View.VISIBLE);
            linOrderCompletedByStore.setVisibility(View.VISIBLE);
        } else {
            linAllStatusView.setVisibility(View.GONE);
        }
        tvReviewCount.setText(AppUtil.optStringNullCheckValue(data.getReview_rating()));

        itemView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                try {
                    Intent iStoreOrderDetails = new Intent(view.getContext(), SupplierOrderDetailsActivity.class);
                    iStoreOrderDetails.putExtra(INTENT_KEY_DETAIL_TYPE, DetailType.REGULAR.name());
                    iStoreOrderDetails.putExtra(AllConstants.INTENT_KEY_STORE, Parcels.wrap(data));
                    ((SupplierStoreDetailActivity) getContext()).startActivityForResult(iStoreOrderDetails, INTENT_REQUEST_CODE_STORE);

                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        });
    }
}