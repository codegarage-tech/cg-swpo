package com.meembusoft.safewaypharmaonline.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.parceler.Parcels;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class BroadcastManager {

    private static final String INTENT_ACTION = "BroadcastManager.Update";
    public static final IntentFilter INTENT_FILTER = new IntentFilter(INTENT_ACTION);
    public static final Intent INTENT = new Intent(INTENT_ACTION);
    public static final String INTENT_KEY_MESSAGE = "INTENT_KEY_MESSAGE";

    public static void registerBroadcastUpdate(@NonNull Context context, BroadcastReceiver broadcastReceiver) {
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(context);
        instance.registerReceiver(broadcastReceiver, INTENT_FILTER);
    }

    public static void registerBroadcastUpdate(@NonNull Context context, IntentFilter intentFilter, BroadcastReceiver broadcastReceiver) {
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(context);
        instance.registerReceiver(broadcastReceiver, intentFilter);
    }

    public static void unregisterBroadcastUpdate(@NonNull Context context, BroadcastReceiver broadcastReceiver) {
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(context);
        instance.unregisterReceiver(broadcastReceiver);
    }

    public static <T> void sendBroadcast(@NonNull Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(INTENT);
    }

    public static <T> void sendBroadcast(@NonNull Context context, T value) {
        INTENT.putExtra(INTENT_KEY_MESSAGE, Parcels.wrap(value));
        LocalBroadcastManager.getInstance(context).sendBroadcast(INTENT);
    }

    public static <T> void sendBroadcast(@NonNull Context context, Intent intent, T value) {
        intent.putExtra(INTENT_KEY_MESSAGE, Parcels.wrap(value));
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static <T> T getBroadcastData(Intent intent) {
        T value = null;
        if (intent != null) {
            Parcelable broadcastData = intent.getParcelableExtra(INTENT_KEY_MESSAGE);
            if (broadcastData != null) {
                value = Parcels.unwrap(broadcastData);
            }
        }
        return value;
    }
}
