package com.meembusoft.safewaypharmaonline.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.adapter.CommonSpinnerAdapter;
import com.meembusoft.safewaypharmaonline.adapter.FilterSessionLeftListAdapter;
import com.meembusoft.safewaypharmaonline.adapter.SwipeRemainderAdapter;
import com.meembusoft.safewaypharmaonline.base.BaseActivity;
import com.meembusoft.safewaypharmaonline.enumeration.FlavorType;
import com.meembusoft.safewaypharmaonline.enumeration.MedicineRemainderTabType;
import com.meembusoft.safewaypharmaonline.enumeration.SearchType;
import com.meembusoft.safewaypharmaonline.model.AppSupplier;
import com.meembusoft.safewaypharmaonline.model.AppUser;
import com.meembusoft.safewaypharmaonline.model.DayScheduling;
import com.meembusoft.safewaypharmaonline.model.FilterSessionLeft;
import com.meembusoft.safewaypharmaonline.model.ParamsAddReminder;
import com.meembusoft.safewaypharmaonline.model.ParamsDeleteNotification;
import com.meembusoft.safewaypharmaonline.model.ParamsUpdateStatusReminder;
import com.meembusoft.safewaypharmaonline.model.Remainder;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.retrofit.APIClient;
import com.meembusoft.safewaypharmaonline.retrofit.APIInterface;
import com.meembusoft.safewaypharmaonline.retrofit.APIResponse;
import com.meembusoft.safewaypharmaonline.util.AlarmManager;
import com.meembusoft.safewaypharmaonline.util.AllConstants;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.BroadcastManager;
import com.meembusoft.safewaypharmaonline.util.DateManager;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.util.SessionUtil;
import com.meembusoft.safewaypharmaonline.view.CanaroTextView;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;

import org.parceler.Parcels;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import vn.luongvo.widget.iosswitchview.SwitchView;

