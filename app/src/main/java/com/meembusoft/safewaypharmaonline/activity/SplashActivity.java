package com.meembusoft.safewaypharmaonline.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.enumeration.FlavorType;
import com.meembusoft.safewaypharmaonline.fcm.fcmutils.FcmManager;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.util.SessionUtil;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SplashActivity extends AppCompatActivity {
    private static String TAG = AppUtil.class.getSimpleName();

    private TextView tvAppVersion;
    private ImageView ivAppLogo, ivAppLogoFlavor;
    FlavorType flavorType;
    private SplashCountDownTimer splashCountDownTimer;
    private final long splashTime = 3 * 1000;
    private final long interval = 1 * 1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initStatusBar();
        initActivityViews();
        initActivityViewsData();
    }

    private void initStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
        StatusBarUtil.setTransparent(SplashActivity.this);
    }

    public void initActivityViews() {
        tvAppVersion = (TextView) findViewById(R.id.tv_app_version);
        ivAppLogo = (ImageView) findViewById(R.id.iv_app_logo);
    }

    public void initActivityViewsData() {
        //Set app version
        String appVersion = AppUtil.getAppVersion(SplashActivity.this);
        if (!AllSettingsManager.isNullOrEmpty(appVersion)) {
            tvAppVersion.setText("Version: " + appVersion);
        }

        splashCountDownTimer = new SplashCountDownTimer(splashTime, interval);
        splashCountDownTimer.start();
    }

    public class SplashCountDownTimer extends CountDownTimer {
        public SplashCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            navigateNextScreen();
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }
    }

    private void navigateNextScreen() {

        try {

            String userType = SessionManager.getStringSetting(getApplicationContext(), SessionUtil.SESSION_KEY_USER_TAG);
            if (!AllSettingsManager.isNullOrEmpty(userType)) {
                flavorType = FlavorType.getFlavor(SessionManager.getStringSetting(getApplicationContext(), SessionUtil.SESSION_KEY_USER_TAG));
            } else {
                flavorType = FlavorType.CUSTOMER;
            }

            switch (flavorType) {
                case CUSTOMER:
                    //Initialize Customer FCM content class
                    FcmManager.initFcmManager(getApplicationContext(), FlavorType.CUSTOMER.name(), new FcmManager.FcmBuilder().setOrderContentClass(CheckoutOrderActivity.class).buildGcmManager());
                    navigateCustomerScreen();
                    break;

                case SUPPLIER:
                    //Initialize Customer FCM content class
                    FcmManager.initFcmManager(getApplicationContext(), FlavorType.SUPPLIER.name(), new FcmManager.FcmBuilder().setOrderContentClass(CheckoutOrderActivity.class).buildGcmManager());
                    navigateCustomerScreen();
//                    Intent intentAppSupplier = new Intent(SplashActivity.this, SupplierStoreDetailActivity.class);
//                    startActivity(intentAppSupplier);
//                    finish();
                    break;

                default:
                    navigateCustomerScreen();
                    break;
            }
        } catch (Exception ex) {
            Logger.d(TAG, ex + ">>Exception");
            navigateCustomerScreen();
        }
    }

    private void navigateCustomerScreen() {
        Intent intentAppCustomer = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intentAppCustomer);
        finish();
    }
}