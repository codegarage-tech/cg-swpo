package com.meembusoft.safewaypharmaonline.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.adapter.SeeMoreStaggeredListAdapter;
import com.meembusoft.safewaypharmaonline.adapter.StaggeredListAdapter;
import com.meembusoft.safewaypharmaonline.base.BaseActivity;
import com.meembusoft.safewaypharmaonline.enumeration.CategoryType;
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

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SeeMoreActivity extends BaseActivity {

    // Toolbar
    private CanaroTextView tvTitle;
    private RelativeLayout rlCart;
    private TextView tvCount;
    private ImageView ivBack;
    private CategoryType mCategoryType;
    private LinearLayout linNoDataFound;

    // Recycler view
    private RecyclerView rvSeeAll;
    // ArrayList
    List<StaggeredMedicineByItem> staggeredMedicineByItem;
    //Adapter
    private StaggeredListAdapter mStaggeredListAdapter;
    private SeeMoreStaggeredListAdapter mSeeMoreStaggeredListAdapter;
    private String categoryId = "";
    //More loading
    private int mOffset = 0, mItemLimit = AllConstants.PER_PAGE_ITEM, mTotalcount = 0;
    private Handler mHandler = new Handler();
    //Background task
    private GetMedicineByCategoryTask getMedicineByCategoryTask;
    private APIInterface mApiInterface;
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
        return R.layout.activity_see_all_screen;
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
            String mParcelableCategoryType = intent.getStringExtra(AllConstants.INTENT_KEY_MEDICINE_BY_CATEGORY_TYPE);
            String mParcelableCategoryId = intent.getStringExtra(AllConstants.INTENT_KEY_CATEGORY_ID);
            if (!AllSettingsManager.isNullOrEmpty(mParcelableCategoryType)) {
                mCategoryType = CategoryType.valueOf(mParcelableCategoryType);
                Logger.d(TAG, TAG + " >>> " + "mCategoryType: " + mCategoryType);
            }
            if (!AllSettingsManager.isNullOrEmpty(mParcelableCategoryId)) {
                categoryId = mParcelableCategoryId;
                Logger.d(TAG, TAG + " >>> " + "categoryId: " + categoryId);
            }

//            Parcelable mParcelable = intent.getParcelableExtra(AllConstants.INTENT_KEY_GENERAL_MEDICINE_BY_CATEGORY);
//            if (mParcelable != null) {
//                staggeredMedicineByItem = Parcels.unwrap(mParcelable);
//                Logger.d(TAG, TAG + " >>> " + "staggeredMedicineByItem: " + staggeredMedicineByItem.toString());
//            }
        }
    }

    @Override
    public void initActivityViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        rvSeeAll = (RecyclerView) findViewById(R.id.rv_see_all);
        tvTitle = (CanaroTextView) findViewById(R.id.text_title);
        tvCount = (TextView) findViewById(R.id.tv_cart);
        rlCart = (RelativeLayout) findViewById(R.id.rl_cart);
        linNoDataFound = (LinearLayout) findViewById(R.id.lin_no_data_found_layout);

        resetCounterView();

    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        // Get User Tag
        initGetTagValue();
        switch (mCategoryType) {

            case BASE_SELLER:
                tvTitle.setText(getResources().getString(R.string.title_base_seller));
                // initMedicineByCategoryRv(staggeredMedicineByItem);
                break;

            case GENERAL_MEDICINE:
                tvTitle.setText(getResources().getString(R.string.title_general_medicine));
                // initMedicineByCategoryRv(staggeredMedicineByItem);
                break;
            case MEDICAL_INSTRUMENT:
                tvTitle.setText(getResources().getString(R.string.title_medical_instrument));
                // initMedicineByCategoryRv(staggeredMedicineByItem);
                break;
            case HERBAL_MEDICINE:
                tvTitle.setText(getResources().getString(R.string.title_herbal_medicine));
                // initMedicineByCategoryRv(staggeredMedicineByItem);
                break;

            case COSMETICS:
                tvTitle.setText(getResources().getString(R.string.title_medical_cosmetics));
                //  initMedicineByCategoryRv(staggeredMedicineByItem);
                break;

            case BABY_FOOD_STATIONARY:
                tvTitle.setText(getResources().getString(R.string.title_baby_food_statioary));
                //  initMedicineByCategoryRv(staggeredMedicineByItem);
                break;

            case MEDICAL_SUPPORT:
                tvTitle.setText(getResources().getString(R.string.title_medical_support));
                //  initMedicineByCategoryRv(staggeredMedicineByItem);
                break;

            case OPTICS_LENS:
                tvTitle.setText(getResources().getString(R.string.title_optics_lens));
                //  initMedicineByCategoryRv(staggeredMedicineByItem);
                break;

            case SHOP:
                tvTitle.setText(getResources().getString(R.string.title_shop));
                //  initMedicineByCategoryRv(staggeredMedicineByItem);
                break;
        }
        setRecyclerView();
        setData(categoryId);
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

    @Override
    public void initActivityActions(Bundle savedInstanceState) {

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

    }

    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {

    }


    public void setRecyclerView() {
        mSeeMoreStaggeredListAdapter = new SeeMoreStaggeredListAdapter(getActivity(),mCategoryType);
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        //rvShopStaggered.addItemDecoration(new SpacesItemDecoration(4));
        rvSeeAll.setLayoutManager(staggeredGridLayoutManager);
        rvSeeAll.setAdapter(mSeeMoreStaggeredListAdapter);
        mSeeMoreStaggeredListAdapter.setMore(R.layout.view_more, new RecyclerArrayAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Logger.d(TAG, TAG + " >>> " + "onLoadMore(mOffset): " + mOffset);
                Logger.d(TAG, TAG + " >>> " + "onLoadMore(mTotalcount): " + mTotalcount);
                Logger.d(TAG, TAG + " >>> " + "onLoadMore(mPlaceOrderListAdapter): " + mSeeMoreStaggeredListAdapter.getCount());
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
                    mSeeMoreStaggeredListAdapter.addAll(new ArrayList<StaggeredMedicineByItem>());

                }
            }
        });
        mSeeMoreStaggeredListAdapter.setNoMore(R.layout.view_nomore);
        mSeeMoreStaggeredListAdapter.setError(R.layout.view_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
                mSeeMoreStaggeredListAdapter.resumeMore();
            }

            @Override
            public void onErrorClick() {
                mSeeMoreStaggeredListAdapter.resumeMore();
            }
        });
    }


    public void setData(String categoryID) {
        if (!NetworkManager.isConnected(getActivity())) {
            dismissPopupDialog();
            //This is for more loading
            mSeeMoreStaggeredListAdapter.pauseMore();
            //   tvSuggestion.setText(getString(R.string.view_please_connect_internet_and_retry));
            Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (getMedicineByCategoryTask != null && getMedicineByCategoryTask.getStatus() == AsyncTask.Status.RUNNING) {
                getMedicineByCategoryTask.cancel(true);
            }

            if (mCategoryType != null) {
                getMedicineByCategoryTask = new GetMedicineByCategoryTask(getActivity(), categoryID, mCategoryType);
                getMedicineByCategoryTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
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

    private void initMedicineByCategoryRv(List<StaggeredMedicineByItem> data) {
        mStaggeredListAdapter = new StaggeredListAdapter(getActivity(), CategoryType.GENERAL_MEDICINE,mFlavorType);
        rvSeeAll.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rvSeeAll.setAdapter(mStaggeredListAdapter);
        mStaggeredListAdapter.setAllData(data, 6);
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


    private class GetMedicineByCategoryTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        String categoryID;
        CategoryType mCategoryType;

        private GetMedicineByCategoryTask(Context context, String categoryId, CategoryType categoryType) {
            mContext = context;
            categoryID = categoryId;
            mCategoryType = categoryType;
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
                Logger.d(TAG, TAG + ">> categoryID" + categoryID);
                Call<APIResponse<CommonMedicinesByItem>> call = null;
                if (mCategoryType != null) {
                    if (mCategoryType == CategoryType.SHOP) {
                        call = mApiInterface.apiGetAllProductMedicinesList(mOffset, mItemLimit,selectUserType,customerOrSupplierId);
                    } else if (mCategoryType == CategoryType.BASE_SELLER) {
                        call = mApiInterface.apiGetBestSellerList(mOffset, mItemLimit,customerOrSupplierId,selectUserType);
                    } else {
                        call = mApiInterface.apiGetMedicineByCategoryList(categoryID, mOffset, mItemLimit,customerOrSupplierId,selectUserType);
                    }

                }
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

                if (result != null && result.isSuccessful()) {
                    Logger.d(TAG, "APIResponse(getMedicineByCategoryTask): onResponse-server = " + result.toString());
                    APIResponse<CommonMedicinesByItem> data = (APIResponse<CommonMedicinesByItem>) result.body();
                    Logger.d("<MedicineByCategoryTask", data.toString() + "");

                    if (data != null && data.getData().getMedicines().size() > 0) {
                        Logger.d(TAG, "APIResponse(getMedicineByCategoryTask()): onResponse-object = " + data.toString());
                        mSeeMoreStaggeredListAdapter.addAll(data.getData().getMedicines());
                        //This is needed for more loading
                        mOffset = mOffset + AllConstants.PER_PAGE_ITEM;
                        mItemLimit = mItemLimit + AllConstants.PER_PAGE_ITEM;
                        mTotalcount = data.getData().getTotals_count();
                        rvSeeAll.setVisibility(View.VISIBLE);
                        linNoDataFound.setVisibility(View.GONE);
                        Logger.d("mTotalcount", mTotalcount + "");
                    } else {
                        mSeeMoreStaggeredListAdapter.clear();
                        rvSeeAll.setVisibility(View.GONE);
                        linNoDataFound.setVisibility(View.VISIBLE);

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