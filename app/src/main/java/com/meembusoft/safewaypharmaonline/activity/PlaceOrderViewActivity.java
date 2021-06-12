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
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.waveswiperefresh.WaveSwipeRefreshRecyclerView;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.adapter.PlaceOrderListAdapter;
import com.meembusoft.safewaypharmaonline.base.BaseActivity;
import com.meembusoft.safewaypharmaonline.enumeration.FlavorType;
import com.meembusoft.safewaypharmaonline.enumeration.LoginSettingType;
import com.meembusoft.safewaypharmaonline.model.AppSupplier;
import com.meembusoft.safewaypharmaonline.model.AppUser;
import com.meembusoft.safewaypharmaonline.model.ParamsOrderTrush;
import com.meembusoft.safewaypharmaonline.model.PlaceOrderByItem;
import com.meembusoft.safewaypharmaonline.model.StoreOrder;
import com.meembusoft.safewaypharmaonline.retrofit.APIClient;
import com.meembusoft.safewaypharmaonline.retrofit.APIInterface;
import com.meembusoft.safewaypharmaonline.retrofit.APIResponse;
import com.meembusoft.safewaypharmaonline.util.AllConstants;
import com.meembusoft.safewaypharmaonline.util.BroadcastManager;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.util.SessionUtil;
import com.meembusoft.safewaypharmaonline.view.CanaroTextView;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;

import org.parceler.Parcels;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

