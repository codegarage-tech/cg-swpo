package com.meembusoft.safewaypharmaonline.medicinereminder.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.util.SessionUtil;

public class Restarted extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.d(ForeverRunningService.TAG, "service trying to stop");
//        if (SessionUtil.getUser(context) != null) {
//            Logger.d(ForeverRunningService.TAG, "User is logged in, so starting service again");
            context.startService(new Intent(context, ForeverRunningService.class));
//        } else {
//            Logger.d(ForeverRunningService.TAG, "User is logged out, so stopping service");
//        }
    }
}