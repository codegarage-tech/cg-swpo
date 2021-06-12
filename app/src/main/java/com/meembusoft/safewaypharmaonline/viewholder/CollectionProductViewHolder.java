package com.meembusoft.safewaypharmaonline.viewholder;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.CategoryActivity2;
import com.meembusoft.safewaypharmaonline.activity.ProductDetailsActivity;
import com.meembusoft.safewaypharmaonline.model.CommonData;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.reversecoder.library.event.OnSingleClickListener;

import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_CATEGORY_ID;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_CATEGORY_TITLE;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class CollectionProductViewHolder extends BaseViewHolder<CommonData> {

    private static String TAG = CollectionProductViewHolder.class.getSimpleName();
    private TextView tvName,tvGenericName,tvAmount;
    private ImageView ivCollection;

    public CollectionProductViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_collecttion_view);

        tvName = (TextView) $(R.id.tv_collection_name);
        ivCollection = (ImageView) $(R.id.iv_collection);
    }

    @Override
    public void setData(final CommonData data) {
        //Set data
        tvName.setText(data.getName());
        AppUtil.loadImage(getContext(), ivCollection, data.getThumb_image(), false, false, true);

        itemView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intentCategory = new Intent(getContext(), CategoryActivity2.class);
                intentCategory.putExtra(INTENT_KEY_CATEGORY_ID, data.getId());
                intentCategory.putExtra(INTENT_KEY_CATEGORY_TITLE, data.getName());
                getContext().startActivity(intentCategory);
            }
        });
    }
}