import static com.meembusoft.safewaypharmaonline.activity.PlaceOrderDetailsActivity.INTENT_ACTION_CUSTOMER_ORDER_DETAIL;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_LOGIN_SETTING_TYPE;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_REQUEST_CODE_PLACE_ORDER_DETAILS;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_REQUEST_CODE_USER_LOGIN;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class PlaceOrderViewActivity extends BaseActivity {


    private ImageView ivBack;
    private CanaroTextView tvTitle;
    private RelativeLayout rlCart;
    private LinearLayout linLoginLayout, linNoDataFound;
    private Button btnLogin;

    private WaveSwipeRefreshRecyclerView rvPlaceOrder;
    private PlaceOrderListAdapter mPlaceOrderListAdapter;
    private AppUser mAppUser;
    //More loading
    private int mOffset = 0, mItemLimit = AllConstants.PER_PAGE_ITEM, mTotalcount = 0;
    private Handler mHandler = new Handler();
    //Background task
    private GetAllOrderPlaceTask getAllOrderPlaceTask;
    private DoOrderDeleteTask doOrderDeleteTask;
    private APIInterface mApiInterface;
    private boolean isSwipeRefreshTask = false;
    private PlaceOrderByItem placeOrderByItem;
    //  Supplier
    private AppSupplier mAppSupplier;
    FlavorType mFlavorType;
    private String customerOrSupplierId = "";
    private String  selectUserType = AllConstants.USER_TYPE_CUSTOMER;
    ;
    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }


    @Override
    public int initActivityLayout() {
        return R.layout.activity_place_order_screen;
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

    }

    @Override
    public void initActivityViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (CanaroTextView) findViewById(R.id.text_title);
        tvTitle.setText(getActivity().getResources().getString(R.string.title_my_order));
        rlCart = (RelativeLayout) findViewById(R.id.rl_cart);
        rlCart.setVisibility(View.GONE);
        rvPlaceOrder = (WaveSwipeRefreshRecyclerView) findViewById(R.id.rv_place_order);
        btnLogin = (Button) findViewById(R.id.btn_login);
        linLoginLayout = (LinearLayout) findViewById(R.id.lin_login_layout);
        linNoDataFound = (LinearLayout) findViewById(R.id.lin_no_data_found_layout);
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        //Register notification broadcast
        Logger.d(TAG, TAG + ">> FCM: notification update is registered");
        BroadcastManager.registerBroadcastUpdate(getActivity(), notificationUpdate);
        BroadcastManager.registerBroadcastUpdate(getActivity(), new IntentFilter(INTENT_ACTION_CUSTOMER_ORDER_DETAIL), notificationUpdate);

        mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        // Get User Tag
        initGetTagValue();
        initializeData();
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {
        ivBack.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });

        btnLogin.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (SessionUtil.getUser(getActivity()) != null) {
                } else {
                    Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
                    intentLogin.putExtra(INTENT_KEY_LOGIN_SETTING_TYPE, LoginSettingType.MENU_TO_LOGIN.name());
                    getActivity().startActivityForResult(intentLogin, INTENT_REQUEST_CODE_USER_LOGIN);
                }
            }
        });
    }

    private void initGetTagValue() {
        String userType = SessionManager.getStringSetting(getActivity(), SessionUtil.SESSION_KEY_USER_TAG);
        if (!AllSettingsManager.isNullOrEmpty(userType)) {
            mFlavorType = FlavorType.getFlavor(SessionManager.getStringSetting(getActivity(), SessionUtil.SESSION_KEY_USER_TAG));
        } else {
            mFlavorType = FlavorType.CUSTOMER;
        }
    }

    private void initializeData() {

        setRecyclerView();

        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
        } else {
            if (mFlavorType == FlavorType.CUSTOMER) {
                selectUserType = AllConstants.USER_TYPE_CUSTOMER;
                if (SessionUtil.getUser(getActivity()) != null) {
                    mAppUser = SessionUtil.getUser(getActivity());
                    customerOrSupplierId = mAppUser.getId();
                    rvPlaceOrder.setVisibility(View.VISIBLE);
                    linLoginLayout.setVisibility(View.GONE);
                    linNoDataFound.setVisibility(View.GONE);
                    setData();
                } else {
                    rvPlaceOrder.setVisibility(View.GONE);
                    linLoginLayout.setVisibility(View.VISIBLE);
                    linNoDataFound.setVisibility(View.GONE);
                }
            } else if (mFlavorType == FlavorType.SUPPLIER){
                selectUserType = AllConstants.USER_TYPE_SUPPLIER;
                if (SessionUtil.getSupplier(getActivity()) != null) {
                    mAppSupplier = SessionUtil.getSupplier(getActivity());
                    customerOrSupplierId = mAppSupplier.getUser_id();
                    rvPlaceOrder.setVisibility(View.VISIBLE);
                    linLoginLayout.setVisibility(View.GONE);
                    linNoDataFound.setVisibility(View.GONE);
                    setData();
                } else {
                    rvPlaceOrder.setVisibility(View.GONE);
                    linLoginLayout.setVisibility(View.VISIBLE);
                    linNoDataFound.setVisibility(View.GONE);
                }
            }
        }
    }

    private void setRecyclerView() {
        mPlaceOrderListAdapter = new PlaceOrderListAdapter(getActivity());
        rvPlaceOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPlaceOrder.setAdapter(mPlaceOrderListAdapter);
        mPlaceOrderListAdapter.setMore(R.layout.view_more, new RecyclerArrayAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Logger.d(TAG, TAG + " >>> " + "onLoadMore(mOffset): " + mOffset);
                Logger.d(TAG, TAG + " >>> " + "onLoadMore(mTotalcount): " + mTotalcount);
                Logger.d(TAG, TAG + " >>> " + "onLoadMore(mPlaceOrderListAdapter): " + mPlaceOrderListAdapter.getCount());
                if (mOffset < mTotalcount) {
                    Logger.d(TAG, TAG + " >>> " + "onLoadMore: started loading");
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setData();
                        }
                    }, 2000);
                } else {
                    //No more item
                    mPlaceOrderListAdapter.addAll(new ArrayList<PlaceOrderByItem>());
                }
            }
        });
        mPlaceOrderListAdapter.setNoMore(R.layout.view_nomore);
        mPlaceOrderListAdapter.setError(R.layout.view_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
                mPlaceOrderListAdapter.resumeMore();
            }

            @Override
            public void onErrorClick() {
                mPlaceOrderListAdapter.resumeMore();
            }
        });
    }

    private void setData() {
        if (!NetworkManager.isConnected(getActivity())) {
          //  dismissPopupDialog();
            //This is for more loading
            mPlaceOrderListAdapter.pauseMore();
            //   tvSuggestion.setText(getString(R.string.view_please_connect_internet_and_retry));
            Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (getAllOrderPlaceTask != null && getAllOrderPlaceTask.getStatus() == AsyncTask.Status.RUNNING) {
                Logger.d(TAG, TAG + " >>> " + "setData: canceling asynctask");
                getAllOrderPlaceTask.cancel(true);
            }

            if (customerOrSupplierId != null && !TextUtils.isEmpty(customerOrSupplierId)) {
                getAllOrderPlaceTask = new GetAllOrderPlaceTask(getActivity());
                getAllOrderPlaceTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    }

    public void deleteOrder(final PlaceOrderByItem placeOrderByItem){
        Logger.d(TAG, TAG + "data >>> " + placeOrderByItem.toString());

        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
        } else {
                if (placeOrderByItem != null) {
                    doOrderDeleteTask = new DoOrderDeleteTask(getActivity(), placeOrderByItem);
                    doOrderDeleteTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }

        }
    }

    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case INTENT_REQUEST_CODE_USER_LOGIN:
                Logger.d(TAG, TAG + " >>> " + "INTENT_REQUEST_CODE_LOGIN: " + requestCode);
                if (data != null && resultCode == RESULT_OK) {
                    if (!NetworkManager.isConnected(getActivity())) {
                        Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                        // loadOfflineTimeData();
                    } else {
                        linLoginLayout.setVisibility(View.GONE);
                        linNoDataFound.setVisibility(View.GONE);
                        rvPlaceOrder.setVisibility(View.VISIBLE);
                        setRecyclerView();
                        if (getAllOrderPlaceTask != null && getAllOrderPlaceTask.getStatus() == AsyncTask.Status.RUNNING) {
                            getAllOrderPlaceTask.cancel(true);
                        }
                        getAllOrderPlaceTask = new GetAllOrderPlaceTask(getActivity());
                        getAllOrderPlaceTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }

                break;

            case INTENT_REQUEST_CODE_PLACE_ORDER_DETAILS:
                Logger.d(TAG, TAG + " >>> " + "REQUEST_CODE_PLACE_ORDER_DETAILS: " + requestCode);
                if (data != null && resultCode == RESULT_OK) {
                    Parcelable mParcelableOrder = data.getParcelableExtra(AllConstants.INTENT_KEY_ORDER_ITEM);
                    if (mParcelableOrder != null) {
                        PlaceOrderByItem mOrder = Parcels.unwrap(mParcelableOrder);
                        Logger.d(TAG, TAG + " >>> " + "mParcelableOrder: >>>" + mOrder.toString() + "");
                        if (mOrder != null) {
                            mPlaceOrderListAdapter.updateItem(mOrder);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void initActivityBackPress() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    @Override
    public void initActivityDestroyTasks() {
        //Unregister notification broadcast
        Logger.d(TAG, TAG + ">> FCM: notification update is unregistered");
        BroadcastManager.unregisterBroadcastUpdate(getActivity(), notificationUpdate);

        dismissPopupDialog();

        if (getAllOrderPlaceTask != null && getAllOrderPlaceTask.getStatus() == AsyncTask.Status.RUNNING) {
            getAllOrderPlaceTask.cancel(true);
        }

        if (doOrderDeleteTask != null && doOrderDeleteTask.getStatus() == AsyncTask.Status.RUNNING) {
            doOrderDeleteTask.cancel(true);
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
    private class GetAllOrderPlaceTask extends AsyncTask<String, Integer, Response> {

        Context mContext;

        private GetAllOrderPlaceTask(Context context) {
            mContext = context;

        }

        @Override
        protected void onPreExecute() {

            if (mOffset == 0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showPopupDialog();
                    }
                }, 50);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Logger.d(TAG, TAG + ">> Background task is cancelled");
        }

        @Override
        protected Response doInBackground(String... params) {
            try {
                Call<APIResponse<StoreOrder>> call = mApiInterface.apiGetPlaceOrderByCustomerList(customerOrSupplierId, mOffset, mItemLimit,selectUserType);
                Response response = call.execute();
                Logger.d("GetAllOrderPlaceTask", response + "response");

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
                Logger.d("isSuccessful", result.isSuccessful() + "");

                if (result.isSuccessful()) {
                    Logger.d(TAG, "APIResponse(GetAllOrderPlaceTask): onResponse-server = " + result.toString());
                    APIResponse<StoreOrder> data = (APIResponse<StoreOrder>) result.body();
                    Logger.d("GetAllOrderPlaceTask", data.toString() + "");

                    if (data.getData() != null && data.getData().getOrders().size() > 0) {
                        rvPlaceOrder.setVisibility(View.VISIBLE);
                        linLoginLayout.setVisibility(View.GONE);
                        linNoDataFound.setVisibility(View.GONE);
                        Logger.d(TAG, "APIResponse(GetAllOrderPlaceTask()): onResponse-object = " + data.toString());
                        mPlaceOrderListAdapter.addAll(data.getData().getOrders());

                        //This is needed for more loading
                        mOffset = mOffset + AllConstants.PER_PAGE_ITEM;
                        mItemLimit = mItemLimit + AllConstants.PER_PAGE_ITEM;
                        mTotalcount = data.getData().getTotals_count();
                        Logger.d("mTotalcount", mTotalcount + "");
                    } else {

                        rvPlaceOrder.setVisibility(View.GONE);
                        linLoginLayout.setVisibility(View.GONE);
                        linNoDataFound.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private class DoOrderDeleteTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        PlaceOrderByItem placeOrderByItem;

        private DoOrderDeleteTask(Context context,PlaceOrderByItem placeOrderByItems) {
            mContext = context;
            placeOrderByItem = placeOrderByItems;

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
        protected void onCancelled() {
            super.onCancelled();
            Logger.d(TAG, TAG + ">> Background task is cancelled");
        }

        @Override
        protected Response doInBackground(String... params) {
            try {
                Logger.d("orderIds", placeOrderByItem.getId() + "orderIds");
                ParamsOrderTrush paramsOrderTrush = new ParamsOrderTrush(placeOrderByItem.getId());
                Logger.d(TAG, TAG + " >>> APIResponse(paramsOrderTrush): " + paramsOrderTrush.toString());
                Call<APIResponse> call = mApiInterface.apiCustomerOrderTrush(paramsOrderTrush);
                Response response = call.execute();
                Logger.d("DoOrderDeleteTask", response + "response");

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
                Logger.d("isSuccessful", result.isSuccessful() + "");

                if (result.isSuccessful()) {
                    Logger.d(TAG, "APIResponse(GetAllOrderPlaceTask): onResponse-server = " + result.toString());
                    APIResponse data = (APIResponse) result.body();
                    Logger.d("DoOrderDeleteTask", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        mPlaceOrderListAdapter.removeItem(placeOrderByItem);

                        Toast.makeText(getActivity(), getResources().getString(R.string.toast_order_delete_mgs), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }


    /*****************************
     * Order notification update *
     *****************************/
    BroadcastReceiver notificationUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();

                if (!AllSettingsManager.isNullOrEmpty(action)) {
                    PlaceOrderByItem order = BroadcastManager.getBroadcastData(intent);

                    if (order != null) {
                        // For place order detail
                        if (action.equalsIgnoreCase(INTENT_ACTION_CUSTOMER_ORDER_DETAIL)) {
                            if (mPlaceOrderListAdapter != null) {
                                mPlaceOrderListAdapter.addOrUpdateItem(order);
                                Logger.d(TAG, TAG + ">>notificationUpdate>> customer: Updated order list from");
                            }
                        } else {
                            // For fcm
                            if (mPlaceOrderListAdapter != null) {
                                mPlaceOrderListAdapter.addOrUpdateItem(order);
                                Logger.d(TAG, TAG + ">>notificationUpdate>> FCM: Updated order list");
                            }
                        }
                    }
                }
            }
        }
    };
}