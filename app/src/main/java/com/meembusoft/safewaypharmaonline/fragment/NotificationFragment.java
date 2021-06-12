package com.meembusoft.safewaypharmaonline.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.SliderLayout;
import com.google.android.material.snackbar.Snackbar;
import com.livechatinc.inappchat.ChatWindowConfiguration;
import com.livechatinc.inappchat.ChatWindowView;
import com.livechatinc.inappchat.models.NewMessageModel;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.HomeActivity;
import com.meembusoft.safewaypharmaonline.adapter.NotificationOrderStatusAdapter;
import com.meembusoft.safewaypharmaonline.adapter.NotificationsListAdapter;
import com.meembusoft.safewaypharmaonline.adapter.PromosListAdapter;
import com.meembusoft.safewaypharmaonline.base.BaseFragment;
import com.meembusoft.safewaypharmaonline.enumeration.FlavorType;
import com.meembusoft.safewaypharmaonline.enumeration.MessageTabType;
import com.meembusoft.safewaypharmaonline.model.AppSupplier;
import com.meembusoft.safewaypharmaonline.model.AppUser;
import com.meembusoft.safewaypharmaonline.model.Notifications;
import com.meembusoft.safewaypharmaonline.model.ParamsDeleteNotification;
import com.meembusoft.safewaypharmaonline.model.ParamsOrderTrush;
import com.meembusoft.safewaypharmaonline.model.PlaceOrderByItem;
import com.meembusoft.safewaypharmaonline.model.Promos;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.retrofit.APIClient;
import com.meembusoft.safewaypharmaonline.retrofit.APIInterface;
import com.meembusoft.safewaypharmaonline.retrofit.APIResponse;
import com.meembusoft.safewaypharmaonline.util.AllConstants;
import com.meembusoft.safewaypharmaonline.util.BroadcastManager;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.util.SessionUtil;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import vn.luongvo.widget.iosswitchview.SwitchView;