import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_SEARCH_TYPE;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_REQUEST_CODE_REMINDER;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class MedicineRemainderActivity extends BaseActivity implements AlarmManager.Callback {

    //Toolbar
    private ImageView ivBack;
    private CanaroTextView tvTitle;
    private LinearLayout linSearch;
    private TextView tvAddRemainder, tvPillboxs, tvSessionLeft, tvNoDataFound;
    private LinearLayout linAddMedicineRemainder, linStartFrom, linTimeView, linAddMedicineRemainderTab, linPillbox, linPillboxTab, linSessionLeft, linSessionLeftTab, linNoDataFound;
    private RecyclerView rvFilterSessionLeft, rvPillbox;
    private FilterSessionLeftListAdapter filterSessionLeftListAdapter;
    private SwipeRemainderAdapter mSwipeRemainderAdapter;
    private CommonSpinnerAdapter perDaySchedulingAdapter, schedulingTypeAdapter;
    private APIInterface mApiInterface;
    private GetAllFilterSessionLeftTask getAllFilterSessionLeftTask;
    private GetAllReminderListsByCustomerTask getAllReminderListsByCustomerTask;
    private DoAddRemindersTask doAddRemindersTask;
    private DoDeleteRemainderTask doDeleteRemainderTask;
    private DoUpdateStatusRemindersTask doUpdateStatusRemindersTask;
    MedicineRemainderTabType remainderTabType = MedicineRemainderTabType.ADD_REMAINDER;
    private AppUser mAppUser;
    private EditText etMedicineQty, etMedicineName, etFromDate, etScheduleTime, etStartFrom;
    private Button btnAddReminder;
    private ImageView ivMedicine;
    private Spinner spPerDayScheduling, spSchedulingType;
    private SwitchView switchRemindMe;
    private RadioGroup rgStartFromDate;
    private RadioButton rbSat, rbSun, rbMon, rbTus, rbWed, rbThu, rbFri;
    //  Supplier
    private AppSupplier mAppSupplier;
    FlavorType mFlavorType;
    private String customerOrSupplierId = "";
    private String selectUserType = AllConstants.USER_TYPE_CUSTOMER;

    private List<Remainder> remainderList = new ArrayList<>();
    private String perDayID = "1";
    private String remindMe = "1";
    private String schedulingType = "";
    private String getStartFromDates = "";
    private String startFromDay = "";
    private String startFrom = "";
    private String medicinesId = "";
    private String id = "0";
    int scheduleTime = 0;
    private StaggeredMedicineByItem medicineByItem;
    // Alarm
    private AlarmManager mAlarm;
    private static String TAG = MedicineRemainderActivity.class.getSimpleName();
    private boolean mIsRepeatable = false;
    // protected final static long POLLING_INTERVAL = 20 * 1000; // every 20 second
    private boolean mRegistered = false;

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }


    @Override
    public int initActivityLayout() {
        return R.layout.activity_medicine_remainder_screen;
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
            Parcelable mParcelable = intent.getParcelableExtra(AllConstants.INTENT_KEY_PRODUCT_ITEM);
            if (mParcelable != null) {
                medicineByItem = Parcels.unwrap(mParcelable);
                Logger.d(TAG, TAG + " >>> " + "medicineByProduct: " + medicineByItem.toString());
            }
        }
    }

    @Override
    public void initActivityViews() {
        ivBack = (ImageView) findViewById(R.id.iv_barcode);
        ivBack.setImageResource(R.drawable.vector_ic_left_arrow);
        linSearch = (LinearLayout) findViewById(R.id.lin_search);

//        tvTitle = (CanaroTextView) findViewById(R.id.text_title);
//        tvTitle.setText("Bullet Test");
        ivMedicine = (ImageView) findViewById(R.id.iv_medicine);
        etMedicineName = (EditText) findViewById(R.id.et_medicine_name);
        etMedicineQty = (EditText) findViewById(R.id.et_medicine_qty);
        etFromDate = (EditText) findViewById(R.id.et_from_date);
        etScheduleTime = (EditText) findViewById(R.id.et_schedule_time);
        etStartFrom = (EditText) findViewById(R.id.et_start_from);
        btnAddReminder = (Button) findViewById(R.id.btn_add_reminder);

        tvAddRemainder = (TextView) findViewById(R.id.tv_add_remainder);
        tvPillboxs = (TextView) findViewById(R.id.tv_pillbox);
        tvSessionLeft = (TextView) findViewById(R.id.tv_session_left);
        tvNoDataFound = (TextView) findViewById(R.id.tv_no_data_found);
        linNoDataFound = (LinearLayout) findViewById(R.id.lin_no_data_found_layout);
        linAddMedicineRemainderTab = (LinearLayout) findViewById(R.id.lin_add_remainder_tab);
        linAddMedicineRemainder = (LinearLayout) findViewById(R.id.lin_add_medicine_remainder);
        linStartFrom = (LinearLayout) findViewById(R.id.lin_start_from);
        linTimeView = (LinearLayout) findViewById(R.id.lin_lay_time_view);
        linPillboxTab = (LinearLayout) findViewById(R.id.lin_pillbox_tab);
        linPillbox = (LinearLayout) findViewById(R.id.lin_medicine_pillbox);
        linSessionLeftTab = (LinearLayout) findViewById(R.id.lin_session_left_todays_tab);
        linSessionLeft = (LinearLayout) findViewById(R.id.lin_session_left);
        rvFilterSessionLeft = (RecyclerView) findViewById(R.id.rv_filter_session_left);
        rvPillbox = (RecyclerView) findViewById(R.id.rv_pillbox);
        spPerDayScheduling = (Spinner) findViewById(R.id.sp_per_day);
        spSchedulingType = (Spinner) findViewById(R.id.sp_scheduleing);
        switchRemindMe = (SwitchView) findViewById(R.id.switch_remind_me);
        rgStartFromDate = (RadioGroup) findViewById(R.id.rg_start_from_date);
        rbSat = (RadioButton) findViewById(R.id.rb_sat);
        rbSun = (RadioButton) findViewById(R.id.rb_sun);
        rbMon = (RadioButton) findViewById(R.id.rb_mon);
        rbTus = (RadioButton) findViewById(R.id.rb_tus);
        rbThu = (RadioButton) findViewById(R.id.rb_thu);
        rbWed = (RadioButton) findViewById(R.id.rb_wed);
        rbFri = (RadioButton) findViewById(R.id.rb_fri);
        schedulingType = getString(R.string.txt_medicine_remainder_schedule_type_daily);
        mAlarm = new AlarmManager(TAG, this);

    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        // Register BroadCast
        BroadcastManager.registerBroadcastUpdate(getActivity(), mAlarmBroadcast);
        mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        // Get User Tag
        initGetTagValue();

        filterSessionLeftListAdapter = new FilterSessionLeftListAdapter(getActivity());
        rvFilterSessionLeft.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFilterSessionLeft.setAdapter(filterSessionLeftListAdapter);

        rvPillbox.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRemainderAdapter = new SwipeRemainderAdapter(this, remainderList);
        rvPillbox.setAdapter(mSwipeRemainderAdapter);
        // Set Visible Tab
        visibleTabLayout(remainderTabType);
        setData(medicineByItem);

        initSpinnerSet();

        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
        } else {

            getDateCompare();
        }
    }

    private void initSpinnerSet() {
        List<DayScheduling> perDaySchedulingList = AppUtil.getDaySchedulingList(getActivity());
        perDaySchedulingAdapter = new CommonSpinnerAdapter(MedicineRemainderActivity.this, CommonSpinnerAdapter.ADAPTER_TYPE.SELECT_PER_DAY);
        spPerDayScheduling.setAdapter(perDaySchedulingAdapter);
        perDaySchedulingAdapter.setData(perDaySchedulingList);
        // spPerDayScheduling.setSelection((userProfileDetails != null) ? perDaySchedulingAdapter.getItemPosition(perDaySchedulingAdapter) : 0);

        List<DayScheduling> schedulingList = AppUtil.getSchedulingList(getActivity());
        schedulingTypeAdapter = new CommonSpinnerAdapter(MedicineRemainderActivity.this, CommonSpinnerAdapter.ADAPTER_TYPE.SELECT_SCHEDULING_TYPE);
        spSchedulingType.setAdapter(schedulingTypeAdapter);
        schedulingTypeAdapter.setData(schedulingList);

    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        linSearch.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intentSearch = new Intent(getActivity(), SearchActivity.class);
                intentSearch.putExtra(INTENT_KEY_SEARCH_TYPE, SearchType.REMINDER.name());
                startActivityForResult(intentSearch, INTENT_REQUEST_CODE_REMINDER);
            }
        });

        linAddMedicineRemainderTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remainderTabType = MedicineRemainderTabType.ADD_REMAINDER;
                visibleTabLayout(remainderTabType);
            }
        });

        linPillboxTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remainderTabType = MedicineRemainderTabType.PILLBOX;
                visibleTabLayout(remainderTabType);
            }
        });

        linSessionLeftTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remainderTabType = MedicineRemainderTabType.SESSION;
                visibleTabLayout(remainderTabType);
            }
        });


        etFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remainderTabType = MedicineRemainderTabType.SESSION;
                visibleTabLayout(remainderTabType);
                AppUtil.datePicker(etFromDate.getText().toString(), etFromDate, false, getActivity());
            }
        });

        etFromDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                allFilterSessionLeft();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


