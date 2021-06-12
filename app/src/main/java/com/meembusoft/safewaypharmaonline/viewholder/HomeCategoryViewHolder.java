package com.meembusoft.safewaypharmaonline.viewholder;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.CategoryActivity;
import com.meembusoft.safewaypharmaonline.activity.CategoryActivity2;
import com.meembusoft.safewaypharmaonline.activity.ProductDetailsActivity;
import com.meembusoft.safewaypharmaonline.model.Category;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.reversecoder.library.event.OnSingleClickListener;

import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_CATEGORY_ID;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_CATEGORY_TITLE;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class HomeCategoryViewHolder extends BaseViewHolder<Category> {

    private static String TAG = HomeCategoryViewHolder.class.getSimpleName();
    private TextView titleView;
    private ImageView ivCategory;

    public HomeCategoryViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_category_feature);

        titleView = (TextView) $(R.id.tv_title);
        ivCategory = (ImageView) $(R.id.iv_category);
    }

    @Override
    public void setData(final Category category) {
        //Set data
        Logger.d(TAG, TAG + " >>> " + "category:" +  category.getId());

        titleView.setText(category.getName());
        AppUtil.loadImage(getContext(), ivCategory, category.getThumb_image(), false, false, true);

        itemView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intentCategory = new Intent(getContext(), CategoryActivity2.class);
                intentCategory.putExtra(INTENT_KEY_CATEGORY_ID, category.getId());
                intentCategory.putExtra(INTENT_KEY_CATEGORY_TITLE, category.getName());
                getContext().startActivity(intentCategory);
            }
        });
    }
}