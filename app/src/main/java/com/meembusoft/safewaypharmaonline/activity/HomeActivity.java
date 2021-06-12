package com.meembusoft.safewaypharmaonline.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.base.BaseLocationActivity;
import com.meembusoft.safewaypharmaonline.enumeration.DetailType;
import com.meembusoft.safewaypharmaonline.enumeration.FlavorType;
import com.meembusoft.safewaypharmaonline.enumeration.LoginSettingType;
import com.meembusoft.safewaypharmaonline.enumeration.SearchType;
import com.meembusoft.safewaypharmaonline.fcm.UpdateTokenTask;
import com.meembusoft.safewaypharmaonline.fcm.fcmutils.OnTokenUpdateListener;
import com.meembusoft.safewaypharmaonline.fcm.fcmutils.TokenFetcher;
import com.meembusoft.safewaypharmaonline.fragment.HomeFragment;
import com.meembusoft.safewaypharmaonline.fragment.MenuFragment;
import com.meembusoft.safewaypharmaonline.fragment.NotificationFragment;
import com.meembusoft.safewaypharmaonline.fragment.OrderListFragment;
import com.meembusoft.safewaypharmaonline.medicinereminder.service.ForeverRunningService;
import com.meembusoft.safewaypharmaonline.medicinereminder.service.Restarted;
import com.meembusoft.safewaypharmaonline.model.AppSupplier;
import com.meembusoft.safewaypharmaonline.model.AppUser;
import com.meembusoft.safewaypharmaonline.model.Notifications;
import com.meembusoft.safewaypharmaonline.model.ShippingAddress;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.model.UserProfile;
import com.meembusoft.safewaypharmaonline.retrofit.APIClient;
import com.meembusoft.safewaypharmaonline.retrofit.APIInterface;
import com.meembusoft.safewaypharmaonline.util.AllConstants;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.ColorPickerManager;
import com.meembusoft.safewaypharmaonline.util.FragmentUtilsManager;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.util.SessionUtil;
import com.meembusoft.safewaypharmaonline.util.StatusBarUtil;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;

import org.parceler.Parcels;

import java.util.List;