//        etFromDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                remainderTabType = MedicineRemainderTabType.SESSION;
//                visibleTabLayout(remainderTabType);
//                AppUtil.datePicker2(etFromDate.getText().toString(), etFromDate, false, getActivity());
//
//                // etFromDate.setText(DateManager.convertDateTime(etFromDate.getText().toString(),"yyyy-MM-dd","MMM dd"));
////                String getDate = DateManager.convertDateTime(etFromDate.getText().toString(), "dd MMM yyyy", "yyyy-MM-dd");
////
////                if (getDate != null) {
////                    Logger.d(TAG, TAG + " >>> " + "getDate " + getDate);
////                    filterSessionLeftListAdapter.clear();
////                    getAllFilterSessionLeftTask = new GetAllFilterSessionLeftTask(getActivity(), getDate);
////                    getAllFilterSessionLeftTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
////                }
//            }
//        });


        etStartFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppUtil.datePicker(etStartFrom.getText().toString(), etStartFrom, false, getActivity());

            }
        });
        etScheduleTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtil.timePicker(etScheduleTime.getText().toString(), etScheduleTime, getActivity());
            }
        });

        switchRemindMe.setOnCheckedChangeListener(new SwitchView.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchView switchView, boolean isChecked) {

                if (isChecked) {
                    remindMe = "1";
                } else {
                    remindMe = "0";
                }

            }
        });


        spPerDayScheduling.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DayScheduling item = (DayScheduling) parent.getItemAtPosition(position);
                perDayID = item.getId();
                spPerDayScheduling.setSelection((item.getTitle() != null) ? perDaySchedulingAdapter.getItemPosition(item.getTitle()) : 0);
                Logger.d(TAG, TAG + " >>> " + "perDayID " + perDayID);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spSchedulingType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DayScheduling item = (DayScheduling) parent.getItemAtPosition(position);
                schedulingType = item.getTitle();
                spSchedulingType.setSelection((item.getTitle() != null) ? schedulingTypeAdapter.getItemPosition(item.getTitle()) : 0);
                Logger.d(TAG, TAG + " >>> " + "schedulingType " + schedulingType);
                if (schedulingType.equalsIgnoreCase(getString(R.string.txt_medicine_remainder_schedule_type_monthly))) {
                    linStartFrom.setVisibility(View.VISIBLE);
                    linTimeView.setVisibility(View.GONE);
                } else {
                    radioButtonAction();
                    linStartFrom.setVisibility(View.GONE);
                    linTimeView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAddReminder.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                if (IsValidationInfo()) {
                    if (mFlavorType == FlavorType.CUSTOMER && customerOrSupplierId != null && customerOrSupplierId != "") {

                        if (schedulingType.equalsIgnoreCase(getString(R.string.txt_medicine_remainder_schedule_type_monthly))) {
                            startFrom = DateManager.convertDateTime(etStartFrom.getText().toString(), "dd MMM yyyy", "yyyy-MM-dd");
                            startFromDay = "";
                        } else {
                            startFrom = "";
                        }
                        ParamsAddReminder mParamsAddReminder = new ParamsAddReminder(id, customerOrSupplierId, medicinesId, etMedicineName.getText().toString(), etMedicineQty.getText().toString(),
                                String.valueOf(scheduleTime), schedulingType, startFromDay, remindMe, perDayID, getStartFromDates);
                        Logger.d(TAG, TAG + " >>> " + "mParamsAddReminder: " + mParamsAddReminder.toString());
                        doAddRemindersTask = new DoAddRemindersTask(getActivity(), mParamsAddReminder);
                        doAddRemindersTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }

                }
            }
        });
        radioButtonAction();

        initTextWatcherComponent();
    }


    /************************
     *  This method will be called
     *  when the input is  changed
     *  by  TextWatcher
     ************************/

    private void initTextWatcherComponent() {


        etMedicineName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("onTextChanged", "onTextChanged:\t" + s.toString());
                try {

                    String medicineName = etMedicineName.getText().toString();
                    Logger.d(TAG, TAG + " >>> " + "mMedicineName: " + medicineName);

                    if (s.toString().equalsIgnoreCase("") || s.toString().length() == 0) {
                        AppUtil.loadImage(getActivity(), ivMedicine, R.mipmap.capsule, false, false, true);
                    } else {
                        if (medicineByItem != null && medicineByItem.getName() != null) {
                            if (medicineName.equalsIgnoreCase(AppUtil.optStringNullCheckValue(medicineByItem.getName()))) {
                                AppUtil.loadImage(getActivity(), ivMedicine, AppUtil.optStringNullCheckValue(medicineByItem.getProduct_image()), false, false, true);
                            } else {
                                AppUtil.loadImage(getActivity(), ivMedicine, R.mipmap.capsule, false, false, true);
                            }
                        } else {
                            AppUtil.loadImage(getActivity(), ivMedicine, R.mipmap.capsule, false, false, true);
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e("beforeTextChanged", "beforeTextChanged:\t" + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("TextWatcherTest", "afterTextChanged:\t" + s.toString());

            }
        });

    }

    public void allFilterSessionLeft() {
        try {
            String getDate = DateManager.convertDateTime(etFromDate.getText().toString(), "dd MMM yyyy", "yyyy-MM-dd");

            if (getDate != null) {
                Logger.d(TAG, TAG + " >>> " + "getDate " + getDate);
                filterSessionLeftListAdapter.clear();
                getAllFilterSessionLeftTask = new GetAllFilterSessionLeftTask(getActivity(), getDate);
                getAllFilterSessionLeftTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void radioButtonAction() {
//               if (schedulingType.equalsIgnoreCase(getString(R.string.txt_medicine_remainder_schedule_type_daily))) {
//            if (rgStartFromDate.isEnabled()) {
//                rgStartFromDate.setEnabled(false);
//            }
//            for (int i = 0; i < rgStartFromDate.getChildCount(); i++) {
//                ((RadioButton) rgStartFromDate.getChildAt(i)).setEnabled(false);
//                rbSat.setChecked(false);
//                rbSun.setChecked(false);
//                rbMon.setChecked(false);
//                rbTus.setChecked(false);
//                rbThu.setChecked(false);
//                rbWed.setChecked(false);
//                rbFri.setChecked(false);
//            }
//        } else {
//            rgStartFromDate.setEnabled(true);
//            rbSat.setChecked(true);
//            rbSun.setChecked(true);
//            rbMon.setChecked(true);
//            rbTus.setChecked(true);
//            rbThu.setChecked(true);
//            rbWed.setChecked(true);
//            rbFri.setChecked(true);
//            for (int i = 0; i < rgStartFromDate.getChildCount(); i++) {
//                ((RadioButton) rgStartFromDate.getChildAt(i)).setEnabled(true);
//            }
//            rgStartFromDate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//
//                @Override
//                public void onCheckedChanged(RadioGroup group, int checkedId) {
//                    int rgCheckedId = rgStartFromDate.getCheckedRadioButtonId();
//                    Logger.d(TAG, TAG + " >>> " + "rgCheckedId " + rgCheckedId);
//
//                    if (rgCheckedId != -1) {
//                        switch (rgCheckedId) {
//                            case R.id.rb_sat:
//                                startFromDay = getString(R.string.title_medicine_remainder_sat);
//                                break;
//                            case R.id.rb_sun:
//                                startFromDay = getString(R.string.title_medicine_remainder_sun);
//                                break;
//                            case R.id.rb_mon:
//                                startFromDay = getString(R.string.title_medicine_remainder_mon);
//                                break;
//                            case R.id.rb_tus:
//                                startFromDay = getString(R.string.title_medicine_remainder_tus);
//                                break;
//                            case R.id.rb_wed:
//                                startFromDay = getString(R.string.title_medicine_remainder_wed);
//                                break;
//                            case R.id.rb_thu:
//                                startFromDay = getString(R.string.title_medicine_remainder_thu);
//                                break;
//                            case R.id.rb_fri:
//                                startFromDay = getString(R.string.title_medicine_remainder_fri);
//                                break;
//                            default:
//                                startFromDay = "";
//                                break;
//                        }
//                        Logger.d(TAG, TAG + " >>> " + "startFromDay " + startFromDay);
//
//                    } else {
//                        Toast.makeText(getActivity(), getResources().getString(R.string.toast_select_your_start_date_mgs), Toast.LENGTH_SHORT).show();
//
//                    }
//                }
//            });
//        }

        rgStartFromDate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int rgCheckedId = rgStartFromDate.getCheckedRadioButtonId();
                Logger.d(TAG, TAG + " >>> " + "rgCheckedId " + rgCheckedId);

                if (rgCheckedId != -1) {
                    switch (rgCheckedId) {
                        case R.id.rb_sat:
                            startFromDay = getString(R.string.title_medicine_remainder_sat).toLowerCase();
                            break;
                        case R.id.rb_sun:
                            startFromDay = getString(R.string.title_medicine_remainder_sun).toLowerCase();
                            break;
                        case R.id.rb_mon:
                            startFromDay = getString(R.string.title_medicine_remainder_mon).toLowerCase();
                            break;
                        case R.id.rb_tus:
                            startFromDay = getString(R.string.title_medicine_remainder_tus).toLowerCase();
                            break;
                        case R.id.rb_wed:
                            startFromDay = getString(R.string.title_medicine_remainder_wed).toLowerCase();
                            break;
                        case R.id.rb_thu:
                            startFromDay = getString(R.string.title_medicine_remainder_thu).toLowerCase();
                            break;
                        case R.id.rb_fri:
                            startFromDay = getString(R.string.title_medicine_remainder_fri).toLowerCase();
                            break;
//                        default:
//                            startFromDay = "";
//                            break;
                    }
                    Logger.d(TAG, TAG + " >>> " + "startFromDay " + startFromDay);

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_select_your_start_date_mgs), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void radioButtonSelected() {
        if (startFromDay.equals(getString(R.string.title_medicine_remainder_sat).toLowerCase()) || startFromDay.equals(getString(R.string.title_medicine_remainder_sat).toUpperCase())) {
            rgStartFromDate.check(R.id.rb_sat);
        } else if (startFromDay.equals(getString(R.string.title_medicine_remainder_sun).toLowerCase()) || startFromDay.equals(getString(R.string.title_medicine_remainder_sun).toUpperCase())) {
            rgStartFromDate.check(R.id.rb_sun);
        } else if (startFromDay.equals(getString(R.string.title_medicine_remainder_mon).toLowerCase()) || startFromDay.equals(getString(R.string.title_medicine_remainder_mon).toUpperCase())) {
            rgStartFromDate.check(R.id.rb_mon);
        } else if (startFromDay.equals(getString(R.string.title_medicine_remainder_tus).toLowerCase()) || startFromDay.equals(getString(R.string.title_medicine_remainder_tus).toUpperCase())) {
            rgStartFromDate.check(R.id.rb_tus);
        } else if (startFromDay.equals(getString(R.string.title_medicine_remainder_wed).toLowerCase()) || startFromDay.equals(getString(R.string.title_medicine_remainder_wed).toUpperCase())) {
            rgStartFromDate.check(R.id.rb_wed);

        } else if (startFromDay.equals(getString(R.string.title_medicine_remainder_thu).toLowerCase()) || startFromDay.equals(getString(R.string.title_medicine_remainder_thu).toUpperCase())) {
            rgStartFromDate.check(R.id.rb_thu);

        } else if (startFromDay.equals(getString(R.string.title_medicine_remainder_fri).toLowerCase()) || startFromDay.equals(getString(R.string.title_medicine_remainder_fri).toUpperCase())) {
            rgStartFromDate.check(R.id.rb_fri);

        } else {
            rgStartFromDate.check(R.id.rb_sat);

        }
        radioButtonCheck();
    }

    private void radioButtonCheck() {
        if (rbSat.isChecked()) {
            startFromDay = getString(R.string.title_medicine_remainder_sat).toLowerCase();

        } else if (rbSun.isChecked()) {
            startFromDay = getString(R.string.title_medicine_remainder_sun).toLowerCase();

        } else if (rbMon.isChecked()) {
            startFromDay = getString(R.string.title_medicine_remainder_mon).toLowerCase();

        } else if (rbTus.isChecked()) {
            startFromDay = getString(R.string.title_medicine_remainder_tus).toLowerCase();

        } else if (rbWed.isChecked()) {
            startFromDay = getString(R.string.title_medicine_remainder_wed).toLowerCase();

        } else if (rbThu.isChecked()) {
            startFromDay = getString(R.string.title_medicine_remainder_thu).toLowerCase();

        } else if (rbFri.isChecked()) {
            startFromDay = getString(R.string.title_medicine_remainder_fri).toLowerCase();

        }

        Logger.d(TAG, TAG + "<< >>> " + "startFromDay: " + startFromDay);

    }

    private void getDateCompare() {
        Date todayDate = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        String today = formatter.format(todayDate);
        //dateValue = AppUtil.compareDates(today, str);
        etFromDate.setText(today);
        etStartFrom.setText(today);
        // Default time set
        String timeString = DateManager.getTimeStringFromDate(DateManager.getDateTimeNowbyAddingTime(0)); //"08:00 AM"
        etScheduleTime.setText(timeString);

        getStartFromDates = DateManager.convertDateTime(etFromDate.getText().toString(), "dd MMM yyyy", "yyyy-MM-dd");
        if (getStartFromDates != null) {
            Logger.d(TAG, TAG + " >>> " + "etFromDate " + etFromDate.getText().toString());
            Logger.d(TAG, TAG + " >>> " + "getDate " + getStartFromDates);
            getAllFilterSessionLeftTask = new GetAllFilterSessionLeftTask(getActivity(), getStartFromDates);
            getAllFilterSessionLeftTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
//        reminderAlarmSet();
    }

//    private void reminderAlarmSet() {
//        try {
//            Date todayDate = Calendar.getInstance().getTime();
//            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//            String getTodayDate = formatter.format(todayDate);
//
//            if (getTodayDate != null && ((TextUtils.isEmpty(SessionUtil.getMedicineReminderDate(getActivity())) || getTodayDate.equalsIgnoreCase(SessionUtil.getMedicineReminderDate(getActivity()))) && !SessionUtil.isTodaysMedicineReminderSet(getActivity()))) {
//                Logger.d(TAG, TAG + " >>> " + "getTodayDate " + getTodayDate);
//
//                Intent intentMedicineReminder = new Intent(getActivity(), MedicineReminderService.class);
//                startService(intentMedicineReminder);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    private void initGetTagValue() {
        String userType = SessionManager.getStringSetting(getActivity(), SessionUtil.SESSION_KEY_USER_TAG);
        if (!AllSettingsManager.isNullOrEmpty(userType)) {
            mFlavorType = FlavorType.getFlavor(SessionManager.getStringSetting(getActivity(), SessionUtil.SESSION_KEY_USER_TAG));
        } else {
            mFlavorType = FlavorType.SUPPLIER;
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


    private void visibleTabLayout(MedicineRemainderTabType remainderTabeTabType) {

        if (remainderTabeTabType != null) {
            if (remainderTabeTabType == MedicineRemainderTabType.ADD_REMAINDER) {
                tvAddRemainder.setTypeface(null, Typeface.BOLD);
                tvPillboxs.setTypeface(null, Typeface.NORMAL);
                tvSessionLeft.setTypeface(null, Typeface.NORMAL);
                linAddMedicineRemainder.setVisibility(View.VISIBLE);
                linPillbox.setVisibility(View.GONE);
                linSessionLeft.setVisibility(View.GONE);
                linNoDataFound.setVisibility(View.GONE);

            } else if (remainderTabeTabType == MedicineRemainderTabType.PILLBOX) {
                tvAddRemainder.setTypeface(null, Typeface.NORMAL);
                tvPillboxs.setTypeface(null, Typeface.BOLD);
                tvSessionLeft.setTypeface(null, Typeface.NORMAL);
                linAddMedicineRemainder.setVisibility(View.GONE);
                linPillbox.setVisibility(View.VISIBLE);
                linSessionLeft.setVisibility(View.GONE);
                linNoDataFound.setVisibility(View.GONE);
                resetField();
                getAllReminderListsByCustomerTask = new GetAllReminderListsByCustomerTask(getActivity());
                getAllReminderListsByCustomerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else if (remainderTabeTabType == MedicineRemainderTabType.SESSION) {
                tvAddRemainder.setTypeface(null, Typeface.NORMAL);
                tvPillboxs.setTypeface(null, Typeface.NORMAL);
                tvSessionLeft.setTypeface(null, Typeface.BOLD);
                linAddMedicineRemainder.setVisibility(View.GONE);
                linPillbox.setVisibility(View.GONE);
                linSessionLeft.setVisibility(View.VISIBLE);
                linNoDataFound.setVisibility(View.GONE);
                resetField();
            } else {
                tvAddRemainder.setTypeface(null, Typeface.BOLD);
                tvPillboxs.setTypeface(null, Typeface.NORMAL);
                tvSessionLeft.setTypeface(null, Typeface.NORMAL);
                linAddMedicineRemainder.setVisibility(View.VISIBLE);
                linPillbox.setVisibility(View.GONE);
                linSessionLeft.setVisibility(View.GONE);
                linNoDataFound.setVisibility(View.GONE);

            }
        }

    }

    private void initFilterSessionLeftView(List<FilterSessionLeft> data) {
        if (data.size() > 0) {
            linNoDataFound.setVisibility(View.GONE);
            filterSessionLeftListAdapter.addAll(data);
        } else {
            linNoDataFound.setVisibility(View.VISIBLE);
            tvNoDataFound.setText(getString(R.string.txt_reminder_session_not_found));

        }
    }


    private void initRemainderView(List<Remainder> data) {
        if (data.size() > 0) {
            linNoDataFound.setVisibility(View.GONE);
            mSwipeRemainderAdapter.setReminder(data);

        } else {
            linNoDataFound.setVisibility(View.VISIBLE);
            tvNoDataFound.setText(getString(R.string.txt_reminder_not_found));

        }
    }


    private boolean IsValidationInfo() {
        String medicineName = etMedicineName.getText().toString();
        String medicineQty = etMedicineQty.getText().toString();
        String time = etScheduleTime.getText().toString();
        if (AllSettingsManager.isNullOrEmpty(medicineName)) {
            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_medicine_name), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (AllSettingsManager.isNullOrEmpty(medicineQty)) {
            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_medicine_qty), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (AllSettingsManager.isNullOrEmpty(time)) {
            Toast.makeText(getActivity(), getString(R.string.toast_please_input_your_scheduling_time), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            scheduleTime = AppUtil.getTimeConvertToSecond(etScheduleTime.getText().toString());
        }

        if (schedulingType.equalsIgnoreCase(getString(R.string.txt_medicine_remainder_schedule_type_daily)) || schedulingType.equalsIgnoreCase(getString(R.string.txt_medicine_remainder_schedule_type_weekly))) {
            if (AllSettingsManager.isNullOrEmpty(startFromDay)) {
                Toast.makeText(getActivity(), getString(R.string.toast_please_select_start_from_date), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public void deleteRemainder(final Remainder remainder, int pos) {
        Logger.d(TAG, TAG + "remainder >>> " + remainder.toString());

        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
        } else {
            if (remainder != null) {
                doDeleteRemainderTask = new DoDeleteRemainderTask(getActivity(), remainder, pos);
                doDeleteRemainderTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

        }
    }

    public void updateRemainder(Remainder remainders) {
        if (remainders != null) {
            remainderTabType = MedicineRemainderTabType.ADD_REMAINDER;
            visibleTabLayout(remainderTabType);
            Logger.d(TAG, TAG + " >>> " + "remainders>>>" + remainders.toString());
            id = remainders.getId();
            medicinesId = remainders.getMedicines_id();
            if (!AllSettingsManager.isNullOrEmpty(remainders.getName())) {
                etMedicineName.setText(remainders.getName());
            }
            if (!AllSettingsManager.isNullOrEmpty(remainders.getQuantity())) {
                etMedicineQty.setText(remainders.getQuantity());
            }

            if (!AllSettingsManager.isNullOrEmpty(remainders.getSchedule_time())) {
                scheduleTime = Integer.parseInt(remainders.getSchedule_time());
            }

            if (!AllSettingsManager.isNullOrEmpty(remainders.getSchudule_type())) {
                schedulingType = remainders.getSchudule_type();
                spSchedulingType.setSelection((remainders.getSchudule_type() != null) ? schedulingTypeAdapter.getItemPosition(remainders.getSchudule_type()) : 0);
            }

            if (!AllSettingsManager.isNullOrEmpty(remainders.getStart_from_day())) {
                startFromDay = remainders.getStart_from_day();
            }

            if (!AllSettingsManager.isNullOrEmpty(remainders.getHow_many_per_day())) {
                perDayID = remainders.getHow_many_per_day();
                spPerDayScheduling.setSelection((remainders.getHow_many_per_day() != null) ? perDaySchedulingAdapter.getItemPosition(remainders.getHow_many_per_day()) : 0);
            }

            AppUtil.loadImage(getActivity(), ivMedicine, remainders.getProduct_image(), false, false, true);
            if (schedulingType.equalsIgnoreCase(getString(R.string.txt_medicine_remainder_schedule_type_daily)) || schedulingType.equalsIgnoreCase(getString(R.string.txt_medicine_remainder_schedule_type_weekly))) {
                radioButtonSelected();
            }
        }
    }

    public void updateStatusReminder(FilterSessionLeft filterSessionLeft) {
        if (doUpdateStatusRemindersTask != null && doUpdateStatusRemindersTask.getStatus() == AsyncTask.Status.RUNNING) {
            doUpdateStatusRemindersTask.cancel(true);
        }
        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
        } else {
            if (filterSessionLeft != null) {
                String getDateUpdate = DateManager.convertDateTime(etFromDate.getText().toString(), "dd MMM yyyy", "yyyy-MM-dd");

                doUpdateStatusRemindersTask = new DoUpdateStatusRemindersTask(getActivity(), filterSessionLeft, getDateUpdate);
                doUpdateStatusRemindersTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

        }

    }

    private void resetField() {
        etMedicineName.setText("");
        etMedicineQty.setText("");
        String timeString = DateManager.getTimeStringFromDate(DateManager.getDateTimeNowbyAddingTime(0)); //"08:00 AM"
        etScheduleTime.setText(timeString);
        perDayID = "1";
        remindMe = "1";
        schedulingType = getString(R.string.txt_medicine_remainder_schedule_type_daily);
        getStartFromDates = "";
        startFromDay = "";
        medicinesId = "";
        id = "0";
        spSchedulingType.setSelection(0);
        spPerDayScheduling.setSelection(0);

    }

    private void setData(StaggeredMedicineByItem medicineByItem) {
        if (medicineByItem != null && medicineByItem.getName() != null) {
            etMedicineName.setText(AppUtil.optStringNullCheckValue(medicineByItem.getName()));
            etMedicineName.setSelection(etMedicineName.getText().length());
            etMedicineQty.setText("1");
            AppUtil.loadImage(getActivity(), ivMedicine, medicineByItem.getProduct_image(), false, false, true);
            medicinesId = medicineByItem.getId();

        }
    }

    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AllConstants.INTENT_REQUEST_CODE_REMINDER && resultCode == RESULT_OK) {
            Parcelable mParcelable = data.getParcelableExtra(AllConstants.INTENT_KEY_PRODUCT_ITEM);
            if (mParcelable != null) {
                medicineByItem = Parcels.unwrap(mParcelable);
                Logger.d(TAG, TAG + "<< >>> " + "medicineByItem: " + medicineByItem.toString());
                setData(medicineByItem);
                if (schedulingType.equalsIgnoreCase(getString(R.string.txt_medicine_remainder_schedule_type_daily)) || schedulingType.equalsIgnoreCase(getString(R.string.txt_medicine_remainder_schedule_type_weekly))) {
                    radioButtonCheck();
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
        //   super.onDestroy();
        dismissPopupDialog();
        if (getAllFilterSessionLeftTask != null && getAllFilterSessionLeftTask.getStatus() == AsyncTask.Status.RUNNING) {
            getAllFilterSessionLeftTask.cancel(true);
        }

        if (getAllReminderListsByCustomerTask != null && getAllReminderListsByCustomerTask.getStatus() == AsyncTask.Status.RUNNING) {
            getAllReminderListsByCustomerTask.cancel(true);
        }

        if (doDeleteRemainderTask != null && doDeleteRemainderTask.getStatus() == AsyncTask.Status.RUNNING) {
            doDeleteRemainderTask.cancel(true);
        }

        if (doUpdateStatusRemindersTask != null && doUpdateStatusRemindersTask.getStatus() == AsyncTask.Status.RUNNING) {
            doUpdateStatusRemindersTask.cancel(true);
        }
        stopReceiver();

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

    @Override
    public void onTriggered() {
        Logger.d(TAG, "Alarm triggered");

        // Register for Wifi scan results
        synchronized (this) {
            Logger.d(TAG, "Registering receiver");

//            IntentFilter mIntentFilter = new IntentFilter(TAG);
//            mIntentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
//            mIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//            if (isSDKAtLeastP()) {
//                mIntentFilter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
//            }

            getActivity().registerReceiver(mAlarmBroadcast, new IntentFilter(TAG));
            mRegistered = true;
        }

    }

    private boolean isSDKAtLeastP() {
        return Build.VERSION.SDK_INT >= 28;
    }

    public void startReceiver(long POLLING_INTERVAL) {
        Logger.d(TAG, "Setting alarm");
        mAlarm.set(POLLING_INTERVAL, mIsRepeatable);
    }

    public void stopReceiver() {
        mAlarm.cancel();
        synchronized (this) {
            if (mRegistered) {
                getActivity().unregisterReceiver(mAlarmBroadcast);
                mRegistered = false;
            }
        }
    }


    /*****************************
     * BroadcastReceiver *
     *****************************/
    BroadcastReceiver mAlarmBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Parse action
            if (intent == null) {
                return;
            }
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            Logger.d(TAG, "Got action: " + intent.getAction());
            if (intent != null) {
                Logger.d(TAG, TAG + ">>mAlarmBroadcast>> ");

            }

            synchronized (this) {
                if (mRegistered)
                    context.unregisterReceiver(this);
                mRegistered = false;
            }
        }
    };

    /************************
     * Server communication *
     ************************/

    private class GetAllFilterSessionLeftTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        String getDates;

        private GetAllFilterSessionLeftTask(Context context, String getDate) {
            mContext = context;
            getDates = getDate;
        }

        @Override
        protected void onPreExecute() {
//            ProgressDialog progressDialog = showProgressDialog();
//            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                    cancel(true);
//                }
//            });
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

                Call<APIResponse<List<FilterSessionLeft>>> call = mApiInterface.apiGetAllFilterSessionLeftList(getDates, customerOrSupplierId);
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
                    Logger.d(TAG, "APIResponse(GetAllFilterSessionLeftTask): onResponse-server = " + result.toString());
                    APIResponse<List<FilterSessionLeft>> data = (APIResponse<List<FilterSessionLeft>>) result.body();
                    Logger.d("GetAllFilterSessionLeftTask", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(GetAllFilterSessionLeftTask()): onResponse-object = " + data.toString());

                        initFilterSessionLeftView(data.getData());
                    } else {
                        linNoDataFound.setVisibility(View.VISIBLE);
                        tvNoDataFound.setText(getString(R.string.txt_reminder_session_not_found));

                    }
                } else {
                    // loadOfflineTimeData();
                    Logger.d(TAG, "onResponse-object = result" + result.message());

                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }


    private class GetAllReminderListsByCustomerTask extends AsyncTask<String, Integer, Response> {

        Context mContext;

        private GetAllReminderListsByCustomerTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
//            ProgressDialog progressDialog = showProgressDialog();
//            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                    cancel(true);
//                }
//            });
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
                Call<APIResponse<List<Remainder>>> call = mApiInterface.apiGetReminderListsByCustomer(customerOrSupplierId);
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
                    Logger.d(TAG, "APIResponse(GetAllReminderListsByCustomerTask): onResponse-server = " + result.toString());
                    Logger.d("GetAllReminderListsByCustomerTask", result.message() + "message");

                    APIResponse<List<Remainder>> data = (APIResponse<List<Remainder>>) result.body();
                    Logger.d("GetAllReminderListsByCustomerTask", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(GetAllReminderListsByCustomerTask()): onResponse-object = " + data.toString());

                        initRemainderView(data.getData());
                    } else {
                        linNoDataFound.setVisibility(View.VISIBLE);
                        tvNoDataFound.setText(getString(R.string.txt_reminder_not_found));
                    }
                } else {
                    // loadOfflineTimeData();
                    Logger.d(TAG, "onResponse-object = result" + result.message());

                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private class DoDeleteRemainderTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        Remainder remainders;
        int position;

        private DoDeleteRemainderTask(Context context, Remainder remainder, int pos) {
            mContext = context;
            remainders = remainder;
            position = pos;

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
                ParamsDeleteNotification mParamsDeleteRemainder = new ParamsDeleteNotification(remainders.getId());
                Logger.d(TAG, TAG + " >>> APIResponse(mParamsDeleteRemainder): " + mParamsDeleteRemainder.toString());
                Call<APIResponse> call = mApiInterface.apiDeleteReminder(mParamsDeleteRemainder);
                Response response = call.execute();
                Logger.d("DoDeleteRemainderTask", response + "response");

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
                Logger.d("isSuccessful", result.isSuccessful() + "");

                if (result.isSuccessful()) {
                    Logger.d(TAG, "APIResponse(DoDeleteRemainderTask): onResponse-server = " + result.toString());
                    APIResponse data = (APIResponse) result.body();
                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d("position", position + "");

                        mSwipeRemainderAdapter.notifyItemRemoved(position);
                        // mNotificationsList.remove(notifications);
                        //   mNotifyOrderStatusAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), getResources().getString(R.string.toast_reminder_delete_mgs), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }


    private class DoAddRemindersTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        ParamsAddReminder mParamsAddReminders;

        private DoAddRemindersTask(Context context, ParamsAddReminder paramsAddReminder) {
            mContext = context;
            mParamsAddReminders = paramsAddReminder;

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
//                ParamsDeleteNotification mParamsDeleteNotification = new ParamsDeleteNotification(notifications.getId());
//                Logger.d(TAG, TAG + " >>> APIResponse(mParamsDeleteNotification): " + mParamsDeleteNotification.toString());
                Call<APIResponse> call = mApiInterface.apiAddMedicineReminder(mParamsAddReminders);
                Response response = call.execute();
                Logger.d("DoAddRemindersTask", response + "response");

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
                Logger.d("isSuccessful", result.isSuccessful() + "");

                if (result.isSuccessful()) {
                    Logger.d(TAG, "APIResponse(DoAddRemindersTask): onResponse-server = " + result.toString());
                    APIResponse data = (APIResponse) result.body();
                    Logger.d("DoAddRemindersTask", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        remainderTabType = MedicineRemainderTabType.PILLBOX;
                        visibleTabLayout(remainderTabType);
                        resetField();
                        Toast.makeText(getActivity(), getResources().getString(R.string.title_add_reminder_successful), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }


    }

    private class DoUpdateStatusRemindersTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        FilterSessionLeft mFilterSessionLeft;
        String mGetDateUpdate;

        private DoUpdateStatusRemindersTask(Context context, FilterSessionLeft filterSessionLeft, String getDateUpdate) {
            mContext = context;
            mFilterSessionLeft = filterSessionLeft;
            mGetDateUpdate = getDateUpdate;

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

                ParamsUpdateStatusReminder paramsUpdateStatusReminder = new ParamsUpdateStatusReminder(customerOrSupplierId, mGetDateUpdate, String.valueOf(mFilterSessionLeft.getTime()), "NEXT");
                Logger.d("paramsUpdateStatusReminder", paramsUpdateStatusReminder.toString() + "");

                Call<APIResponse> call = mApiInterface.apiUpdateStatusReminder(paramsUpdateStatusReminder);
                Response response = call.execute();
                Logger.d("DoUpdateStatusRemindersTask", response + "response");

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
                Logger.d("isSuccessful", result.isSuccessful() + "");

                if (result.isSuccessful()) {
                    Logger.d(TAG, "APIResponse(DoUpdateStatusRemindersTask): onResponse-server = " + result.toString());
                    APIResponse data = (APIResponse) result.body();
                    Logger.d("DoUpdateStatusRemindersTask", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, TAG + "<<<getStatus>>" + data.getStatus());

                        if (filterSessionLeftListAdapter != null) {
                            mFilterSessionLeft.setStatus("NEXT");
                            filterSessionLeftListAdapter.updateItem(mFilterSessionLeft);
                            Toast.makeText(getActivity(), getResources().getString(R.string.title_medicine_remainder_status_successful), Toast.LENGTH_SHORT).show();
                        }

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