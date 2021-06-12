package com.meembusoft.safewaypharmaonline.viewholder;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.PlaceOrderDetailsActivity;
import com.meembusoft.safewaypharmaonline.activity.PlaceOrderViewActivity;
import com.meembusoft.safewaypharmaonline.activity.SupplierCompletedActivity;
import com.meembusoft.safewaypharmaonline.enumeration.DetailType;
import com.meembusoft.safewaypharmaonline.enumeration.OrderStatusType;
import com.meembusoft.safewaypharmaonline.model.PlaceOrderByItem;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.reversecoder.library.event.OnSingleClickListener;

import org.parceler.Parcels;

import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_DETAIL_TYPE;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_ORDER_ITEM;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_REQUEST_CODE_PLACE_ORDER_DETAILS;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class PlaceOrderListViewHolder extends BaseViewHolder<PlaceOrderByItem> {

    private static String TAG = PlaceOrderListViewHolder.class.getSimpleName();
    private TextView tvPlaceOrderName, tvPlaceOrderTotals, tvPlaceOrderAdress, tvPlaceOrderDate, tvPlaceOrderStatus;
    private View rowPlaceOrderViewRibbon;
    Activity mActivity;
    private Button btnPlaceOrderDelete;

    public PlaceOrderListViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_place_order_item);
        mActivity = ((Activity) getContext());

        tvPlaceOrderName = (TextView) $(R.id.tv_place_order_name);
        tvPlaceOrderTotals = (TextView) $(R.id.tv_place_order_totals_amount);
        tvPlaceOrderAdress = (TextView) $(R.id.tv_place_order_address);
        tvPlaceOrderDate = (TextView) $(R.id.tv_place_order_date);
        tvPlaceOrderStatus = (TextView) $(R.id.tv_place_order_transaction_status);
        btnPlaceOrderDelete = (Button) $(R.id.btn_place_order_delete);
        rowPlaceOrderViewRibbon = (View) $(R.id.row_place_order_view_ribbon);

    }

    @Override
    public void setData(final PlaceOrderByItem data) {
        //Set data

        tvPlaceOrderName.setText(AppUtil.optStringNullCheckValue(data.getCustomer_name()));
        tvPlaceOrderAdress.setText(AppUtil.optStringNullCheckValue(data.getShiping_address()));
        tvPlaceOrderDate.setText(AppUtil.optStringNullCheckValue(data.getOrder_date()));
        tvPlaceOrderTotals.setText(getContext().getResources().getString(R.string.title_tk) + " " +AppUtil.optStringNullCheckValue( data.getTotal_price()) + "");
        tvPlaceOrderStatus.setText(OrderStatusType.getMessage(data.getStatus()));
        Logger.d("PlaceOrderByItem>>>", data.toString());

        if (data.getStatus().equalsIgnoreCase(OrderStatusType.RECEIVED_BY_USER.toString()) || data.getStatus().equalsIgnoreCase(OrderStatusType.DONE.toString())) {
            rowPlaceOrderViewRibbon.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorOrderStateGreen));
            tvPlaceOrderStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.colorOrderStateGreen));
        } else {
            rowPlaceOrderViewRibbon.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorOrderStateBlue));
            tvPlaceOrderStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.colorOrderStateBlue));
        }

        if (mActivity instanceof PlaceOrderViewActivity) {
            btnPlaceOrderDelete.setVisibility(View.VISIBLE);
        } else {
            btnPlaceOrderDelete.setVisibility(View.GONE);
        }

        btnPlaceOrderDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d("getId>>>", data.getId());
                ((PlaceOrderViewActivity)  getContext()).deleteOrder(data);
            }
        });
//
//       btnPlaceOrderDelete.setOnClickListener(new OnSingleClickListener() {
//            @Override
//            public void onSingleClick(View view) {
//                if (mActivity instanceof PlaceOrderViewActivity) {
//                    Logger.d("getId>>>", data.getId());
//
//                    ((PlaceOrderViewActivity) mActivity).deleteOrder(data.getId());
//                }
//            }
//        });

        itemView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                try {
                    if (data.getItems() != null && data.getItems().size() > 0) {
                        Intent iOrderDetails = new Intent(getContext(), PlaceOrderDetailsActivity.class);
                        iOrderDetails.putExtra(INTENT_KEY_ORDER_ITEM, Parcels.wrap(data));
                        iOrderDetails.putExtra(INTENT_KEY_DETAIL_TYPE, DetailType.REGULAR.name());
                        if (mActivity instanceof SupplierCompletedActivity) {
                            ((SupplierCompletedActivity) getContext()).startActivityForResult(iOrderDetails, INTENT_REQUEST_CODE_PLACE_ORDER_DETAILS);
                        } else if (mActivity instanceof PlaceOrderViewActivity) {
                            ((PlaceOrderViewActivity) getContext()).startActivityForResult(iOrderDetails, INTENT_REQUEST_CODE_PLACE_ORDER_DETAILS);
                        }
                    } else {
                        Toast.makeText(getContext(), getContext().getString(R.string.toast_no_item_found), Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        });
    }
}