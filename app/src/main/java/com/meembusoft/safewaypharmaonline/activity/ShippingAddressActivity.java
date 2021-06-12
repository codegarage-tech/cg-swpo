package com.meembusoft.safewaypharmaonline.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
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

import com.jude.easyrecyclerview.waveswiperefresh.WaveSwipeRefreshLayout;
import com.jude.easyrecyclerview.waveswiperefresh.WaveSwipeRefreshRecyclerView;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.adapter.AllCollectionListAdapter;
import com.meembusoft.safewaypharmaonline.adapter.ShippingAddressListAdapter;
import com.meembusoft.safewaypharmaonline.base.BaseActivity;
import com.meembusoft.safewaypharmaonline.base.BaseLocationActivity;
import com.meembusoft.safewaypharmaonline.enumeration.FlavorType;
import com.meembusoft.safewaypharmaonline.enumeration.ShippingType;
import com.meembusoft.safewaypharmaonline.model.AppSupplier;
import com.meembusoft.safewaypharmaonline.model.AppUser;
import com.meembusoft.safewaypharmaonline.model.CommonData;
import com.meembusoft.safewaypharmaonline.model.ParamsAddShippingAddress;
import com.meembusoft.safewaypharmaonline.model.ParamsEditShippingAddress;
import com.meembusoft.safewaypharmaonline.model.ShippingAddress;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.retrofit.APIClient;
import com.meembusoft.safewaypharmaonline.retrofit.APIInterface;
import com.meembusoft.safewaypharmaonline.retrofit.APIResponse;
import com.meembusoft.safewaypharmaonline.util.AllConstants;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.util.SessionUtil;
import com.meembusoft.safewaypharmaonline.view.CanaroTextView;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;

