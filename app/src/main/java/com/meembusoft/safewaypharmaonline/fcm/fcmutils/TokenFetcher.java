package com.meembusoft.safewaypharmaonline.fcm.fcmutils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.meembusoft.safewaypharmaonline.util.Logger;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class TokenFetcher extends AsyncTask<Void, Void, String> {

    private static final String TAG = TokenFetcher.class.getSimpleName();
    private static final String INTENT_ACTION = "TokenFetcher.Update";
    public static final IntentFilter INTENT_FILTER = new IntentFilter(INTENT_ACTION);
    private static final Intent INTENT = new Intent(INTENT_ACTION);
    private Context mContext;
    private OnTokenUpdateListener mOnTokenUpdateListener;

    public TokenFetcher(Context context, OnTokenUpdateListener onTokenUpdateListener) {
        this.mContext = context.getApplicationContext();
        this.mOnTokenUpdateListener = onTokenUpdateListener;
    }

    @Override
    protected String doInBackground(Void... params) {
        return "";
    }

    @Override
    protected void onPostExecute(String token) {
        super.onPostExecute(token);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
//                String mUpdatedToken = "";
                Logger.d(TAG, TAG + ">> " + "onSuccess(newToken): " + newToken);

//                if (!FcmUtil.isNullOrEmpty(newToken)) {
//                    Logger.d(TAG, TAG + ">> " + "token is found");
//                    mUpdatedToken = newToken;
//
//                    //Check with previous saved token
//                    String previousToken = FcmUtil.getToken(mContext);
//                    if (!FcmUtil.isNullOrEmpty(previousToken)) {
//                        Logger.d(TAG, TAG + ">> " + "Token is found into session");
//
//                        //Check new token with existing one
//                        if (!previousToken.equalsIgnoreCase(newToken)) {
//                            Logger.d(TAG, TAG + ">> " + "Session token is not same as new token");
//                            Logger.d(TAG, TAG + ">> " + "Saving token into session");
//                            FcmUtil.saveToken(mContext, mUpdatedToken);
//                        } else {
//                            Logger.d(TAG, TAG + ">> " + "Both new and previous tokens are same");
//                        }
//                    } else {
//                        Logger.d(TAG, TAG + ">> " + "There is no token found in session");
//                        Logger.d(TAG, TAG + ">> " + "Saving token into session");
//                        FcmUtil.saveToken(mContext, mUpdatedToken);
//                    }
//                } else {
//                    Logger.d(TAG, TAG + ">> " + "token is not found");
//                }

//                //Send broadcast to the context
//                LocalBroadcastManager.getInstance(mContext).sendBroadcast(INTENT);

                //Notify user about token update
//                Logger.d(TAG, TAG + ">> " + "mUpdatedToken: " + mUpdatedToken);
//                Logger.d(TAG, TAG + ">> " + "Notifying user");
                mOnTokenUpdateListener.onTokenUpdate(newToken);
            }
        });
    }
}