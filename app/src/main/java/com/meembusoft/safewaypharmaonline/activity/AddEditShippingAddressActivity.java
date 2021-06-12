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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.adapter.ShippingAddressListAdapter;
import com.meembusoft.safewaypharmaonline.base.BaseLocationActivity;
import com.meembusoft.safewaypharmaonline.enumeration.FlavorType;
import com.meembusoft.safewaypharmaonline.enumeration.ShippingType;
import com.meembusoft.safewaypharmaonline.geocoding.LocationAddressListener;
import com.meembusoft.safewaypharmaonline.geocoding.ReverseGeocoderAddressTask;
import com.meembusoft.safewaypharmaonline.geocoding.UserLocationAddress;
import com.meembusoft.safewaypharmaonline.model.AppSupplier;
import com.meembusoft.safewaypharmaonline.model.AppUser;
import com.meembusoft.safewaypharmaonline.model.ParamsAddShippingAddress;
import com.meembusoft.safewaypharmaonline.model.ParamsEditShippingAddress;
import com.meembusoft.safewaypharmaonline.model.ShippingAddress;
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

import org.parceler.Parcels;

import retrofit2.Call;
import retrofit2.Response;

import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_ORDER_SHIPPING_TYPE;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AddEditShippingAddressActivity extends BaseLocationActivity {

    //Toolbar
    private ImageView ivBack,ivAddress;
    private LinearLayout llLeftBack;
    private RelativeLayout rlAddress;
    private CanaroTextView tvTitle;
    private EditText etShippingName,etShippingPhone,etShippingAddress, etShippingCity, etShippingState, etShippingZip;
    private Button btnSubmit;
    private String name = "", phone = "", streetAddress = "", state = "", city = "", zipcode = "";
    private ShippingAddressListAdapter addressListAdapter;
    private AppUser mAppUser;
    //Background task
    private DoUserAddEditShippingAddressTask doUserAddEditShippingAddressTask;
    private APIInterface mApiInterface;
    private boolean isSwipeRefreshTask = false;
    private ShippingType mShippingType;
    private ShippingAddress mShippingAddress;
    ReverseGeocoderAddressTask currentLocationTask;
    Location mLocation;
    String mAddress = "";
    private double latitude = 0.0, longitude = 0.0;

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
    public void onLocationFound(Location location) {
        if (location != null) {
            mLocation = location;
        }
    }

    @Override
    public LOCATION_UPDATE_FREQUENCY initLocationUpdateFrequency() {
        return LOCATION_UPDATE_FREQUENCY.ONCE;
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_add_shipping_address_screen;
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
            String mParcelableShippingType = intent.getStringExtra(INTENT_KEY_ORDER_SHIPPING_TYPE);
            if (!AllSettingsManager.isNullOrEmpty(mParcelableShippingType)) {
                mShippingType = ShippingType.valueOf(mParcelableShippingType);
                Logger.d(TAG, TAG + " >>> " + "mShippingType: " + mParcelableShippingType);
            }

            Parcelable mParcelable = intent.getParcelableExtra(AllConstants.INTENT_KEY_ORDER_SHIPPING);
            if (mParcelable != null) {
                mShippingAddress = Parcels.unwrap(mParcelable);
                Logger.d(TAG, TAG + " >>> " + "mShippingAddress: " + mShippingAddress.toString());
            }
        }
    }

    @Override
    public void initActivityViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivAddress = (ImageView) findViewById(R.id.iv_address);
        tvTitle = (CanaroTextView) findViewById(R.id.text_title);
        tvTitle.setText(getActivity().getResources().getString(R.string.title_add_shipping_address));
        rlAddress = (RelativeLayout) findViewById(R.id.rl_address);
        llLeftBack = (LinearLayout) findViewById(R.id.ll_left_back);
        etShippingName = (EditText) findViewById(R.id.et_shipping_name);
        etShippingPhone = (EditText) findViewById(R.id.et_shipping_phone);
        etShippingAddress = (EditText) findViewById(R.id.et_shipping_address);
        etShippingCity = (EditText) findViewById(R.id.et_shipping_city);
        etShippingState = (EditText) findViewById(R.id.et_shipping_state);
        etShippingZip = (EditText) findViewById(R.id.et_shipping_zip);
        btnSubmit = (Button) findViewById(R.id.btn_submit);


    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        // Get User Tag
        initGetTagValue();
        if (mShippingType != null) {
            switch (mShippingType) {
                case ADD_TO_SHIPPING:
                    btnSubmit.setText(getResources().getString(R.string.txt_sa_add));
                    break;
                case EDIT_TO_SHIPPING:
                    setData();
                    break;
            }
        }

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

    private void setData() {
        btnSubmit.setText(getResources().getString(R.string.txt_sa_edit));
        ivAddress.setVisibility(View.GONE);
        if (mShippingAddress != null) {
            etShippingName.setText(AppUtil.optStringNullCheckValue(mShippingAddress.getName()));
            etShippingPhone.setText(AppUtil.optStringNullCheckValue(mShippingAddress.getPhone()));
            etShippingAddress.setText(AppUtil.optStringNullCheckValue(mShippingAddress.getAddress()));
            etShippingCity.setText(AppUtil.optStringNullCheckValue(mShippingAddress.getCity()));
            etShippingState.setText(AppUtil.optStringNullCheckValue(mShippingAddress.getState()));
            etShippingZip.setText(AppUtil.optStringNullCheckValue(mShippingAddress.getZipcode()));
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

        btnSubmit.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (!NetworkManager.isConnected(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                } else {
                    if (IsValidationInfo()) {
                        mAddress = streetAddress + ", " + state + "," + city ;
                        Logger.d(TAG, TAG + "> mAddress" + mAddress);
                        Logger.d(TAG, TAG + "> mLocation" + mLocation.toString());

                        if (mLocation != null) {
                            if ((currentLocationTask != null) && (currentLocationTask.getStatus() == AsyncTask.Status.RUNNING)) {
                                currentLocationTask.cancel(true);
                            }
                            currentLocationTask = new ReverseGeocoderAddressTask(getActivity(), mAddress, new LocationAddressListener() {
                                @Override
                                public void getLocationAddress(UserLocationAddress userLocationAddress) {
                                    if (userLocationAddress != null) {
                                        Logger.d(TAG, "UserLocationAddress: " + userLocationAddress.toString());
                                        latitude = userLocationAddress.getLatitude();
                                        longitude = userLocationAddress.getLongitude();
                                        Logger.d(TAG, TAG + "> latitude" + latitude + "\n" + longitude);
                                        // Shipping Address
                                        doUserAddEditShippingAddressTask = new DoUserAddEditShippingAddressTask(getActivity(), mShippingType);
                                        doUserAddEditShippingAddressTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                    } else {
                                        Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_valid_address), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                            currentLocationTask.execute(mLocation);
                        }

                    }
                }
            }
        });

        rlAddress.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (!NetworkManager.isConnected(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                } else {
                    if (IsValidationInfo()) {
                        if (IsValidationInfo()) {
                            mAddress = streetAddress + ", " + state + "," + city + "," + zipcode + ",";
                            Logger.d(TAG, TAG + "> mAddress" + mAddress);

                            if (mLocation != null) {
                                if ((currentLocationTask != null) && (currentLocationTask.getStatus() == AsyncTask.Status.RUNNING)) {
                                    currentLocationTask.cancel(true);
                                }
                                currentLocationTask = new ReverseGeocoderAddressTask(getActivity(), mAddress, new LocationAddressListener() {
                                    @Override
                                    public void getLocationAddress(UserLocationAddress userLocationAddress) {
                                        if (userLocationAddress != null) {
                                            Logger.d(TAG, "UserLocationAddress: " + userLocationAddress.toString());
                                            latitude = userLocationAddress.getLatitude();
                                            longitude = userLocationAddress.getLongitude();
                                            Logger.d(TAG, TAG + "> latitude" + latitude + "\n" + longitude);

                                            // Shipping Address
                                            doUserAddEditShippingAddressTask = new DoUserAddEditShippingAddressTask(getActivity(), mShippingType);
                                            doUserAddEditShippingAddressTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                                        } else {
                                            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_valid_address), Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                                currentLocationTask.execute(mLocation);
                            }

                        }

                    }
                }
            }
        });

    }

    private boolean IsValidationInfo() {

        name = etShippingName.getText().toString();
        phone = etShippingPhone.getText().toString();
        streetAddress = etShippingAddress.getText().toString();
        state = etShippingState.getText().toString();
        city = etShippingCity.getText().toString();
        zipcode = etShippingZip.getText().toString();

        if (AllSettingsManager.isNullOrEmpty(name)) {
            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_name), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (AllSettingsManager.isNullOrEmpty(phone)) {
            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_phone), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (AllSettingsManager.isNullOrEmpty(streetAddress)) {
            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_street_address), Toast.LENGTH_SHORT).show();
            return false;
        }
//        if (AllSettingsManager.isNullOrEmpty(state)) {
//            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_state), Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        if (AllSettingsManager.isNullOrEmpty(city)) {
//            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_city), Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (AllSettingsManager.isNullOrEmpty(zipcode)) {
//            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_zipcode), Toast.LENGTH_SHORT).show();
//            return false;
//        }
        return true;
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

        if (doUserAddEditShippingAddressTask != null && doUserAddEditShippingAddressTask.getStatus() == AsyncTask.Status.RUNNING) {
            doUserAddEditShippingAddressTask.cancel(true);
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

    private class DoUserAddEditShippingAddressTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        ShippingType mShippingType;

        public DoUserAddEditShippingAddressTask(Context context, ShippingType shippingType) {
            mContext = context;
            mShippingType = shippingType;
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

                Call<APIResponse<ShippingAddress>> call = null;
                Logger.d("<<<mShippingType", mShippingType);

                switch (mShippingType) {
                    case ADD_TO_SHIPPING:
                        if (customerOrSupplierId != null && customerOrSupplierId != "") {
                            ParamsAddShippingAddress mParamsAddShippingAddress = new ParamsAddShippingAddress(customerOrSupplierId,selectUserType, name,phone,streetAddress, city, state, zipcode, String.valueOf(longitude), String.valueOf(latitude));
                            call = mApiInterface.apiUserAddNewShippingAddress(mParamsAddShippingAddress);
                            Logger.d(TAG, TAG + " >>> " + "mParamsAddShippingAddress: " + mParamsAddShippingAddress.toString());
                        }
                        break;
                    case EDIT_TO_SHIPPING:
                        if (mShippingAddress != null && (!AllSettingsManager.isNullOrEmpty(mShippingAddress.getId()))) {
                            ParamsEditShippingAddress mParamsEditShippingAddress = new ParamsEditShippingAddress(mShippingAddress.getCustomer_id(),selectUserType, mShippingAddress.getId(),  name,phone,streetAddress, city, state, zipcode, String.valueOf(longitude), String.valueOf(latitude));
                            call = mApiInterface.apiUserEditShippingAddress(mParamsEditShippingAddress);
                            Logger.d(TAG, TAG + " >>> " + "mParamsEditShippingAddress: " + mParamsEditShippingAddress.toString());

                        }
                        break;
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
                    Logger.d(TAG, "APIResponse(DoUserAddEditShippingAddressTask): onResponse-server = " + result.toString());
                    APIResponse<ShippingAddress> data = (APIResponse<ShippingAddress>) result.body();
                    Logger.d("AppUser>>", data.toString());

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(DoUserAddEditShippingAddressTask()): onResponse-object = " + data.toString());

                        if (mShippingType != null) {
                            Logger.d("mShippingType", mShippingType);

                            if (data.getData() != null) {
                                SessionUtil.setUserShippingAddress(getActivity(), APIResponse.getResponseString(data.getData()));
                                Logger.d("getData>>", data.getData().toString());
                            }
                            switch (mShippingType) {
                                case ADD_TO_SHIPPING:
                                    Intent iAddShippingAddress = new Intent();
                                    iAddShippingAddress.putExtra(INTENT_KEY_ORDER_SHIPPING_TYPE, ShippingType.ADD_TO_SHIPPING.name());
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                                    setResult(RESULT_OK, iAddShippingAddress);
                                    finish();
                                    break;
                                case EDIT_TO_SHIPPING:
                                    Intent iEditShippingAddress = new Intent();
                                    iEditShippingAddress.putExtra(INTENT_KEY_ORDER_SHIPPING_TYPE, ShippingType.EDIT_TO_SHIPPING.name());
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                                    setResult(RESULT_OK, iEditShippingAddress);
                                    finish();
                                    break;
                            }
                            Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_SHORT).show();
                        }

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