package com.meembusoft.safewaypharmaonline.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.HomeActivity;
import com.meembusoft.safewaypharmaonline.activity.MapActivity;
import com.meembusoft.safewaypharmaonline.activity.PlaceOrderDetailsActivity;
import com.meembusoft.safewaypharmaonline.activity.SupplierOrderDetailsActivity;
import com.meembusoft.safewaypharmaonline.enumeration.DetailType;
import com.meembusoft.safewaypharmaonline.enumeration.FlavorType;
import com.meembusoft.safewaypharmaonline.enumeration.OrderNotificationType;
import com.meembusoft.safewaypharmaonline.fcm.fcmutils.FcmUtil;
import com.meembusoft.safewaypharmaonline.model.AppSupplier;
import com.meembusoft.safewaypharmaonline.model.AppUser;
import com.meembusoft.safewaypharmaonline.model.Notifications;
import com.meembusoft.safewaypharmaonline.model.PlaceOrderByItem;
import com.meembusoft.safewaypharmaonline.model.Promos;
import com.meembusoft.safewaypharmaonline.retrofit.APIResponse;
import com.meembusoft.safewaypharmaonline.util.AllConstants;
import com.meembusoft.safewaypharmaonline.util.BroadcastManager;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.util.SessionUtil;
import com.reversecoder.library.util.AllSettingsManager;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.Map;

