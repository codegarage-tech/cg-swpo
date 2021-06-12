//package com.meembusoft.safewaypharmaonline.medicinereminder;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.preference.PreferenceManager;
//
//public class AlarmUtil {
//
//    public static String getStringSetting(Context context, String key, String defaultValue) {
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
//        return sp.getString(key, defaultValue);
//
//    }
//
//    public static void setStringSetting(Context context, String key, String value) {
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString(key, value);
//        editor.commit();
//    }
//
//    public static boolean isNullOrEmpty(String myString) {
//        return myString == null ? true : myString.length() == 0 || myString.equalsIgnoreCase("null") || myString.equalsIgnoreCase("");
//    }
//
//}