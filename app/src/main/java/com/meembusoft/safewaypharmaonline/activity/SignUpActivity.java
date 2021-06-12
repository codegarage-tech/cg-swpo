package com.meembusoft.safewaypharmaonline.activity;

import android.annotation.SuppressLint;
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

import com.jaeger.library.StatusBarUtil;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.base.BaseActivity;
import com.meembusoft.safewaypharmaonline.enumeration.LoginSettingType;
import com.meembusoft.safewaypharmaonline.model.AppUser;
import com.meembusoft.safewaypharmaonline.model.ParamsUserRegistration;
import com.meembusoft.safewaypharmaonline.retrofit.APIClient;
import com.meembusoft.safewaypharmaonline.retrofit.APIInterface;
import com.meembusoft.safewaypharmaonline.retrofit.APIResponse;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.view.CanaroTextView;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.util.AllSettingsManager;

import retrofit2.Call;
import retrofit2.Response;

import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_LOGIN_SETTING_TYPE;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SignUpActivity extends BaseActivity {

    //Toolbar
    private TextView tvLogin;
    private CanaroTextView tvTitle;

    private EditText etFullName,etMobileNumber,etPassword;
    private Button btnRegSubmit;
    private ImageView ivBack;
    private LinearLayout linLeftBack,linSignUpNext;


    //Background task
    private APIInterface mApiInterface;
    private DoUserRegistrationTask doUserRegistrationTask;

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }


    @Override
    public int initActivityLayout() {
        return R.layout.activity_sign_up;
    }

    @Override
    public void initStatusBarView() {
        StatusBarUtil.setTransparent(getActivity());
    }

    @Override
    public void initNavigationBarView() {

    }

    @Override
    public void initIntentData(Bundle savedInstanceState, Intent intent) {

//        Parcelable mParcelable = intent.getParcelableExtra(AllConstants.INTENT_KEY_VIDEO_LIST);
//        if (mParcelable != null) {
//            videosList = Parcels.unwrap(mParcelable);
//            Logger.d(TAG, TAG + " >>> " + "videosList: " + videosList.toString());
//        }
    }

    @Override
    public void initActivityViews() {
        ivBack = (ImageView)findViewById(R.id.iv_back);
        tvTitle = (CanaroTextView) findViewById(R.id.text_title);
        tvTitle.setText(getActivity().getResources().getString(R.string.title_sign_up));
        tvLogin = (TextView) findViewById(R.id.tv_login);
        etFullName = (EditText) findViewById(R.id.et_full_name);
        etMobileNumber = (EditText) findViewById(R.id.et_mobile_no);
        etPassword= (EditText) findViewById(R.id.et_password);
        //btnRegSubmit= (Button) findViewById(R.id.btn_reg_submit);
        linLeftBack = (LinearLayout) findViewById(R.id.ll_left_back);
        linSignUpNext = (LinearLayout) findViewById(R.id.lin_sign_up_next);
        linLeftBack.setVisibility(View.GONE);

    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
            // loadOfflineTimeData();
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

        tvLogin.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intentReg = new Intent(getActivity(), LoginActivity.class);
                startActivity(intentReg);
                finish();
            }
        });

        linSignUpNext.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (IsValidationInfo()){
                    //Registration AppUser
                    ParamsUserRegistration paramUserRegistration = new ParamsUserRegistration(etFullName.getText().toString(), etMobileNumber.getText().toString(), etPassword.getText().toString());
                    doUserRegistrationTask = new DoUserRegistrationTask(getActivity(), paramUserRegistration);
                    doUserRegistrationTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        });
    }

    private boolean IsValidationInfo(){
        String fullName = etFullName.getText().toString();
        String mobileNum = etMobileNumber.getText().toString();
        String password = etPassword.getText().toString();
        if (AllSettingsManager.isNullOrEmpty(fullName)){
            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_fullname), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (AllSettingsManager.isNullOrEmpty(mobileNum)){
            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_mobile), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (AllSettingsManager.isNullOrEmpty(password)){
            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_password), Toast.LENGTH_SHORT).show();
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

        if (doUserRegistrationTask != null && doUserRegistrationTask.getStatus() == AsyncTask.Status.RUNNING) {
            doUserRegistrationTask.cancel(true);
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

                if (result != null ) {
                    Logger.d(TAG, "APIResponse(DoUserRegistrationTask): onResponse-server = " + result.toString());
                    APIResponse<AppUser> data = (APIResponse<AppUser>) result.body();
                    Logger.d("AppUserRegistration", data.toString());

                    if (data != null) {
                        Logger.d(TAG, "APIResponse(DoUserRegistrationTask()): onResponse-object = " + data.toString());

                        if (data.getStatus().equalsIgnoreCase("1")) {
                            Logger.d(TAG, "APIResponse(DoUserRegistrationTask()): onResponse-mAppUser = " + data.toString());
                            Toast.makeText(getActivity(), getResources().getString(R.string.title_register_successful), Toast.LENGTH_LONG).show();
                            Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
                            intentLogin.putExtra(INTENT_KEY_LOGIN_SETTING_TYPE, LoginSettingType.OTHERS.name());
                            startActivity(intentLogin);
                            finish();
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



}