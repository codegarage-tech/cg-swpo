package com.meembusoft.safewaypharmaonline.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.waveswiperefresh.WaveSwipeRefreshRecyclerView;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.adapter.CategoryListAdapter;
import com.meembusoft.safewaypharmaonline.adapter.MedicineByCategoryListAdapter;
import com.meembusoft.safewaypharmaonline.adapter.PlaceOrderListAdapter;
import com.meembusoft.safewaypharmaonline.base.BaseActivity;
import com.meembusoft.safewaypharmaonline.enumeration.CategoryType;
import com.meembusoft.safewaypharmaonline.enumeration.FlavorType;
import com.meembusoft.safewaypharmaonline.model.AppSupplier;
import com.meembusoft.safewaypharmaonline.model.AppUser;
import com.meembusoft.safewaypharmaonline.model.Category;
import com.meembusoft.safewaypharmaonline.model.CommonMedicinesByItem;
import com.meembusoft.safewaypharmaonline.model.PlaceOrderByItem;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class CategoryActivity extends BaseActivity {

    //Toolbar
    private ImageView ivBack;
    private CanaroTextView tvTitle;
    private TextView tvCount;
    private RelativeLayout rlCart;
    private LinearLayout llLeftBack;

    private CategoryType mCategoryType;
    private LinearLayout linNoDataFound;
    private WaveSwipeRefreshRecyclerView rvMedicineByCategory;
    private RecyclerView rvCategoryItem;
    private CategoryListAdapter categoryListAdapter;
    private MedicineByCategoryListAdapter mMedicineByCategoryListAdapter;
    private String categoryId = "1";
    //More loading
    private int mOffset = 0,mItemLimit = AllConstants.PER_PAGE_ITEM, mTotalcount = 0;
    private Handler mHandler = new Handler();
    //Background task
    private GetCategoryTask getCategoryTask;
    private GetMedicineByCategoryTask getMedicineByCategoryTask;
    private APIInterface mApiInterface;
    private String customerOrSupplierId = "";
    private String selectUserType = AllConstants.USER_TYPE_CUSTOMER;
    AppUser mAppUser;

    //  Supplier
    private AppSupplier mAppSupplier;
    FlavorType mFlavorType;
    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }


    @Override
    public int initActivityLayout() {
        return R.layout.activity_category_screen;
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
        ivBack = (ImageView)findViewById(R.id.iv_back);
        tvTitle = (CanaroTextView) findViewById(R.id.text_title);
        tvTitle.setText(getActivity().getResources().getString(R.string.title_categories));
        tvCount = (TextView)findViewById(R.id.tv_cart);
        llLeftBack = (LinearLayout) findViewById(R.id.ll_left_back);
        rlCart = (RelativeLayout) findViewById(R.id.rl_cart);
        rvCategoryItem = (RecyclerView) findViewById(R.id.rv_category_item);
        rvMedicineByCategory = (WaveSwipeRefreshRecyclerView) findViewById(R.id.rv_medicine_by_category);
//        rvMedicineByCategory = (RecyclerView) findViewById(R.id.rv_medicine_by_category);
        linNoDataFound = (LinearLayout) findViewById(R.id.lin_no_data_found);


    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        // Get User Tag
        initGetTagValue();

        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
        } else {
            getCategoryTask = new GetCategoryTask(getActivity());
            getCategoryTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            setData(categoryId);
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
                    selectUserType = AllConstants.USER_TYPE_CUSTOMER;
                    if (mAppUser != null) {
                        customerOrSupplierId = mAppUser.getId();
                    }
                    break;
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

    public void  categoryByMedicine(String categoryID) {
        setRecyclerView();
        setData(categoryId);

    }

    public void setRecyclerView() {
        mMedicineByCategoryListAdapter = new MedicineByCategoryListAdapter(getActivity(),"");
//        rvMedicineByCategory.setLayoutManager(new GridLayoutManager(getActivity(), AppUtil.getGridSpanCount3(getActivity())));
//        rvMedicineByCategory.setAdapter(mMedicineByCategoryListAdapter);
        mMedicineByCategoryListAdapter.setMore(R.layout.view_more, new RecyclerArrayAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Logger.d(TAG, TAG + " >>> " + "onLoadMore(mOffset): " + mOffset);
                Logger.d(TAG, TAG + " >>> " + "onLoadMore(mTotalcount): " + mTotalcount);
                Logger.d(TAG, TAG + " >>> " + "onLoadMore(mPlaceOrderListAdapter): " + mMedicineByCategoryListAdapter.getCount());
                if (mOffset < mTotalcount) {
                    Logger.d(TAG, TAG + " >>> " + "onLoadMore: started loading");
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setData(categoryId);
                        }
                    }, 2000);
                } else {
                    //No more item
                    mMedicineByCategoryListAdapter.addAll(new ArrayList<StaggeredMedicineByItem>());
                }
            }
        });
        mMedicineByCategoryListAdapter.setNoMore(R.layout.view_nomore);
        mMedicineByCategoryListAdapter.setError(R.layout.view_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
                mMedicineByCategoryListAdapter.resumeMore();
            }

            @Override
            public void onErrorClick() {
                mMedicineByCategoryListAdapter.resumeMore();
            }
        });
    }


    public void setData(String categoryID) {
        setRecyclerView();
        categoryId = categoryID;
        if (!NetworkManager.isConnected(getActivity())) {
            dismissPopupDialog();
            //This is for more loading
            mMedicineByCategoryListAdapter.pauseMore();
            //   tvSuggestion.setText(getString(R.string.view_please_connect_internet_and_retry));
            Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (getMedicineByCategoryTask != null && getMedicineByCategoryTask.getStatus() == AsyncTask.Status.RUNNING) {
                Logger.d(TAG, TAG + " >>>categoryId " +categoryId);
                getMedicineByCategoryTask.cancel(true);
            }

            getMedicineByCategoryTask = new GetMedicineByCategoryTask(getActivity(),categoryId);
            getMedicineByCategoryTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }



    public void medicineByCategoryId(String categoryId){
        Log.e("categoryId",categoryId+">>");
        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
        } else {
            getMedicineByCategoryTask = new GetMedicineByCategoryTask(getActivity(),categoryId);
            getMedicineByCategoryTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }
    }

    private void initCategoryRecyclerView(List<Category> categoryList) {
        categoryListAdapter = new CategoryListAdapter(getActivity());
        rvCategoryItem.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvCategoryItem.setAdapter(categoryListAdapter);
        if (categoryListAdapter != null) {
            categoryListAdapter.setAdapterData(categoryList);

        }
    }