import cn.ymex.popup.controller.AlertController;
import cn.ymex.popup.dialog.PopupDialog;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_FCM_NOTIFICATION_MESSAGE;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_SEARCH_KEYWORD;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_SEARCH_TYPE;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_REQUEST_CODE_SHIPPING_ADDRESS;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_REQUEST_CODE_USER_LOGIN;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class HomeActivity extends BaseLocationActivity {

    //Toolbar
    private Toolbar toolbar;
    private TextView tvCount;
    private ImageView ivLogout, ivProfile, ivBarcode, ivSupplier;
    private LinearLayout linSearch;
    private RelativeLayout rlCart;
    public static final int ID_HOME = 1;
    public static final int ID_CART = 2;
    public static final int ID_NOTIFICATION = 3;
    public static final int ID_ACCOUNT = 4;
    private MeowBottomNavigation bottomNavigation;

    //Background task
    private TokenFetcher tokenFetcher;
    // private UpdateTokenTask updateTokenTask;
    private APIInterface mApiInterface;
    UpdateTokenTask updateTokenTask;


    private AppUser mAppUser;
    private UserProfile mUserProfile;
    private String deviceUniqueIdentity = "";

    Notifications mNotification;
    String mParcelableDetailType = "";
    DetailType mDetailType = null;
    //  Supplier
    private AppSupplier mAppSupplier;
    FlavorType mFlavorType;
    private String customerOrSupplierId = "";
    private String selectUserType = AllConstants.USER_TYPE_CUSTOMER;
    private ShippingAddress mShippingAddress;

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }

    @Override
    public void onLocationFound(Location location) {
//        if (location != null) {
//
//            //Check internet connection
//            if (NetworkManager.isConnected(getActivity())) {
//                if (FragmentUtilsManager.getVisibleSupportFragment(HomeActivity.this, lastSelectedItem.getNavigationId().name()) instanceof ProductsFragment) {
//                    //Request reverse geocoding for address
//                    if ((currentLocationTask != null) && (currentLocationTask.getStatus() == AsyncTask.Status.RUNNING)) {
//                        currentLocationTask.cancel(true);
//                    }
//
//                    currentLocationTask = new ReverseGeocoderTask(getActivity(), new LocationAddressListener() {
//                        @Override
//                        public void getLocationAddress(UserLocationAddress locationAddress) {
//                            if (locationAddress != null) {
//                                mLocationAddress = locationAddress;
//                                Logger.d(TAG, "UserLocationAddress: " + mLocationAddress.toString());
////                                        String addressText = String.format("%s, %s, %s, %s", locationAddress.getStreetAddress(), locationAddress.getCity(), locationAddress.getState(), locationAddress.getCountry());
//
//                                //Set address to the toolbar
//                                setToolBarTitle(mLocationAddress.getAddressLine());
//                            }
//                        }
//                    });
//                    currentLocationTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, location);
//                }
//            }
//        }
    }

    @Override
    public LOCATION_UPDATE_FREQUENCY initLocationUpdateFrequency() {
        return LOCATION_UPDATE_FREQUENCY.ONCE;
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_home;
    }

    @Override
    public void initStatusBarView() {
        StatusBarUtil.setColor(getActivity(), getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void initNavigationBarView() {

    }

    @Override
    public void initIntentData(Bundle savedInstanceState, Intent intent) {
        if (intent != null) {
            mParcelableDetailType = intent.getStringExtra(AllConstants.INTENT_KEY_DETAIL_TYPE);
            Logger.d(TAG, TAG + " >>> " + "mParcelableDetailType: " + mParcelableDetailType);

            if (!AllSettingsManager.isNullOrEmpty(mParcelableDetailType)) {
                mDetailType = DetailType.valueOf(mParcelableDetailType);
                Logger.d(TAG, TAG + " >>> " + "mDetailType: " + mDetailType);

                switch (mDetailType) {
                    case NOTIFICATION:
                        Parcelable mParcelable = intent.getParcelableExtra(AllConstants.INTENT_KEY_FCM_NOTIFICATION_MESSAGE);
                        if (mParcelable != null) {
                            mNotification = Parcels.unwrap(mParcelable);
                            Logger.d(TAG, TAG + " >>>MapActivity " + "placeOrderByItem: " + mNotification.toString());
//                            OrderId = placeOrderByItem.getId();
                        }
                        break;
                }
            }


        }
    }

    @Override
    public void initActivityViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvCount = (TextView) findViewById(R.id.tv_count);
        linSearch = (LinearLayout) findViewById(R.id.lin_search);
        rlCart = (RelativeLayout) findViewById(R.id.rl_cart);
        rlCart.setVisibility(View.GONE);
        tvCount.setVisibility(View.GONE);
        ivLogout = (ImageView) findViewById(R.id.iv_logout);
        ivProfile = (ImageView) findViewById(R.id.iv_profile);
        // ivSupplier = (ImageView) findViewById(R.id.iv_supplier);
        ivBarcode = (ImageView) findViewById(R.id.iv_barcode);
        bottomNavigation = (MeowBottomNavigation) findViewById(R.id.bottom_navigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_HOME, R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_CART, R.drawable.vector_ic_cart));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_NOTIFICATION, R.drawable.vector_ic_notification_white));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_ACCOUNT, R.drawable.vector_ic_menu));

        initGetTagValue();


        // For the very first time home is selected
        if (mDetailType != null && mDetailType == DetailType.NOTIFICATION && mNotification != null) {
            changeNavigationFragment(mNotification, new NotificationFragment());
        } else {
            FragmentUtilsManager.changeSupportFragment(HomeActivity.this, new HomeFragment());
            bottomNavigation.show(ID_HOME, false);
        }

        initTokenForFCM();

    }

    private void updateSession() {
        if (mFlavorType == FlavorType.SUPPLIER) {
            mAppSupplier = SessionUtil.getSupplier(getActivity());
            if (mAppSupplier != null) {
                customerOrSupplierId = mAppSupplier.getUser_id();
            }
        } else {
            mAppUser = SessionUtil.getUser(getActivity());
            if (mAppUser != null) {
                customerOrSupplierId = mAppUser.getId();
            }
        }

        mUserProfile = SessionUtil.getUserProfile(getActivity());
        resetCounterView();


    }

    public void initGetTagValue() {
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
                        if (!AppUtil.isMyServiceRunning(getActivity(), ForeverRunningService.class)) {
                            openSetting();
                            startMedicineReminderService();
                        } else {
                            Logger.d(ForeverRunningService.TAG, "Medicine reminder service is already running.");
                        }
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
            mShippingAddress = SessionUtil.getUserShippingAddress(getActivity());

            Logger.d(TAG, TAG + " >>> " + "selectUserType " + selectUserType);
            Logger.d(TAG, TAG + " >>> " + "customerOrSupplierId " + customerOrSupplierId);
        }

        ivProfileSetResource();
        updateSession();

    }


    private void ivProfileSetResource() {
        if (mFlavorType == FlavorType.SUPPLIER) {
            ivLogout.setVisibility(View.GONE);
            ivProfile.setImageResource(R.drawable.vector_ic_supplier);
            ivProfile.setVisibility(View.VISIBLE);
        } else {
            ivProfile.setImageResource(R.drawable.vector_ic_user_grey);
        }
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {

//        bottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
//            @Override
//            public Unit invoke(MeowBottomNavigation.Model model) {
//                return null;
//            }
//        });

        rlCart.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                rlCart.setVisibility(View.VISIBLE);
                ivLogout.setVisibility(View.GONE);
                ivProfile.setVisibility(View.GONE);
                FragmentUtilsManager.changeSupportFragment(HomeActivity.this, new OrderListFragment());
                bottomNavigation.show(ID_CART, false);
            }
        });

        linSearch.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intentSearch = new Intent(getActivity(), SearchActivity.class);
                intentSearch.putExtra(INTENT_KEY_SEARCH_TYPE, SearchType.REGULAR.name());
                startActivity(intentSearch);
            }
        });


        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFlavorType == FlavorType.SUPPLIER) {
                    if (mAppSupplier != null) {
                        Intent iSupplierActivity = new Intent(getActivity(), SupplierStoreDetailActivity.class);
                        startActivity(iSupplierActivity);
                    }
                } else {
                    MenuFragment menuFragment = (MenuFragment) FragmentUtilsManager.getVisibleSupportFragment(HomeActivity.this, R.id.container);
                    if (menuFragment != null) {
                        Logger.d(TAG, TAG + " >>> " + "Updating menu list");
                        menuFragment.logOutDialog(getResources().getString(R.string.dialog_logout_title), getResources().getString(R.string.dialog_logout_mgs));
                    }
                }
            }
        });
        ivBarcode.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                new IntentIntegrator(getActivity())
                        .setPrompt(getString(R.string.txt_scan_your_qr_code_or_bar_code))
                        .setOrientationLocked(true)
                        .setCaptureActivity(BarCodeScannerActivity.class)
                        .setRequestCode(REQUEST_CODE).initiateScan();
            }
        });

