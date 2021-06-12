package com.meembusoft.safewaypharmaonline.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.developers.imagezipper.ImageZipper;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.adapter.ShippingAddressListAdapter;
import com.meembusoft.safewaypharmaonline.base.BaseActivity;
import com.meembusoft.safewaypharmaonline.base.BaseLocationActivity;
import com.meembusoft.safewaypharmaonline.enumeration.FlavorType;
import com.meembusoft.safewaypharmaonline.geocoding.LocationAddressListener;
import com.meembusoft.safewaypharmaonline.geocoding.ReverseGeocoderAddressTask;
import com.meembusoft.safewaypharmaonline.geocoding.UserLocationAddress;
import com.meembusoft.safewaypharmaonline.model.AppSupplier;
import com.meembusoft.safewaypharmaonline.model.AppUser;
import com.meembusoft.safewaypharmaonline.model.ParamsSupplierProfileUpdate;
import com.meembusoft.safewaypharmaonline.model.ParamsUserProfileUpdate;
import com.meembusoft.safewaypharmaonline.model.ShippingAddress;
import com.meembusoft.safewaypharmaonline.model.UserProfile;
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
import com.reversecoder.library.util.AllSettingsManager;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.meembusoft.safewaypharmaonline.util.SessionUtil.SESSION_KEY_USER_TAG;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class UserProfileActivity extends BaseLocationActivity {

    //Toolbar
    private ImageView ivBack, ivProfile;
    private LinearLayout llLeftBack, llUserViewProfile, llUserEditProfile, llEditUserProfileDoeAge, linEditProfileSupplierShop, linViewUserProfileDoeAge;
    private RelativeLayout rlEdit;
    private CanaroTextView tvTitle;
    private EditText etUserName, etUserEmail, etUserProfileDoe, etUserProfileAge,etUserProfileBlood, etSupplierShopName, etUserAddress, etUserPhone, etUserPassword;
    private TextView tvEdit, tvUserName, tvUserEmail, tvUserPhone, tvUserAddress, tvUserAge, tvUserDOE, tvUserGender,tvUserBlood, tvUserGenderTitle,tvUserBloodTitle;
    private Button btnEdit;
    private FrameLayout frameLayoutAvatar;
    private String name = "", phone = "", streetAddress = "",  blood = "", age = "", doe = "", password = "", email = "", state = "", city = "", zipcode = "", gender = "Male";
    private String mImagePath = "", mBase64 = "", userID = "", shopName = "";
    private RadioGroup rgProfileSex;
    private RadioButton rbMale, rbFemale;
    private ShippingAddressListAdapter addressListAdapter;
    private AppUser mAppUser;
    private AppSupplier mAppSupplier;

    //Background task
    private DoUserProfileUpdateTask doUserProfileUpdateTask;
    private GetProfileDetailsTask getProfileDetailsTask;
    private APIInterface mApiInterface;
    private boolean isSwipeRefreshTask = false;
    private FlavorType mFlavorType;
    private ShippingAddress mShippingAddress;
    ReverseGeocoderAddressTask currentLocationTask;
    Location mLocation;
    String mAddress = "";
    private double latitude = 0.0, longitude = 0.0;
    private boolean isEdit;


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
    public String[] initActivityPermissions() {
        return new String[]{Manifest.permission.CAMERA};
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_profile;
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
            String mParcelablemFlavorType = intent.getStringExtra(SESSION_KEY_USER_TAG);
            if (!AllSettingsManager.isNullOrEmpty(mParcelablemFlavorType)) {
                mFlavorType = FlavorType.valueOf(mParcelablemFlavorType);
                Logger.d(TAG, TAG + " >>> " + "mParcelablemFlavorType: " + mParcelablemFlavorType);
            }

        }
    }

    @Override
    public void initActivityViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (CanaroTextView) findViewById(R.id.text_title);
        tvTitle.setText(getActivity().getResources().getString(R.string.title_user_profile));
        rlEdit = (RelativeLayout) findViewById(R.id.rl_edit);
        frameLayoutAvatar = (FrameLayout) findViewById(R.id.frameLayout_avatar_edit);
        llLeftBack = (LinearLayout) findViewById(R.id.ll_left_back);
        llUserViewProfile = (LinearLayout) findViewById(R.id.lin_lay_user_profile_view_screen);
        llUserEditProfile = (LinearLayout) findViewById(R.id.lin_lay_user_edit_profile_view_screen);
        llEditUserProfileDoeAge = (LinearLayout) findViewById(R.id.lin_edit_user_profile_doe_age);
        linEditProfileSupplierShop = (LinearLayout) findViewById(R.id.lin_edit_profile_supplier_shop);
        linViewUserProfileDoeAge = (LinearLayout) findViewById(R.id.lin_view_user_profile_doe_age);
        tvEdit = (TextView) findViewById(R.id.tv_edit);
        tvUserName = (TextView) findViewById(R.id.txt_user_profile_name);
        tvUserEmail = (TextView) findViewById(R.id.txt_user_profile_email);
        tvUserAddress = (TextView) findViewById(R.id.txt_user_profile_address);
        tvUserPhone = (TextView) findViewById(R.id.txt_user_profile_phone);
        tvUserAge = (TextView) findViewById(R.id.txt_user_profile_age);
        tvUserDOE = (TextView) findViewById(R.id.txt_user_profile_doe);
        tvUserGender = (TextView) findViewById(R.id.txt_user_profile_gender);
        tvUserBlood = (TextView) findViewById(R.id.txt_user_profile_blood);
        tvUserGenderTitle = (TextView) findViewById(R.id.txt_user_profile_gender_title);
        tvUserBloodTitle = (TextView) findViewById(R.id.txt_user_profile_blood_title);
        etUserName = (EditText) findViewById(R.id.et_profile_name);
        etUserEmail = (EditText) findViewById(R.id.et_profile_email);
        etUserProfileDoe = (EditText) findViewById(R.id.et_profile_doe);
        etUserProfileAge = (EditText) findViewById(R.id.et_profile_age);
        etUserProfileBlood = (EditText) findViewById(R.id.et_profile_blood);
        etSupplierShopName = (EditText) findViewById(R.id.et_profile_supplier_shop_name);
        etUserAddress = (EditText) findViewById(R.id.et_profile_address);
        etUserPhone = (EditText) findViewById(R.id.et_profile_phone);
        etUserPassword = (EditText) findViewById(R.id.et_profile_password);
        rgProfileSex = (RadioGroup) findViewById(R.id.rg_profile_sex);
        rbMale = (RadioButton) findViewById(R.id.rb_profile_male);
        rbFemale = (RadioButton) findViewById(R.id.rb_profile_female);
        ivProfile = (ImageView) findViewById(R.id.iv_profile);
        btnEdit = (Button) findViewById(R.id.btn_profile_edit);
        // Logger.d("mAppUser", mAppUser.getId());

    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);

        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
        } else {

            if (mFlavorType != null) {
                switch (mFlavorType) {
                    case CUSTOMER:
                        tvUserGenderTitle.setText(getActivity().getResources().getString(R.string.title_user_profile_gender));

                        llEditUserProfileDoeAge.setVisibility(View.VISIBLE);
                        linEditProfileSupplierShop.setVisibility(View.GONE);
                        linViewUserProfileDoeAge.setVisibility(View.VISIBLE);
                        mAppUser = SessionUtil.getUser(getActivity());
                        if (mAppUser != null) {
                            userID = mAppUser.getId();
                        }
                        break;
                    case SUPPLIER:
                        tvUserGenderTitle.setText(getActivity().getResources().getString(R.string.title_user_profile_supplier_shop));
                        llEditUserProfileDoeAge.setVisibility(View.GONE);
                        linEditProfileSupplierShop.setVisibility(View.VISIBLE);
                        linEditProfileSupplierShop.setVisibility(View.VISIBLE);
                        tvUserBloodTitle.setVisibility(View.GONE);
                        tvUserBlood.setVisibility(View.GONE);
                        mAppSupplier = SessionUtil.getSupplier(getActivity());
                        if (mAppSupplier != null) {
                            userID = mAppSupplier.getUser_id();
                        }
                        break;
                }
                getProfileDetailsTask = new GetProfileDetailsTask(getActivity(), userID, mFlavorType);
                getProfileDetailsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }

    }

    private void setData() {
//        ivAddress.setVisibility(View.GONE);
//        if (mShippingAddress != null) {
//            etUserName.setText(AppUtil.optStringNullCheckValue(mShippingAddress.getName()));
//            etUserEmail.setText(AppUtil.optStringNullCheckValue(mShippingAddress.getName()));
//        }
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {

        llLeftBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        etUserProfileDoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtil.datePicker(etUserProfileDoe.getText().toString(), etUserProfileDoe,true, getActivity());

//                String dateValue = AppUtil.formatDateFromString("dd-MM-yyyy", "yyyy-MM-dd", AppUtil.optStringNullCheckValue(etUserProfileDoe.getText().toString()));
//                etUserProfileDoe.setText(dateValue);
            }
        });


        rgProfileSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_profile_male:
                        gender = AllConstants.GENDER_MALE;
                        break;
                    case R.id.rb_profile_female:
                        gender = AllConstants.GENDER_FEMALE;
                        Logger.d(TAG, TAG + "> gender" + gender);
                        break;
                }
            }
        });

        rlEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEdit) {

                    llUserViewProfile.setVisibility(View.GONE);
                    llUserEditProfile.setVisibility(View.VISIBLE);
                    isEdit = true;
                    //    editableTextShowBasic();
                    tvEdit.setText(getActivity().getResources().getString(R.string.title_user_profile_cancel));

                } else {

                    llUserViewProfile.setVisibility(View.VISIBLE);
                    llUserEditProfile.setVisibility(View.GONE);
                    isEdit = false;
                    tvEdit.setText(getActivity().getResources().getString(R.string.title_user_profile_edit));

                }
            }
        });

        btnEdit.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (!NetworkManager.isConnected(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                } else {
                    if (IsValidationInfo()) {


                        if (mFlavorType != null) {
                            switch (mFlavorType) {
                                case CUSTOMER:
                                    doUserProfileUpdateTask = new DoUserProfileUpdateTask(getActivity(), mFlavorType);
                                    doUserProfileUpdateTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                    break;
                                case SUPPLIER:
                                    mAddress = streetAddress ;
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
                                                    doUserProfileUpdateTask = new DoUserProfileUpdateTask(getActivity(), mFlavorType);
                                                    doUserProfileUpdateTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                                } else {
                                                    Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_valid_address), Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });
                                        currentLocationTask.execute(mLocation);
                                    }
                                    break;
                            }
                        }
                    }
                }
            }
        });

        frameLayoutAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPicture();
            }
        });
    }


    private void showPicture() {
        Matisse.from(getActivity())
                .choose(MimeType.ofImage())
                .theme(R.style.Matisse_Dracula)
                .capture(true)
                .setDefaultCaptureStrategy()
                .countable(false)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .maxSelectable(1)
                .maxSelectable(1)
                .imageEngine(new GlideEngine())
                .forResult(AllConstants.INTENT_REQUEST_CODE_IMAGE_PICKER);
    }

    private void initProfileData(UserProfile userProfile) {
        SessionUtil.setUserProfile(getActivity(), APIResponse.getResponseString(userProfile));

        if (userProfile != null) {

            tvUserName.setText(AppUtil.optStringNullCheckValue(userProfile.getName()));
            etUserName.setText(AppUtil.optStringNullCheckValue(userProfile.getName()));
            tvUserEmail.setText(AppUtil.optStringNullCheckValue(userProfile.getEmail()));
            etUserEmail.setText(AppUtil.optStringNullCheckValue(userProfile.getEmail()));
            tvUserPhone.setText(AppUtil.optStringNullCheckValue(userProfile.getPhone()));
            etUserPhone.setText(AppUtil.optStringNullCheckValue(userProfile.getPhone()));
            tvUserAddress.setText(AppUtil.optStringNullCheckValue(userProfile.getAddress()));
            etUserAddress.setText(AppUtil.optStringNullCheckValue(userProfile.getAddress()));

            if (mFlavorType != null) {
                switch (mFlavorType) {
                    case CUSTOMER:
                        llEditUserProfileDoeAge.setVisibility(View.VISIBLE);
                        linEditProfileSupplierShop.setVisibility(View.GONE);
                        linViewUserProfileDoeAge.setVisibility(View.VISIBLE);
                        tvUserAge.setText(AppUtil.optStringNullCheckValue(userProfile.getAge()));
                        tvUserBlood.setText(AppUtil.optStringNullCheckValue(userProfile.getBlood()));
                        etUserProfileAge.setText(AppUtil.optStringNullCheckValue(userProfile.getAge()));
                        String dateOfBirthDate = AppUtil.formatDateFromString("yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd", AppUtil.optStringNullCheckValue(userProfile.getDateofbirth()));

                        tvUserDOE.setText(AppUtil.optStringNullCheckValue(dateOfBirthDate));
                        etUserProfileDoe.setText(AppUtil.optStringNullCheckValue(dateOfBirthDate));
                        tvUserGender.setText(AppUtil.optStringNullCheckValue(userProfile.getGender()));
                        if (userProfile.getGender().equalsIgnoreCase(AllConstants.GENDER_MALE)) {
                            rgProfileSex.check(R.id.rb_profile_male);
                        } else if (userProfile.getGender().equalsIgnoreCase(AllConstants.GENDER_FEMALE)) {
                            rgProfileSex.check(R.id.rb_profile_female);
                        }
                        AppUtil.loadImage(getActivity(), ivProfile, userProfile.getThumb_photo(), false, true, false);

                        break;
                    case SUPPLIER:
                        llEditUserProfileDoeAge.setVisibility(View.GONE);
                        linEditProfileSupplierShop.setVisibility(View.VISIBLE);
                        linViewUserProfileDoeAge.setVisibility(View.GONE);
                        tvUserBloodTitle.setVisibility(View.GONE);
                        tvUserBlood.setVisibility(View.GONE);
                        tvUserGender.setText(AppUtil.optStringNullCheckValue(userProfile.getShop_name()));
                        etSupplierShopName.setText(AppUtil.optStringNullCheckValue(userProfile.getShop_name()));
                        etUserProfileBlood.setText(AppUtil.optStringNullCheckValue(userProfile.getBlood()));
                        AppUtil.loadImage(getActivity(), ivProfile, userProfile.getLogo(), false, true, false);

                        break;
                }
            }


        }
    }

    private boolean IsValidationInfo() {

        name = etUserName.getText().toString();
        email = etUserEmail.getText().toString();
        phone = etUserPhone.getText().toString();
        streetAddress = etUserAddress.getText().toString();
        doe = etUserProfileDoe.getText().toString();
        age = etUserProfileAge.getText().toString();
        blood = etUserProfileBlood.getText().toString();
        password = etUserPassword.getText().toString();
        shopName = etSupplierShopName.getText().toString();

        if (AllSettingsManager.isNullOrEmpty(name)) {
            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_name), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (AllSettingsManager.isNullOrEmpty(email)) {
            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_email), Toast.LENGTH_SHORT).show();
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

        if (AllSettingsManager.isNullOrEmpty(password)) {
            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_password), Toast.LENGTH_SHORT).show();
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
        if (requestCode == AllConstants.INTENT_REQUEST_CODE_IMAGE_PICKER && resultCode == RESULT_OK) {
            List<String> mData = Matisse.obtainPathResult(data);

            if (mData.size() == 1) {
                mImagePath = mData.get(0);
                Logger.d(TAG, "MatisseImage: " + mImagePath);

                AppUtil.loadImage(getActivity(), ivProfile, mImagePath, false, true, false);

                try {
                    File imageZipperFile = new ImageZipper(getActivity())
                            .setQuality(100)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .compressToFile(new File(mImagePath));
                    mBase64 = ImageZipper.getBase64forImage(imageZipperFile);
                    Logger.d(TAG, "MatisseImage(mBase64): " + mBase64);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
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
        dismissPopupDialog();

        if (getProfileDetailsTask != null && getProfileDetailsTask.getStatus() == AsyncTask.Status.RUNNING) {
            getProfileDetailsTask.cancel(true);
        }

        if (doUserProfileUpdateTask != null && doUserProfileUpdateTask.getStatus() == AsyncTask.Status.RUNNING) {
            doUserProfileUpdateTask.cancel(true);
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

    private class GetProfileDetailsTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        String userID;
        FlavorType mFlavorType;

        private GetProfileDetailsTask(Context context, String userId, FlavorType flavorType) {
            mContext = context;
            userID = userId;
            mFlavorType = flavorType;
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
                Call<APIResponse<UserProfile>> call = null;
                switch (mFlavorType) {
                    case CUSTOMER:
                        call = mApiInterface.apiGetUserProfile(userID);
                        break;
                    case SUPPLIER:
                        call = mApiInterface.apiGetSupplierProfile(userID);
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

                if (result != null && result.isSuccessful()) {
                    Logger.d(TAG, "APIResponse(GetProfileDetailsTask): onResponse-server = " + result.toString());
                    APIResponse<UserProfile> data = (APIResponse<UserProfile>) result.body();
                    Logger.d("GetProfileDetailsTask", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(GetProfileDetailsTask()): onResponse-object = " + data.toString());
                        initProfileData(data.getData());
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


    private class DoUserProfileUpdateTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        FlavorType mFlavorType;

        public DoUserProfileUpdateTask(Context context, FlavorType flavorType) {
            mContext = context;
            mFlavorType = flavorType;
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

                Call<APIResponse<UserProfile>> call = null;
                Logger.d("<<<mFlavorType", mFlavorType);

                switch (mFlavorType) {
                    case CUSTOMER:
                        if (mAppUser != null && (!AllSettingsManager.isNullOrEmpty(mAppUser.getId()))) {
                            ParamsUserProfileUpdate mParamsUserProfileUpdate = new ParamsUserProfileUpdate(mAppUser.getId(), name, phone, email, streetAddress, age, gender, doe,blood, password, mBase64);
                            call = mApiInterface.apiUserProfileUpdate(mParamsUserProfileUpdate);
                            Logger.d(TAG, TAG + " >>> " + "mParamsUserProfileUpdate: " + mParamsUserProfileUpdate.toString());
                        }
                        break;
                    case SUPPLIER:
                        if (mAppSupplier != null && (!AllSettingsManager.isNullOrEmpty(mAppSupplier.getUser_id()))) {
                            ParamsSupplierProfileUpdate mParamsSupplierProfileUpdate = new ParamsSupplierProfileUpdate(mAppSupplier.getUser_id(), name, phone, email, shopName, streetAddress, longitude + "", latitude + "", password, mBase64);
                            call = mApiInterface.apiSupplierProfileUpdate(mParamsSupplierProfileUpdate);
                            Logger.d(TAG, TAG + " >>> " + "mParamsSupplierProfileUpdate: " + mParamsSupplierProfileUpdate.toString());
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
                    APIResponse<UserProfile> data = (APIResponse<UserProfile>) result.body();
                    Logger.d("AppUser>>", data.toString());

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(DoUserAddEditShippingAddressTask()): onResponse-object = " + data.toString());

                        if (mFlavorType != null) {
                            Logger.d("mFlavorType", mFlavorType);
                            llUserViewProfile.setVisibility(View.VISIBLE);
                            llUserEditProfile.setVisibility(View.GONE);
                            isEdit = false;
                            tvEdit.setText(getActivity().getResources().getString(R.string.title_user_profile_edit));
                            initProfileData(data.getData());
//                            switch (mFlavorType) {
//                                case CUSTOMER:
//                                    isEdit = false;
//                                    tvEdit.setText(getActivity().getResources().getString(R.string.title_user_profile_edit));
//                                    initProfileData(data.getData());
//                                    break;
//                                case SUPPLIER:
////                                    Intent iEditShippingAddress = new Intent();
////                                    iEditShippingAddress.putExtra(INTENT_KEY_ORDER_SHIPPING_TYPE, ShippingType.EDIT_TO_SHIPPING.name());
////                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
////                                    setResult(RESULT_OK, iEditShippingAddress);
////                                    finish();
//                                    break;
//                            }
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