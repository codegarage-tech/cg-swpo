package com.meembusoft.safewaypharmaonline.viewholder;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.model.FilterSessionLeft;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.melih.strokedtextview.StrokedTextView;
import com.reversecoder.library.util.AllSettingsManager;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class FilterSessionLeftGridViewHolder extends BaseViewHolder<String> {

    private static String TAG = FilterSessionLeftGridViewHolder.class.getSimpleName();
    private ImageView ivMedicine;

    public FilterSessionLeftGridViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_filter_grid_view);

        ivMedicine = (ImageView) $(R.id.iv_medicine);
    }

    @Override
    public void setData(final String data) {
        //Set data
        if (!AllSettingsManager.isNullOrEmpty(data)) {
            AppUtil.loadImage(getContext(), ivMedicine, data, false, false, true);
        } else {
            AppUtil.loadImage(getContext(), ivMedicine, R.mipmap.capsule, false, false, true);

        }

    }
}