package com.meembusoft.safewaypharmaonline.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.adapter.PlaceOrderDetailsListAdapter;
import com.meembusoft.safewaypharmaonline.base.BaseActivity;
import com.meembusoft.safewaypharmaonline.enumeration.DetailType;
import com.meembusoft.safewaypharmaonline.enumeration.OrderStatusType;
import com.meembusoft.safewaypharmaonline.model.OrderItems;
import com.meembusoft.safewaypharmaonline.model.ParamsOrderReceivedByUser;
import com.meembusoft.safewaypharmaonline.model.PlaceOrderByItem;
import com.meembusoft.safewaypharmaonline.retrofit.APIClient;
import com.meembusoft.safewaypharmaonline.retrofit.APIInterface;
import com.meembusoft.safewaypharmaonline.retrofit.APIResponse;
import com.meembusoft.safewaypharmaonline.util.AllConstants;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.BroadcastManager;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.view.CanaroTextView;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.util.AllSettingsManager;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import retrofit2.Call;
import retrofit2.Response;

import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_FULL_SCREEN_IMAGE_URL;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_ORDER_ITEM;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class PlaceOrderDetailsActivity extends BaseActivity {

    //Toolbar
    private ImageView ivBack, ivPicture;
    private CanaroTextView tvTitle;
    private RelativeLayout relOrderStatusView;

    private LinearLayout linPlaceOrderDetails, linNoDataFound;
    // private WaveSwipeRefreshRecyclerView rvPlaceOrder;
    private RecyclerView rvPlaceOrder;
    private TextView tvTotalsAmount, tvSubtotalsAmount, tvDeliveryFeeAmount, tvDiscountAmount;
    private Button btnOrderStatus;

    private PlaceOrderDetailsListAdapter placeOrderDetailsListAdapter;
    private boolean isSwipeRefreshTask = false;
    private List<OrderItems> itemList = new ArrayList<>();
    private PlaceOrderByItem placeOrderByItem;
    //Background task
    private DoOrderReceivedStatusByUserTask doOrderReceivedStatusByUserTask;
    private APIInterface mApiInterface;
    float subTotalAmount = 0.0f, shippingAmount = 0.0f, discountAmount = 0.0f, netTotalAmount = 0.0f, ratingLabel = 0.0f;
    private MaterialRatingBar materialRatingBar;
    private DetailType mDetailType;
    public static String INTENT_ACTION_CUSTOMER_ORDER_DETAIL = "INTENT_ACTION_CUSTOMER_ORDER_DETAIL";

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }


    @Override
    public int initActivityLayout() {
        return R.layout.activity_place_order_details_screen;
    }

    @Override
    public void initStatusBarView() {
        // StatusBarUtil.setTransparent(getActivity());
    }

    @Override
    public void initNavigationBarView() {

    }

    @Override
    public void initIntentData(Bundle savedInstanceState, Intent intent) {
        if (intent != null) {
            String mParcelableDetailType = intent.getStringExtra(AllConstants.INTENT_KEY_DETAIL_TYPE);
            if (!AllSettingsManager.isNullOrEmpty(mParcelableDetailType)) {
                mDetailType = DetailType.valueOf(mParcelableDetailType);
                Logger.d(TAG, TAG + " >>> " + "mDetailType: " + mDetailType);

                switch (mDetailType) {
                    case REGULAR:
                        Parcelable mParcelable = intent.getParcelableExtra(AllConstants.INTENT_KEY_ORDER_ITEM);
                        if (mParcelable != null) {
                            placeOrderByItem = Parcels.unwrap(mParcelable);
                            Logger.d(TAG, TAG + " >>> " + "placeOrderByItem: " + placeOrderByItem.toString());
                        }
                        break;
                    case FCM:
                        Parcelable mFCMParcelable = intent.getParcelableExtra(AllConstants.INTENT_KEY_FCM_ORDER_ITEM);
                        if (mFCMParcelable != null) {
                            placeOrderByItem = Parcels.unwrap(mFCMParcelable);
                            Logger.d(TAG, TAG + " >>> " + "placeOrderByItem: " + placeOrderByItem.toString());
                        }
                        break;
                }
            }
        }
    }

    @Override
    public void initActivityViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (CanaroTextView) findViewById(R.id.text_title);
        tvTitle.setText(getActivity().getResources().getString(R.string.title_order_details));
        rvPlaceOrder = (RecyclerView) findViewById(R.id.rv_place_details_order);
        // rvPlaceOrder = (WaveSwipeRefreshRecyclerView ) findViewById(R.id.rv_place_details_order);
        tvSubtotalsAmount = (TextView) findViewById(R.id.tv_subtotals_amt);
        tvDeliveryFeeAmount = (TextView) findViewById(R.id.tv_delivery_fee);
        tvDiscountAmount = (TextView) findViewById(R.id.tv_discount_amt);
        tvTotalsAmount = (TextView) findViewById(R.id.tv_totals_amount);
        ivPicture = (ImageView) findViewById(R.id.iv_picture);
        btnOrderStatus = (Button) findViewById(R.id.btn_order_status);
        relOrderStatusView = (RelativeLayout) findViewById(R.id.rel_order_status_view);
        linPlaceOrderDetails = (LinearLayout) findViewById(R.id.lin_place_order_details);
        linNoDataFound = (LinearLayout) findViewById(R.id.lin_no_data_found_layout);
        materialRatingBar = (MaterialRatingBar) findViewById(R.id.rb_item_rating);
    }

    private void setStatusView() {
        if (placeOrderByItem != null) {
            if (placeOrderByItem.getStatus().equalsIgnoreCase(OrderStatusType.DELIVERED_TO_DELIVERY_MAN.toString())) {
                btnOrderStatus.setVisibility(View.VISIBLE);
                relOrderStatusView.setVisibility(View.VISIBLE);
                materialRatingBar.setVisibility(View.VISIBLE);
                btnOrderStatus.setText(getResources().getString(R.string.txt_order_received_status));
            } else {
                btnOrderStatus.setVisibility(View.GONE);
                relOrderStatusView.setVisibility(View.GONE);
                materialRatingBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
        } else {
            if (placeOrderByItem != null) {
                setStatusView();
                // Register BroadCast
                BroadcastManager.registerBroadcastUpdate(getActivity(), notificationUpdate);

                initItemList(placeOrderByItem);
            }
        }
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {
        ivBack.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                initActivityBackPress();
            }
        });