//        ivSupplier.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAppSupplier = SessionUtil.getSupplier(getActivity());
//                if (mAppSupplier != null) {
//                    Intent iSupplierActivity = new Intent(getActivity(), SupplierStoreDetailActivity.class);
//                    startActivity(iSupplierActivity);
//                }
//            }
//        });


        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()) {
                    case ID_HOME:
                        ivLogout.setVisibility(View.GONE);
                        ivProfile.setVisibility(View.GONE);
                        ivProfileSetResource();
                        FragmentUtilsManager.changeSupportFragment(HomeActivity.this, new HomeFragment());
                        break;
                    case ID_CART:
                        ivLogout.setVisibility(View.GONE);
                        ivProfile.setVisibility(View.GONE);
                        ivProfileSetResource();
                        FragmentUtilsManager.changeSupportFragment(HomeActivity.this, new OrderListFragment());

                        break;
                    case ID_NOTIFICATION:
                        ivLogout.setVisibility(View.GONE);
                        ivProfile.setVisibility(View.GONE);
                        ivProfileSetResource();
                        FragmentUtilsManager.changeSupportFragment(HomeActivity.this, new NotificationFragment());
                        break;
                    case ID_ACCOUNT:
                        ivProfile.setVisibility(View.GONE);
                        updateSession();
                        FragmentUtilsManager.changeSupportFragment(HomeActivity.this, new MenuFragment());
                        logOutView(mAppUser, mUserProfile);

                        break;
                }
                return null;
            }
        });
    }

    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case INTENT_REQUEST_CODE_USER_LOGIN:
                Logger.d(TAG, TAG + " >>> " + "INTENT_REQUEST_CODE_LOGIN: " + requestCode);
                LoginSettingType loginSettingType;
                if (data != null && resultCode == RESULT_OK) {
                    String mParcelableCategoryType = data.getStringExtra(AllConstants.INTENT_KEY_LOGIN_SETTING_TYPE);
                    mAppUser = SessionUtil.getUser(getActivity());

                    if (!AllSettingsManager.isNullOrEmpty(mParcelableCategoryType)) {
                        loginSettingType = LoginSettingType.valueOf(mParcelableCategoryType);
                        Logger.d(TAG, TAG + " >>> " + "loginSettingType: " + loginSettingType);
                        if (loginSettingType != null) {
                            switch (loginSettingType) {
                                case MENU_TO_LOGIN:
                                    FragmentUtilsManager.changeSupportFragment((HomeActivity) getActivity(), new MenuFragment());

                                    break;

                                case ORDER_CART_TO_LOGIN:
                                    updateSubTotals();
                                    // FragmentUtilsManager.changeSupportFragment((HomeActivity) getActivity(), new OrderListFragment());
                                    break;

                                case HOME_TO_LOGIN:
//                                    List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
//                                    if (fragmentList != null && fragmentList.size()>0) {
//                                        for (Fragment fragment : fragmentList) {
//                                            if (fragment instanceof HomeFragment) {
//                                                ((HomeFragment) fragment).onFragmentResult(requestCode,resultCode,data);
//                                            }
//                                        }
//                                        }
                                    break;

                            }
                        }
                        if (customerOrSupplierId != null && !TextUtils.isEmpty(customerOrSupplierId)) {
                            initTokenForFCM();
                        }
                    }

                }

                break;

            case INTENT_REQUEST_CODE_SHIPPING_ADDRESS:
                openFragmentOnActivityResult(requestCode, resultCode, data);
                break;
            case REQUEST_CODE:
                IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);
                Logger.d(TAG, "Scanned: " + result.getContents());
                if (!TextUtils.isEmpty(result.getContents())) {
//                    Toast.makeText(getActivity(), getString(R.string.txt_product_code) + ": " + result.getContents(), Toast.LENGTH_SHORT).show();
                    Intent intentBarcode = new Intent(getActivity(), SearchActivity.class);
                    intentBarcode.putExtra(INTENT_KEY_SEARCH_TYPE, SearchType.BARCODE.name());
                    intentBarcode.putExtra(INTENT_KEY_SEARCH_KEYWORD, result.getContents());
//                    intentBarcode.putExtra(INTENT_KEY_SEARCH_KEYWORD, "97156818");
                    startActivity(intentBarcode);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.txt_sorry_we_could_not_detect_your_medicine), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public MeowBottomNavigation getBottomNavigation() {
        return bottomNavigation;
    }

    public void resetCounterView() {
        List<StaggeredMedicineByItem> data = AppUtil.getAllStoredMedicineByItems(getActivity(), mFlavorType);
        if (data.size() > 0) {
            tvCount.setText(data.size() + "");
            tvCount.setVisibility(View.VISIBLE);
            bottomNavigation.setCount(ID_CART, data.size() + "");
        } else {
            tvCount.setVisibility(View.GONE);
            bottomNavigation.clearCount(ID_CART);

        }
    }

    public void updateSubTotals() {
        //send back press event to the fragment
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null && fragmentList.size() > 0) {
            for (Fragment fragment : fragmentList) {
                if (fragment instanceof OrderListFragment) {
//                    ((OrderListFragment) fragment).initFragmentViewsData();
                    ((OrderListFragment) fragment).updateTotalAmount();
                }
            }
        }
        resetCounterView();
    }

    public void profileIconInvisible() {
        ivProfile.setVisibility(View.GONE);

    }

    private void changeNavigationFragment(Notifications notifications, Fragment fragment) {
        Logger.d(TAG, "Notifications: " + notifications.toString());
        Bundle bundle = new Bundle();
        bundle.putParcelable(INTENT_KEY_FCM_NOTIFICATION_MESSAGE, Parcels.wrap(notifications));
        fragment.setArguments(bundle);
        FragmentUtilsManager.changeSupportFragment(HomeActivity.this, fragment);
        bottomNavigation.show(ID_NOTIFICATION, false);

    }

    public void logOutView(AppUser appUser, UserProfile userProfile) {
        if (appUser != null) {
            ivProfile.setImageResource(R.drawable.vector_ic_user_grey);
//            ivProfile.setColorFilter(ContextCompat.getColor(getActivity(), R.color.color_grey_600));
            ivProfile.setVisibility(View.VISIBLE);
            if (userProfile != null && userProfile.getThumb_photo() != null && userProfile.getThumb_photo() != "") {
                AppUtil.loadImage(getActivity(), ivProfile, userProfile.getThumb_photo(), false, true, false);
            }
        } else {
            ivProfile.setVisibility(View.GONE);
            ivProfileSetResource();
        }

    }

    @Override
    public void initActivityBackPress() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            showAppCloseDialog(getResources().getString(R.string.dialog_close_app_title), getResources().getString(R.string.dialog_do_you_want_to_close_the_app));
        }
    }

    @Override
    public void initActivityDestroyTasks() {
        dismissPopupDialog();
        if (tokenFetcher != null && tokenFetcher.getStatus() == AsyncTask.Status.RUNNING) {
            tokenFetcher.cancel(true);
        }

        if (updateTokenTask != null && updateTokenTask.getStatus() == AsyncTask.Status.RUNNING) {
            updateTokenTask.cancel(true);
        }

        // Medicine reminder
        Logger.d(ForeverRunningService.TAG, "onDestroy");
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarted.class);
        sendBroadcast(broadcastIntent);
    }

    @Override
    public void initActivityPermissionResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    @Override
    public void onResume() {
        super.onResume();
        resetCounterView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /************************
     * Methods for schedule *
     ************************/
    private void openSetting() {
        if (Build.BRAND.equalsIgnoreCase("xiaomi")) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            startActivityForResult(intent, 2);
        }
    }

    private void startMedicineReminderService() {
//        if (SessionUtil.getUser(getActivity()) != null) {
//            Logger.d(ForeverRunningService.TAG, "User is logged in, so starting service again");
            startService(new Intent(getActivity(), ForeverRunningService.class));
            Logger.d(ForeverRunningService.TAG, "Medicine reminder service is started.");
//        } else {
//            Logger.d(ForeverRunningService.TAG, "User is logged out, so stopping service");
//        }
    }

