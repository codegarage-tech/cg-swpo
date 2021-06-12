//package com.meembusoft.safewaypharmaonline.medicinereminder;
//
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.util.Log;
//
//import androidx.core.os.UserManagerCompat;
//
//import com.meembusoft.safewaypharmaonline.R;
//import com.meembusoft.safewaypharmaonline.util.Logger;
//import com.meembusoft.safewaypharmaonline.util.SessionUtil;
//
//import java.util.Calendar;
//
///**
// * @author Md. Rashadul Alam
// */
//public class AlarmBootReceiver extends BroadcastReceiver {
//
//    private static final String TAG = AlarmBootReceiver.class.getSimpleName();
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//
//        boolean bootCompleted = false;
//
//        //Check boot completed
//        if (intent != null) {
//            String action = intent.getAction();
//
//            if (action != null) {
//                Log.d(TAG, "Received action: " + action + ", user unlocked: " + UserManagerCompat.isUserUnlocked(context));
//
//                //Check boot type
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    bootCompleted = Intent.ACTION_LOCKED_BOOT_COMPLETED.equals(action);
//                } else {
//                    bootCompleted = Intent.ACTION_BOOT_COMPLETED.equals(action);
//                }
//            }
//        }
//        if (!bootCompleted) {
//            return;
//        }
//
//        //Set alarm
//        initDailyMedicineReminder(context);
//        startMedicineReminderService(context);
//    }
//
//    private void initDailyMedicineReminder(Context context) {
//        // Prepare schedule item
//        Calendar mAlertTime = Calendar.getInstance();
//        mAlertTime.set(Calendar.HOUR_OF_DAY, 1);
//        mAlertTime.set(Calendar.MINUTE, 0);
//        mAlertTime.set(Calendar.SECOND, 0);
//        String mDate = AlarmConstants.DATE_FORMAT.format(mAlertTime.getTime());
//        ScheduleItem scheduleItem = new ScheduleItem((int) System.currentTimeMillis(), context.getString(R.string.txt_medicine_reminder), context.getString(R.string.txt_please_take_your_medicine), mDate, mAlertTime.getTimeInMillis(), REPEAT_TYPE.DAILY);
//        Logger.d(TAG, TAG + ">>initDailyMedicineReminder>>scheduleItem: " + scheduleItem);
//        //Set daily alarm
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(context, PeriodicReminderReceiver.class);
//        intent.putExtra(AlarmConstants.INTENT_KEY_SCHEDULE_DATA_ALARM_RECEIVER, BaseParcelable.toByteArray(scheduleItem));
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, scheduleItem.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        long timeInMilliseconds = scheduleItem.getTimeInMillis();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMilliseconds, pendingIntent);
//        } else {
//            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMilliseconds, pendingIntent);
//        }
//        // Store daily alarm info
//        SessionUtil.setPeriodiServiceMedicineReminder(context, true);
//    }
//
//    private void startMedicineReminderService(Context context) {
//        try {
//            Logger.d(TAG, TAG + ">>startMedicineReminderService>>Starting service");
//            Intent intentMedicineReminder = new Intent(context, MedicineReminderService.class);
//            context.startService(intentMedicineReminder);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//}