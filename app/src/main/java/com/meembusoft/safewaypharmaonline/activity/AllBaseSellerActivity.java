package com.meembusoft.safewaypharmaonline.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;

import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.waveswiperefresh.WaveSwipeRefreshRecyclerView;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.adapter.AllBaseSellerListAdapter;
import com.meembusoft.safewaypharmaonline.base.BaseActivity;
import com.meembusoft.safewaypharmaonline.enumeration.FlavorType;
import com.meembusoft.safewaypharmaonline.model.AppSupplier;
import com.meembusoft.safewaypharmaonline.model.AppUser;
import com.meembusoft.safewaypharmaonline.model.CommonMedicinesByItem;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.retrofit.APIClient;
import com.meembusoft.safewaypharmaonline.retrofit.APIInterface;
import com.meembusoft.safewaypharmaonline.retrofit.APIResponse;
import com.meembusoft.safewaypharmaonline.util.AllConstants;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.util.SessionUtil;
import com.meembusoft.safewaypharmaonline.view.CanaroTextView;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AllBaseSellerActivity extends BaseActivity {


    //Toolbar
    private ImageView ivBack;
    private CanaroTextView tvTitle;
    private TextView tvCount;
    private LinearLayout llLeftBack;

    private RelativeLayout rlCart;
    private LinearLayout linNoDataFound;

    private WaveSwipeRefreshRecyclerView rvCollection;
    private AllBaseSellerListAdapter allBaseSellerListAdapter;
    // ArrayList
    List<StaggeredMedicineByItem> baseSellerList;
    private int mOffset = 0, mItemLimit = AllConstants.PER_PAGE_ITEM, mTotalcount = 0;
    private Handler mHandler = new Handler();
    //Background task
    private GetAllBaseSellerTask getAllBaseSellerTask;
    private APIInterface mApiInterface;
    private boolean isSwipeRefreshTask = false;
    private AppUser mAppUser;

    //  Supplier
    private AppSupplier mAppSupplier;
    FlavorType mFlavorType;
    private String customerOrSupplierId = "";
    private String selectUserType = "customer";

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
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (CanaroTextView) findViewById(R.id.text_title);
        tvTitle.setText(getActivity().getResources().getString(R.string.title_base_seller));
        tvCount = (TextView) findViewById(R.id.tv_cart);
        rlCart = (RelativeLayout) findViewById(R.id.rl_cart);
        llLeftBack = (LinearLayout) findViewById(R.id.ll_left_back);
        rvCollection = (WaveSwipeRefreshRecyclerView) findViewById(R.id.rv_collection);
        linNoDataFound = (LinearLayout) findViewById(R.id.lin_no_data_found_layout);
        resetCounterView();

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
            setData();
//            getAllBaseSellerTask = new GetAllBaseSellerTask(getActivity());
//            getAllBaseSellerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {
        llLeftBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

//        rvCollection.setRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (!NetworkManager.isConnected(getActivity())) {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
//                    rvCollection.setRefreshing(false);
//                    return;
//                }
//
//                if (getAllBaseSellerTask != null && getAllBaseSellerTask.getStatus() == AsyncTask.Status.RUNNING) {
//                    getAllBaseSellerTask.cancel(true);
//                }
//
//                isSwipeRefreshTask = true;
//                getAllBaseSellerTask = new GetAllBaseSellerTask(getActivity());
//                getAllBaseSellerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            }
//        });
    }

    private void initGetTagValue() {
        String userType = SessionManager.getStringSetting(getActivity(), SessionUtil.SESSION_KEY_USER_TAG);
        if (!AllSettingsManager.isNullOrEmpty(userType)) {
            mFlavorType = FlavorType.getFlavor(SessionManager.getStringSetting(getActivity(), SessionUtil.SESSION_KEY_USER_TAG));
        } else {
            mFlavorType = FlavorType.CUSTOMER;
        }

        if (mFlavorType != null) {
            switch (mFlavorType) {
                case CUSTOMER:
                    mAppUser = SessionUtil.getUser(getActivity());
                    selectUserType = "customer";
                    if (mAppUser != null) {
                        customerOrSupplierId = mAppUser.getId();
                    }
                    break;
                case SUPPLIER:
                    mAppSupplier = SessionUtil.getSupplier(getActivity());
                    selectUserType = "supplier";
                    if (mAppSupplier != null) {
                        customerOrSupplierId = mAppSupplier.getUser_id();
                    }
                    break;
            }

            Logger.d(TAG, TAG + " >>> " + "selectUserType " + selectUserType);
            Logger.d(TAG, TAG + " >>> " + "customerOrSupplierId " + customerOrSupplierId);
        }
    }


    public void resetCounterView() {
        List<StaggeredMedicineByItem> data = AppUtil.getAllStoredMedicineByItems(getActivity());
        if (data.size() > 0) {
            tvCount.setText(data.size() + "");
            tvCount.setVisibility(View.VISIBLE);
            rlCart.setVisibility(View.GONE);
        } else {
            rlCart.setVisibility(View.GONE);
            tvCount.setVisibility(View.GONE);
        }
    }

    private void setRecyclerView() {
        allBaseSellerListAdapter = new AllBaseSellerListAdapter(getActivity());
        rvCollection.setLayoutManager(new GridLayoutManager(getActivity(), AppUtil.getGridSpanCount(getActivity())));
        rvCollection.setAdapter(allBaseSellerListAdapter);
        allBaseSellerListAdapter.setMore(R.layout.view_more, new RecyclerArrayAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Logger.d(TAG, TAG + " >>> " + "onLoadMore(mOffset): " + mOffset);
                Logger.d(TAG, TAG + " >>> " + "onLoadMore(mTotalcount): " + mTotalcount);
                Logger.d(TAG, TAG + " >>> " + "onLoadMore(mPlaceOrderListAdapter): " + allBaseSellerListAdapter.getCount());
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
                    allBaseSellerListAdapter.addAll(new ArrayList<StaggeredMedicineByItem>());
                }
            }
        });
        allBaseSellerListAdapter.setNoMore(R.layout.view_nomore);
        allBaseSellerListAdapter.setError(R.layout.view_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
                allBaseSellerListAdapter.resumeMore();
            }

            @Override
            public void onErrorClick() {
                allBaseSellerListAdapter.resumeMore();
            }
        });
    }

    private void setData() {
        if (!NetworkManager.isConnected(getActivity())) {
            dismissPopupDialog();
            //This is for more loading
            allBaseSellerListAdapter.pauseMore();
            //   tvSuggestion.setText(getString(R.string.view_please_connect_internet_and_retry));
            Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (getAllBaseSellerTask != null && getAllBaseSellerTask.getStatus() == AsyncTask.Status.RUNNING) {
                Logger.d(TAG, TAG + " >>> " + "setData: canceling asynctask");
                getAllBaseSellerTask.cancel(true);
            }

            getAllBaseSellerTask = new GetAllBaseSellerTask(getActivity());
            getAllBaseSellerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }
    }

    private void initBaseSellerRv(List<StaggeredMedicineByItem> baseSellerLists) {
        isSwipeRefreshTask = false;
        rvCollection.setRefreshing(false);
        if (baseSellerLists != null && baseSellerLists.size() > 0) {
            rvCollection.setVisibility(View.VISIBLE);
            linNoDataFound.setVisibility(View.GONE);
            allBaseSellerListAdapter = new AllBaseSellerListAdapter(getActivity());
            rvCollection.setLayoutManager(new GridLayoutManager(getActivity(), AppUtil.getGridSpanCount(getActivity())));
            rvCollection.setAdapter(allBaseSellerListAdapter);
            allBaseSellerListAdapter.addAll(baseSellerLists);
        } else {
            rvCollection.setVisibility(View.GONE);
            linNoDataFound.setVisibility(View.VISIBLE);
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

        if (getAllBaseSellerTask != null && getAllBaseSellerTask.getStatus() == AsyncTask.Status.RUNNING) {
            getAllBaseSellerTask.cancel(true);
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

    private class GetAllBaseSellerTask extends AsyncTask<String, Integer, Response> {

        Context mContext;

        private GetAllBaseSellerTask(Context context) {
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
                Call<APIResponse<CommonMedicinesByItem>> call = mApiInterface.apiGetBestSellerList(mOffset, mItemLimit,customerOrSupplierId,selectUserType);
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
//                if (!isSwipeRefreshTask) {
//                    dismissProgressDialog();
//                }
//
//                isSwipeRefreshTask = false;
//                rvCollection.setRefreshing(false);

                dismissPopupDialog();

                if (result != null && result.isSuccessful()) {
                    Logger.d(TAG, "APIResponse(GetAllBaseSellerTask): onResponse-server = " + result.toString());
                    APIResponse<CommonMedicinesByItem> data = (APIResponse<CommonMedicinesByItem>) result.body();
                    Logger.d("GetAllBaseSellerTask", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(GetAllBaseSellerTask()): onResponse-object = " + data.toString());
                        //   initBaseSellerRv(data.getData().getMedicines());
                        allBaseSellerListAdapter.addAll(data.getData().getMedicines());

                        //This is needed for more loading
                        mOffset = mOffset + AllConstants.PER_PAGE_ITEM;
                        mItemLimit = mItemLimit + AllConstants.PER_PAGE_ITEM;
                        mTotalcount = data.getData().getTotals_count();

                        Logger.d("mTotalcount", mTotalcount + "");
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