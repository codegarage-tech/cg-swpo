package com.meembusoft.safewaypharmaonline.activity;

import android.app.Activity;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.adapter.StoreAcceptOrderListAdapter;
import com.meembusoft.safewaypharmaonline.adapter.StoreOrderListAdapter;
import com.meembusoft.safewaypharmaonline.base.BaseActivity;
import com.meembusoft.safewaypharmaonline.enumeration.FlavorType;
import com.meembusoft.safewaypharmaonline.enumeration.LoginSettingType;
import com.meembusoft.safewaypharmaonline.enumeration.OrderActionType;
import com.meembusoft.safewaypharmaonline.enumeration.OrderStatusType;
import com.meembusoft.safewaypharmaonline.fcm.UpdateTokenTask;
import com.meembusoft.safewaypharmaonline.fcm.fcmutils.OnTokenUpdateListener;
import com.meembusoft.safewaypharmaonline.fcm.fcmutils.TokenFetcher;
import com.meembusoft.safewaypharmaonline.model.AppSupplier;
import com.meembusoft.safewaypharmaonline.model.PlaceOrderByItem;
import com.meembusoft.safewaypharmaonline.model.Store;
import com.meembusoft.safewaypharmaonline.model.StoreOrder;
import com.meembusoft.safewaypharmaonline.retrofit.APIClient;
import com.meembusoft.safewaypharmaonline.retrofit.APIInterface;
import com.meembusoft.safewaypharmaonline.retrofit.APIResponse;
import com.meembusoft.safewaypharmaonline.util.AllConstants;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.BroadcastManager;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.util.SessionUtil;
import com.meembusoft.safewaypharmaonline.view.CanaroTextView;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;

import org.parceler.Parcels;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.ymex.popup.controller.AlertController;
import cn.ymex.popup.dialog.PopupDialog;
import retrofit2.Call;
import retrofit2.Response;

