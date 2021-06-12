package com.meembusoft.safewaypharmaonline.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.model.Notifications;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.reversecoder.library.event.OnSingleClickListener;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class NotificationsViewHolder extends BaseViewHolder<Notifications> {

    private static String TAG = NotificationsViewHolder.class.getSimpleName();
    private TextView tvTitle,tvMgs,tvDate;

    public NotificationsViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_notification_view);

        tvTitle = (TextView) $(R.id.tv_notification_title);
        tvMgs = (TextView) $(R.id.tv_notification_mgs);
        tvDate = (TextView) $(R.id.tv_notification_date);
    }

    @Override
    public void setData(final Notifications data) {
        //Set data
        tvTitle.setText(AppUtil.optStringNullCheckValue(data.getTitle()));
        tvMgs.setText(AppUtil.optStringNullCheckValue(data.getMessage()));
        tvDate.setText(AppUtil.optStringNullCheckValue(data.getCreated_at()));


    }
}