//    private void initMedicineByCategoryRv(List<StaggeredMedicineByItem> medicineByCategories) {
//        medicineByCategoryListAdapter = new MedicineByCategoryListAdapter(getActivity());
//        rvMedicineByCategory.setLayoutManager(new GridLayoutManager(getActivity(), AppUtil.getGridSpanCount3(getActivity())));
//        rvMedicineByCategory.setAdapter(medicineByCategoryListAdapter);
//        medicineByCategoryListAdapter.addAll(medicineByCategories);
//    }


    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void initActivityBackPress() {
        finish();

    }

    @Override
    public void initActivityDestroyTasks() {
        dismissPopupDialog();
        if (getCategoryTask != null && getCategoryTask.getStatus() == AsyncTask.Status.RUNNING) {
            getCategoryTask.cancel(true);
        }
        if (getMedicineByCategoryTask != null && getMedicineByCategoryTask.getStatus() == AsyncTask.Status.RUNNING) {
            getMedicineByCategoryTask.cancel(true);
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

    private class GetCategoryTask extends AsyncTask<String, Integer, Response> {

        Context mContext;

        private GetCategoryTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            showPopupDialog();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Logger.d(TAG, TAG + ">> Background task is cancelled");
        }

        @Override
        protected Response doInBackground(String... params) {
            try {
                Call<APIResponse<List<Category>>> call = mApiInterface.apiGetCategories();
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
                    Logger.d(TAG, "APIResponse(getCategoryTask): onResponse-server = " + result.toString());
                    APIResponse<List<Category>> data = (APIResponse<List<Category>>) result.body();
                    Logger.d("getCategoryTask", data.toString() + "");
                    Log.e("getCategoryTask>>",data.toString()+"");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(getCategoryTask()): onResponse-object = " + data.toString());

                        initCategoryRecyclerView(data.getData());

                    }
                } else {
                    // loadOfflineTimeData();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private class GetMedicineByCategoryTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        String categoryID;

        private GetMedicineByCategoryTask(Context context,String categoryId) {
            mContext = context;
            categoryID = categoryId;

        }

        @Override
        protected void onPreExecute() {
            if (mOffset == 0) {

                showPopupDialog();
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
                Logger.d(TAG, TAG + ">> categoryID"+categoryID);

                Call<APIResponse<CommonMedicinesByItem>> call = mApiInterface.apiGetMedicineByCategoryList(categoryID,mOffset,mItemLimit,customerOrSupplierId,selectUserType);
                Response response = call.execute();
                Logger.d("GetAllOrderPlaceTask", response+ "response");

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
                    Logger.d(TAG, "APIResponse(getMedicineByCategoryTask): onResponse-server = " + result.toString());
                    APIResponse<CommonMedicinesByItem> data = (APIResponse<CommonMedicinesByItem>) result.body();
                    Logger.d("<MedicineByCategoryTask", data.toString() + "");

                    if ( data.getData().getMedicines().size()>0) {
                        Logger.d("<getMedicines", data.getData().getMedicines().size() + "");

                        Logger.d(TAG, "APIResponse(getMedicineByCategoryTask()): onResponse-object = " + data.toString());
                       // mMedicineByCategoryListAdapter.addAll( data.getData().getMedicines());
                        rvMedicineByCategory.setLayoutManager(new GridLayoutManager(getActivity(), AppUtil.getGridSpanCount3(getActivity())));
                        rvMedicineByCategory.setAdapter(mMedicineByCategoryListAdapter);
                        mMedicineByCategoryListAdapter.addAll( data.getData().getMedicines());
                        //This is needed for more loading
                        mOffset = mOffset + AllConstants.PER_PAGE_ITEM;
                        mItemLimit = mItemLimit + AllConstants.PER_PAGE_ITEM;
                        mTotalcount = data.getData().getTotals_count();
                        linNoDataFound.setVisibility(View.GONE);
                        Logger.d("mTotalcount", mTotalcount+ "");
                    }
                } else {
                    // loadOfflineTimeData();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}