import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_DETAIL_TYPE;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_FCM_NOTIFICATION_MESSAGE;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_FCM_ORDER_ITEM;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String TAG = "MyFirebaseMessagingService";
    //Broadcast
    private static final String CHANNEL_NAME = "FCM";
    private static final String CHANNEL_DESC = "Firebase Cloud Messaging";
    private AppSupplier mAppSupplier;
    AppUser mAppUser;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            Map<String, String> data = remoteMessage.getData();
            Logger.d(TAG, TAG + ">>onMessageReceived>> remoteMessage: " + remoteMessage.toString());
            Logger.d(TAG, TAG + ">>onMessageReceived>> data: " + data.get("data"));
            Logger.d(TAG, TAG + ">>onMessageReceived>> notification.getBody(): " + notification.getBody());
            Logger.d(TAG, TAG + ">>onMessageReceived>> notification.getTitle(): " + notification.getTitle());
            FcmUtil.printFcmMessage(data);
            String getFcmData = FcmUtil.getFcmValue(data, "data");
            String getNotificationType = FcmUtil.getFcmValue(data, "notification_type");
            String getUserType = FcmUtil.getFcmValue(data, "user_type");
            Logger.d(TAG, TAG + ">>onMessageReceived>>getFcmData>> data: " + getFcmData);
            Logger.d(TAG, TAG + ">>onMessageReceived>>getNotificationType>> data: " + getNotificationType);
            Logger.d(TAG, TAG + ">>onMessageReceived>>getUserType>> data: " + getUserType);
            Log.e("getNotificationType", getNotificationType + "");

            Logger.d(TAG, TAG + "<<<>>getLastSelectedNotification>>" + SessionUtil.getLastSelectedNotification(getApplicationContext()));

            if (SessionUtil.getLastSelectedNotification(getApplicationContext()) != 0) {
                sendNotification(notification, getUserType, getNotificationType, getFcmData);
            }


        } catch (Exception e) {
            Logger.d(TAG, TAG + ">>onMessageReceived>>Exception: " + e.getMessage());
        }
    }

    private void sendNotification(RemoteMessage.Notification notification, String userTypes, String notificationType, String fcmData) throws IOException {
        try {

            Intent intent = null;
            PlaceOrderByItem mPlaceOrderByItem = null;
            Notifications mNotifications = null;
            Promos promos = null;
            Logger.d(TAG, TAG + ">>getLastSelectedNotification>>" + SessionUtil.getLastSelectedNotification(getApplicationContext()));
            Logger.d(TAG, TAG + ">>getLastSelectedOrderInfo>>" + SessionUtil.getLastSelectedOrderInfo(getApplicationContext()));
            Logger.d(TAG, TAG + ">>getLastSelectedPromos>>" + SessionUtil.getLastSelectedPromos(getApplicationContext()));
            if (notificationType != null && notificationType.equalsIgnoreCase(OrderNotificationType.APP_NOTIFICATION.toString())) {
//                String fcm = "{\"id\":\"3\",\"notification_type\":\"app_notification\",\"title\":\"Notification title1\",\"message\":\"It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, \",\"status\":\"1\",\"created_at\":\"2020-01-02 11:50:22\"}";
//                Logger.d(TAG, TAG + ">>fcm>> data: " + fcm);

                if (SessionUtil.getLastSelectedNotification(getApplicationContext()) != 0) {
                    mNotifications = APIResponse.getResponseObject(fcmData, Notifications.class);
                    intent = new Intent(this, HomeActivity.class);
                    intent.putExtra(INTENT_KEY_FCM_NOTIFICATION_MESSAGE, Parcels.wrap(mNotifications));
                    Logger.d(TAG, TAG + ">>mNotifications>> data: " + mNotifications.toString());
                }
            } else if (notificationType != null && notificationType.equalsIgnoreCase(OrderNotificationType.PROMO_NOTIFICATION.toString())) {
                if (SessionUtil.getLastSelectedPromos(getApplicationContext()) != 0) {
                    promos = APIResponse.getResponseObject(fcmData, Promos.class);
                    if (!AllSettingsManager.isNullOrEmpty(promos.getLink())) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(promos.getLink()));
                        startActivity(browserIntent);
                    }
                }
            } else {
                if (SessionUtil.getLastSelectedOrderInfo(getApplicationContext()) != 0) {
                    mPlaceOrderByItem = APIResponse.getResponseObject(fcmData, PlaceOrderByItem.class);
                    if (userTypes.equalsIgnoreCase(FlavorType.CUSTOMER.toString())) {
                        mAppUser = SessionUtil.getUser(this);
                        Logger.d(TAG, TAG + ">>sendNotification>>responseOrder>> data: " + mPlaceOrderByItem.toString());
                        if (mAppUser != null) {
                            if (notificationType.equalsIgnoreCase(OrderNotificationType.ACCEPT_ORDER.toString())) {
                                Logger.d(TAG, TAG + ">>sendNotification>> notification ACCEPT_ORDER content activity>>MapActivity");
                                intent = new Intent(this, MapActivity.class);
                                intent.putExtra(INTENT_KEY_FCM_ORDER_ITEM, Parcels.wrap(mPlaceOrderByItem));
                            } else if (notificationType.equalsIgnoreCase(OrderNotificationType.DELIVERY_ON_THE_WAY.toString())) {
                                Logger.d(TAG, TAG + ">>sendNotification>> notification DELIVERY_ON_THE_WAY content activity>>PlaceOrderDetailsActivity");
                                intent = new Intent(this, PlaceOrderDetailsActivity.class);
                                intent.putExtra(INTENT_KEY_FCM_ORDER_ITEM, Parcels.wrap(mPlaceOrderByItem));
                            } else {
                                Logger.d(TAG, TAG + ">>sendNotification>> notification content activity>>notificationType did not match for customer");
                            }
                        }
                    } else if (userTypes.equalsIgnoreCase(FlavorType.SUPPLIER.toString())) {
                        mAppSupplier = SessionUtil.getSupplier(this);
                        Logger.d(TAG, TAG + ">>sendNotification>>mPlaceOrderByItem>> data: " + mPlaceOrderByItem.toString());
                        if (mAppSupplier != null) {
                            if (notificationType.equalsIgnoreCase(OrderNotificationType.NEW_ORDER.toString())) {
                                Logger.d(TAG, TAG + ">>sendNotification>> notification NEW_ORDER content activity>>SupplierOrderDetailsActivity");
                                intent = new Intent(this, SupplierOrderDetailsActivity.class);
                                intent.putExtra(AllConstants.INTENT_KEY_FCM_ORDER_ITEM, Parcels.wrap(mPlaceOrderByItem));
                            } else if (notificationType.equalsIgnoreCase(OrderNotificationType.PAYMENT_RECEIPT.toString())) {
                                Logger.d(TAG, TAG + ">>sendNotification>> notification content PAYMENT_RECEIPT activity>>SupplierOrderDetailsActivity");
                                intent = new Intent(this, SupplierOrderDetailsActivity.class);
                                intent.putExtra(AllConstants.INTENT_KEY_FCM_ORDER_ITEM, Parcels.wrap(mPlaceOrderByItem));
                            } else {
                                Logger.d(TAG, TAG + ">>sendNotification>> notification content activity>>notificationType did not match for supplier");
                            }
                        }
                    }
                }
            }

            if (intent != null) {
                // Send broadcast
                if (notificationType != null && notificationType.equalsIgnoreCase(OrderNotificationType.APP_NOTIFICATION.toString())) {
                    intent.putExtra(INTENT_KEY_DETAIL_TYPE, DetailType.NOTIFICATION.name());
                    Logger.d(TAG, TAG + ">>NOTIFICATION>> data: " + DetailType.NOTIFICATION.name());

                } else {
                    intent.putExtra(INTENT_KEY_DETAIL_TYPE, DetailType.FCM.name());

                }

                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.notification_channel_id))
                        .setContentTitle(notification.getTitle())
                        .setContentText(notification.getBody())
                        .setAutoCancel(true)
                        // .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notifications))
                        .setContentIntent(pendingIntent)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setColor(getResources().getColor(R.color.colorAccent))
                        .setLights(Color.RED, 1000, 300)
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setSmallIcon(R.mipmap.ic_launcher);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(getString(R.string.notification_channel_id), CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                    channel.setDescription(CHANNEL_DESC);
                    channel.setShowBadge(true);
                    channel.canShowBadge();
                    channel.enableLights(true);
                    channel.setLightColor(Color.RED);
                    channel.enableVibration(true);
                    channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

                    if (notificationManager != null) {
                        notificationManager.createNotificationChannel(channel);
                    }
                }
                if (notificationManager != null) {
                    notificationManager.notify(0, notificationBuilder.build());
                }

                // Send broadcast
                if (notificationType != null && notificationType.equalsIgnoreCase(OrderNotificationType.APP_NOTIFICATION.toString())) {
                    Logger.d(TAG, TAG + ">>APP_NOTIFICATION>> data: " + mNotifications.getMessage());
                    if (SessionUtil.getLastSelectedNotification(getApplicationContext()) != 0) {
                        BroadcastManager.sendBroadcast(this, mNotifications);
                    }
                } else if (notificationType != null && notificationType.equalsIgnoreCase(OrderNotificationType.PROMO_NOTIFICATION.toString())) {
                    if (SessionUtil.getLastSelectedPromos(getApplicationContext()) != 0) {
                        Logger.d(TAG, TAG + ">>PROMO_NOTIFICATION>> data: " + promos.getTitle());
                        if (SessionUtil.getLastSelectedPromos(getApplicationContext()) != 0) {
                            BroadcastManager.sendBroadcast(this, promos);
                        }
                    }
                } else {
                    if (SessionUtil.getLastSelectedOrderInfo(getApplicationContext()) != 0) {
                        Logger.d(TAG, TAG + ">>Place Oder>> data: " + mPlaceOrderByItem.toString());
                        BroadcastManager.sendBroadcast(this, mPlaceOrderByItem);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.d(TAG, TAG + ">>sendNotification>>Exception: " + ex.getMessage());
        }
    }
}