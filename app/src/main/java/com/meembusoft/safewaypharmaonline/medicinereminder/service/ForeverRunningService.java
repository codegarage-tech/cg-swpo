package com.meembusoft.safewaypharmaonline.medicinereminder.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.meembusoft.safewaypharmaonline.BuildConfig;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.ReminderActivity;
import com.meembusoft.safewaypharmaonline.enumeration.FlavorType;
import com.meembusoft.safewaypharmaonline.model.AppUser;
import com.meembusoft.safewaypharmaonline.model.FilterSessionLeft;
import com.meembusoft.safewaypharmaonline.retrofit.APIClient;
import com.meembusoft.safewaypharmaonline.retrofit.APIInterface;
import com.meembusoft.safewaypharmaonline.retrofit.APIResponse;
import com.meembusoft.safewaypharmaonline.util.AllConstants;
import com.meembusoft.safewaypharmaonline.util.DateManager;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.util.SessionUtil;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Response;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.O;
import static android.app.NotificationManager.IMPORTANCE_HIGH;

public class ForeverRunningService extends Service {

    public static String TAG = ForeverRunningService.class.getSimpleName();
    private String todayDate = "";
    private GetAllFilterSessionLeftTask getAllFilterSessionLeftTask;
    private APIInterface mApiInterface;
    private String customerOrSupplierId = "";
    private static final String CHANNEL_ID = "alarm_channel";
    private NotificationManager alarmNotificationManager;
    public static final int NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        if (SDK_INT > O) {
            startMyOwnForeground();
        } else {
            startForeground(1, new Notification());
        }
    }

    @RequiresApi(O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Logger.d(TAG, "onTaskRemoved ");

        sendBroadCast(rootIntent);
    }

    public void sendBroadCast(Intent rootIntent) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarted.class);
        this.sendBroadcast(broadcastIntent);
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
//        if (SessionUtil.getUser(getApplicationContext()) != null) {
        Logger.d(TAG, "onDestroy Service");
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarted.class);
        this.sendBroadcast(broadcastIntent);
        sendBroadCast(broadcastIntent);
//        }

        if (getAllFilterSessionLeftTask != null && getAllFilterSessionLeftTask.getStatus() == AsyncTask.Status.RUNNING) {
            getAllFilterSessionLeftTask.cancel(true);
        }
    }

    public void startTimer() {
        mApiInterface = APIClient.getClient(getApplicationContext()).create(APIInterface.class);
        TimerTask timerTask = null;
        Timer timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Logger.d(TAG, "startTimer");

                Date date = Calendar.getInstance().getTime();
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                todayDate = formatter.format(date);
                Logger.d(TAG, TAG + " >>> " + "todayDate: " + todayDate);

                String sessionDate = SessionUtil.getDailyMedicineReminderInfo(getApplicationContext());
                Logger.d(TAG, TAG + " >>> " + "sessionDate: " + sessionDate);
                if (!TextUtils.isEmpty(sessionDate)) {
                    String[] info = sessionDate.split(File.separator);
                    if (info.length == 2) {
                        String storedDate = info[0];
                        boolean storedSuccess = Boolean.parseBoolean(info[1]);
                        Logger.d(TAG, TAG + " >>> " + "sessionDate>>date: " + storedDate);
                        Logger.d(TAG, TAG + " >>> " + "sessionDate>>success: " + storedSuccess);
                        if (!todayDate.equalsIgnoreCase(storedDate) || (todayDate.equalsIgnoreCase(storedDate) && !storedSuccess)) {
                            Logger.d(TAG, TAG + " >>> " + "Performing periodic request");
                            // Send api request for daily alarm data
                            fetchDailyReminderData();
                        } else {
                            Logger.d(TAG, TAG + " >>> " + "Already periodic request is done");

                            // Get saved time and notify user after check the time
                            String storedTime = SessionUtil.getDailyMedicineReminderTime(getApplicationContext());
                            if (!TextUtils.isEmpty(storedTime)) {
                                Logger.d(TAG, TAG + " >>> " + "storedTime: " + storedTime);
                                List<FilterSessionLeft> data = ((MedicineReminders) APIResponse.getResponseObject(storedTime, MedicineReminders.class)).getData();
                                if (data != null && data.size() > 0) {
                                    Logger.d(TAG, TAG + " >>> " + "storedTime>>reformed: " + data.size());

                                    String currentTime = DateManager.getTimeStringFromDate(new Date());
                                    String addedTime = DateManager.getAddedOrSubtractedTime(currentTime, 55);
                                    String subtractedTime = DateManager.getAddedOrSubtractedTime(currentTime, -55);
                                    Logger.d(TAG, TAG + " >>> " + "currentTime: " + currentTime);
                                    Logger.d(TAG, TAG + " >>> " + "currentTime>>add: " + addedTime);
                                    Logger.d(TAG, TAG + " >>> " + "currentTime>>subtract: " + subtractedTime);

                                    for(int i = 0; i < data.size(); i++) {
                                      //  for(FilterSessionLeft filterSessionLeft: data){
                                        Logger.d(TAG, TAG + " >>> " + "storedTime>>filterSessionLeft: " + data.get(i).toString());
                                        boolean isTimeBetween = DateManager.isTimeBetweenTwoTime(subtractedTime, addedTime, data.get(i).getFormattedTime());
                                        Logger.d(TAG, TAG + " >>> " + "currentTime>>between: " + isTimeBetween);
                                        if(isTimeBetween){
                                            // TODO: Show notification here.
                                            sendNotification(data.get(i),i);
                                            Logger.d(TAG, TAG + " >>> " + "currentTime>>: show notification here");
                                        }
                                    }

//                                   //  Test code
//                                    boolean isTimeBetween = DateManager.isTimeBetweenTwoTime(subtractedTime, addedTime, "09:50 PM");
//                                    Logger.d(TAG, TAG + " >>> " + "currentTime>>between: " + isTimeBetween);
//                                    if (isTimeBetween) {
//                                        // TODO: Show notification here.
//                                        sendNotification("09:50 PM");
//                                        Logger.d(TAG, TAG + " >>> " + "currentTime>>: show notification here");
//                                    }
                                }
                            }
                        }
                    }
                } else {
                    Logger.d(TAG, TAG + " >>> " + "NO session data is found, Performing periodic request");
                    // Send api request for daily alarm data
                    fetchDailyReminderData();
                }
            }
        };
        // For debug time is 20 second and release time is 1 minute
