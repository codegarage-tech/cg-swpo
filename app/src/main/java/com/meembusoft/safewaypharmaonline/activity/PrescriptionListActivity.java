package com.meembusoft.safewaypharmaonline.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.waveswiperefresh.WaveSwipeRefreshRecyclerView;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.adapter.PlaceOrderListAdapter;
import com.meembusoft.safewaypharmaonline.adapter.PrescriptionListAdapter;
import com.meembusoft.safewaypharmaonline.base.BaseActivity;
import com.meembusoft.safewaypharmaonline.enumeration.FlavorType;
import com.meembusoft.safewaypharmaonline.enumeration.LoginSettingType;
import com.meembusoft.safewaypharmaonline.model.AppSupplier;
import com.meembusoft.safewaypharmaonline.model.AppUser;
import com.meembusoft.safewaypharmaonline.model.PlaceOrderByItem;
import com.meembusoft.safewaypharmaonline.model.StoreOrder;
import com.meembusoft.safewaypharmaonline.retrofit.APIClient;
import com.meembusoft.safewaypharmaonline.retrofit.APIInterface;
import com.meembusoft.safewaypharmaonline.retrofit.APIResponse;
import com.meembusoft.safewaypharmaonline.util.AllConstants;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.util.SessionUtil;
import com.meembusoft.safewaypharmaonline.view.CanaroTextView;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_LOGIN_SETTING_TYPE;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_REQUEST_CODE_USER_LOGIN;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class PrescriptionListActivity extends BaseActivity {


    private ImageView ivBack;
    private CanaroTextView tvTitle;
    private RelativeLayout rlCart;

    private WaveSwipeRefreshRecyclerView rvPrescription;
    private PrescriptionListAdapter mPrescriptionListAdapter;
    private AppUser mAppUser;
    //More loading
    private int mOffset = 0,mItemLimit = AllConstants.PER_PAGE_ITEM, mTotalcount = 0;
    private Handler mHandler = new Handler();
    //Background task
    private GetAllPrescriptionListTask getAllPrescriptionListTask;
    private APIInterface mApiInterface;
    private boolean isSwipeRefreshTask = false;
    //  Supplier
    private AppSupplier mAppSupplier;
    FlavorType mFlavorType;
    private String customerOrSupplierId = "";
    private String selectUserType = AllConstants.USER_TYPE_CUSTOMER;

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }



    @Override
    public int initActivityLayout() {
        return R.layout.activity_collection_screen;
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
//        if (intent != null) {
//            Parcelable mParcelable = intent.getParcelableExtra(AllConstants.INTENT_KEY_BASE_SELLER);
//            if (mParcelable != null) {
//                baseSellerList = Parcels.unwrap(mParcelable);
//                Logger.d(TAG, TAG + " >>> " + "baseSellerList: " + baseSellerList.toString());
//            }
//        }
    }

    @Override
    public void initActivityViews() {
        ivBack = (ImageView)findViewById(R.id.iv_back);
        tvTitle = (CanaroTextView) findViewById(R.id.text_title);
        tvTitle.setText(getActivity().getResources().getString(R.string.title_prescription));
        rlCart = (RelativeLayout) findViewById(R.id.rl_cart);
        rlCart.setVisibility(View.GONE);
        rvPrescription = (WaveSwipeRefreshRecyclerView ) findViewById(R.id.rv_collection);

    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {

        mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        // Get User Tag
        initGetTagValue();
        setRecyclerView();

        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
        } else {

            switch (mFlavorType) {
                case CUSTOMER:
                    mAppUser = SessionUtil.getUser(getActivity());
                    selectUserType = AllConstants.USER_TYPE_CUSTOMER;
                    if (mAppUser != null) {
                        customerOrSupplierId = mAppUser.getId();
                        rvPrescription.setVisibility(View.VISIBLE);
                        setData();
                    }
                    break;
                case SUPPLIER:
                    mAppSupplier = SessionUtil.getSupplier(getActivity());
                    selectUserType = AllConstants.USER_TYPE_SUPPLIER;
                    if (mAppSupplier != null) {
                        customerOrSupplierId = mAppSupplier.getUser_id();
                        rvPrescription.setVisibility(View.VISIBLE);
                        setData();
                    }
                    break;
            }
            Logger.d(TAG, TAG + " >>> " + "selectUserType " + selectUserType);
            Logger.d(TAG, TAG + " >>> " + "customerOrSupplierId " + customerOrSupplierId);


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

    private void initGetTagValue() {
        String userType = SessionManager.getStringSetting(getActivity(), SessionUtil.SESSION_KEY_USER_TAG);
        if (!AllSettingsManager.isNullOrEmpty(userType)) {
            mFlavorType = FlavorType.getFlavor(SessionManager.getStringSetting(getActivity(), SessionUtil.SESSION_KEY_USER_TAG));
        } else {
            mFlavorType = FlavorType.CUSTOMER;
        }



    }

    private void setRecyclerView() {
        mPrescriptionListAdapter = new PrescriptionListAdapter(getActivity());
        rvPrescription.setLayoutManager(new GridLayoutManager(getActivity(), AppUtil.getGridSpanCount(getActivity())));
        rvPrescription.setAdapter(mPrescriptionListAdapter);
        mPrescriptionListAdapter.setMore(R.layout.view_more, new RecyclerArrayAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Logger.d(TAG, TAG + " >>> " + "onLoadMore(mOffset): " + mOffset);
                Logger.d(TAG, TAG + " >>> " + "onLoadMore(mTotalcount): " + mTotalcount);
                Logger.d(TAG, TAG + " >>> " + "onLoadMore(mPlaceOrderListAdapter): " + mPrescriptionListAdapter.getCount());
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
                    mPrescriptionListAdapter.addAll(new ArrayList<PlaceOrderByItem>());
                }
            }
        });
        mPrescriptionListAdapter.setNoMore(R.layout.view_nomore);
        mPrescriptionListAdapter.setError(R.layout.view_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
                mPrescriptionListAdapter.resumeMore();
            }

            @Override
            public void onErrorClick() {
                mPrescriptionListAdapter.resumeMore();
            }
        });
    }



    private void setData() {
        if (!NetworkManager.isConnected(getActivity())) {
            dismissPopupDialog();
            //This is for more loading
            mPrescriptionListAdapter.pauseMore();
         //   tvSuggestion.setText(getString(R.string.view_please_connect_internet_and_retry));
            Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (getAllPrescriptionListTask != null && getAllPrescriptionListTask.getStatus() == AsyncTask.Status.RUNNING) {
                Logger.d(TAG, TAG + " >>> " + "setData: canceling asynctask");
                getAllPrescriptionListTask.cancel(true);
            }

            if (mAppUser!= null) {
                getAllPrescriptionListTask = new GetAllPrescriptionListTask(getActivity());
                getAllPrescriptionListTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
        dismissPopupDialog();

        if (getAllPrescriptionListTask != null && getAllPrescriptionListTask.getStatus() == AsyncTask.Status.RUNNING) {
            getAllPrescriptionListTask.cancel(true);
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

    private class GetAllPrescriptionListTask extends AsyncTask<String, Integer, Response> {

        Context mContext;

        private GetAllPrescriptionListTask(Context context) {
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
                Call<APIResponse<StoreOrder>> call = mApiInterface.apiGetPlaceOrderByCustomerList(customerOrSupplierId,mOffset,mItemLimit,selectUserType);
                Response response = call.execute();
                Logger.d("GetAllPrescriptionListTask", response+ "response");

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
                    Logger.d(TAG, "APIResponse(GetAllPrescriptionListTask): onResponse-server = " + result.toString());
                    APIResponse<StoreOrder> data = (APIResponse<StoreOrder>) result.body();
                    Logger.d("GetAllPrescriptionListTask", data.toString() + "");

                    if (data.getData() != null &&  data.getData().getOrders().size()>0 ) {
                        Logger.d(TAG, "APIResponse(GetAllPrescriptionListTask()): onResponse-object = " + data.toString());
                        mPrescriptionListAdapter.addAll(data.getData().getOrders());

                        //This is needed for more loading
                        mOffset = mOffset + AllConstants.PER_PAGE_ITEM;
                        mItemLimit = mItemLimit + AllConstants.PER_PAGE_ITEM;
                        mTotalcount = data.getData().getTotals_count();

                        Logger.d("mTotalcount", mTotalcount+ "");

                    } else {
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


}