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
public class LatestProductViewHolder extends BaseViewHolder<StaggeredMedicineByItem> {

    private static String TAG = LatestProductViewHolder.class.getSimpleName();
    private TextView tvEmpty,tvName, tvGenericName, tvAmount,tvStrikeAmount;
    private ImageView ivProduct;

    public LatestProductViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_related_product_view);
        tvEmpty = (TextView) $(R.id.tv_empty);
        tvName = (TextView) $(R.id.tv_product_name);
        tvGenericName = (TextView) $(R.id.tv_product_generic_name);
        tvAmount = (TextView) $(R.id.tv_product_amount);
        ivProduct = (ImageView) $(R.id.iv_product);
        tvStrikeAmount = (TextView) $(R.id.tv_strike_amount);

    }

    @Override
    public void setData(final StaggeredMedicineByItem latestProduct) {
        //Set data
        tvName.setText(AppUtil.optStringNullCheckValue(latestProduct.getName())+" "+AppUtil.optStringNullCheckValue(latestProduct.getForm_name()));
        tvGenericName.setText(AppUtil.optStringNullCheckValue(latestProduct.getGeneric_name()));
        // Discount Calculation
        float discountPercent = (((!AllSettingsManager.isNullOrEmpty(latestProduct.getDiscount_percent()) && (Float.parseFloat(latestProduct.getDiscount_percent()) > 0))) ? (Float.parseFloat(latestProduct.getDiscount_percent())) :  0.00f);
        if (!AllSettingsManager.isNullOrEmpty(latestProduct.getDiscount_percent()) && discountPercent > 0) {
            tvStrikeAmount.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);            try {
                float discountCost = (((!AllSettingsManager.isNullOrEmpty(latestProduct.getDiscount_percent()) && (Float.parseFloat(latestProduct.getDiscount_percent()) > 0))) ? (Float.parseFloat(latestProduct.getDiscount_percent())) : 0.00f);
                float originSellingPrice = (((!AllSettingsManager.isNullOrEmpty(latestProduct.getSelling_price()) && (Float.parseFloat(latestProduct.getSelling_price()) > 0))) ? (Float.parseFloat(latestProduct.getSelling_price())) : 0.00f);
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
            tvAmount.setText(getContext().getString(R.string.title_tk)+" " +AppUtil.optStringNullCheckValue(latestProduct.getSelling_price()));
            tvStrikeAmount.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        }

        AppUtil.loadImage(getContext(), ivProduct, latestProduct.getProduct_image(), false, false, true);

        itemView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intentDetails = new Intent(getContext(), ProductDetailsActivity.class);
                intentDetails.putExtra(INTENT_KEY_PRODUCT_ITEM, Parcels.wrap(latestProduct));
                getContext().startActivity(intentDetails);
            }
        });
    }
}