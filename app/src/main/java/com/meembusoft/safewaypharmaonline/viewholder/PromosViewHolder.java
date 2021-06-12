package com.meembusoft.safewaypharmaonline.viewholder;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.CategoryActivity2;
import com.meembusoft.safewaypharmaonline.model.CommonData;
import com.meembusoft.safewaypharmaonline.model.Promos;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.melih.strokedtextview.StrokedTextView;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.util.AllSettingsManager;

import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_CATEGORY_ID;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_CATEGORY_TITLE;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class PromosViewHolder extends BaseViewHolder<Promos> {

    private static String TAG = PromosViewHolder.class.getSimpleName();
    private ImageView ivPromos;
    private StrokedTextView tvStrokedTitle;

    public PromosViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_promos_item);

        tvStrokedTitle = (StrokedTextView) $(R.id.tv_stroked_title);
        ivPromos = (ImageView) $(R.id.iv_promos);
    }

    @Override
    public void setData(final Promos data) {
        //Set data
        tvStrokedTitle.setText(data.getTitle());
        AppUtil.loadImage(getContext(), ivPromos, data.getImg_url(), false, false, true);

        itemView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (!AllSettingsManager.isNullOrEmpty(data.getLink())) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.getLink()));
                    getContext().startActivity(browserIntent);
                }
            }
        });
    }
}