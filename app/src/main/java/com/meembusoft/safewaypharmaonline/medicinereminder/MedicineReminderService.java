//package com.meembusoft.safewaypharmaonline.medicinereminder;
//
//import android.app.AlarmManager;
//import android.app.IntentService;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Handler;
//import android.text.TextUtils;
//
//import com.meembusoft.safewaypharmaonline.R;
//import com.meembusoft.safewaypharmaonline.enumeration.FlavorType;
//import com.meembusoft.safewaypharmaonline.model.AppUser;
//import com.meembusoft.safewaypharmaonline.model.FilterSessionLeft;
//import com.meembusoft.safewaypharmaonline.retrofit.APIClient;
//import com.meembusoft.safewaypharmaonline.retrofit.APIInterface;
//import com.meembusoft.safewaypharmaonline.retrofit.APIResponse;
//import com.meembusoft.safewaypharmaonline.util.AllConstants;
//import com.meembusoft.safewaypharmaonline.util.DateManager;
//import com.meembusoft.safewaypharmaonline.util.Logger;
//import com.meembusoft.safewaypharmaonline.util.SessionUtil;
//import com.reversecoder.library.network.NetworkManager;
//import com.reversecoder.library.storage.SessionManager;
//import com.reversecoder.library.util.AllSettingsManager;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Response;
//
//import static com.meembusoft.safewaypharmaonline.medicinereminder.AlarmConstants.INTENT_KEY_SCHEDULE_DATA_ALARM_RECEIVER;
//
//public class MedicineReminderService extends IntentService {
//    private APIInterface mApiInterface;
//    private String customerOrSupplierId = "";
//    FlavorType mFlavorType;
//    private String selectUserType = AllConstants.USER_TYPE_CUSTOMER;
//    private AppUser mAppUser;
//    private GetAllFilterSessionLeftTask getAllFilterSessionLeftTask;
//    public static final String TAG = "MedicineReminderService";
//
//    public MedicineReminderService() {
//        super("MedicineReminderService");
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
////        String message = intent.getStringExtra("message");
////        intent.setAction(MainActivity.FILTER_ACTION_KEY);
////
////        String resultMessage = "The Result String  after 6 seconds of processing.." + message;
////        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent.putExtra("broadcastMessage", resultMessage));
//        mApiInterface = APIClient.getClient(getApplicationContext()).create(APIInterface.class);
//
//        if (NetworkManager.isConnected(this)) {
//            // Get User Tag
//            initGetTagValue();
//        } else {
//            SessionUtil.setTodaysMedicineReminder(this, false);
//        }
//    }
//
//    @Override
//    public void onStart(Intent intent, int startId) {
//        super.onStart(intent, startId);
////         Toast.makeText(getApplicationContext(), "Intent SERVICE CREATED", Toast.LENGTH_SHORT).show();
//        Logger.d(TAG, TAG + " >MedicineReminderService>> Intent SERVICE CREATED");
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
////        if (getAllFilterSessionLeftTask != null && getAllFilterSessionLeftTask.getStatus() == AsyncTask.Status.RUNNING) {
////            getAllFilterSessionLeftTask.cancel(true);
////        }
////        final Handler handler = new Handler();
////        handler.postDelayed(new Runnable() {
////            @Override
////            public void run() {
////                if (getAllFilterSessionLeftTask != null && getAllFilterSessionLeftTask.getStatus() == AsyncTask.Status.RUNNING) {
////                    getAllFilterSessionLeftTask.cancel(true);
////                }
////            }
////        }, 4000);
////        Toast.makeText(getApplicationContext(), "Intent SERVICE DESTROYED", Toast.LENGTH_SHORT).show();
//        Logger.d(TAG, TAG + " >MedicineReminderService>> Intent SERVICE DESTROYED");
//    }
//
//    private void initGetTagValue() {
//        String userType = SessionManager.getStringSetting(getApplicationContext(), SessionUtil.SESSION_KEY_USER_TAG);
//        if (!AllSettingsManager.isNullOrEmpty(userType)) {
//            mFlavorType = FlavorType.getFlavor(SessionManager.getStringSetting(getApplicationContext(), SessionUtil.SESSION_KEY_USER_TAG));
//        } else {
//            mFlavorType = FlavorType.SUPPLIER;
//        }
//
//        if (mFlavorType != null) {
//            switch (mFlavorType) {
//                case CUSTOMER:
//                    mAppUser = SessionUtil.getUser(getApplicationContext());
//                    selectUserType = AllConstants.USER_TYPE_CUSTOMER;
//                    if (mAppUser != null) {
//                        customerOrSupplierId = mAppUser.getId();
//                    }
//                    break;
//            }
//            if (mAppUser != null && customerOrSupplierId != null && !TextUtils.isEmpty(customerOrSupplierId)) {
//                // Do api request, pick data and set alarm
//                allFilterSessionLeft();
//            } else {
//                SessionUtil.setTodaysMedicineReminder(this, false);
//            }
////            Logger.d(TAG, TAG + " MedicineReminderService>>> " + "customerOrSupplierId " + customerOrSupplierId);
//        }
//    }
//
//    public void allFilterSessionLeft() {
//        try {
//            Date todayDate = Calendar.getInstance().getTime();
//            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//            String getTodayDate = formatter.format(todayDate);
//            if (getTodayDate != null) {
//                Logger.d(TAG, TAG + " >>> " + "getTodayDate " + getTodayDate);
//                if (getAllFilterSessionLeftTask != null && getAllFilterSessionLeftTask.getStatus() == AsyncTask.Status.RUNNING) {
//                    getAllFilterSessionLeftTask.cancel(true);
//                }
//                getAllFilterSessionLeftTask = new GetAllFilterSessionLeftTask(getApplicationContext(), getTodayDate);
//                getAllFilterSessionLeftTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    private void setMedicineReminder(List<FilterSessionLeft> data, String getDates) {
//        if (data != null && data.size() > 0) {
//          AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            Intent intent = new Intent(this, MedicineReminderReceiver.class);
////
////            ////////////////////////// single alarm testing start /////////////////////////////
////            Logger.d(TAG, TAG + ">>setMedicineReminder>>getDates: " + getDates);
////            Date date = Calendar.getInstance().getTime();
////            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
////            String todayDate = formatter.format(date);
////            String formattedTime = todayDate + " " + "02:44 AM";
////            long getTimeinMillis = DateManager.getTransactionTime(formattedTime);
////            Logger.d(TAG, TAG + ">>setMedicineReminder>>formattedTime: " + formattedTime);
////            Logger.d(TAG, TAG + ">>setMedicineReminder>>getTimeinMillis: " + getTimeinMillis);
////
////            ScheduleItem scheduleItem = new ScheduleItem((int)getTimeinMillis, getString(R.string.txt_medicine_reminder), getString(R.string.txt_please_take_your_medicine), formattedTime, getTimeinMillis, REPEAT_TYPE.MINUTELY);
////            Logger.d(TAG, TAG + ">>setMedicineReminder>>scheduleItem: " + scheduleItem);
////            intent.putExtra(INTENT_KEY_SCHEDULE_DATA_ALARM_RECEIVER, BaseParcelable.toByteArray(scheduleItem));
////            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, scheduleItem.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
////
////            // Delete previous set reminder
////            alarmManager.cancel(pendingIntent);
////            notificationManager.cancel(scheduleItem.getId());
////            Logger.d(TAG, TAG + ">>setMedicineReminder>>Cancelled previous set alarm");
////
////            // Set new reminder
////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////                alarmManager.setExact(AlarmManager.RTC_WAKEUP, getTimeinMillis, pendingIntent);
////            } else {
////                alarmManager.set(AlarmManager.RTC_WAKEUP, getTimeinMillis, pendingIntent);
////            }
//            ////////////////////////// single alarm testing end /////////////////////////////
//            for (FilterSessionLeft filterSessionLeft : data) {
//                Logger.d(TAG, TAG + ">>setMedicineReminder>>getDates: " + getDates);
//                Logger.d(TAG, TAG + ">>setMedicineReminder>>getFormattedTime: " + filterSessionLeft.getFormattedTime());
//
//                String alarmTime = getDates + " " + filterSessionLeft.getFormattedTime();
//                Logger.d(TAG, TAG + ">>setMedicineReminder>>alarmTime: " + alarmTime);
//                long getTimeinMillis = DateManager.getTransactionTime(alarmTime);
//                Logger.d(TAG, TAG + ">>setMedicineReminder>>getTimeinMillis: " + getTimeinMillis);
//
//                ScheduleItem scheduleItem = new ScheduleItem((int) getTimeinMillis, getString(R.string.txt_medicine_reminder), getString(R.string.txt_please_take_your_medicine), alarmTime, getTimeinMillis, REPEAT_TYPE.DAILY);
//                Logger.d(TAG, TAG + ">>setMedicineReminder>>scheduleItem: " + scheduleItem);
//                intent.putExtra(INTENT_KEY_SCHEDULE_DATA_ALARM_RECEIVER, BaseParcelable.toByteArray(scheduleItem));
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, scheduleItem.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                // Delete previous set reminder
//                alarmManager.cancel(pendingIntent);
//                notificationManager.cancel(scheduleItem.getId());
//
//                // Set new reminder
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, getTimeinMillis, pendingIntent);
//                } else {
//                    alarmManager.set(AlarmManager.RTC_WAKEUP, getTimeinMillis, pendingIntent);
//                }
//            }
//            if (getAllFilterSessionLeftTask != null && getAllFilterSessionLeftTask.getStatus() == AsyncTask.Status.RUNNING) {
//                getAllFilterSessionLeftTask.cancel(true);
//            }
//        }
//    }
//
//    /************************
//     * Server communication *
//     ************************/
//
//    private class GetAllFilterSessionLeftTask extends AsyncTask<String, Integer, Response> {
//
//        Context mContext;
//        String getDates;
//
//        private GetAllFilterSessionLeftTask(Context context, String getDate) {
//            mContext = context;
//            getDates = getDate;
//        }
//
//        @Override
//        protected void onPreExecute() {
////            ProgressDialog progressDialog = showProgressDialog();
////            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
////                @Override
////                public void onCancel(DialogInterface dialog) {
////                    cancel(true);
////                }
////            });
//            // showPopupDialog();
//        }
//
//        @Override
//        protected void onCancelled() {
//            super.onCancelled();
//            Logger.d(TAG, TAG + ">> Background task is cancelled");
//        }
//
//        @Override
//        protected Response doInBackground(String... params) {
//            try {
//
//                Call<APIResponse<List<FilterSessionLeft>>> call = mApiInterface.apiGetAllFilterSessionLeftList(getDates, customerOrSupplierId);
//                Response response = call.execute();
//                if (response.isSuccessful()) {
//                    return response;
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Response result) {
//            try {
//                //dismissPopupDialog();
//
//                if (result != null && result.isSuccessful()) {
//                    Logger.d(TAG, "APIResponse(MedicineReminderService): onResponse-server = " + result.toString());
//                    APIResponse<List<FilterSessionLeft>> data = (APIResponse<List<FilterSessionLeft>>) result.body();
//                    Logger.d("MedicineReminderService", data.toString() + "");
//
//                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
//                        Logger.d(TAG, "APIResponse(MedicineReminderService()): onResponse-object = " + data.toString());
//
//                        setMedicineReminder(data.getData(), getDates);
//                        SessionUtil.setTodaysMedicineReminder(mContext, true);
//                        SessionUtil.setMedicineReminderDate(mContext, getDates);
//                    } else {
//                        SessionUtil.setTodaysMedicineReminder(mContext, false);
//                    }
//                } else {
//                    // loadOfflineTimeData();
//                    SessionUtil.setTodaysMedicineReminder(mContext, false);
//
//                    Logger.d(TAG, "onResponse-object = result" + result.message());
//
//                }
//            } catch (Exception exception) {
//                exception.printStackTrace();
//            }
//        }
//    }
//
//}