import static com.meembusoft.safewaypharmaonline.activity.SupplierOrderDetailsActivity.INTENT_ACTION_SUPPLIER_ORDER_DETAIL;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_FROM_DATE;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_LOGIN_SETTING_TYPE;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_STORE;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_STORE_ID;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_TO_DATE;
import static com.meembusoft.safewaypharmaonline.util.SessionUtil.SESSION_KEY_USER_TAG;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SupplierStoreDetailActivity extends BaseActivity {

    //Toolbar
    private ImageView ivBack, ivLogout;
    private CanaroTextView tvTitle;
    private LinearLayout linLeftBack;
    private RelativeLayout relSupplierRequest;
    private EditText etFromDate, etToDate;
    private RecyclerView rvStore, rvAcceptStore;
    private LinearLayout linSupplierOurSales;
    // private EasyRecyclerView rvAcceptStore;
    private int dateValue;
    private TextView tvTotalsAccepted, tvRequestCount, tvTotalsSales;
    //Adapter
    private StoreOrderListAdapter mStoreOrderListAdapter;
    private StoreAcceptOrderListAdapter mStoreAcceptOrderListAdapter;
    private OrderActionType orderStoreSettingType;
    //   private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;

    //More loading
    private int mOffset = 0, mItemLimit = AllConstants.PER_PAGE_ITEM, mTotalcount = 0;
    private Handler mHandler = new Handler();
    //Background task
    private GetAllStoreOrderListTask getAllStoreOrderListTask;
    private GetAllAcceptedOrderTask getAllAcceptedOrderTask;
    private GetStoreListTask getStoreListTask;
    private APIInterface mApiInterface;
    private TokenFetcher tokenFetcher;
    private UpdateTokenTask updateTokenTask;

    private String deviceUniqueIdentity = "";
    PlaceOrderByItem mStore;
    //  Supplier
    private AppSupplier mAppSupplier;
    FlavorType mFlavorType;
    private String customerOrSupplierId = "";
    private String selectUserType = AllConstants.USER_TYPE_SUPPLIER;
    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }


    @Override
    public int initActivityLayout() {
        return R.layout.activity_supplier_screen;
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
            Parcelable mParcelableStore = getIntent().getParcelableExtra(INTENT_KEY_STORE);
            if (mParcelableStore != null) {
                mStore = Parcels.unwrap(mParcelableStore);
                Logger.d(TAG, TAG + " >>> " + "mParcelableStore: " + mStore.toString());
            }
        }
    }

    @Override
    public void initActivityViews() {
        // mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.main_swipe);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivLogout = (ImageView) findViewById(R.id.iv_logout);
        ivLogout.setVisibility(View.GONE);
        tvTitle = (CanaroTextView) findViewById(R.id.text_title);
        tvTitle.setText(getActivity().getResources().getString(R.string.title_supplier_details));
        linLeftBack = (LinearLayout) findViewById(R.id.ll_left_back);
        linSupplierOurSales = (LinearLayout) findViewById(R.id.lin_supplier_our_sales);
        relSupplierRequest = (RelativeLayout) findViewById(R.id.rel_supplier_request);
        relSupplierRequest.setVisibility(View.GONE);
        tvRequestCount = (TextView) findViewById(R.id.tv_request_count);
        tvTotalsAccepted = (TextView) findViewById(R.id.tv_totals_accepted);
        tvTotalsSales = (TextView) findViewById(R.id.tv_totals_sales);
        etFromDate = (EditText) findViewById(R.id.et_from_date);
        etToDate = (EditText) findViewById(R.id.et_to_date);
        rvStore = (RecyclerView) findViewById(R.id.rv_store);
        // rvAcceptStore = (EasyRecyclerView) findViewById(R.id.rv_accept_store);
        rvAcceptStore = (RecyclerView) findViewById(R.id.rv_accept_store);
        mAppSupplier = SessionUtil.getSupplier(getActivity());
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        // Register BroadCast
        BroadcastManager.registerBroadcastUpdate(getActivity(), notificationUpdate);
        BroadcastManager.registerBroadcastUpdate(getActivity(), new IntentFilter(INTENT_ACTION_SUPPLIER_ORDER_DETAIL), notificationUpdate);

        mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        // Get User Tag
        initGetTagValue();
        getDateCompare();
        setAcceptedOrderRecyclerView();

        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
            // loadOfflineTimeData();
        } else {
            if (getAllStoreOrderListTask != null && getAllStoreOrderListTask.getStatus() == AsyncTask.Status.RUNNING) {
                Logger.d(TAG, TAG + " >>> " + "setData: canceling asynctask");
                getAllStoreOrderListTask.cancel(true);
            }

            if (mAppSupplier != null && !TextUtils.isEmpty(mAppSupplier.getUser_id())) {
                getAllStoreOrderListTask = new GetAllStoreOrderListTask(getActivity(), mAppSupplier.getUser_id());
                getAllStoreOrderListTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                Logger.d(TAG, TAG + " >>> " + "etFromDate:" + etFromDate.getText().toString() + "\n" + etToDate.getText().toString());
                setAllAcceptedOrderData(etFromDate.getText().toString(), etToDate.getText().toString());
                initTokenForFCM();

            }
        }

        // initWaveSwipeRefreshLayout();
    }

    private void initGetTagValue() {
        String userType = SessionManager.getStringSetting(getActivity(), SessionUtil.SESSION_KEY_USER_TAG);
        if (!AllSettingsManager.isNullOrEmpty(userType)) {
            mFlavorType = FlavorType.getFlavor(SessionManager.getStringSetting(getActivity(), SessionUtil.SESSION_KEY_USER_TAG));
        } else {
            mFlavorType = FlavorType.SUPPLIER;
        }

        if (mFlavorType != null) {
            switch (mFlavorType) {
                case SUPPLIER:
                    mAppSupplier = SessionUtil.getSupplier(getActivity());
                    selectUserType = AllConstants.USER_TYPE_SUPPLIER;
                    if (mAppSupplier != null) {
                        customerOrSupplierId = mAppSupplier.getUser_id();
                    }
                    break;
            }

            Logger.d(TAG, TAG + " >>> " + "selectUserType " + selectUserType);
            Logger.d(TAG, TAG + " >>> " + "customerOrSupplierId " + customerOrSupplierId);
        }
    }


    @Override
    public void initActivityActions(Bundle savedInstanceState) {

        linLeftBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initActivityBackPress();
            }
        });

        ivLogout.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                showAppCloseDialog(getResources().getString(R.string.dialog_logout_title), getResources().getString(R.string.dialog_logout_mgs), getResources().getString(R.string.dialog_logout_title));

            }
        });

        etFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtil.datePicker(etFromDate.getText().toString(), etFromDate,true, getActivity());

            }
        });

        etFromDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                setDateFilterAcceptedOrderListSupplier();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtil.datePicker(etToDate.getText().toString(), etToDate, true,getActivity());
            }
        });

        etToDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                setDateFilterAcceptedOrderListSupplier();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        linSupplierOurSales.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent iCompletedActivity = new Intent(getActivity(), SupplierCompletedActivity.class);
                iCompletedActivity.putExtra(INTENT_KEY_STORE_ID, mAppSupplier.getUser_id());
                iCompletedActivity.putExtra(INTENT_KEY_FROM_DATE, etFromDate.getText().toString());
                iCompletedActivity.putExtra(INTENT_KEY_TO_DATE, etToDate.getText().toString());
                startActivity(iCompletedActivity);
            }
        });


        relSupplierRequest.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent iUserProfile = new Intent(getActivity(), UserProfileActivity.class);
                iUserProfile.putExtra(SESSION_KEY_USER_TAG, FlavorType.SUPPLIER.name());
                startActivity(iUserProfile);
            }
        });

