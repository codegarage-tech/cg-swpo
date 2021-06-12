package com.meembusoft.safewaypharmaonline.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.reversecoder.library.event.OnSingleClickListener;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AllBestSellerProductViewHolder extends BaseViewHolder<StaggeredMedicineByItem> {

    private static String TAG = AllBestSellerProductViewHolder.class.getSimpleName();
    private ImageView ivBaseSeller;

    public AllBestSellerProductViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_base_seller);

        ivBaseSeller = (ImageView) $(R.id.iv_base_seller);
    }

    @Override
    public void setData(final StaggeredMedicineByItem baseSellerItem) {
        //Set data
        AppUtil.loadImage(getContext(), ivBaseSeller, baseSellerItem.getThumb_photo(), false, false, true);

        itemView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
//                Intent intentProductDetails = new Intent(getContext(), ProductDetailsActivity.class);
//                intentProductDetails.putExtra(INTENT_KEY_PRODUCT_ITEM, Parcels.wrap(medicineByItem));
//                getContext().startActivity(intentProductDetails);
            }
        });
    }
}