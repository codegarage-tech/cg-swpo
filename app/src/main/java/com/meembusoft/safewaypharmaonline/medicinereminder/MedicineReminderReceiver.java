//package com.meembusoft.safewaypharmaonline.medicinereminder;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.media.RingtoneManager;
//import android.widget.Toast;
//
//import androidx.core.app.NotificationCompat;
//import androidx.core.content.ContextCompat;
//
//import com.meembusoft.safewaypharmaonline.R;
//import com.meembusoft.safewaypharmaonline.activity.HomeActivity;
//import com.meembusoft.safewaypharmaonline.activity.ReminderActivity;
//import com.meembusoft.safewaypharmaonline.util.Logger;
//
//import static android.app.NotificationManager.IMPORTANCE_HIGH;
//import static android.content.Context.NOTIFICATION_SERVICE;
//import static android.os.Build.VERSION.SDK_INT;
//import static android.os.Build.VERSION_CODES.O;
//import static com.meembusoft.safewaypharmaonline.medicinereminder.AlarmConstants.INTENT_KEY_SCHEDULE_DATA_ALARM_RECEIVER;
//import static com.meembusoft.safewaypharmaonline.medicinereminder.AlarmConstants.INTENT_KEY_SCHEDULE_DATA_CONTENT_INTENT;
//
//
///**
// * @author Md. Rashadul Alam
// */
//public class MedicineReminderReceiver extends BroadcastReceiver {
//
//    private ScheduleItem scheduleItem;
//    private String TAG = MedicineReminderReceiver.class.getSimpleName();
//    private static final String CHANNEL_ID = "alarm_channel";
//    private NotificationManager alarmNotificationManager;
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        scheduleItem = (ScheduleItem) BaseParcelable.toParcelable(intent.getByteArrayExtra(INTENT_KEY_SCHEDULE_DATA_ALARM_RECEIVER), ScheduleItem.CREATOR);
//        Logger.d(TAG, TAG + ">>onReceive>>scheduleItem: " + scheduleItem);
//        if (context == null) {
//            return;
//        }
//
//        if (scheduleItem == null) {
//            return;
//        }
//
//        // Send notification
//        long currentTime = System.currentTimeMillis();
//        Logger.d(TAG, TAG + ">>onReceive>>notification time: " + scheduleItem.getTimeInMillis());
//        Logger.d(TAG, TAG + ">>onReceive>>current time: " + currentTime);
//        long scheduleTime = scheduleItem.getTimeInMillis() + 1000;
//        Logger.d(TAG, TAG + ">>onReceive>>scheduleTime: " + scheduleTime);
//        Logger.d(TAG, TAG + ">>onReceive>>sendNotification on");
//
//        if (scheduleItem.getTimeInMillis() == currentTime || scheduleTime >= currentTime ) {
//            sendNotification(context, scheduleItem);
//            Logger.d(TAG, TAG + ">>onReceive>>sendNotification on");
//
//            Toast.makeText(context, "MY Notify :", Toast.LENGTH_LONG).show();
//            Logger.d(TAG, TAG + ">>MY Notify  notification as time is up");
//
//        } else {
//            Logger.d(TAG, TAG + ">>onReceive>>Skipping notification as time is up");
//
//        }
//    }
//
////    private void sendNotification(Context context, ScheduleItem scheduleItem) {
////        Intent intentNotification = new Intent(context, ReminderActivity.class);
////        intentNotification.putExtra(INTENT_KEY_SCHEDULE_DATA_CONTENT_INTENT, scheduleItem);
////       // intentNotification.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
////        PendingIntent pendingIntentNotification = PendingIntent.getActivity(context, scheduleItem.getId(), intentNotification, PendingIntent.FLAG_UPDATE_CURRENT);
////
////        Notification.BigTextStyle bigStyle = new Notification.BigTextStyle();
////        bigStyle.setBigContentTitle(scheduleItem.getTitle() + "(" + scheduleItem.getAlarmTime() + ")");
////        bigStyle.bigText(scheduleItem.getContent());
////        Notification n = new Notification.Builder(context)
////                .setSmallIcon(R.mipmap.ic_launcher)
////                .setContentTitle(scheduleItem.getTitle() + "(" + scheduleItem.getAlarmTime() + ")")
////                .setContentText(scheduleItem.getContent())
////                .setPriority(Notification.PRIORITY_MAX)
////                .setWhen(System.currentTimeMillis())
////                .setStyle(bigStyle)
////                .setContentIntent(pendingIntentNotification)
////                .setAutoCancel(true)
////                .build();
////
////        n.defaults |= Notification.DEFAULT_VIBRATE;
////        n.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
////        n.defaults |= Notification.DEFAULT_SOUND;
////        Toast.makeText(context, "<<<MY Notify :)", Toast.LENGTH_LONG).show();
////        Logger.d(TAG, TAG + "<<<>scheduleItem Notify"+scheduleItem.toString());
////
////        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
////        notificationManager.notify(scheduleItem.getId(), n);
////    }
//
//    //handle notification
//    private void sendNotification(Context context,ScheduleItem scheduleItem) {
//        alarmNotificationManager = (NotificationManager) context
//                .getSystemService(Context.NOTIFICATION_SERVICE);
//        Intent intentNotification = new Intent(context, ReminderActivity.class);
//        intentNotification.putExtra(INTENT_KEY_SCHEDULE_DATA_CONTENT_INTENT, scheduleItem);
//        //get pending intent
//        PendingIntent contentIntent = PendingIntent.getActivity(context,  scheduleItem.getId(),
//                intentNotification, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        //Create notification
//
//        createNotificationChannel(context);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
//        builder.setSmallIcon(R.mipmap.ic_launcher);
//        builder.setColor(ContextCompat.getColor(context, R.color.colorAccent));
//        builder.setContentTitle(scheduleItem.getTitle() + "(" + scheduleItem.getAlarmTime() + ")");
//        builder.setPriority(Notification.PRIORITY_MAX);
//        builder.setContentText(scheduleItem.getContent());
//        builder.setVibrate(new long[] {1000,500,1000,500,1000,500});
//        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//        builder.setAutoCancel(true);
//        builder.setPriority(Notification.PRIORITY_HIGH);
//        builder.setContentIntent(contentIntent);
//
////        manager.notify(id, builder.build());
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
//
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