//        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (!NetworkManager.isConnected(getActivity())) {
//                    Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
//                    // loadOfflineTimeData();
//                } else {
//
//                    if (getAllStoreOrderListTask != null && getAllStoreOrderListTask.getStatus() == AsyncTask.Status.RUNNING) {
//                        getAllStoreOrderListTask.cancel(true);
//                    }
//                    if (getAllAcceptedOrderTask != null && getAllAcceptedOrderTask.getStatus() == AsyncTask.Status.RUNNING) {
//                        getAllAcceptedOrderTask.cancel(true);
//                    }
//
//                    if (mAppSupplier != null) {
//                        getAllStoreOrderListTask = new GetAllStoreOrderListTask(getActivity(), mAppSupplier.getUser_id());
//                        getAllStoreOrderListTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                        setData();
//                    }
//                }
//            }
//        });

    }

    private void getDateCompare() {
        Date todayDate = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String today = formatter.format(todayDate);
        //dateValue = AppUtil.compareDates(today, str);
        etFromDate.setText(today);
        etToDate.setText(today);
    }


    private void setDateFilterAcceptedOrderListSupplier() {
        setAcceptedOrderRecyclerView();
        mOffset = 0;
        mItemLimit = AllConstants.PER_PAGE_ITEM;
        mStoreAcceptOrderListAdapter.clear();
//        mStoreAcceptOrderListAdapter.removeAll();
        setAllAcceptedOrderData(etFromDate.getText().toString(), etToDate.getText().toString());
    }

    private void setAcceptedOrderRecyclerView() {
        mStoreAcceptOrderListAdapter = new StoreAcceptOrderListAdapter(getActivity());
        rvAcceptStore.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rvAcceptStore.setAdapter(mStoreAcceptOrderListAdapter);
        mStoreAcceptOrderListAdapter.setMore(R.layout.view_more, new RecyclerArrayAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Logger.d(TAG, TAG + " >>> " + "onLoadMore(mOffset): " + mOffset);
                Logger.d(TAG, TAG + " >>> " + "onLoadMore(mTotalcount): " + mTotalcount);
                Logger.d(TAG, TAG + " >>> " + "onLoadMore(mPlaceOrderListAdapter): " + mStoreAcceptOrderListAdapter.getCount());
                if (mOffset < mTotalcount) {
                    Logger.d(TAG, TAG + " >>> " + "onLoadMore: started loading");
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setAllAcceptedOrderData(etFromDate.getText().toString(), etToDate.getText().toString());
                        }
                    }, 2000);
                } else {
                    //No more item
                    mStoreAcceptOrderListAdapter.addAll(new ArrayList<PlaceOrderByItem>());
                }
            }
        });
        mStoreAcceptOrderListAdapter.setNoMore(R.layout.view_nomore);
        mStoreAcceptOrderListAdapter.setError(R.layout.view_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
                mStoreAcceptOrderListAdapter.resumeMore();
            }

            @Override
            public void onErrorClick() {
                mStoreAcceptOrderListAdapter.resumeMore();
            }
        });
    }

    private void initGetStoreOrderList(List<PlaceOrderByItem> storeList) {
        if (storeList != null && storeList.size() > 0) {
            mStoreOrderListAdapter = new StoreOrderListAdapter(getActivity());
            rvStore.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
            rvStore.setAdapter(mStoreOrderListAdapter);
            mStoreOrderListAdapter.addAll(storeList);
        }
    }

    // Set data for Accepted List
    private void setAllAcceptedOrderData(String fromDate, String toDate) {
        Logger.d(TAG, TAG + " >>> " + "fromDate:" + fromDate + "\n" + toDate);

        if (!NetworkManager.isConnected(getActivity())) {
            dismissPopupDialog();
            //This is for more loading
            mStoreAcceptOrderListAdapter.pauseMore();
            //   tvSuggestion.setText(getString(R.string.view_please_connect_internet_and_retry));
            Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (getAllAcceptedOrderTask != null && getAllAcceptedOrderTask.getStatus() == AsyncTask.Status.RUNNING) {
                Logger.d(TAG, TAG + " >>> " + "setData: canceling asynctask");
                getAllAcceptedOrderTask.cancel(true);
            }

            getAllAcceptedOrderTask = new GetAllAcceptedOrderTask(getActivity(), fromDate, toDate);
            getAllAcceptedOrderTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

//            if (mAppUser!= null) {
//                getAllOrderPlaceTask = new PlaceOrderViewActivity.GetAllOrderPlaceTask(getActivity());
//                getAllOrderPlaceTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            }
        }
    }

