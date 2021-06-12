package com.meembusoft.safewaypharmaonline.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.waveswiperefresh.WaveSwipeRefreshRecyclerView;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.adapter.PlaceOrderListAdapter;
import com.meembusoft.safewaypharmaonline.base.BaseActivity;
import com.meembusoft.safewaypharmaonline.enumeration.LoginSettingType;
import com.meembusoft.safewaypharmaonline.model.AppUser;
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
public class SupplierCompletedActivity extends BaseActivity {


    private ImageView ivBack;
    private CanaroTextView tvTitle;
    private RelativeLayout rlCart;
    private LinearLayout linNoDataFound;
    private RecyclerView rvCompletedOrder;
    private PlaceOrderListAdapter mPlaceOrderListAdapter;
    private String supplierID = "",fromDate = "",toDate = "";
    //More loading
    private int mOffset = 0, mItemLimit = AllConstants.PER_PAGE_ITEM, mTotalcount = 0;
    private Handler mHandler = new Handler();
    //Background task
    private GetAllCompletedOrderTask getAllCompletedOrderTask;
    private APIInterface mApiInterface;

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }


    @Override
    public int initActivityLayout() {
        return R.layout.activity_supplier_completed_order_screen;
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
            String mParcelableStoreId = intent.getStringExtra(AllConstants.INTENT_KEY_STORE_ID);
            String mParcelableFromDate = intent.getStringExtra(AllConstants.INTENT_KEY_FROM_DATE);
            String mParcelableToDate = intent.getStringExtra(AllConstants.INTENT_KEY_TO_DATE);

            if (!AllSettingsManager.isNullOrEmpty(mParcelableStoreId)) {
                supplierID = mParcelableStoreId;
                Logger.d(TAG, TAG + " >>> " + "supplierID: " + supplierID);
            }
            if (!AllSettingsManager.isNullOrEmpty(mParcelableFromDate)) {
                fromDate = mParcelableFromDate;
                Logger.d(TAG, TAG + " >>> " + "fromDate: " + fromDate);
            }
            if (!AllSettingsManager.isNullOrEmpty(mParcelableToDate)) {
                toDate = mParcelableToDate;
                Logger.d(TAG, TAG + " >>> " + "toDate: " + toDate);
            }
        }
    }

    @Override
    public void initActivityViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (CanaroTextView) findViewById(R.id.text_title);
        tvTitle.setText(getActivity().getResources().getString(R.string.title_completed_order));
        rlCart = (RelativeLayout) findViewById(R.id.rl_cart);
        rlCart.setVisibility(View.GONE);
        rvCompletedOrder = (RecyclerView) findViewById(R.id.rv_completed_order);
        linNoDataFound = (LinearLayout) findViewById(R.id.lin_no_data_found_layout);

    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);

        setRecyclerView();

        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
        } else {
            setData();
        }
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {
        ivBack.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });
    }

    private void setRecyclerView() {
        mPlaceOrderListAdapter = new PlaceOrderListAdapter(getActivity());
        rvCompletedOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCompletedOrder.setAdapter(mPlaceOrderListAdapter);
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
            dismissPopupDialog();
            //This is for more loading
            mPlaceOrderListAdapter.pauseMore();
            //   tvSuggestion.setText(getString(R.string.view_please_connect_internet_and_retry));
            Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
        } else {
            if (getAllCompletedOrderTask != null && getAllCompletedOrderTask.getStatus() == AsyncTask.Status.RUNNING) {
                Logger.d(TAG, TAG + " >>> " + "setData: canceling asynctask");
                getAllCompletedOrderTask.cancel(true);
            }

            if (supplierID != null) {
                getAllCompletedOrderTask = new GetAllCompletedOrderTask(getActivity(),fromDate,toDate);
                getAllCompletedOrderTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    }


    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {

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
        dismissPopupDialog();

        if (getAllCompletedOrderTask != null && getAllCompletedOrderTask.getStatus() == AsyncTask.Status.RUNNING) {
            getAllCompletedOrderTask.cancel(true);
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
    private class GetAllCompletedOrderTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        String mFromDate,mToDate;

        private GetAllCompletedOrderTask(Context context,String fromDate, String toDate) {
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
                Call<APIResponse<StoreOrder>> call = mApiInterface.apiGetAllStoreCompletedOrderList(supplierID,mFromDate,mToDate,mOffset,mItemLimit);
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

                    if (data.getData()!= null ) {
                        Logger.d(TAG, "APIResponse(GetAllAcceptedOrderTask()): onResponse-object = " + data.toString());
                        if (data.getData().getOrders().size() > 0) {
                            mPlaceOrderListAdapter.addAll(data.getData().getOrders());
                            //This is needed for more loading
                            mOffset = mOffset + AllConstants.PER_PAGE_ITEM;
                            mItemLimit = mItemLimit + AllConstants.PER_PAGE_ITEM;
                            mTotalcount = data.getData().getTotals_count();

                            Logger.d("mItemLimit", mItemLimit + "");
                        } else {
                            rvCompletedOrder.setVisibility(View.GONE);
                            linNoDataFound.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_LONG).show();
                        }

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