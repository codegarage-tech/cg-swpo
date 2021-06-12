package com.meembusoft.safewaypharmaonline.viewholder;

import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.ProductDetailsActivity;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.util.AllSettingsManager;

import org.parceler.Parcels;

import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_PRODUCT_ITEM;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class EssentialProductViewHolder extends BaseViewHolder<StaggeredMedicineByItem> {

    private static String TAG = EssentialProductViewHolder.class.getSimpleName();
    private ImageView ivProduct;
    private TextView tvEmpty,tvName,tvGenericName,tvAmount,tvStrikeAmount;
    public EssentialProductViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_essential_feature);

        ivProduct = (ImageView) $(R.id.iv_product);
        tvEmpty = (TextView) $(R.id.tv_empty);
        tvName = (TextView) $(R.id.tv_name);
        tvGenericName = (TextView) $(R.id.tv_product_generic_name);
        tvAmount = (TextView) $(R.id.tv_product_amount);
        tvStrikeAmount = (TextView) $(R.id.tv_strike_amount);

    }

    @Override
    public void setData(final StaggeredMedicineByItem data) {
        //Set data
        
        tvName.setText(AppUtil.optStringNullCheckValue(data.getName())+" "+AppUtil.optStringNullCheckValue(data.getForm_name()));
        tvGenericName.setText(AppUtil.optStringNullCheckValue(data.getGeneric_name()));
        // Discount Calculation
        float discountPercent = (((!AllSettingsManager.isNullOrEmpty(data.getDiscount_percent()) && (Float.parseFloat(data.getDiscount_percent()) > 0))) ? (Float.parseFloat(data.getDiscount_percent())) :  0.00f);
        if (!AllSettingsManager.isNullOrEmpty(data.getDiscount_percent()) && discountPercent > 0) {
            tvStrikeAmount.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
            try {
                float discountCost = (((!AllSettingsManager.isNullOrEmpty(data.getDiscount_percent()) && (Float.parseFloat(data.getDiscount_percent()) > 0))) ? (Float.parseFloat(data.getDiscount_percent())) : 0.00f);
                float originSellingPrice = (((!AllSettingsManager.isNullOrEmpty(data.getSelling_price()) && (Float.parseFloat(data.getSelling_price()) > 0))) ? (Float.parseFloat(data.getSelling_price())) : 0.00f);
                float minusSubtotalToDiscountCost = AppUtil.getTotalPromotionalDiscountPrice(originSellingPrice, discountCost);
                Log.e("minusToDiscountCost", minusSubtotalToDiscountCost + "llMinus");
                String finalSubtotalToDiscountCost = AppUtil.formatDoubleString(minusSubtotalToDiscountCost);
                tvAmount.setText(getContext().getString(R.string.title_tk)+" " +"" + finalSubtotalToDiscountCost);
                tvStrikeAmount.setText(getContext().getString(R.string.title_tk) + " " + AppUtil.formatDoubleString(originSellingPrice));
                tvStrikeAmount.setPaintFlags(tvStrikeAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } catch (NumberFormatException nfe) {
                System.out.println("NumberFormatException: " + nfe.getMessage());
            }
        } else {
            tvAmount.setText(getContext().getString(R.string.title_tk)+" " +AppUtil.optStringNullCheckValue(data.getSelling_price()));
            tvStrikeAmount.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);

        }

        AppUtil.loadImage(getContext(), ivProduct, data.getProduct_image(), false, false, true);

        itemView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intentProductDetails = new Intent(getContext(), ProductDetailsActivity.class);
                intentProductDetails.putExtra(INTENT_KEY_PRODUCT_ITEM, Parcels.wrap(data));
                getContext().startActivity(intentProductDetails);
            }
        });
    }
}