//    private void initWaveSwipeRefreshLayout() {
//        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
//        mWaveSwipeRefreshLayout.setWaveColor(getResources().getColor(R.color.colorPrimaryDark));
//    }

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
                    Logger.d(TAG, TAG + " >>> " + "order>>notificationUpdate: " + order.toString());

                    if (order != null) {
                        // For store order detail
                        if (action.equalsIgnoreCase(INTENT_ACTION_SUPPLIER_ORDER_DETAIL)) {
                            if (order.getStatus().equalsIgnoreCase(OrderStatusType.ACCEPTED.toString())) {
                                if (mStoreAcceptOrderListAdapter != null) {
                                    mStoreOrderListAdapter.removeItem(order);
                                    mStoreAcceptOrderListAdapter.addOrUpdateItem(order);
                                    Logger.d(TAG, TAG + ">>notificationUpdate>> store detail: Add or update to store accepted list from notification click");
                                }
                            } else if (order.getStatus().equalsIgnoreCase(OrderStatusType.CANCELLED.toString())) {
                                if (mStoreOrderListAdapter != null) {
                                    mStoreOrderListAdapter.removeItem(order);
                                    Logger.d(TAG, TAG + ">>notificationUpdate>> store detail: removed form the store from notification click");
                                }
                            }
                        } else {
                            // For FCM
                            if (order.getStatus().equalsIgnoreCase(OrderStatusType.PENDING.toString())) {
                                if (mStoreOrderListAdapter != null) {
                                    mStoreOrderListAdapter.addStore(order);
                                    Logger.d(TAG, TAG + ">>notificationUpdate>> FCM: Added new order to the store");
                                }
                            } else if (order.getStatus().equalsIgnoreCase(OrderStatusType.RECEIVED_BY_USER.toString())) {
                                if (mStoreAcceptOrderListAdapter != null) {
                                    mStoreAcceptOrderListAdapter.updateItem(order);
                                    Logger.d(TAG, TAG + ">>notificationUpdate>> FCM: Updated order to the store");
                                }
                            }
                            Logger.d(TAG, TAG + ">>notificationUpdate>> FCM: Updated store detail");
                        }
                    }
                }
            }
        }
    };

    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AllConstants.INTENT_REQUEST_CODE_STORE:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        String mParcelableOrderSettingType = data.getStringExtra(AllConstants.INTENT_KEY_ORDER_SETTING_TYPE);
                        if (!AllSettingsManager.isNullOrEmpty(mParcelableOrderSettingType)) {
                            orderStoreSettingType = OrderActionType.valueOf(mParcelableOrderSettingType);
                            Logger.d(TAG, TAG + "initActivityOnResult>>> " + "mParcelableOrderSettingType: " + mParcelableOrderSettingType);
                            Logger.d(TAG, TAG + "initActivityOnResult>>> " + "orderStoreSettingType: " + orderStoreSettingType.name());
                            Parcelable mParcelableOrder = data.getParcelableExtra(AllConstants.INTENT_KEY_STORE);

                            if (orderStoreSettingType == OrderActionType.SKIP) {
                                if (mParcelableOrder != null) {
                                    PlaceOrderByItem mOrder = Parcels.unwrap(mParcelableOrder);
                                    if (mOrder != null) {
                                        Logger.d(TAG, TAG + "initActivityOnResult>>>SKIP " + "mOrder: " + mOrder.toString());
                                        mStoreOrderListAdapter.removeItem(mOrder);
                                    }
                                }
                            } else if (orderStoreSettingType == OrderActionType.ACCEPT) {
                                if (mParcelableOrder != null) {
                                    PlaceOrderByItem mOrder = Parcels.unwrap(mParcelableOrder);
                                    if (mOrder != null) {
                                        Logger.d(TAG, TAG + "initActivityOnResult>>>ACCEPT " + "mOrder: " + mOrder.toString());
                                        mStoreOrderListAdapter.removeItem(mOrder);
                                        mStoreAcceptOrderListAdapter.addStore(mOrder);
                                    }
                                }
                            } else if (orderStoreSettingType == OrderActionType.STATUS) {
                                if (mParcelableOrder != null) {
                                    PlaceOrderByItem mOrder = Parcels.unwrap(mParcelableOrder);
                                    if (mOrder != null) {
                                        Logger.d(TAG, TAG + "initActivityOnResult>>>STATUS " + "mOrder: " + mOrder.toString());
                                        mStoreAcceptOrderListAdapter.updateItem(mOrder);
                                        Logger.d(TAG, TAG + "OrderStatusType>>>STATUS " + "mOrder: " + OrderStatusType.DONE.toString());
                                        if (mOrder.getStatus().equalsIgnoreCase(OrderStatusType.DONE.toString())) {
                                            setAllAcceptedOrderData(etFromDate.getText().toString(), etToDate.getText().toString());
                                        }
                                        initTokenForFCM();
                                    }
                                }
                            }
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
            //showAppCloseDialog(getResources().getString(R.string.dialog_close_app_title), getResources().getString(R.string.dialog_do_you_want_to_close_the_app), getResources().getString(R.string.dialog_appout));
        }
    }

    @Override
    public void initActivityDestroyTasks() {
        // Unregister BroadCast
        BroadcastManager.unregisterBroadcastUpdate(getActivity(), notificationUpdate);

        dismissPopupDialog();

        if (getStoreListTask != null && getStoreListTask.getStatus() == AsyncTask.Status.RUNNING) {
            getStoreListTask.cancel(true);
        }

        if (getAllStoreOrderListTask != null && getAllStoreOrderListTask.getStatus() == AsyncTask.Status.RUNNING) {
            getAllStoreOrderListTask.cancel(true);
        }
        if (getAllAcceptedOrderTask != null && getAllAcceptedOrderTask.getStatus() == AsyncTask.Status.RUNNING) {
            getAllAcceptedOrderTask.cancel(true);
        }

        if (tokenFetcher != null && tokenFetcher.getStatus() == AsyncTask.Status.RUNNING) {
            tokenFetcher.cancel(true);
        }

        if (updateTokenTask != null && updateTokenTask.getStatus() == AsyncTask.Status.RUNNING) {
            updateTokenTask.cancel(true);
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

    private void showAppCloseDialog(String title, String message, String state) {
        PopupDialog.create(getActivity())
                .outsideTouchHide(false)
                .dismissTime(1000 * 5)
                .controller(AlertController.build()
                        .title(title + "\n")
                        .message(message)
                        .clickDismiss(true)
                        .negativeButton(getString(R.string.dialog_cancel), null)
                        .positiveButton(getString(R.string.dialog_ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (state.equalsIgnoreCase(getResources().getString(R.string.dialog_logout_title))) {
                                    SessionUtil.setSupplier(getActivity(), "");
                                    SessionManager.setStringSetting(getActivity(), SessionUtil.SESSION_KEY_USER_TAG, FlavorType.CUSTOMER.toString());
                                    Intent intentLogin = new Intent(getActivity(), HomeActivity.class);
                                    intentLogin.putExtra(INTENT_KEY_LOGIN_SETTING_TYPE, LoginSettingType.OTHERS.name());
                                    startActivity(intentLogin);
                                    finish();
                                } else {
                                    finish();
                                }
                            }
                        }))
                .show();
    }

    /************************
     * Server communication *
     ************************/

    private class GetStoreListTask extends AsyncTask<String, Integer, Response> {

        Context mContext;

        private GetStoreListTask(Context context) {
            mContext = context;
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
                Call<APIResponse<List<Store>>> call = mApiInterface.apiGetStoreList();
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

                if (result != null && result.isSuccessful()) {
                    Logger.d(TAG, "APIResponse(GetStoreListTask): onResponse-server = " + result.toString());
                    APIResponse<List<Store>> data = (APIResponse<List<Store>>) result.body();
                    Logger.d("GetStoreListTask", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(GetStoreListTask()): onResponse-object = " + data.toString());
                        // initVideoPlayerList(data.getData());
                    } else {
                        Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();

                    // loadOfflineTimeData();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
//
//    private void initVideoPlayerList(List<Store> storeList) {
//        if (storeList!=null && storeList.size()>0) {
//            storeAcceptOrderListAdapter = new StoreAcceptOrderListAdapter(getActivity());
//            rvAcceptStore.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
//            rvAcceptStore.setAdapter(storeAcceptOrderListAdapter);
//            storeAcceptOrderListAdapter.addAll(storeList);
//        }
//    }


    /************************
     * Server communication *
     ************************/
    private class GetAllStoreOrderListTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        String storeID;

        private GetAllStoreOrderListTask(Context context, String storeId) {
            mContext = context;
            storeID = storeId;

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
                Call<APIResponse<StoreOrder>> call = mApiInterface.apiGetAllStoreOrder(customerOrSupplierId,selectUserType);
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

                if (result != null && result.isSuccessful()) {
                    Logger.d(TAG, "APIResponse(GetAllStoreOrderListTask): onResponse-server = " + result.toString());
                    APIResponse<StoreOrder> data = (APIResponse<StoreOrder>) result.body();
                    Logger.d("GetAllStoreOrderListTask", data.toString() + "");
                    //    tvTotalsSales.setText(data.getData().getOur_sale() + " /-");
                    if (data != null && data.getData().getOrders().size() > 0) {
                        Logger.d(TAG, "APIResponse(GetAllFavouriteTask()): onResponse-object = " + data.toString());

                        tvRequestCount.setVisibility(View.VISIBLE);
                        tvRequestCount.setText(data.getData().getTotals_count() + "");
                        initGetStoreOrderList(data.getData().getOrders());
                        Logger.d("getOrders", data.getData().getOrders().size() + "");

                    } else {
                        Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_LONG).show();
                        tvRequestCount.setVisibility(View.GONE);
                    }

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();

                    // loadOfflineTimeData();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }


    private class GetAllAcceptedOrderTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        String mFromDate, mToDate;

        private GetAllAcceptedOrderTask(Context context, String fromDate, String toDate) {
            mContext = context;
            mFromDate = fromDate;
            mToDate = toDate;

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
                Call<APIResponse<StoreOrder>> call = mApiInterface.apiGetAllStoreAcceptedOrderList(customerOrSupplierId, mFromDate, mToDate, mOffset, mItemLimit,selectUserType);
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
                //   mWaveSwipeRefreshLayout.setRefreshing(false);

                dismissPopupDialog();
                Logger.d("isSuccessful", result.isSuccessful() + "");

                if (result.isSuccessful()) {
                    Logger.d(TAG, "APIResponse(GetAllAcceptedOrderTask): onResponse-server = " + result.toString());
                    APIResponse<StoreOrder> data = (APIResponse<StoreOrder>) result.body();
                    Logger.d("GetAllAcceptedOrderTask>>", data.toString() + "");

                    if (data.getData() != null) {
                        Logger.d(TAG, "APIResponse(GetAllAcceptedOrderTask()): onResponse-object = " + data.toString());
                        if (data.getData().getOrders().size() > 0) {
                            mStoreAcceptOrderListAdapter.addAll(data.getData().getOrders());
                            //This is needed for more loading
                            mOffset = mOffset + AllConstants.PER_PAGE_ITEM;
                            mItemLimit = mItemLimit + AllConstants.PER_PAGE_ITEM;
                            mTotalcount = data.getData().getTotals_count();
                            tvTotalsAccepted.setText(mTotalcount + "");
                            tvTotalsAccepted.setVisibility(View.VISIBLE);

                            if (AppUtil.optStringNullCheckValue(data.getData().getTotals_done_amount()).equalsIgnoreCase("") || data.getData().getTotals_done_amount().equalsIgnoreCase("null")) {
                                tvTotalsSales.setText("0.0 /-");
                            } else {
                                tvTotalsSales.setText(AppUtil.optStringNullCheckValue(data.getData().getTotals_done_amount()) + "/-");
                            }

                            Logger.d("mItemLimit", mItemLimit + "");
                        } else {
                            mStoreAcceptOrderListAdapter.clear();
                            if (AppUtil.optStringNullCheckValue(data.getData().getTotals_done_amount()).equalsIgnoreCase("") || data.getData().getTotals_done_amount().equalsIgnoreCase("null")) {
                                tvTotalsSales.setText("0.0 /-");
                            } else {
                                tvTotalsSales.setText(AppUtil.optStringNullCheckValue(data.getData().getTotals_done_amount()) + "/-");
                            }
                          //  Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_LONG).show();
                        }

                    } else {
                        //  Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_LONG).show();
                        mStoreAcceptOrderListAdapter.clear();
                        tvTotalsSales.setText("0.0 /-");
                        tvTotalsAccepted.setVisibility(View.GONE);
                    }

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    /****************************************
     * Methods for firebase cloud Messaging *
     ****************************************/
    private void initTokenForFCM() {
        if (NetworkManager.isConnected(getActivity())) {
            if (tokenFetcher != null && tokenFetcher.getStatus() == AsyncTask.Status.RUNNING) {
                tokenFetcher.cancel(true);
            }
            tokenFetcher = new TokenFetcher(getActivity(), new OnTokenUpdateListener() {
                @Override
                public void onTokenUpdate(String update) {
                    if (!AllSettingsManager.isNullOrEmpty(update)) {
                        Logger.d(TAG, TAG + ">>updateToken " + update + "update");
                        //  Send server request for updating token
                        if (updateTokenTask != null && updateTokenTask.getStatus() == AsyncTask.Status.RUNNING) {
                            updateTokenTask.cancel(true);
                        }
                        if (mAppSupplier != null && !TextUtils.isEmpty(mAppSupplier.getUser_id())) {
                            updateTokenTask = new UpdateTokenTask(getActivity(), FlavorType.SUPPLIER, mAppSupplier.getUser_id(), update);
                            updateTokenTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                    }
                }
            });
            tokenFetcher.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

/*
    private void initTokenForFCM() {
        if (NetworkManager.isConnected(getActivity())) {
            if (tokenFetcher != null && tokenFetcher.getStatus() == AsyncTask.Status.RUNNING) {
                tokenFetcher.cancel(true);
            }
            tokenFetcher = new TokenFetcher(getActivity(), new OnTokenUpdateListener() {
                @Override
                public void onTokenUpdate(String update) {
                    Logger.d(TAG, TAG + ">>UpdateTokenTask>>onTokenUpdate: " + update);
                    Logger.d(TAG, TAG + ">>UpdateTokenTask>> calling ");
                    updateTokenTask = new UpdateTokenTask(getActivity(), update);
                    updateTokenTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


//                    if (!AllSettingsManager.isNullOrEmpty(update)) {
//                        Logger.d(TAG, TAG + ">>updateToken " +update+ "update" );
//                        //Update is found now update the previous token
//                        boolean isUpdateFound = false;
//                        String userType = SessionManager.getStringSetting(getApplicationContext(), SessionUtil.SESSION_KEY_USER_TAG);
//                        if (!AllSettingsManager.isNullOrEmpty(userType)) {
//                            mFlavorType = FlavorType.getFlavor(SessionManager.getStringSetting(getApplicationContext(), SessionUtil.SESSION_KEY_USER_TAG));
//                        } else {
//                            mFlavorType = FlavorType.SUPPLIER;
//                        }
//
//                        switch (mFlavorType) {
//                            case SUPPLIER:
//                                if ((mAppSupplier != null) && (!mAppSupplier.getToken().equalsIgnoreCase(update))) {
//                                    Logger.d(TAG, TAG + ">> " + "New token is found and server request is sending");
//                                    isUpdateFound = true;
//                                    deviceUniqueIdentity = SessionManager.getStringSetting(getActivity(), AllConstants.SESSION_DEVICE_IDENTIFIER);
//                                    if (AllSettingsManager.isNullOrEmpty(deviceUniqueIdentity)) {
//                                        deviceUniqueIdentity = AppUtil.getAppDeviceUniqueIdentifier(getActivity());
//                                    }
//                                    Logger.d(TAG, TAG + " >>> " + "AppUser(deviceUniqueIdentity): " + deviceUniqueIdentity);
//
//                                } else {
//                                    Logger.d(TAG, TAG + ">> " + "New token is not found and server request is not sending");
//                                }
//                                break;
//
//                        }
//
//                        //  Send server request for updating token
//                        if (isUpdateFound) {
//                            if (updateTokenTask != null && updateTokenTask.getStatus() == AsyncTask.Status.RUNNING) {
//                                updateTokenTask.cancel(true);
//                            }
//
//                            updateTokenTask = new UpdateTokenTask(getActivity(), update);
//                            updateTokenTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                        }
//                    }

//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Logger.d(TAG, TAG + ">>UpdateTokenTask>> calling ");
//                            updateTokenTask = new UpdateTokenTask(getActivity(), update);
//                            updateTokenTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                        }
//                    });
                }
            });
            tokenFetcher.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }
*/

/*
    private class UpdateTokenTask extends AsyncTask<String, Integer, Response> {

        private Context mContext;
        private String mToken = "";

        private UpdateTokenTask(Context context, String token) {
            mContext = context;
            mToken = token;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Response doInBackground(String... params) {
            try {
                Response response = null;

                ParamsUpdateFcm paramAppUser = new ParamsUpdateFcm("supplier", mAppSupplier.getUser_id(), mToken);
                Logger.d(TAG, TAG + ">>UpdateTokenTask>> paramAppUser:" + paramAppUser.toString());
                Call<APIResponse> callUser = mApiInterface.apiUpdateFcmToken(paramAppUser);
                response = callUser.execute();

//                switch (mFlavorType) {
//                    case CUSTOMER:
//                        ParamsUpdateFcm paramAppUser = new ParamsUpdateFcm(userType, mAppSupplier.getUser_id(), mToken);
//                        Logger.d(TAG, TAG + ">>UpdateTokenTask>> paramAppUser:" + paramAppUser.toString());
//                        Call<APIResponse> callUser = mApiInterface.apiUpdateFcmToken(paramAppUser);
//                        response = callUser.execute();
//                        break;
//
//                }

                Logger.d(TAG, TAG + ">>UpdateTokenTask>> response:" + response);
                if (response.isSuccessful()) {
                    return response;
                }
            } catch (Exception ex) {
                Logger.d(TAG, TAG + ">>UpdateTokenTask>> exception:" + ex.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Response result) {
            try {
                if (result != null && result.isSuccessful()) {

                    Logger.d(TAG, TAG + ">>UpdateTokenTask>>: onResponse-server = " + result.toString());
                    APIResponse dataUser = (APIResponse) result.body();

                    if (dataUser != null && dataUser.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, TAG + ">>UpdateTokenTask>> : onResponse-object = " + dataUser.toString());
                        //Set user
                        SessionManager.setStringSetting(getActivity(), SessionUtil.SESSION_KEY_USER_TAG, FlavorType.SUPPLIER.toString());
                        SessionUtil.setSupplier(getActivity(), APIResponse.getResponseString(dataUser.getData()));

                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
*/
}