import org.parceler.Parcels;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_ORDER_SHIPPING_TYPE;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ShippingAddressActivity extends BaseActivity {

    //Toolbar
    private ImageView ivBack,ivAddress;
    private LinearLayout llLeftBack;
    private RelativeLayout rlCart,rlAddress;
    private CanaroTextView tvTitle;

    private LinearLayout linNoDataFound;
    private WaveSwipeRefreshRecyclerView rvShippingAddress;
    private ShippingAddressListAdapter addressListAdapter;
    private ShippingAddress mShippingAddress;

    //Background task
    private GetAllShippingAddressTask getAllShippingAddressTask;
    private DoUserUpdateShippingAddressTask doUserUpdateShippingAddressTask;
    private APIInterface mApiInterface;
    private boolean isSwipeRefreshTask = false;
    private AppUser mAppUser;
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
        return R.layout.activity_shipping_address_screen;
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
            Parcelable mParcelable = intent.getParcelableExtra(AllConstants.INTENT_KEY_ORDER_SHIPPING);
            if (mParcelable != null) {
                mShippingAddress = Parcels.unwrap(mParcelable);
                Logger.d(TAG, TAG + " >>> " + "<<mShippingAddress: " + mShippingAddress.toString());
            }
        }
    }

    @Override
    public void initActivityViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (CanaroTextView) findViewById(R.id.text_title);
        tvTitle.setText(getActivity().getResources().getString(R.string.title_add_shipping_address));
        rlCart = (RelativeLayout) findViewById(R.id.rl_cart);
        rlAddress = (RelativeLayout) findViewById(R.id.rl_address);
        rlAddress.setVisibility(View.GONE);
        llLeftBack = (LinearLayout) findViewById(R.id.ll_left_back);
        rvShippingAddress = (WaveSwipeRefreshRecyclerView ) findViewById(R.id.rv_shipping_address);
        linNoDataFound = (LinearLayout) findViewById(R.id.lin_no_data_found_layout);

        //Logger.d("mAppUser", mAppUser.getId());
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        // Get User Tag
        initGetTagValue();
        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
        } else {
            if (customerOrSupplierId != null && customerOrSupplierId != "") {
                getAllShippingAddressTask = new GetAllShippingAddressTask(getActivity(), customerOrSupplierId);
                getAllShippingAddressTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }


    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {

        llLeftBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iShippingAddress = new Intent();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                setResult(RESULT_OK, iShippingAddress);
                finish();
            }
        });
        rvShippingAddress.setRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!NetworkManager.isConnected(getActivity())) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                    rvShippingAddress.setRefreshing(false);
                    return;
                }

                if (getAllShippingAddressTask != null && getAllShippingAddressTask.getStatus() == AsyncTask.Status.RUNNING) {
                    getAllShippingAddressTask.cancel(true);
                }

                isSwipeRefreshTask = true;
                if (customerOrSupplierId != null && customerOrSupplierId != "") {
                    getAllShippingAddressTask = new GetAllShippingAddressTask(getActivity(), customerOrSupplierId);
                    getAllShippingAddressTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

    public void  updateShippingAddress(ShippingAddress shippingAddress) {
        if (shippingAddress != null) {
            Logger.d("<<<mShippingAddressdata", shippingAddress.toString());

            doUserUpdateShippingAddressTask = new DoUserUpdateShippingAddressTask(getActivity(), shippingAddress);
            doUserUpdateShippingAddressTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.toast_please_shipping_address_invalid), Toast.LENGTH_SHORT).show();
        }
    }


    private void initShippingAddressRv(List<ShippingAddress> shippingAddressList) {
        isSwipeRefreshTask = false;
        rvShippingAddress.setRefreshing(false);
        addressListAdapter = new ShippingAddressListAdapter(getActivity());
        rvShippingAddress.clear();
        if (shippingAddressList!=null && shippingAddressList.size()>0) {
            rvShippingAddress.setVisibility(View.VISIBLE);
            linNoDataFound.setVisibility(View.GONE);
            rvShippingAddress.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            rvShippingAddress.setAdapter(addressListAdapter);
            if (addressListAdapter != null) {
                addressListAdapter.setAdapterData(shippingAddressList,mShippingAddress);
            }
        } else  {
            rvShippingAddress.setVisibility(View.GONE);
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

        if (getAllShippingAddressTask != null && getAllShippingAddressTask.getStatus() == AsyncTask.Status.RUNNING) {
            getAllShippingAddressTask.cancel(true);
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
    private class GetAllShippingAddressTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        String mAppUserID;

        private GetAllShippingAddressTask(Context context, String appUserId) {
            mContext = context;
            mAppUserID = appUserId;

        }

        @Override
        protected void onPreExecute() {
            if (!isSwipeRefreshTask) {
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
                Call<APIResponse<List<ShippingAddress>>> call = mApiInterface.apiGetAllShippingAddressList(mAppUserID,selectUserType);
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
                if (!isSwipeRefreshTask) {
                    dismissPopupDialog();
                }

                isSwipeRefreshTask = false;
                rvShippingAddress.setRefreshing(false);

                if (result != null && result.isSuccessful()) {
                    Logger.d(TAG, "APIResponse(GetAllShippingAddressTask): onResponse-server = " + result.toString());
                    APIResponse<List<ShippingAddress>> data = (APIResponse<List<ShippingAddress>>) result.body();
                    Logger.d("GetAllShippingAddressTask", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(GetAllShippingAddressTask()): onResponse-object = " + data.toString());
                        initShippingAddressRv(data.getData());
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

    private class DoUserUpdateShippingAddressTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        ShippingAddress mUpdateShippingAddress;

        public DoUserUpdateShippingAddressTask(Context context, ShippingAddress shippingAddress) {
            mContext = context;
            mUpdateShippingAddress = shippingAddress;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Logger.d(TAG, TAG + ">> Background task is cancelled");
        }

        @Override
        protected void onPreExecute() {
            showPopupDialog();
        }

        @Override
        protected Response doInBackground(String... params) {
            try {

                Call<APIResponse<ShippingAddress>> call = null;
                Logger.d("<<<mShippingAddress", mUpdateShippingAddress.toString());

                        if (mUpdateShippingAddress != null && (!AllSettingsManager.isNullOrEmpty(mUpdateShippingAddress.getId()))) {
                            ParamsEditShippingAddress mParamsEditShippingAddress = new ParamsEditShippingAddress(mUpdateShippingAddress.getCustomer_id(),selectUserType, mUpdateShippingAddress.getId(),  mUpdateShippingAddress.getName(),mUpdateShippingAddress.getPhone(),mUpdateShippingAddress.getAddress(), mUpdateShippingAddress.getCity(), mUpdateShippingAddress.getState(), mUpdateShippingAddress.getZipcode(), mUpdateShippingAddress.getLongitude(), mUpdateShippingAddress.getLatitude());
                            call = mApiInterface.apiUserEditShippingAddress(mParamsEditShippingAddress);
                            Logger.d(TAG, TAG + " >>> " + "mParamsEditShippingAddress: " + mParamsEditShippingAddress.toString());

                        }

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
                    Logger.d(TAG, "APIResponse(DoUserUpdateShippingAddressTask): onResponse-server = " + result.toString());
                    APIResponse<ShippingAddress> data = (APIResponse<ShippingAddress>) result.body();
                    Logger.d("AppUser>>", data.toString());

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(DoUserUpdateShippingAddressTask()): onResponse-object = " + data.toString());

                            if (data.getData() != null) {
                                SessionUtil.setUserShippingAddress(getActivity(), APIResponse.getResponseString(data.getData()));
                                Logger.d("getData>>", data.getData().toString());
                            }
                            Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_SHORT).show();

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