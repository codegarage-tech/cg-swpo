package com.meembusoft.safewaypharmaonline.fcm;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.meembusoft.safewaypharmaonline.enumeration.FlavorType;
import com.meembusoft.safewaypharmaonline.model.AppSupplier;
import com.meembusoft.safewaypharmaonline.model.AppUser;
import com.meembusoft.safewaypharmaonline.model.ParamsUpdateFcm;
import com.meembusoft.safewaypharmaonline.retrofit.APIClient;
import com.meembusoft.safewaypharmaonline.retrofit.APIInterface;
import com.meembusoft.safewaypharmaonline.retrofit.APIResponse;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.util.SessionUtil;
import com.reversecoder.library.storage.SessionManager;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class UpdateTokenTask extends AsyncTask<String, Integer, String> {
    private static String TAG = "UpdateTokenTask";

    private Context mContext;
    private String mToken = "";
    private String mUserID = "";
    FlavorType mFlavorType;

    public UpdateTokenTask(Context context, FlavorType flavorType, String userID, String token) {
        mContext = context.getApplicationContext();
        mFlavorType = flavorType;
        mUserID = userID;
        mToken = token;
        Logger.d(TAG, TAG + " >>> UpdateTokenTask: " + mUserID + "\n" + token + "\n" + mFlavorType);

    }

    @Override
    protected String doInBackground(String... params) {
        try {
            if (mFlavorType != null && !TextUtils.isEmpty(mUserID) && !TextUtils.isEmpty(mToken)) {
                APIInterface mApiInterface = APIClient.getClient(mContext).create(APIInterface.class);
                Response response = null;
                ParamsUpdateFcm paramAppUser = new ParamsUpdateFcm(mFlavorType.toString(), mUserID, mToken);
                Logger.d(TAG, TAG + " >>> APIResponse(paramAppUser): " + paramAppUser.toString());
                Call<APIResponse> callUser = mApiInterface.apiUpdateFcmToken(paramAppUser);
                response = callUser.execute();

                Logger.d(TAG, TAG + " >>> " + "response: " + response);
                if (response != null && response.isSuccessful()) {

                    Logger.d(TAG, TAG + ">>UpdateTokenTask>>: onResponse-server = " + response.toString());
                    APIResponse dataUser = (APIResponse) response.body();
                    Logger.d(TAG, TAG + ">>UpdateTokenTask>> : onResponse-object = " + dataUser.toString());

                    if (dataUser != null && dataUser.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, TAG + ">>mFlavorType>> := " + mFlavorType.toString());
                        switch (mFlavorType) {
                            case CUSTOMER:
                                //Set customer user
                                SessionManager.setStringSetting(mContext, SessionUtil.SESSION_KEY_USER_TAG, FlavorType.CUSTOMER.toString());
                                 if (dataUser.getData() != null) {
                                     SessionUtil.setSupplier(mContext, "null");
                                     SessionUtil.setUser(mContext, APIResponse.getResponseString(dataUser.getData()));
                                 }
                                break;
                            case SUPPLIER:
                                //Set supplier user
                                SessionManager.setStringSetting(mContext, SessionUtil.SESSION_KEY_USER_TAG, FlavorType.SUPPLIER.toString());
                                if (dataUser.getData() != null) {
                                    SessionUtil.setUser(mContext, "null");
                                    SessionUtil.setSupplier(mContext, APIResponse.getResponseString(dataUser.getData()));
                                }
                                break;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