import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_FCM_NOTIFICATION_MESSAGE;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class NotificationFragment extends BaseFragment implements ChatWindowView.ChatWindowEventsListener{

    private TextView tvOrders,tvChats,tvPromos,tvSettings,tvNoDataFound;
    private LinearLayout linNotificationView,linMessageOrder,linMessagePromos,linMessageSettings,linMessageChats,linOrderNotification,linChats,linPromos,linSetting, linNoDataFound;
    private RecyclerView rvNotificationList,rvPromosList;
    private SwipeableRecyclerView rvOrderNotificationList;
    private SwitchView switchSettingNotification,switchSettingChat,switchSettingOrderInfo,switchSettingPromoMgs;
    private CardView cvSetting;
    private NotificationsListAdapter notificationListAdapter;
    private PromosListAdapter mPromosListAdapter;
    private NotificationOrderStatusAdapter mNotifyOrderStatusAdapter;
    private List<Notifications> mNotificationsList;
    ChatWindowView emmbeddedChatWindow;
    HashMap<String, String> customVariables = new HashMap<>();
    //Background task
    //GetAllNotificationListTask getAllNotificationListTask;
    private GetAllOrderStatusListTask getAllOrderStatusListTask;
    private DoDeleteNotificationTask doDeleteNotificationTask;
    private GetAllPromosListTask getAllPromosListTask;
    private APIInterface mApiInterface;
    private Notifications mNotifications;
    private MessageTabType messageTabType =  MessageTabType.ORDERS;
    private AppUser mAppUser;

    //  Supplier
    private AppSupplier mAppSupplier;
    FlavorType mFlavorType;
    private String customerOrSupplierId = "";
    private String selectUserType = AllConstants.USER_TYPE_CUSTOMER;

    public static NotificationFragment newInstance() {
        NotificationFragment fragment = new NotificationFragment();
        return fragment;
    }

    @Override
    public int initFragmentLayout() {
        return R.layout.fragment_notification;
    }

    @Override
    public void initFragmentBundleData(Bundle bundle) {
        if (bundle != null) {
            Parcelable mParcelable = bundle.getParcelable(INTENT_KEY_FCM_NOTIFICATION_MESSAGE);
            if (mParcelable != null) {
                mNotifications = Parcels.unwrap(mParcelable);
                Logger.d(TAG, TAG + " >>>mNotifyFrag " + "mNotifications: " + mNotifications.toString());
            }

        }
    }

    @Override
    public void initFragmentViews(View parentView) {

        tvOrders = (TextView)parentView.findViewById(R.id.tv_orders) ;
        tvChats = (TextView)parentView.findViewById(R.id.tv_chats) ;
        tvPromos = (TextView)parentView.findViewById(R.id.tv_promos) ;
        tvSettings = (TextView)parentView.findViewById(R.id.tv_settings) ;
        tvNoDataFound = (TextView)parentView.findViewById(R.id.tv_no_data_found) ;
        linNotificationView = (LinearLayout) parentView.findViewById(R.id.lin_notification_view);
        linNoDataFound = (LinearLayout) parentView.findViewById(R.id.lin_no_data_found_layout);
        linMessageOrder = (LinearLayout) parentView.findViewById(R.id.lin_message_order);
        linMessageChats = (LinearLayout) parentView.findViewById(R.id.lin_message_chats);
        linMessagePromos = (LinearLayout) parentView.findViewById(R.id.lin_message_promos);
        linMessageSettings = (LinearLayout) parentView.findViewById(R.id.lin_message_settings);
        linOrderNotification = (LinearLayout) parentView.findViewById(R.id.lin_order_notification);
        linChats = (LinearLayout) parentView.findViewById(R.id.lin_chats);
        linPromos= (LinearLayout) parentView.findViewById(R.id.lin_promos);
        cvSetting = (CardView) parentView.findViewById(R.id.cv_setting);
        switchSettingNotification = (SwitchView) parentView.findViewById(R.id.switch_setting_notification);
        switchSettingChat = (SwitchView) parentView.findViewById(R.id.switch_setting_chat);
        switchSettingOrderInfo = (SwitchView) parentView.findViewById(R.id.switch_setting_order_info);
        switchSettingPromoMgs = (SwitchView) parentView.findViewById(R.id.switch_setting_promo_mgs);
        rvNotificationList = (RecyclerView) parentView.findViewById(R.id.rv_notification_list);
        rvPromosList = (RecyclerView) parentView.findViewById(R.id.rv_promos);
        rvOrderNotificationList = (SwipeableRecyclerView) parentView.findViewById(R.id.rv_order_notification);
        emmbeddedChatWindow = (ChatWindowView)parentView.findViewById(R.id.embedded_chat_window);

    }

    @Override
    public void initFragmentViewsData() {
        // Register BroadCast
      //  BroadcastManager.registerBroadcastUpdate(getActivity(), notificationUpdate);

        mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        // Get User Tag
        initGetTagValue();
        // Set Visible Tab
        visibleTabLayout(messageTabType);

        //Set dummy json user
//        AppUser user = new AppUser("1", "Md. Rashadul Alam", "01794620787", "rashed.droid@gmail.com", "Ka-13, South Kuril", "");
//        SessionUtil.setUser(getActivity(),APIResponse.getResponseString(user));
//        ((HomeActivity)getActivity()).initNavigationDrawer();
        notificationListAdapter = new NotificationsListAdapter(getActivity());
        rvNotificationList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNotificationList.setAdapter(notificationListAdapter);

        mPromosListAdapter = new PromosListAdapter(getActivity());
        rvPromosList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPromosList.setAdapter(mPromosListAdapter);


        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
        } else {
            getAllOrderStatusListTask = new GetAllOrderStatusListTask(getActivity());
            getAllOrderStatusListTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            getAllPromosListTask = new GetAllPromosListTask(getActivity());
            getAllPromosListTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }

        setSettingDataInfo(mNotifications);
        checkNotifyStates();
    }



    @Override
    public void initFragmentActions() {

        linMessageOrder.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               messageTabType = MessageTabType.ORDERS;
               visibleTabLayout(messageTabType);
           }
       });

        linMessageChats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageTabType = MessageTabType.CHATS;
                visibleTabLayout(messageTabType);
            }
        });

        linMessagePromos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageTabType = MessageTabType.PROMOS;
                visibleTabLayout(messageTabType);
            }
        });

        linMessageSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageTabType = MessageTabType.SETTINGS;
                visibleTabLayout(messageTabType);

            }
        });

        rvOrderNotificationList.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {
//                mList.remove(position);
                Notifications notifications = mNotificationsList.get(position);
                if (!NetworkManager.isConnected(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                } else {

                    if (notifications != null && notifications.getId() != null) {
                        doDeleteNotificationTask = new DoDeleteNotificationTask(getActivity(), notifications);
                        doDeleteNotificationTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }

                }

            }

            @Override
            public void onSwipedRight(int position) {
//                mList.remove(position);
//                mAdapter.notifyDataSetChanged();
                Snackbar.make(rvOrderNotificationList,
                        "Item " + position + " Removed",
                        Snackbar.LENGTH_LONG).show();
            }
        });

        initListenerSwitching();
    }

    private void initGetTagValue() {
        String userType = SessionManager.getStringSetting(getActivity(), SessionUtil.SESSION_KEY_USER_TAG);
        if (!AllSettingsManager.isNullOrEmpty(userType)) {
            mFlavorType = FlavorType.getFlavor(SessionManager.getStringSetting(getActivity(), SessionUtil.SESSION_KEY_USER_TAG));
        } else {
            mFlavorType = FlavorType.SUPPLIER;
        }

        if (mFlavorType != null) {
            switch (mFlavorType) {
                case CUSTOMER:
                    mAppUser = SessionUtil.getUser(getActivity());
                    selectUserType = AllConstants.USER_TYPE_CUSTOMER;
                    if (mAppUser != null) {
                        customerOrSupplierId = mAppUser.getId();
                    }
                    break;

                case SUPPLIER:
                    mAppSupplier = SessionUtil.getSupplier(getActivity());
                    selectUserType = AllConstants.USER_TYPE_SUPPLIER;
                    if (mAppSupplier != null) {
                        customerOrSupplierId = mAppSupplier.getUser_id();
                    }
                    break;
            }

            Logger.d(TAG, TAG + " >>> " + "selectUserType " + selectUserType);
            Logger.d(TAG, TAG + " >>> " + "customerOrSupplierId " + customerOrSupplierId);
        }

    }


    public void startEmmbeddedChat() {
        ChatWindowConfiguration configuration = new ChatWindowConfiguration(
                "12073272",
                "Q9H5aVnGR",
                "Rashedul Alam",
                "rashed.droid@gmail.com",
                customVariables
        );
        if (!emmbeddedChatWindow.isInitialized()) {
            emmbeddedChatWindow.setUpWindow(configuration);
            emmbeddedChatWindow.setUpListener(this);
            emmbeddedChatWindow.initialize();
        }
        emmbeddedChatWindow.showChatWindow();
    }
    private void visibleTabLayout(MessageTabType messageTabType){

        if (messageTabType != null) {
            if (messageTabType == MessageTabType.ORDERS) {
                tvOrders.setTypeface(null, Typeface.BOLD);
                tvChats.setTypeface(null, Typeface.NORMAL);
                tvPromos.setTypeface(null, Typeface.NORMAL);
                tvSettings.setTypeface(null, Typeface.NORMAL);
                linOrderNotification.setVisibility(View.VISIBLE);
                linPromos.setVisibility(View.GONE);
                linChats.setVisibility(View.GONE);
                cvSetting.setVisibility(View.GONE);
            } else if (messageTabType == MessageTabType.CHATS) {
                tvOrders.setTypeface(null, Typeface.NORMAL);
                tvChats.setTypeface(null, Typeface.BOLD);
                tvPromos.setTypeface(null, Typeface.NORMAL);
                tvSettings.setTypeface(null, Typeface.NORMAL);
                linOrderNotification.setVisibility(View.GONE);
                linChats.setVisibility(View.VISIBLE);
                linPromos.setVisibility(View.GONE);
                cvSetting.setVisibility(View.GONE);
                linNoDataFound.setVisibility(View.GONE);
                if (SessionUtil.getLastSelectedChat(getActivity()) == 1) {
                    emmbeddedChatWindow.setVisibility(View.VISIBLE);
                    linNoDataFound.setVisibility(View.GONE);
                    startEmmbeddedChat();
                } else {
                    emmbeddedChatWindow.setVisibility(View.GONE);
                    linNoDataFound.setVisibility(View.VISIBLE);
                    tvNoDataFound.setText(getString(R.string.txt_chat_is_not_found));
                }
            } else if (messageTabType == MessageTabType.PROMOS) {
                tvOrders.setTypeface(null, Typeface.NORMAL);
                tvChats.setTypeface(null, Typeface.NORMAL);
                tvPromos.setTypeface(null, Typeface.BOLD);
                tvSettings.setTypeface(null, Typeface.NORMAL);
                linOrderNotification.setVisibility(View.GONE);
                linChats.setVisibility(View.GONE);
                linPromos.setVisibility(View.VISIBLE);
                cvSetting.setVisibility(View.GONE);
            } else if (messageTabType == MessageTabType.SETTINGS) {
                tvOrders.setTypeface(null, Typeface.NORMAL);
                tvChats.setTypeface(null, Typeface.NORMAL);
                tvPromos.setTypeface(null, Typeface.NORMAL);
                tvSettings.setTypeface(null, Typeface.BOLD);
                linOrderNotification.setVisibility(View.GONE);
                linChats.setVisibility(View.GONE);
                linPromos.setVisibility(View.GONE);
                cvSetting.setVisibility(View.VISIBLE);
                linNoDataFound.setVisibility(View.GONE);
            } else {
                tvOrders.setTypeface(null, Typeface.BOLD);
                tvChats.setTypeface(null, Typeface.NORMAL);
                tvPromos.setTypeface(null, Typeface.NORMAL);
                linOrderNotification.setVisibility(View.VISIBLE);
                linChats.setVisibility(View.GONE);
                linPromos.setVisibility(View.GONE);
                cvSetting.setVisibility(View.GONE);
                linNoDataFound.setVisibility(View.GONE);

            }
        }

    }


    private void checkNotifyStates() {
        Logger.d(TAG, TAG + ">>getLastSelectedNotification>>"+SessionUtil.getLastSelectedNotification(getActivity()));
        Logger.d(TAG, TAG + ">>getLastSelectedOrderInfo>>"+SessionUtil.getLastSelectedOrderInfo(getActivity()));
        Logger.d(TAG, TAG + ">>getLastSelectedPromos>>"+SessionUtil.getLastSelectedPromos(getActivity()));
         if (SessionUtil.getLastSelectedNotification(getActivity()) == 0) {
                switchSettingNotification.setChecked(false);
             SessionUtil.setLastSelectedChat(getActivity(),  0);

            } else {
                switchSettingNotification.setChecked( true );
             SessionUtil.setLastSelectedChat(getActivity(),  1);

         }
        if (SessionUtil.getLastSelectedChat(getActivity()) == 0) {
                switchSettingChat.setChecked(false);
            } else {
                switchSettingChat.setChecked(true);
            }

        if (SessionUtil.getLastSelectedOrderInfo(getActivity()) == 0) {
                switchSettingOrderInfo.setChecked(false);
            } else {
                switchSettingOrderInfo.setChecked(true);
            }

        if (SessionUtil.getLastSelectedPromos(getActivity()) == 0) {
            switchSettingPromoMgs.setChecked(false);
        } else {
            switchSettingPromoMgs.setChecked(true);
        }
    }

    private void initListenerSwitching() {

        switchSettingNotification.setOnCheckedChangeListener(new SwitchView.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchView switchView, boolean isChecked) {

                if (isChecked) {
                    SessionUtil.setLastSelectedNotificationn(getActivity(),  1);
                    switchSettingOrderInfo.setChecked(true);
                    switchSettingPromoMgs.setChecked(true);
                } else {
                    SessionUtil.setLastSelectedNotificationn(getActivity(),  0);
                    switchSettingOrderInfo.setChecked(false);
                    switchSettingPromoMgs.setChecked(false);

                }

            }
        });

        switchSettingChat.setOnCheckedChangeListener(new SwitchView.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchView switchView, boolean isChecked) {

                if (isChecked) {
                    SessionUtil.setLastSelectedChat(getActivity(),  1);
                } else {
                    SessionUtil.setLastSelectedChat(getActivity(),  0);

                }

            }
        });

        switchSettingOrderInfo.setOnCheckedChangeListener(new SwitchView.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchView switchView, boolean isChecked) {

                if (isChecked) {
                    SessionUtil.setLastSelectedOrderInfo(getActivity(),  1);
                } else {
                    SessionUtil.setLastSelectedOrderInfo(getActivity(),  0);

                }

            }
        });

        switchSettingPromoMgs.setOnCheckedChangeListener(new SwitchView.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchView switchView, boolean isChecked) {

                if (isChecked) {
                    SessionUtil.setLastSelectedPromos(getActivity(),  1);
                } else {
                    SessionUtil.setLastSelectedPromos(getActivity(),  0);

                }

            }
        });
    }

    @Override
    public void initFragmentBackPress() {

    }

    @Override
    public void initFragmentOnResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void initFragmentUpdate(Object object) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getAllOrderStatusListTask != null && getAllOrderStatusListTask.getStatus() == AsyncTask.Status.RUNNING) {
            getAllOrderStatusListTask.cancel(true);
        }

        if (doDeleteNotificationTask != null && doDeleteNotificationTask.getStatus() == AsyncTask.Status.RUNNING) {
            doDeleteNotificationTask.cancel(true);
        }

        if (getAllPromosListTask != null && getAllPromosListTask.getStatus() == AsyncTask.Status.RUNNING) {
            getAllPromosListTask.cancel(true);
        }

        // Unregister BroadCast
        //BroadcastManager.unregisterBroadcastUpdate(getActivity(), notificationUpdate);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void initNotificationView(List<Notifications> data) {

        if (data.size() > 0) {
            linNotificationView.setVisibility(View.VISIBLE);
            linNoDataFound.setVisibility(View.GONE);
            notificationListAdapter.addAll(data);
        } else {
            linNotificationView.setVisibility(View.GONE);
            linNoDataFound.setVisibility(View.VISIBLE);
        }
    }

    private void initPromosView(List<Promos> data) {
        if (data.size() > 0) {
            linOrderNotification.setVisibility(View.VISIBLE);
            linNoDataFound.setVisibility(View.GONE);
            mPromosListAdapter.addAll(data);
        } else {
            linNoDataFound.setVisibility(View.VISIBLE);
            linOrderNotification.setVisibility(View.GONE);
            tvNoDataFound.setText(getString(R.string.txt_promo_is_not_found));

        }

        }

    private void initOrderStatusView(List<Notifications> data) {
        if (data.size() > 0) {
            linNoDataFound.setVisibility(View.GONE);
            mNotifyOrderStatusAdapter = new NotificationOrderStatusAdapter(data);
            rvOrderNotificationList.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvOrderNotificationList.setAdapter(mNotifyOrderStatusAdapter);
        }else {
            linNoDataFound.setVisibility(View.VISIBLE);
            linPromos.setVisibility(View.GONE);
            tvNoDataFound.setText(getString(R.string.txt_notification_is_not_found));

        }
        }


    /*****************************
     * Order notification update *
     *****************************/
    BroadcastReceiver notificationUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Notifications notifications = BroadcastManager.getBroadcastData(intent);
            if (notifications != null) {
                mNotifications = notifications;
                setSettingDataInfo(mNotifications);
                Logger.d(TAG, TAG + ">>notificationUpdate>> FCM: Updated Notification List");
            }
        }
    };

    private void setSettingDataInfo(Notifications notifications) {

        if (notifications != null) {
            notificationListAdapter.addNotification(notifications);
//            shippingAddressAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onChatWindowVisibilityChanged(boolean visible) {

    }

    @Override
    public void onNewMessage(NewMessageModel message, boolean windowVisible) {
        Logger.d(TAG,  message.toString()+ "NewMessageModel");
        Logger.d(TAG,  windowVisible+ "windowVisible");

    }

    @Override
    public void onStartFilePickerActivity(Intent intent, int requestCode) {

    }

    @Override
    public boolean handleUri(Uri uri) {
        return false;
    }

    /************************
     * Server communication *
     ************************/

    private class GetAllOrderStatusListTask extends AsyncTask<String, Integer, Response> {

        Context mContext;

        private GetAllOrderStatusListTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
//            ProgressDialog progressDialog = showProgressDialog();
//            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                    cancel(true);
//                }
//            });
            showPopupDialog();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Logger.d(TAG, TAG + ">> Background task is cancelled");
        }

        @Override
        protected Response doInBackground(String... params) {
            try {
                Call<APIResponse<List<Notifications>>> call = mApiInterface.apiGetAllOrderNotifyStatusList(customerOrSupplierId,selectUserType);
                Response response = call.execute();
                if (response.isSuccessful()) {
                    return response;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Response result) {
            try {
                dismissPopupDialog();

                if (result != null && result.isSuccessful()) {
                    Logger.d(TAG, "APIResponse(GetAllOrderStatusListTask): onResponse-server = " + result.toString());
                    APIResponse<List<Notifications>> data = (APIResponse<List<Notifications>>) result.body();
                    Logger.d("GetAllOrderStatusListTask", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(GetAllOrderStatusListTask()): onResponse-object = " + data.toString());
                        mNotificationsList = data.getData();
                        initOrderStatusView(mNotificationsList);

                       // initNotificationView(data.getData());
                    } else {
                        linNoDataFound.setVisibility(View.VISIBLE);
                        tvNoDataFound.setText(getString(R.string.txt_notification_is_not_found));
                    }
                } else {
                    // loadOfflineTimeData();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private class DoDeleteNotificationTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        Notifications  notifications;

        private DoDeleteNotificationTask(Context context,Notifications notification) {
            mContext = context;
            notifications = notification;

        }

        @Override
        protected void onPreExecute() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showPopupDialog();
                }
            }, 50);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Logger.d(TAG, TAG + ">> Background task is cancelled");
        }

        @Override
        protected Response doInBackground(String... params) {
            try {
                ParamsDeleteNotification mParamsDeleteNotification = new ParamsDeleteNotification(notifications.getId());
                Logger.d(TAG, TAG + " >>> APIResponse(mParamsDeleteNotification): " + mParamsDeleteNotification.toString());
                Call<APIResponse> call = mApiInterface.apiDeleteNotification(mParamsDeleteNotification);
                Response response = call.execute();
                Logger.d("DoOrderDeleteTask", response + "response");

                if (response.isSuccessful()) {
                    return response;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Response result) {
            try {
                dismissPopupDialog();
                Logger.d("isSuccessful", result.isSuccessful() + "");

                if (result.isSuccessful()) {
                    Logger.d(TAG, "APIResponse(DoDeleteNotificationTask): onResponse-server = " + result.toString());
                    APIResponse data = (APIResponse) result.body();
                    Logger.d("DoDeleteNotificationTask", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        //mPlaceOrderListAdapter.removeItem(placeOrderByItem);
                        Logger.d("notifications", notifications.toString() + "");

                        mNotificationsList.remove(notifications);
                        mNotifyOrderStatusAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), getResources().getString(R.string.toast_notification_delete_mgs), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private class GetAllPromosListTask extends AsyncTask<String, Integer, Response> {

        Context mContext;

        private GetAllPromosListTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
//            ProgressDialog progressDialog = showProgressDialog();
//            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                    cancel(true);
//                }
//            });
            showPopupDialog();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Logger.d(TAG, TAG + ">> Background task is cancelled");
        }

        @Override
        protected Response doInBackground(String... params) {
            try {
                Call<APIResponse<List<Promos>>> call = mApiInterface.apiGetAllPromosList();
                Response response = call.execute();
                if (response.isSuccessful()) {
                    return response;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Response result) {
            try {
                dismissPopupDialog();

                if (result != null && result.isSuccessful()) {
                    Logger.d(TAG, "APIResponse(GetAllOrderStatusListTask): onResponse-server = " + result.toString());
                    APIResponse<List<Promos>> data = (APIResponse<List<Promos>>) result.body();
                    Logger.d("GetAllOrderStatusListTask", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(GetAllOrderStatusListTask()): onResponse-object = " + data.toString());

                         initPromosView(data.getData());
                    } else {
                        linNoDataFound.setVisibility(View.VISIBLE);
                        tvNoDataFound.setText(getString(R.string.txt_promo_is_not_found));
                    }
                } else {
                    // loadOfflineTimeData();
                    Logger.d(TAG, "onResponse-object = result" + result.message());

                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }



}