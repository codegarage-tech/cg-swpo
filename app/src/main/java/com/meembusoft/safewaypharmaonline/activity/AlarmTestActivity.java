//package com.meembusoft.safewaypharmaonline.activity;
//
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//
//import com.meembusoft.safewaypharmaonline.R;
//import com.meembusoft.safewaypharmaonline.base.BaseActivity;
//import com.meembusoft.safewaypharmaonline.medicinereminder.AlarmConstants;
//import com.meembusoft.safewaypharmaonline.medicinereminder.BaseParcelable;
//import com.meembusoft.safewaypharmaonline.medicinereminder.PeriodicReminderReceiver;
//import com.meembusoft.safewaypharmaonline.medicinereminder.REPEAT_TYPE;
//import com.meembusoft.safewaypharmaonline.medicinereminder.ScheduleItem;
//
//import java.util.Calendar;
//
////import tech.codegarage.scheduler.enumeration.REPEAT_TYPE;
////import tech.codegarage.scheduler.model.BaseParcelable;
////import tech.codegarage.scheduler.model.ScheduleItem;
////
////import static tech.codegarage.scheduler.util.AllConstants.DATE_FORMAT;
////import static tech.codegarage.scheduler.util.AllConstants.INTENT_KEY_SCHEDULE_DATA_ALARM_RECEIVER;
//
//
///**
// * @author Md. Rashadul Alam
// * Email: rashed.droid@gmail.com
// */
//public class AlarmTestActivity extends BaseActivity {
//
//    @Override
//    public String[] initActivityPermissions() {
//        return new String[]{};
//    }
//
//
//    @Override
//    public int initActivityLayout() {
//        return R.layout.activity_myorder_list_screen;
//    }
//
//    @Override
//    public void initStatusBarView() {
//        // StatusBarUtil.setTransparent(getActivity());
//    }
//
//    @Override
//    public void initNavigationBarView() {
//
//    }
//
//    @Override
//    public void initIntentData(Bundle savedInstanceState, Intent intent) {
//
//    }
//
//    @Override
//    public void initActivityViews() {
////        setReminder();
//        //setPeriodicReminder();
////        initQuoteOfTheDayAlarm();
//
//        // Prepare schedule item
//        Calendar mAlertTime = Calendar.getInstance();
//        mAlertTime.set(Calendar.HOUR_OF_DAY, 16);
//        mAlertTime.set(Calendar.MINUTE, 8);
//        mAlertTime.set(Calendar.SECOND, 0);
//        String mDate = AlarmConstants.DATE_FORMAT.format(mAlertTime.getTime());
//        ScheduleItem scheduleItem = new ScheduleItem((int) System.currentTimeMillis(), getString(R.string.txt_medicine_reminder), getString(R.string.txt_please_take_your_medicine), mDate, mAlertTime.getTimeInMillis(), REPEAT_TYPE.DAILY);
//        //Set daily alarm
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(getActivity(), PeriodicReminderReceiver.class);
//        intent.putExtra(AlarmConstants.INTENT_KEY_SCHEDULE_DATA_ALARM_RECEIVER, BaseParcelable.toByteArray(scheduleItem));
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), scheduleItem.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        long timeInMilliseconds = scheduleItem.getTimeInMillis();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMilliseconds, pendingIntent);
//        } else {
//            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMilliseconds, pendingIntent);
//        }
//    }
//
//    @Override
//    public void initActivityViewsData(Bundle savedInstanceState) {
//    }
//
//    @Override
//    public void initActivityActions(Bundle savedInstanceState) {
//    }
//
//    @Override
//    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {
//    }
//
//    @Override
//    public void initActivityBackPress() {
//        finish();
//    }
//
//    @Override
//    public void initActivityDestroyTasks() {
//    }
//
//    @Override
//    public void initActivityPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
//
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//    }
////
////    private void setPeriodicReminder() {
////
////        List<Alarm> alarmList = new ArinAlarmController().getAllAlarm(AlarmTestActivity.this);
////        Log.i("Arindam", "" + alarmList.size());
////        new ArinAlarmController().deleteAllAlarm(AlarmTestActivity.this);
////
////        Calendar calendar = Calendar.getInstance();
////        //for test purpose it is set to 12:45am
////        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 45, 0);
//////        calendar.setTimeInMillis(System.currentTimeMillis());
//////        calendar.add(Calendar.MINUTE, 1);
//////        calendar.set(Calendar.HOUR_OF_DAY, 22);
//////        calendar.set(Calendar.MINUTE, 37);
////
////
////        Alarm alarm = new Alarm();
////        alarm.setExternalId(10);
////        alarm.setPeriodic(true);
////        alarm.setPeriodicType(Alarm.TIMEUNIT_MINUTE);
////        alarm.setPeriodicValue(1);
////        alarm.setPeriodicStartTime(calendar.getTimeInMillis());
////        alarm.setEnabled(true);
////        alarm.setTitle("");
////        alarm.setMessage("");
////        alarm.setAlarmHandleListener(StartPeriodicMedicineReminder.class.getCanonicalName());
////
////        new ArinAlarmController().setAlarm(AlarmTestActivity.this, alarm);
////
////    }
////
////
////    private void initQuoteOfTheDayAlarm() {
////
////        if (!SessionUtil.isPeriodicCheckServiceSet(getActivity())) {
////            Calendar mAlertTime = Calendar.getInstance();
//////        Calendar currentTime = Calendar.getInstance();
//////        currentTime.add(Calendar.MINUTE, 1);
//////        mAlertTime.set(Calendar.HOUR_OF_DAY, currentTime.get(Calendar.HOUR_OF_DAY));
//////        mAlertTime.set(Calendar.MINUTE, currentTime.get(Calendar.MINUTE));
//////        mAlertTime.set(Calendar.SECOND, 0);
//////        String mDate = DATE_FORMAT.format(mAlertTime.getTime());
////
////            mAlertTime.set(Calendar.HOUR_OF_DAY, 0);
////            mAlertTime.set(Calendar.MINUTE, 25);
////            mAlertTime.set(Calendar.SECOND, 0);
////            String mDate = DATE_FORMAT.format(mAlertTime.getTime());
////            ScheduleItem scheduleItem = new ScheduleItem(10, getString(R.string.txt_medicine_reminder), getString(R.string.txt_please_take_your_medicine), mDate, mAlertTime.getTimeInMillis(), REPEAT_TYPE.DAILY);
////
//////        if (!AllSettingsManager.isNullOrEmpty(mDate)) {
//////            ScheduleItem scheduleItem = new ScheduleItem(10, getString(R.string.txt_medicine_reminder), getString(R.string.txt_please_take_your_medicine), mDate, mAlertTime.getTimeInMillis(), REPEAT_TYPE.DAILY);
//////            Intent service = new Intent(AlarmTestActivity.this, AlarmService.class);
//////            service.putExtra(INTENT_KEY_SCHEDULE_DATA_ALARM_SERVICE, scheduleItem);
//////            service.setAction(INTENT_ACTION_CREATE);
//////            startService(service);
//////        }
////
////
////            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
////
////            Intent intent = new Intent(this, MedicineAlarmReceiver.class);
////            intent.putExtra(INTENT_KEY_SCHEDULE_DATA_ALARM_RECEIVER, BaseParcelable.toByteArray(scheduleItem));
////            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, scheduleItem.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
////
////            long timeInMilliseconds = scheduleItem.getTimeInMillis();
////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////                alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMilliseconds, pendingIntent);
////            } else {
////                alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMilliseconds, pendingIntent);
////            }
////
////            SessionUtil.setPeriodiServiceMedicineReminder(getActivity(), true);
////        }
////        // AppUtils.setStringSetting(this, SESSION_KEY_SCHEDULE_DATA, ScheduleItem.convertFromObjectToString(scheduleItem));
////
////    }
//}