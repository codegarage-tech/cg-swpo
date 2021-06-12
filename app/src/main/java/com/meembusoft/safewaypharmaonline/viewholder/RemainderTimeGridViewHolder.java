package com.meembusoft.safewaypharmaonline.viewholder;

import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.util.AppUtil;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class RemainderTimeGridViewHolder extends BaseViewHolder<String> {

    private static String TAG = RemainderTimeGridViewHolder.class.getSimpleName();
    private TextView tvTime;

    public RemainderTimeGridViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_time_grid_view);

        tvTime = (TextView) $(R.id.tv_time);
    }

    @Override
    public void setData(final String data) {
        //Set data
//        String attributes = "";
//        if (data != null) {
//            attributes = data;
//            String[] result = attributes.split("'");
//            attributes = result[0];
//        }

        tvTime.setText(AppUtil.optStringNullCheckValue(data));
    }
}