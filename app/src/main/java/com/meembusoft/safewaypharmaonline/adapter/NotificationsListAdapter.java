package com.meembusoft.safewaypharmaonline.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.meembusoft.safewaypharmaonline.model.CommonData;
import com.meembusoft.safewaypharmaonline.model.Notifications;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.viewholder.NotificationsViewHolder;
import com.meembusoft.safewaypharmaonline.viewholder.WhyChooseUsViewHolder;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class NotificationsListAdapter extends RecyclerArrayAdapter<Notifications> {
    private String TAG = NotificationsListAdapter.class.getSimpleName();

    private static final int VIEW_TYPE_REGULAR = 1;

    public NotificationsListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getViewType(int position) {
        return VIEW_TYPE_REGULAR;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_REGULAR:
                return new NotificationsViewHolder(parent);
            default:
                throw new InvalidParameterException();
        }
    }

    public void addNotification(Notifications mNotificationData) {
        if (mNotificationData != null) {
            Logger.d(TAG, TAG + " >>>mNotifyFrag " + "mNotifications: " + mNotificationData.toString());
            List<Notifications> properties = new ArrayList<>();
            properties.addAll( getAllData() );
            properties.add( mNotificationData );
            clear();
            addAll( properties );
            notifyDataSetChanged();
        }
    }

}