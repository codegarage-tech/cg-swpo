//package com.meembusoft.safewaypharmaonline.medicinereminder;
//
//import android.app.AlarmManager;
//import android.app.IntentService;
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.media.RingtoneManager;
//import android.os.Build;
//import android.widget.Toast;
//
//import androidx.core.app.NotificationCompat;
//import androidx.core.content.ContextCompat;
//
//import com.meembusoft.safewaypharmaonline.R;
//import com.meembusoft.safewaypharmaonline.activity.HomeActivity;
//import com.meembusoft.safewaypharmaonline.util.Logger;
//
//import static android.app.NotificationManager.IMPORTANCE_HIGH;
//import static android.os.Build.VERSION.SDK_INT;
//import static android.os.Build.VERSION_CODES.O;
//import static com.meembusoft.safewaypharmaonline.medicinereminder.AlarmConstants.INTENT_ACTION_CANCEL;
//import static com.meembusoft.safewaypharmaonline.medicinereminder.AlarmConstants.INTENT_ACTION_CREATE;
//import static com.meembusoft.safewaypharmaonline.medicinereminder.AlarmConstants.INTENT_ACTION_DELETE;
//import static com.meembusoft.safewaypharmaonline.medicinereminder.AlarmConstants.INTENT_KEY_SCHEDULE_DATA_ALARM_RECEIVER;
//import static com.meembusoft.safewaypharmaonline.medicinereminder.AlarmConstants.INTENT_KEY_SCHEDULE_DATA_ALARM_SERVICE;
//import static com.meembusoft.safewaypharmaonline.medicinereminder.AlarmConstants.SESSION_KEY_SCHEDULE_DATA;
//
///**
// * @author Md. Rashadul Alam
// */
//public class PeriodicReminderService extends IntentService {
//
//    private IntentFilter matcher;
//    private ScheduleItem mScheduleItem;
//    private String TAG = PeriodicReminderService.class.getSimpleName();
//    private static final String CHANNEL_ID = "alarm_channel";
//    private NotificationManager alarmNotificationManager;
//
//    public PeriodicReminderService() {
//        super(PeriodicReminderService.class.getSimpleName());
//        matcher = new IntentFilter();
//        matcher.addAction(INTENT_ACTION_CREATE);
//        matcher.addAction(INTENT_ACTION_DELETE);
//        matcher.addAction(INTENT_ACTION_CANCEL);
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        String action = intent.getAction();
//        mScheduleItem = intent.getParcelableExtra(INTENT_KEY_SCHEDULE_DATA_ALARM_SERVICE);
//        Logger.d(TAG, TAG + "<<>>onHandleIntent>>scheduleItem: " + mScheduleItem);
//        if (mScheduleItem != null && matcher.matchAction(action)) {
//            executeSchedule(action, mScheduleItem);
//        }
//    }
//
//    private void executeSchedule(String action, ScheduleItem scheduleItem) {
//
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//
//        Intent intent = new Intent(this, PeriodicReminderReceiver.class);
//        intent.putExtra(INTENT_KEY_SCHEDULE_DATA_ALARM_RECEIVER, BaseParcelable.toByteArray(scheduleItem));
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, scheduleItem.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        if (INTENT_ACTION_CREATE.equals(action)) {
//
//            long timeInMilliseconds = scheduleItem.getTimeInMillis();
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMilliseconds, AlarmManager.INTERVAL_DAY, pendingIntent);
//
////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////                alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMilliseconds, pendingIntent);
////            } else {
////                alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMilliseconds, pendingIntent);
////            }
//            AlarmUtil.setStringSetting(this, SESSION_KEY_SCHEDULE_DATA, ScheduleItem.convertFromObjectToString(scheduleItem));
//            //sendNotification(scheduleItem);
//        } else if (INTENT_ACTION_DELETE.equals(action)) {
//
//            alarmManager.cancel(pendingIntent);
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.cancel(scheduleItem.getId());
//
//        } else if (INTENT_ACTION_CANCEL.equals(action)) {
//
//            alarmManager.cancel(pendingIntent);
//        }
//    }
//
//    //handle notification
//    private void sendNotification(ScheduleItem scheduleItem) {
//        alarmNotificationManager = (NotificationManager) this
//                .getSystemService(Context.NOTIFICATION_SERVICE);
//
//        //get pending intent
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, HomeActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
//
//        //Create notification
//
//        createNotificationChannel(this);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
//        builder.setSmallIcon(R.mipmap.ic_launcher);
//        builder.setColor(ContextCompat.getColor(this, R.color.colorAccent));
//        builder.setContentTitle(scheduleItem.getTitle() + scheduleItem.getAlarmTime());
//        builder.setContentText(scheduleItem.getContent());
//        builder.setVibrate(new long[] {1000,500,1000,500,1000,500});
//        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//        builder.setAutoCancel(true);
//        builder.setPriority(Notification.PRIORITY_HIGH);
//        builder.setContentIntent(contentIntent);
//
////        manager.notify(id, builder.build());
//        Toast.makeText(this, "createNotificationChannel!", Toast.LENGTH_SHORT).show();
////
////        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
////                this).setContentTitle("Alarm").setSmallIcon(R.mipmap.ic_launcher)
////                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
////                .setContentText(msg).setAutoCancel(true);
////        alamNotificationBuilder.setContentIntent(contentIntent);
//
//        //notiy notification manager about new notification
//        alarmNotificationManager.notify(scheduleItem.getId(), builder.build());
//    }
//
//    private static void createNotificationChannel(Context ctx) {
//        if(SDK_INT < O) return;
//
//        final NotificationManager mgr = ctx.getSystemService(NotificationManager.class);
//        if(mgr == null) return;
//
//        final String name = "Simple Alarm";
//        if(mgr.getNotificationChannel(name) == null) {
//            final NotificationChannel channel =
//                    new NotificationChannel(CHANNEL_ID, name, IMPORTANCE_HIGH);
//            channel.enableVibration(true);
//            channel.setVibrationPattern(new long[] {1000,500,1000,500,1000,500});
//            channel.setBypassDnd(true);
//            mgr.createNotificationChannel(channel);
//        }
//    }
//}
