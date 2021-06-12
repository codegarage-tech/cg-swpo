package com.meembusoft.safewaypharmaonline.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.model.Notifications;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.view.TimeAgoTextView;

import java.util.List;



public class NotificationOrderStatusAdapter extends RecyclerView.Adapter<NotificationOrderStatusAdapter.ViewHolder> {

    private final List<Notifications> mList;

    public NotificationOrderStatusAdapter(List<Notifications> list) {
        mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_order_notification_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notifications mNotifications = mList.get(position);
        holder.tvTitle.setText(mNotifications.getTitle());
        holder.tvDescription.setText(mNotifications.getDescription());
        if (!AppUtil.optStringNullCheckValue(mNotifications.getNotify_time()).equalsIgnoreCase("")){
            long getTime = Long.parseLong(mNotifications.getNotify_time())*1000;
            Log.e("getTime",getTime+"getTime");
            holder.timestamp.setTimestamp(getTime);
            holder.timestamp.setVisibility(View.VISIBLE);
        } else {
            holder.timestamp.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvDescription;
        private TimeAgoTextView timestamp;

        public ViewHolder(@NonNull View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tv_notification_title);
            tvDescription = v.findViewById(R.id.tv_notification_description);
            timestamp = v.findViewById(R.id.tv_notification_timestamp);
        }
    }
}