//    private void startMedicineReminderService(Context context) {
//        try {
//            Date todayDate = Calendar.getInstance().getTime();
//            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//            String getTodayDate = formatter.format(todayDate);
//
//            if (getTodayDate != null && ((TextUtils.isEmpty(SessionUtil.getMedicineReminderDate(context)) || getTodayDate.equalsIgnoreCase(SessionUtil.getMedicineReminderDate(getActivity()))) && !SessionUtil.isTodaysMedicineReminderSet(getActivity()))) {
//                Logger.d(TAG, TAG + ">>startMedicineReminderService>>Starting service");
//                Intent intentMedicineReminder = new Intent(context, MedicineReminderService.class);
//                context.startService(intentMedicineReminder);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    private void initDailyMedicineReminder() {
//        // if (!SessionUtil.isPeriodicCheckServiceSet(getActivity())) {
//        // Prepare schedule item for periodic reminder. It fires alarm daily 1 AM
//        Calendar mAlertTime = Calendar.getInstance();
//        mAlertTime.set(Calendar.HOUR_OF_DAY, 0);
//        mAlertTime.set(Calendar.MINUTE, 35);
//        mAlertTime.set(Calendar.SECOND, 0);
//        String mDate = AlarmConstants.DATE_FORMAT.format(mAlertTime.getTime());
//        ScheduleItem scheduleItem = new ScheduleItem((int) System.currentTimeMillis(), getString(R.string.txt_medicine_reminder), getString(R.string.txt_please_take_your_medicine), mDate, mAlertTime.getTimeInMillis(), REPEAT_TYPE.DAILY);
//        Logger.d(TAG, TAG + ">>initDailyMedicineReminder>>scheduleItem: " + scheduleItem);
//        //Set daily alarm
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(getActivity(), PeriodicReminderReceiver.class);
//        intent.putExtra(AlarmConstants.INTENT_KEY_SCHEDULE_DATA_ALARM_RECEIVER, BaseParcelable.toByteArray(scheduleItem));
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), scheduleItem.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        long timeInMilliseconds = scheduleItem.getTimeInMillis();
//        Logger.d(TAG, TAG + ">>timeInMilliseconds>> " + timeInMilliseconds);
//
//        //set a repeating alarm
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeInMilliseconds, AlarmManager.INTERVAL_DAY, pendingIntent);
//
////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////                if (alarmManager != null) {
////                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,timeInMilliseconds,6 * 60 * 1000,pendingIntent); // alarm will repeat after every 15 minutes
////                    //alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMilliseconds, pendingIntent);
////                }
////            } else {
////                if (alarmManager != null) {
////                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,timeInMilliseconds,6 * 60 * 1000,pendingIntent); // alarm will repeat after every 15 minutes
////
////                    // alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMilliseconds, pendingIntent);
////                }
////            }
//        // Store daily alarm info
//        SessionUtil.setPeriodiServiceMedicineReminder(getActivity(), true);
//    }

    private void showAppCloseDialog(String title, String message) {
        PopupDialog.create(getActivity())
                .outsideTouchHide(false)
                .dismissTime(1000 * 5)
                .controller(AlertController.build()
                        .title(title + "\n")
                        .message(message)
                        .clickDismiss(true)
                        .negativeButton(getString(R.string.dialog_cancel), null)
                        .positiveButton(getString(R.string.dialog_ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SessionManager.setStringSetting(getActivity(), AllConstants.SESSION_KEY_HOME, "");
                                finish();
                            }
                        }))
                .show();
    }

    private void openFragmentOnActivityResult(int requestCode, int resultCode, Intent data) {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null) {
            for (Fragment fragment : fragmentList) {
                if (fragment instanceof OrderListFragment) {
                    ((OrderListFragment) fragment).initFragmentOnResult(requestCode, resultCode, data);
                    ((OrderListFragment) fragment).setFrameLayoutMargin();

                }
            }
        }
    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = HomeActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    public void setThemeColor(ImageView sliderImageView) {
        int color = 0;
        if (sliderImageView != null) {
            color = ColorPickerManager.getPixelColor(sliderImageView, 350, 50);
        } else {
            color = getResources().getColor(R.color.colorPrimary);
        }
        Logger.d(TAG, "ColorCode: " + color);
        // Avoid white color
        if (!(color + "").startsWith("-32")) {
            toolbar.setBackgroundColor(color);
            StatusBarUtil.setColor(getActivity(), color);
//            bottomNavigation.setBezierColor(color);
        }
    }

    /****************************************
     * Methods for firebase cloud Messaging *
     ****************************************/
    private void initTokenForFCM() {
        if (NetworkManager.isConnected(getActivity())) {
            if (tokenFetcher != null && tokenFetcher.getStatus() == AsyncTask.Status.RUNNING) {
                tokenFetcher.cancel(true);
            }
            tokenFetcher = new TokenFetcher(getActivity(), new OnTokenUpdateListener() {
                @Override
                public void onTokenUpdate(String update) {
                    if (!AllSettingsManager.isNullOrEmpty(update)) {
                        Logger.d(TAG, TAG + ">>updateToken " + update + "update");
                        //  Send server request for updating token
                        if (updateTokenTask != null && updateTokenTask.getStatus() == AsyncTask.Status.RUNNING) {
                            updateTokenTask.cancel(true);
                        }
                        Logger.d(TAG, TAG + ">>customerOrSupplierId " + customerOrSupplierId + "  customerOrSupplierId");

                        if (customerOrSupplierId != null && !TextUtils.isEmpty(customerOrSupplierId)) {
                            updateTokenTask = new UpdateTokenTask(getActivity(), mFlavorType, customerOrSupplierId, update);
                            updateTokenTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                    }
                }
            });
            tokenFetcher.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }


/*
    private class UpdateTokenTask extends AsyncTask<String, Integer, Response> {

        private Context mContext;
        private String mToken = "";

        private UpdateTokenTask(Context context, String token) {
            mContext = context;
            mToken = token;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Response doInBackground(String... params) {
            try {
                Response response = null;

                ParamsUpdateFcm paramAppUser = new ParamsUpdateFcm("cutomer", mAppUser.getId(), mToken);
                Logger.d(TAG, TAG + ">>UpdateTokenTask>> paramAppUser:" + paramAppUser.toString());
                Call<APIResponse> callUser = mApiInterface.apiUpdateFcmToken(paramAppUser);
                response = callUser.execute();

//                switch (mFlavorType) {
//                    case CUSTOMER:
//                        ParamsUpdateFcm paramAppUser = new ParamsUpdateFcm(userType, mAppSupplier.getUser_id(), mToken);
//                        Logger.d(TAG, TAG + ">>UpdateTokenTask>> paramAppUser:" + paramAppUser.toString());
//                        Call<APIResponse> callUser = mApiInterface.apiUpdateFcmToken(paramAppUser);
//                        response = callUser.execute();
//                        break;
//
//                }

                Logger.d(TAG, TAG + ">>UpdateTokenTask>> response:" + response);
                if (response.isSuccessful()) {
                    return response;
                }
            } catch (Exception ex) {
                Logger.d(TAG, TAG + ">>UpdateTokenTask>> exception:" + ex.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Response result) {
            try {
                if (result != null && result.isSuccessful()) {

                    Logger.d(TAG, TAG + ">>UpdateTokenTask>>: onResponse-server = " + result.toString());
                    APIResponse dataUser = (APIResponse) result.body();

                    if (dataUser != null && dataUser.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, TAG + ">>UpdateTokenTask>> : onResponse-object = " + dataUser.toString());
                        //Set user
                        SessionManager.setStringSetting(getActivity(), SessionUtil.SESSION_KEY_USER_TAG, FlavorType.SUPPLIER.toString());
                        SessionUtil.setSupplier(getActivity(), APIResponse.getResponseString(dataUser.getData()));

                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
*/

}