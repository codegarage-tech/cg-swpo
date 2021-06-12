//package com.meembusoft.safewaypharmaonline.medicinereminder;
//
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.media.RingtoneManager;
//import android.widget.Toast;
//
//import androidx.core.app.NotificationCompat;
//
//import com.meembusoft.safewaypharmaonline.R;
//import com.meembusoft.safewaypharmaonline.activity.ReminderActivity;
//import com.meembusoft.safewaypharmaonline.util.Logger;
//
//import java.util.Calendar;
//
//import static com.meembusoft.safewaypharmaonline.medicinereminder.AlarmConstants.DATE_FORMAT;
//import static com.meembusoft.safewaypharmaonline.medicinereminder.AlarmConstants.INTENT_ACTION_CREATE;
//import static com.meembusoft.safewaypharmaonline.medicinereminder.AlarmConstants.INTENT_KEY_SCHEDULE_DATA_ALARM_RECEIVER;
//import static com.meembusoft.safewaypharmaonline.medicinereminder.AlarmConstants.INTENT_KEY_SCHEDULE_DATA_ALARM_SERVICE;
//import static com.meembusoft.safewaypharmaonline.medicinereminder.AlarmConstants.INTENT_KEY_SCHEDULE_DATA_CONTENT_INTENT;
//
//
///**
// * @author Md. Rashadul Alam
// */
//public class PeriodicReminderReceiver extends BroadcastReceiver {
//
//    private ScheduleItem scheduleItem;
//    private String TAG = PeriodicReminderReceiver.class.getSimpleName();
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//
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
//        REPEAT_TYPE frequency = scheduleItem.getFrequency();
//        if (frequency != REPEAT_TYPE.NONE) {
//
//            Calendar repeatTime = Calendar.getInstance();
//            repeatTime.setTimeInMillis(scheduleItem.getTimeInMillis());
//
//            switch (frequency) {
//                case MINUTELY:
//                    repeatTime.add(Calendar.MINUTE, 1);
//                    break;
//                case HOURLY:
//                    repeatTime.add(Calendar.HOUR, 1);
//                    break;
//                case DAILY:
//                    repeatTime.add(Calendar.DATE, 1);
//                    break;
//                case WEEKLY:
//                    repeatTime.add(Calendar.DATE, 7);
//                    break;
//                case MONTHLY:
//                    repeatTime.add(Calendar.MONTH, 1);
//                    break;
//                case YEARLY:
//                    repeatTime.add(Calendar.YEAR, 1);
//                    break;
//            }
//
//            //Set updated schedule
//            String mDate = DATE_FORMAT.format(repeatTime.getTime());
//            ScheduleItem updatedSchedule = new ScheduleItem(scheduleItem.getId(), scheduleItem.getTitle(), scheduleItem.getContent(), mDate, repeatTime.getTimeInMillis(), scheduleItem.getFrequency());
//            Logger.d(TAG, TAG + ">>onReceive>>scheduleItem(updatedSchedule): " + updatedSchedule);
//            Toast.makeText(context, "ALARM!! ALARM!!", Toast.LENGTH_SHORT).show();
//
//            //Set alarm again for repeating
//            Logger.d(TAG, TAG + ">>onReceive>>Starting alarm service");
//            Intent setAlarm = new Intent(context, PeriodicReminderService.class);
//            setAlarm.putExtra(INTENT_KEY_SCHEDULE_DATA_ALARM_SERVICE, updatedSchedule);
//            setAlarm.setAction(INTENT_ACTION_CREATE);
//            context.startService(setAlarm);
//        }
//
//        // Start medicine reminder service
//        Logger.d(TAG, TAG + ">>onReceive>>Starting medicine reminder service");
//        Intent intentMedicineReminder = new Intent(context, MedicineReminderService.class);
//        context.startService(intentMedicineReminder);
//    }
//
//    private void sendNotification(Context context, ScheduleItem scheduleItem) {
//        Intent intentQuoteOfTheDay = new Intent(context, ReminderActivity.class);
//        intentQuoteOfTheDay.putExtra(INTENT_KEY_SCHEDULE_DATA_CONTENT_INTENT, scheduleItem);
////        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
////        stackBuilder.addParentStack(Scheduler.getContentClass());
////        stackBuilder.addNextIntent(result);
////        PendingIntent clicked = stackBuilder.getPendingIntent(scheduleItem.getId(), PendingIntent.FLAG_UPDATE_CURRENT);
//        intentQuoteOfTheDay.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pendingIntentQuoteOfTheDay = PendingIntent.getActivity(context, scheduleItem.getId(), intentQuoteOfTheDay, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Notification.BigTextStyle bigStyle = new Notification.BigTextStyle();
//        bigStyle.setBigContentTitle(scheduleItem.getTitle() + "(" + scheduleItem.getAlarmTime() + ")");
//        bigStyle.bigText(scheduleItem.getContent());
//        Notification n = new Notification.Builder(context)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(scheduleItem.getTitle() + "(" + scheduleItem.getAlarmTime() + ")")
//                .setContentText(scheduleItem.getContent())
//                .setPriority(Notification.PRIORITY_MAX)
//                .setWhen(System.currentTimeMillis())
//                .setStyle(bigStyle)
//                .setContentIntent(pendingIntentQuoteOfTheDay)
//                .setAutoCancel(true)
//                .build();
//
//        n.defaults |= Notification.DEFAULT_VIBRATE;
//        n.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        n.defaults |= Notification.DEFAULT_SOUND;
//
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(scheduleItem.getId(), n);
//    }
//}