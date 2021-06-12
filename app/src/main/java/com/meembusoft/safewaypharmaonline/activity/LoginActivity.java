package com.meembusoft.safewaypharmaonline.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.fxn.BubbleTabBar;
import com.fxn.OnBubbleClickListener;
import com.jaeger.library.StatusBarUtil;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.base.BaseActivity;
import com.meembusoft.safewaypharmaonline.enumeration.FlavorType;
import com.meembusoft.safewaypharmaonline.enumeration.LoginSettingType;
import com.meembusoft.safewaypharmaonline.model.AppSupplier;
import com.meembusoft.safewaypharmaonline.model.AppUser;
import com.meembusoft.safewaypharmaonline.model.ParamsSupplierLogin;
import com.meembusoft.safewaypharmaonline.model.ParamsUserLogin;
import com.meembusoft.safewaypharmaonline.model.ParamsUserRegistration;
import com.meembusoft.safewaypharmaonline.model.Suppliers;
import com.meembusoft.safewaypharmaonline.model.SystemSetting;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_LOGIN_SETTING_TYPE;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class LoginActivity extends BaseActivity {

    //Toolbar
    private ImageView ivBack, ivSupplierUser,ivPassword,ivCross;
    private CanaroTextView tvTitle;
    private TextView tvRegister;
    private EditText etMobileNumber, etSupplierUsername, etPassword;
    private Button btnLoginSubmit;
    private LinearLayout linLeftBack, linLoginNext,linUserName,linUserPass, linSignUp;
    private CardView cvUserMobileNo, cvSupplierUserame;
    private BubbleTabBar bubbleTabBar;

    //Background task
    private DoUserLoginTask doUserLoginTask;
    private DoSupplierLoginTask doSupplierLoginTask;
    private DoUserRegistrationTask doUserRegistrationTask;
    private GetSystemSettingTask getSystemSettingTask;
    private GetSupplierTask getSupplierTask;
    private APIInterface mApiInterface;

    LoginSettingType loginSettingType;
    private RequestQueue requestQueue;
    String userTag = "";

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initStatusBarView() {
        StatusBarUtil.setColorNoTranslucent(this, getResources().getColor(R.color.colorPrimaryDark));
      //  StatusBarUtil.setTransparent(LoginActivity.this);
    }

    @Override
    public void initNavigationBarView() {

    }

    @Override
    public void initIntentData(Bundle savedInstanceState, Intent intent) {
        if (intent != null) {
            String mParcelableLoginSettingType = intent.getStringExtra(AllConstants.INTENT_KEY_LOGIN_SETTING_TYPE);
            if (!AllSettingsManager.isNullOrEmpty(mParcelableLoginSettingType)) {
                loginSettingType = LoginSettingType.valueOf(mParcelableLoginSettingType);
                Logger.e("loginSettingType>>", loginSettingType + "loginSettingType");
                Logger.d(TAG, TAG + " >>> " + "loginSettingType: " + loginSettingType);
            }

        }
    }

    @Override
    public void initActivityViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivSupplierUser = (ImageView) findViewById(R.id.iv_supplier_username);
        ivPassword = (ImageView) findViewById(R.id.iv_password);
        tvTitle = (CanaroTextView) findViewById(R.id.text_title);
        tvTitle.setText(getActivity().getResources().getString(R.string.title_login));
        tvRegister = (TextView) findViewById(R.id.tv_register);
        etMobileNumber = (EditText) findViewById(R.id.et_mobile_no);
        etSupplierUsername = (EditText) findViewById(R.id.et_supplier_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        // btnLoginSubmit = (Button) findViewById(R.id.btn_login_submit);
        // linUserMobileNo = (LinearLayout) findViewById(R.id.lin_user_mobile_no);
        linLeftBack = (LinearLayout) findViewById(R.id.ll_left_back);
        linLoginNext = (LinearLayout) findViewById(R.id.lin_login_next);
        linSignUp = (LinearLayout) findViewById(R.id.lin_sign_up);
        linUserName = (LinearLayout) findViewById(R.id.lin_supplier_username);
        linUserPass = (LinearLayout) findViewById(R.id.lin_user_pass);
        cvUserMobileNo = (CardView) findViewById(R.id.cardview_user_mobile_no);
        cvSupplierUserame = (CardView) findViewById(R.id.cardview_supplier_username);
        bubbleTabBar = (BubbleTabBar) findViewById(R.id.bubble_tabbar);

        userTag = AllConstants.userType[0];
        linLeftBack.setVisibility(View.GONE);
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        requestQueue = Volley.newRequestQueue(this);

        mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);

        if (AppUtil.isDebug(getActivity())) {
            etMobileNumber.setText("01673302251");
            etPassword.setText("123456");
        }
        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
        } else {

//            //Login AppUser
//            ParamsUserLogin paramUserLogin = new ParamsUserLogin("01714524525", "123456");
//            Logger.d(TAG, TAG + " >>> " + "paramUserLogin: " + paramUserLogin.toString());
//            doUserLoginTask = new DoUserLoginTask(getActivity(), paramUserLogin);
//            doUserLoginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

//            //Registration AppUser
//            ParamsUserRegistration paramUserRegistration = new ParamsUserRegistration("Pharmacy", "01627809666", "123456");
//            doUserRegistrationTask = new DoUserRegistrationTask(getActivity(), paramUserRegistration);
//            doUserRegistrationTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

//            getSystemSettingTask = new GetSystemSettingTask(getActivity());
//            getSystemSettingTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//            getSupplierTask  = new GetSupplierTask(getActivity(),"");
//            getSupplierTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//            getAllProductMedicinesTask = new GetAllProductMedicinesTask(getActivity(),"");
//            getAllProductMedicinesTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//            getMedicineSearchTask = new GetMedicineSearchTask(getActivity(),"");
//            getMedicineSearchTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }


    @Override
    public void initActivityActions(Bundle savedInstanceState) {

        linLeftBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });


        bubbleTabBar.addBubbleListener(new OnBubbleClickListener() {
            @Override
            public void onBubbleClick(int id) {
                switch (id) {
                    case R.id.user_customer:
                        userTag = AllConstants.userType[0];
                        cvUserMobileNo.setVisibility(View.VISIBLE);
                        cvSupplierUserame.setVisibility(View.GONE);
                        linSignUp.setVisibility(View.VISIBLE);
                        etPassword.requestFocus();
                        linUserName.setBackgroundResource(R.drawable.shape_rectangle_border_white_bg_shade);
                        linUserPass.setBackgroundResource(R.drawable.shape_rectangle_border_white_bg_shade);
                        ivSupplierUser.setBackgroundTintList(getResources().getColorStateList( R.color.colorPrimaryDark));
                        ivPassword.setBackgroundTintList(getResources().getColorStateList( R.color.colorPrimaryDark));

                        if (AppUtil.isDebug(getActivity())) {
                            etMobileNumber.setText("01673302251");
                            etPassword.setText("123456");
                        }
                        break;
                    case R.id.user_supplier:
                        userTag = AllConstants.userType[1];
                        cvUserMobileNo.setVisibility(View.GONE);
                        cvSupplierUserame.setVisibility(View.VISIBLE);
                        linSignUp.setVisibility(View.GONE);
                        linUserName.setBackgroundResource(R.drawable.shape_rectangle_border_grey_bg_white);
                        linUserPass.setBackgroundResource(R.drawable.shape_rectangle_border_grey_bg_white);
                        ivSupplierUser.setBackgroundTintList(getResources().getColorStateList( R.color.color_grey_500));
                        ivPassword.setBackgroundTintList(getResources().getColorStateList( R.color.color_grey_500));

                        etPassword.requestFocus();
                        if (AppUtil.isDebug(getActivity())) {
                           //etSupplierUsername.setText("razzak@gmail.com");
                           etSupplierUsername.setText("alamin@gmail.com");
//                            etSupplierUsername.setText("sina@gmail.com");
                          // etPassword.setText("098765");
                           etPassword.setText("1234");
                        }
                        break;
                }
            }
        });

        linLoginNext.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                if (IsValidationInfo()) {
                    //Login AppUser
                    if (userTag.equalsIgnoreCase(AllConstants.userType[0])) {
                        ParamsUserLogin paramUserLogin = new ParamsUserLogin(etMobileNumber.getText().toString(), etPassword.getText().toString());
                        Logger.d(TAG, TAG + " >>> " + "paramUserLogin: " + paramUserLogin.toString());
                        doUserLoginTask = new DoUserLoginTask(getActivity(), paramUserLogin);
                        doUserLoginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else if (userTag.equalsIgnoreCase(AllConstants.userType[1])) {
                        ParamsSupplierLogin paramsSupplierLogin = new ParamsSupplierLogin(etSupplierUsername.getText().toString(), etPassword.getText().toString());
                        Logger.d(TAG, TAG + " >>> " + "paramsSupplierLogin: " + paramsSupplierLogin.toString());
                        doSupplierLoginTask = new DoSupplierLoginTask(getActivity(), paramsSupplierLogin);
                        doSupplierLoginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
            }
        });

        tvRegister.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intentReg = new Intent(getActivity(), SignUpActivity.class);
                startActivity(intentReg);
                finish();
            }
        });
    }

    private boolean IsValidationInfo() {
        String mobileNum = etMobileNumber.getText().toString();
        String username = etSupplierUsername.getText().toString();
        String password = etPassword.getText().toString();
        if (userTag.equalsIgnoreCase(AllConstants.userType[0])) {
            if (AllSettingsManager.isNullOrEmpty(mobileNum)) {
                Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_mobile), Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (userTag.equalsIgnoreCase(AllConstants.userType[1])) {
            if (AllSettingsManager.isNullOrEmpty(username)) {
                Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_username), Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (AllSettingsManager.isNullOrEmpty(password)) {
            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_password), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (AllSettingsManager.isNullOrEmpty(userTag)) {
            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_user), Toast.LENGTH_SHORT).show();
            return false;
        }
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

        if (doUserLoginTask != null && doUserLoginTask.getStatus() == AsyncTask.Status.RUNNING) {
            doUserLoginTask.cancel(true);
        }

        if (doSupplierLoginTask != null && doSupplierLoginTask.getStatus() == AsyncTask.Status.RUNNING) {
            doSupplierLoginTask.cancel(true);
        }

        if (doUserRegistrationTask != null && doUserRegistrationTask.getStatus() == AsyncTask.Status.RUNNING) {
            doUserRegistrationTask.cancel(true);
        }

        if (getSystemSettingTask != null && getSystemSettingTask.getStatus() == AsyncTask.Status.RUNNING) {
            getSystemSettingTask.cancel(true);
        }

        if (getSupplierTask != null && getSupplierTask.getStatus() == AsyncTask.Status.RUNNING) {
            getSupplierTask.cancel(true);
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

    private class DoUserLoginTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        ParamsUserLogin mParamUserLogin;

        public DoUserLoginTask(Context context, ParamsUserLogin paramUserLogin) {
            mContext = context;
            mParamUserLogin = paramUserLogin;
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
                Logger.d(TAG, TAG + " >>> " + "mParamUserLogin: " + mParamUserLogin.toString());
                Call<APIResponse> call = mApiInterface.apiUserLogin(mParamUserLogin);

                Response response = call.execute();
                Logger.d(TAG, TAG + " >>> " + "LoginResponse: " + response);
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
                    Logger.d(TAG, "APIResponse(DoUserLoginTask): onResponse-server = " + result.toString());
                    APIResponse data = (APIResponse) result.body();

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(DoUserLoginTask()): onResponse-object = " + data.toString());
                        //Set user
                        SessionManager.setStringSetting(getActivity(), SessionUtil.SESSION_KEY_USER_TAG, FlavorType.CUSTOMER.toString());
                        SessionUtil.setUser(getActivity(), APIResponse.getResponseString(data.getData()));
                        AppUser mAppUser = SessionUtil.getUser(getActivity());
                        Logger.d("AppUser>>", data.toString());

                        if (mAppUser.getShiping_address() != null) {
                            SessionUtil.setUserShippingAddress(getActivity(), APIResponse.getResponseString(mAppUser.getShiping_address()));
                        } else {
                            SessionUtil.setUserShippingAddress(getActivity(), "");
                        }
                        SessionUtil.setSupplier(getActivity(), "null");

                        if (loginSettingType != null) {

                            switch (loginSettingType) {
                                case HOME_TO_LOGIN:
                                    Intent iHomeToLogin = new Intent();
                                    iHomeToLogin.putExtra(INTENT_KEY_LOGIN_SETTING_TYPE, LoginSettingType.HOME_TO_LOGIN.name());
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                                    setResult(RESULT_OK, iHomeToLogin);
                                    finish();
                                    break;
                                case MENU_TO_LOGIN:
                                    Intent iMenuToLogin = new Intent();
                                    iMenuToLogin.putExtra(INTENT_KEY_LOGIN_SETTING_TYPE, LoginSettingType.MENU_TO_LOGIN.name());
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                                    setResult(RESULT_OK, iMenuToLogin);
                                    finish();
                                    break;

                                case ORDER_CART_TO_LOGIN:
                                    Intent iOrderToLogin = new Intent();
                                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                                    setResult(RESULT_OK, iOrderToLogin);
                                    finish();
                                    break;

                                case DETAILS_TO_LOGIN:
                                    Intent intentMyOrder = new Intent(getActivity(), MyOrderActivity.class);
                                    startActivity(intentMyOrder);
                                    finish();
                                    break;

                                case OTHERS:
                                    Intent intentHome = new Intent(getActivity(), HomeActivity.class);
                                    intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intentHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intentHome);
                                    finish();
                                    break;
                            }
                            Toast.makeText(getActivity(), getResources().getString(R.string.title_login_successful), Toast.LENGTH_LONG).show();

                        } else {
                            Intent intentHome = new Intent(getActivity(), HomeActivity.class);
                            intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intentHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intentHome);
                            finish();
                            Toast.makeText(getActivity(), getResources().getString(R.string.title_login_successful), Toast.LENGTH_LONG).show();
                        }
//
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private class DoSupplierLoginTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        ParamsSupplierLogin mParamsSupplierLogin;

        public DoSupplierLoginTask(Context context, ParamsSupplierLogin paramSupplierLogin) {
            mContext = context;
            mParamsSupplierLogin = paramSupplierLogin;
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
                Logger.d(TAG, TAG + " >>> " + "mParamsSupplierLogin: " + mParamsSupplierLogin.toString());
                Call<APIResponse> call = mApiInterface.apiSupplierUserLogin(mParamsSupplierLogin);

                Response response = call.execute();
                Logger.d(TAG, TAG + " >>> " + "LoginResponse: " + response);
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
                    Logger.d(TAG, "APIResponse(DoSupplierLoginTask): onResponse-server = " + result.toString());
                    APIResponse data = (APIResponse) result.body();

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(DoSupplierLoginTask()): onResponse-object = " + data.toString());
                        SessionManager.setStringSetting(getActivity(), SessionUtil.SESSION_KEY_USER_TAG, FlavorType.SUPPLIER.toString());
                        SessionUtil.setSupplier(getActivity(), APIResponse.getResponseString(data.getData()));
                        AppSupplier mAppSupplier = SessionUtil.getSupplier(getActivity());
                        if (mAppSupplier.getShiping_address() != null) {
                            SessionUtil.setUserShippingAddress(getActivity(), APIResponse.getResponseString(mAppSupplier.getShiping_address()));
                        } else {
                            SessionUtil.setUserShippingAddress(getActivity(), "");
                        }

                        SessionUtil.setUser(getActivity(), "null");
                        Intent iSupplierActivity = new Intent(getActivity(), HomeActivity.class);
                        iSupplierActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        iSupplierActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        iSupplierActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(iSupplierActivity);

                        finish();
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

    private class DoUserRegistrationTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        ParamsUserRegistration mParamUserReg;

        public DoUserRegistrationTask(Context context, ParamsUserRegistration paramUserReg) {
            mContext = context;
            mParamUserReg = paramUserReg;
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
                Logger.d(TAG, TAG + " >>> " + "mParamUserRegistration: " + mParamUserReg.toString());
                Call<APIResponse> call = mApiInterface.apiUserRegistration(mParamUserReg);

                Response response = call.execute();
                Logger.d(TAG, TAG + " >>> " + "response: " + response);
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
                    Logger.d(TAG, "APIResponse(DoUserRegistrationTask): onResponse-server = " + result.toString());
                    APIResponse<AppUser> data = (APIResponse<AppUser>) result.body();
                    Logger.d("AppUserRegistration", data.toString());

                    if (data != null) {
                        Logger.d(TAG, "APIResponse(DoUserRegistrationTask()): onResponse-object = " + data.toString());

                        if (data.getStatus().equalsIgnoreCase("1")) {
                            Logger.d(TAG, "APIResponse(DoUserRegistrationTask()): onResponse-mAppUser = " + data.toString());

                        } else {
                            Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }


    private class GetSupplierTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        String categoryID;

        private GetSupplierTask(Context context, String categoryId) {
            mContext = context;
            categoryID = categoryId;

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
                Call<APIResponse<List<Suppliers>>> call = mApiInterface.apiGetSuppliersList("0", "10");
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
                    Logger.d(TAG, "APIResponse(GetSupplierTask): onResponse-server = " + result.toString());
                    APIResponse<List<Suppliers>> data = (APIResponse<List<Suppliers>>) result.body();
                    Logger.d("GetSupplierTask", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(GetSupplierTask()): onResponse-object = " + data.toString());


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

    private class GetSystemSettingTask extends AsyncTask<String, Integer, Response> {

        Context mContext;

        private GetSystemSettingTask(Context context) {
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
                Call<APIResponse<List<SystemSetting>>> call = mApiInterface.apiGetSystemSettingList();
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
                    Logger.d(TAG, "APIResponse(GetSystemSettingTask): onResponse-server = " + result.toString());
                    APIResponse<List<SystemSetting>> data = (APIResponse<List<SystemSetting>>) result.body();
                    Logger.d("GetSystemSettingTask", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(GetSystemSettingTask()): onResponse-object = " + data.toString());


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


}