package com.meembusoft.safewaypharmaonline.viewholder;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.ProductDetailsActivity;
import com.meembusoft.safewaypharmaonline.model.Category;
import com.meembusoft.safewaypharmaonline.model.CommonData;
import com.meembusoft.safewaypharmaonline.model.RelatedMedicine;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.reversecoder.library.event.OnSingleClickListener;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class WhyChooseUsViewHolder extends BaseViewHolder<CommonData> {

    private static String TAG = WhyChooseUsViewHolder.class.getSimpleName();
    private TextView tvName;
    private ImageView ivWhyChooseUs;

    public WhyChooseUsViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_why_choose_us);

        tvName = (TextView) $(R.id.tv_name);
        ivWhyChooseUs = (ImageView) $(R.id.iv_why_choose_us);
    }

    @Override
    public void setData(final CommonData data) {
        //Set data
        tvName.setText(data.getName());

        AppUtil.loadImage(getContext(), ivWhyChooseUs, data.getIcon(), false, false, true);

        itemView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
//                Intent intentDetails = new Intent(getContext(), ProductDetailsActivity.class);
//                getContext().startActivity(intentDetails);
            }
        });
    }
}