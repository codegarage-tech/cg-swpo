package com.meembusoft.safewaypharmaonline.util;

import android.content.Context;

import com.meembusoft.safewaypharmaonline.model.AppSupplier;
import com.meembusoft.safewaypharmaonline.model.AppUser;
import com.meembusoft.safewaypharmaonline.model.ShippingAddress;
import com.meembusoft.safewaypharmaonline.model.UserProfile;
import com.meembusoft.safewaypharmaonline.retrofit.APIResponse;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;

import static com.jude.easyrecyclerview.EasyRecyclerView.TAG;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.SESSION_KEY_ADDRESS;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SessionUtil {

    // Session key
    private static final String SESSION_KEY_USER = "SESSION_KEY_USER";
    private static final String SESSION_KEY_SUPPLIER = "SESSION_KEY_SUPPLIER";
    public static final String SESSION_KEY_USER_PROFILE = "SESSION_KEY_USER_PROFILE";
    public static final String SESSION_KEY_USER_TAG = "SESSION_KEY_USER_TAG";
    public static final String SESSION_KEY_ORDER_INFO_NOTIFY = "SESSION_KEY_ORDER_INFO_NOTIFY";
    public static final String SESSION_KEY_CHAT_NOTIFY = "SESSION_KEY_CHAT_NOTIFY";
    public static final String SESSION_KEY_PROMOS_NOTIFY = "SESSION_KEY_PROMOS_NOTIFY";
    public static final String SESSION_KEY_ALL_NOTIFY = "SESSION_KEY_ALL_NOTIFY";
//    public static final String SESSION_KEY_IS_MEDICINE_REMINDER_SET = "SESSION_KEY_IS_MEDICINE_REMINDER_SET";
//    public static final String SESSION_KEY_ISCHECK_SERVICE_MEDICINE_REMINDER_SET = "SESSION_KEY_ISCHECK_SERVICE_MEDICINE_REMINDER_SET";
    public static final String SESSION_KEY_DAILY_MEDICINE_REMINDER_INFO = "SESSION_KEY_DAILY_MEDICINE_REMINDER_INFO";
    public static final String SESSION_KEY_DAILY_MEDICINE_REMINDER_TIME = "SESSION_KEY_DAILY_MEDICINE_REMINDER_TIME";

    public static AppUser getUser(Context context) {
        AppUser mAppUser = null;
        String appUser = SessionManager.getStringSetting(context, SESSION_KEY_USER, "");
        if (!AllSettingsManager.isNullOrEmpty(appUser)) {
            mAppUser = APIResponse.getResponseObject(appUser, AppUser.class);
        }
        return mAppUser;
    }

    public static void setUser(Context context, String jsonValue) {
        SessionManager.setStringSetting(context, SESSION_KEY_USER, jsonValue);
    }

    public static UserProfile getUserProfile(Context context) {
        UserProfile mUserProfile = null;
        String userProfile = SessionManager.getStringSetting(context, SESSION_KEY_USER_PROFILE, "");
        if (!AllSettingsManager.isNullOrEmpty(userProfile)) {
            mUserProfile = APIResponse.getResponseObject(userProfile, UserProfile.class);
        }
        return mUserProfile;
    }

    public static void setUserProfile(Context context, String jsonValue) {
        SessionManager.setStringSetting(context, SESSION_KEY_USER_PROFILE, jsonValue);
    }


    public static ShippingAddress getUserShippingAddress(Context context) {
        ShippingAddress mShippingAddress = null;
        String appUserShippingAddress = SessionManager.getStringSetting(context, SESSION_KEY_ADDRESS, "");
        Logger.d(TAG, "appUserShippingAddress(supplier):>>> " + appUserShippingAddress.toString());

        if (!AllSettingsManager.isNullOrEmpty(appUserShippingAddress)) {
            mShippingAddress = APIResponse.getResponseObject(appUserShippingAddress, ShippingAddress.class);
        }
        return mShippingAddress;
    }


    public static void setUserShippingAddress(Context context, String jsonValue) {
        SessionManager.setStringSetting(context, SESSION_KEY_ADDRESS, jsonValue);
    }

    public static AppSupplier getSupplier(Context context) {
        AppSupplier mAppSupplier = null;
        String appUser = SessionManager.getStringSetting(context, SESSION_KEY_SUPPLIER, "");
        if (!AllSettingsManager.isNullOrEmpty(appUser)) {
            mAppSupplier = APIResponse.getResponseObject(appUser, AppSupplier.class);
        }
        return mAppSupplier;
    }

    public static void setSupplier(Context context, String jsonValue) {
        SessionManager.setStringSetting(context, SESSION_KEY_SUPPLIER, jsonValue);
    }


    public static int getLastSelectedOrderInfo(Context context) {
        return SessionManager.getIntegerSetting(context, SESSION_KEY_ORDER_INFO_NOTIFY, 1);
    }

    public static void setLastSelectedOrderInfo(Context context, int sectionPosition) {
        SessionManager.setIntegerSetting(context, SESSION_KEY_ORDER_INFO_NOTIFY, sectionPosition);
    }

    public static int getLastSelectedChat(Context context) {
        return SessionManager.getIntegerSetting(context, SESSION_KEY_CHAT_NOTIFY, 1);
    }

    public static void setLastSelectedChat(Context context, int sectionPosition) {
        SessionManager.setIntegerSetting(context, SESSION_KEY_CHAT_NOTIFY, sectionPosition);
    }

    public static int getLastSelectedPromos(Context context) {
        return SessionManager.getIntegerSetting(context, SESSION_KEY_PROMOS_NOTIFY, 1);
    }

    public static void setLastSelectedPromos(Context context, int sectionPosition) {
        SessionManager.setIntegerSetting(context, SESSION_KEY_PROMOS_NOTIFY, sectionPosition);
    }

    public static int getLastSelectedNotification(Context context) {
        return SessionManager.getIntegerSetting(context, SESSION_KEY_ALL_NOTIFY, 1);
    }

    public static void setLastSelectedNotificationn(Context context, int sectionPosition) {
        SessionManager.setIntegerSetting(context, SESSION_KEY_ALL_NOTIFY, sectionPosition);
    }

//    public static boolean isTodaysMedicineReminderSet(Context context) {
//        return SessionManager.getBooleanSetting(context, SESSION_KEY_IS_MEDICINE_REMINDER_SET, false);
//    }
//
//    public static void setTodaysMedicineReminder(Context context, boolean isMedicineReminderSet) {
//        SessionManager.setBooleanSetting(context, SESSION_KEY_IS_MEDICINE_REMINDER_SET, isMedicineReminderSet);
//    }
//
//    public static boolean isPeriodicCheckServiceSet(Context context) {
//        return SessionManager.getBooleanSetting(context, SESSION_KEY_ISCHECK_SERVICE_MEDICINE_REMINDER_SET, false);
//    }
//
//    public static void setPeriodiServiceMedicineReminder(Context context, boolean isMedicineReminderSet) {
//        SessionManager.setBooleanSetting(context, SESSION_KEY_ISCHECK_SERVICE_MEDICINE_REMINDER_SET, isMedicineReminderSet);
//    }

    public static String getDailyMedicineReminderInfo(Context context) {
        return SessionManager.getStringSetting(context, SESSION_KEY_DAILY_MEDICINE_REMINDER_INFO, "");
    }

    public static void setDailyMedicineReminderInfo(Context context, String isMedicineReminderSet) {
        SessionManager.setStringSetting(context, SESSION_KEY_DAILY_MEDICINE_REMINDER_INFO, isMedicineReminderSet);
    }

    public static String getDailyMedicineReminderTime(Context context) {
        return SessionManager.getStringSetting(context, SESSION_KEY_DAILY_MEDICINE_REMINDER_TIME, "");
    }

    public static void setDailyMedicineReminderTime(Context context, String isMedicineReminderSet) {
        SessionManager.setStringSetting(context, SESSION_KEY_DAILY_MEDICINE_REMINDER_TIME, isMedicineReminderSet);
    }
}