//        rvPlaceOrder.setRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (!NetworkManager.isConnected(getActivity())) {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
//                    rvPlaceOrder.setRefreshing(false);
//                    return;
//                }
//
//                isSwipeRefreshTask = true;
//                initItemList();
//            }
//        });

        btnOrderStatus.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (!NetworkManager.isConnected(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                } else {
                    //Order Received Status
                    if (placeOrderByItem != null && (placeOrderByItem.getStatus().equalsIgnoreCase(OrderStatusType.DELIVERED_TO_DELIVERY_MAN.toString()))) {
                        if (doOrderReceivedStatusByUserTask != null && doOrderReceivedStatusByUserTask.getStatus() == AsyncTask.Status.RUNNING) {
                            doOrderReceivedStatusByUserTask.cancel(true);
                        }

                        doOrderReceivedStatusByUserTask = new DoOrderReceivedStatusByUserTask(getActivity());
                        doOrderReceivedStatusByUserTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
            }
        });

        ivPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (placeOrderByItem != null) {
                    Intent iFullImageActivity = new Intent(getActivity(), FullScreenActivity.class);
                    iFullImageActivity.putExtra(INTENT_KEY_FULL_SCREEN_IMAGE_URL, placeOrderByItem.getImage());
                    startActivity(iFullImageActivity);
                }
            }
        });

        materialRatingBar.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                ratingLabel = rating;
                Logger.d(TAG, TAG + " >>>rating " + ratingLabel + "GOOD");

            }
        });
    }

    private void initItemList(PlaceOrderByItem mPlaceOrderByItem) {
//        isSwipeRefreshTask = false;
//        rvPlaceOrder.setRefreshing(false);
        if (mPlaceOrderByItem != null) {
            if (mPlaceOrderByItem.getItems() != null && mPlaceOrderByItem.getItems().size() > 0) {
                Logger.d(TAG, TAG + " >>> " + "placeOrderByItem: " + mPlaceOrderByItem.getItems().toString());
                subTotalAmount = AppUtil.formatFloat(AppUtil.getStoreOrderFromSubtotalPrice(mPlaceOrderByItem.getItems()));

                linPlaceOrderDetails.setVisibility(View.VISIBLE);
                linNoDataFound.setVisibility(View.GONE);
                placeOrderDetailsListAdapter = new PlaceOrderDetailsListAdapter(getActivity());
                rvPlaceOrder.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                rvPlaceOrder.setAdapter(placeOrderDetailsListAdapter);
                placeOrderDetailsListAdapter.addAll(mPlaceOrderByItem.getItems());
            } else {
                linPlaceOrderDetails.setVisibility(View.GONE);
                linNoDataFound.setVisibility(View.VISIBLE);
            }
            shippingAmount = (((!AllSettingsManager.isNullOrEmpty(mPlaceOrderByItem.getShiping_charge()) && (Float.parseFloat(mPlaceOrderByItem.getShiping_charge()) > 0.0f))) ? (Float.parseFloat(mPlaceOrderByItem.getShiping_charge())) : 0.0f);
            discountAmount = (((!AllSettingsManager.isNullOrEmpty(mPlaceOrderByItem.getDiscount()) && (Float.parseFloat(mPlaceOrderByItem.getDiscount()) > 0.0f))) ? (Float.parseFloat(mPlaceOrderByItem.getDiscount())) : 0.0f);
            netTotalAmount = AppUtil.getTotalPrice(subTotalAmount, discountAmount, shippingAmount);

            tvSubtotalsAmount.setText(subTotalAmount + " " + getResources().getString(R.string.title_tk));
            tvDeliveryFeeAmount.setText(shippingAmount + " " + getResources().getString(R.string.title_tk));
            tvDiscountAmount.setText(discountAmount + " " + getResources().getString(R.string.title_tk));
            tvTotalsAmount.setText(netTotalAmount + " " + getResources().getString(R.string.title_tk));

            if (mPlaceOrderByItem.getImage() != null) {
                AppUtil.loadImage(getActivity(), ivPicture, mPlaceOrderByItem.getImage(), false, false, true);
            }
        }
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        if (intent != null) {
//            Parcelable mFCMParcelable = intent.getParcelableExtra(AllConstants.INTENT_KEY_FCM_ORDER_ITEM);
//            if (mFCMParcelable != null) {
//                placeOrderByItem = Parcels.unwrap(mFCMParcelable);
//                Logger.d(TAG, TAG + " >>>FCM " + "placeOrderByItem: " + placeOrderByItem.toString());
//                setStatusView();
//                initItemList(placeOrderByItem);
//            }
//        }
//    }

    /*****************************
     * Order notification update *
     *****************************/
    BroadcastReceiver notificationUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            PlaceOrderByItem order = BroadcastManager.getBroadcastData(intent);
            if (order != null) {
                placeOrderByItem = order;
                setStatusView();
                Logger.d(TAG, TAG + ">>notificationUpdate>> FCM: Updated order detail");
            }
        }
    };

    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void initActivityBackPress() {
        if (mDetailType == DetailType.REGULAR) {
            Intent intentAccepted = new Intent();
            intentAccepted.putExtra(INTENT_KEY_ORDER_ITEM, Parcels.wrap(placeOrderByItem));
            overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
            setResult(RESULT_OK, intentAccepted);
        } else {
            BroadcastManager.registerBroadcastUpdate(getActivity(), new IntentFilter(INTENT_ACTION_CUSTOMER_ORDER_DETAIL), notificationUpdate);
            BroadcastManager.sendBroadcast(getActivity(), new Intent(INTENT_ACTION_CUSTOMER_ORDER_DETAIL) ,placeOrderByItem);
        }
        finish();
    }

    @Override
    public void initActivityDestroyTasks() {
        dismissPopupDialog();
        // Unregister BroadCast
        BroadcastManager.unregisterBroadcastUpdate(getActivity(), notificationUpdate);

        if (doOrderReceivedStatusByUserTask != null && doOrderReceivedStatusByUserTask.getStatus() == AsyncTask.Status.RUNNING) {
            doOrderReceivedStatusByUserTask.cancel(true);
        }
    }

    @Override
    public void initActivityPermissionResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /************************
     * Server communication *
     ************************/
    private class DoOrderReceivedStatusByUserTask extends AsyncTask<String, Integer, Response> {

        Context mContext;

        public DoOrderReceivedStatusByUserTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Logger.d(TAG, TAG + ">> Background task is cancelled");
        }

        @Override
        protected void onPreExecute() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showPopupDialog();
                }
            }, 50);
        }

        @Override
        protected Response doInBackground(String... params) {
            try {
                ParamsOrderReceivedByUser mParamsDeliveryOnTheWay = new ParamsOrderReceivedByUser(placeOrderByItem.getId(), String.valueOf(ratingLabel));
                Logger.d(TAG, TAG + " mParamsDeliveryOnTheWay>>> " + "DoOrderReceivedStatusByUserTask: " + mParamsDeliveryOnTheWay.toString());

                Call<APIResponse> call = mApiInterface.apiOrderReceivedByUser(mParamsDeliveryOnTheWay);

                Response response = call.execute();
                if (response.isSuccessful()) {
                    return response;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Response result) {
            try {
                dismissPopupDialog();

                if (result != null) {
                    Logger.d(TAG, "APIResponse(DoOrderDeliveryStatusTask): onResponse-server = " + result.toString());
                    APIResponse data = (APIResponse) result.body();
                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(DoOrderReceivedStatusByUserTask()): onResponse-object = " + data.toString());
                        placeOrderByItem.setStatus(OrderStatusType.RECEIVED_BY_USER.toString());
                        Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_SHORT).show();
                        initActivityBackPress();
                    } else {
                        Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}