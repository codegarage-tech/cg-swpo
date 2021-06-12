package com.meembusoft.safewaypharmaonline.viewholder;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.MedicineRemainderActivity;
import com.meembusoft.safewaypharmaonline.activity.ProductDetailsActivity;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.reversecoder.library.event.OnSingleClickListener;

import org.parceler.Parcels;

import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_PRODUCT_ITEM;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class BestSellerProductViewHolder extends BaseViewHolder<StaggeredMedicineByItem> {

    private static String TAG = BestSellerProductViewHolder.class.getSimpleName();
    private ImageView ivBaseSeller;
    private TextView tvName,tvGenericName;
    public BestSellerProductViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_base_seller_horizontal);

        ivBaseSeller = (ImageView) $(R.id.iv_base_seller);
        tvName = (TextView) $(R.id.tv_name);
        tvGenericName = (TextView) $(R.id.tv_generic_name);
    }

    @Override
    public void setData(final StaggeredMedicineByItem baseSellerItem) {
        //Set data
        tvName.setText(AppUtil.optStringNullCheckValue(baseSellerItem.getName()));
        tvGenericName.setText(AppUtil.optStringNullCheckValue(baseSellerItem.getGeneric_name()));

        AppUtil.loadImage(getContext(), ivBaseSeller, baseSellerItem.getProduct_image(), false, false, true);

        itemView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intentProductDetails = new Intent(getContext(), ProductDetailsActivity.class);
                intentProductDetails.putExtra(INTENT_KEY_PRODUCT_ITEM, Parcels.wrap(baseSellerItem));
                getContext().startActivity(intentProductDetails);
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intentMedicineRemainder = new Intent(getContext(), MedicineRemainderActivity.class);
                intentMedicineRemainder.putExtra(INTENT_KEY_PRODUCT_ITEM, Parcels.wrap(baseSellerItem));
                getContext().startActivity(intentMedicineRemainder);
                return true;
            }
        });
    }
}