//        timer.schedule(timerTask, 101, (BuildConfig.DEBUG ? (20 * 1000) : (60 * 1000)));
        timer.schedule(timerTask, 101, (BuildConfig.DEBUG ? (60 * 1000) : (60 * 1000)));
    }

    private void fetchDailyReminderData() {
        FlavorType mFlavorType;
        String selectUserType = AllConstants.USER_TYPE_CUSTOMER;
        AppUser mAppUser = null;
        String userType = SessionManager.getStringSetting(getApplicationContext(), SessionUtil.SESSION_KEY_USER_TAG);
        if (!AllSettingsManager.isNullOrEmpty(userType)) {
            mFlavorType = FlavorType.getFlavor(SessionManager.getStringSetting(getApplicationContext(), SessionUtil.SESSION_KEY_USER_TAG));
        } else {
            mFlavorType = FlavorType.SUPPLIER;
        }
        if (mFlavorType != null) {
            switch (mFlavorType) {
                case CUSTOMER:
                    mAppUser = SessionUtil.getUser(getApplicationContext());
                    selectUserType = AllConstants.USER_TYPE_CUSTOMER;
                    if (mAppUser != null) {
                        customerOrSupplierId = mAppUser.getId();
                    }
                    break;
            }
            if (NetworkManager.isConnected(getApplicationContext()) && !TextUtils.isEmpty(customerOrSupplierId)) {
                // Do api request, pick data and set alarm
                if (getAllFilterSessionLeftTask != null && getAllFilterSessionLeftTask.getStatus() == AsyncTask.Status.RUNNING) {
                    getAllFilterSessionLeftTask.cancel(true);
                }
                getAllFilterSessionLeftTask = new GetAllFilterSessionLeftTask(getApplicationContext(), todayDate, customerOrSupplierId);
                getAllFilterSessionLeftTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            }
        }
    }

    //    //handle notification
    private void sendNotification(FilterSessionLeft filterSessionLeft, int index) {

        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        //get pending intent
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, ReminderActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        //Create notification

        createNotificationChannel(getApplicationContext());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
        builder.setContentTitle(getString(R.string.txt_medicine_reminder) + "(" + filterSessionLeft.getFormattedTime() + ")");
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setContentText(getString(R.string.txt_please_take_your_medicine));
        builder.setVibrate(new long[] {1000,500,1000,500,1000,500});
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.setContentIntent(contentIntent);

//        manager.notify(id, builder.build());
        List<String> getZId = filterSessionLeft.getId();
        alarmNotificationManager.notify(index+1, builder.build());

    }



    private static void createNotificationChannel(Context ctx) {
        if(SDK_INT < O) return;
        final NotificationManager mgr = ctx.getSystemService(NotificationManager.class);
        if(mgr == null) return;
        final String name = "Simple Alarm";
        if(mgr.getNotificationChannel(name) == null) {
            final NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, name, IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[] {1000,500,1000,500,1000,500});
            channel.setBypassDnd(true);
            mgr.createNotificationChannel(channel);
        }
    }

    private class GetAllFilterSessionLeftTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        String mTodayDate, mUserId;

        private GetAllFilterSessionLeftTask(Context context, String todayDate, String userId) {
            mContext = context;
            mTodayDate = todayDate;
            mUserId = userId;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Logger.d(TAG, TAG + ">> Background task is cancelled");
        }

        @Override
        protected Response doInBackground(String... params) {
            Logger.d(TAG, TAG + ">> doInBackground is called");
            try {
                Call<APIResponse<List<FilterSessionLeft>>> call = mApiInterface.apiGetAllFilterSessionLeftList(mTodayDate, mUserId);
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
                if (result != null && result.isSuccessful()) {
                    Logger.d(TAG, "APIResponse(MedicineReminderService): onResponse-server = " + result.toString());
                    Logger.d(TAG, "APIResponse(MedicineReminderService): onResponse-json = " + result.body());
                    APIResponse<List<FilterSessionLeft>> data = (APIResponse<List<FilterSessionLeft>>) result.body();
                    Logger.d("MedicineReminderService", data.toString() + "");
                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(MedicineReminderService()): onResponse-object = " + data.toString());
                        // Store daily medicine data
                        SessionUtil.setDailyMedicineReminderTime(getApplicationContext(), APIResponse.getResponseString(new MedicineReminders(data.getData())));
                        storeMedicineReminderInfo(mTodayDate, true);
                    } else {
                        storeMedicineReminderInfo(mTodayDate, false);
                    }
                } else {
                    storeMedicineReminderInfo(mTodayDate, false);
                    Logger.d(TAG, "onResponse-object = result" + result.message());
                }
            } catch (Exception exception) {
                Logger.d(TAG, "exception: " + exception.getMessage());
            }
        }
    }

    private void storeMedicineReminderInfo(String date, boolean isSuccess) {
        SessionUtil.setDailyMedicineReminderInfo(getApplicationContext(), date + File.separator + isSuccess);
    }
}