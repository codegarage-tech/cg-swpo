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
import com.meembusoft.safewaypharmaonline.model.RelatedMedicine;
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
public class RelatedProductViewHolder extends BaseViewHolder<RelatedMedicine> {

    private static String TAG = RelatedProductViewHolder.class.getSimpleName();
    private TextView tvEmpty,tvName,tvGenericName,tvAmount,tvStrikeAmount;
    private ImageView ivProduct;

    public RelatedProductViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_related_product_view);

        tvEmpty = (TextView) $(R.id.tv_empty);
        tvName = (TextView) $(R.id.tv_product_name);
        tvGenericName = (TextView) $(R.id.tv_product_generic_name);
        tvAmount = (TextView) $(R.id.tv_product_amount);
        tvStrikeAmount = (TextView) $(R.id.tv_strike_amount);
        ivProduct = (ImageView) $(R.id.iv_product);
    }

    @Override
    public void setData(final RelatedMedicine relatedMedicine) {
        //Set data
        tvName.setText(AppUtil.optStringNullCheckValue(relatedMedicine.getName())+" "+AppUtil.optStringNullCheckValue(relatedMedicine.getForm_name()));
        tvGenericName.setText(AppUtil.optStringNullCheckValue(relatedMedicine.getGeneric_name()));
        //tvAmount.setText(getContext().getString(R.string.title_tk)+" "+relatedMedicine.getSelling_price());

        // Discount Calculation
        float discountPercent = (((!AllSettingsManager.isNullOrEmpty(relatedMedicine.getDiscount_percent()) && (Float.parseFloat(relatedMedicine.getDiscount_percent()) > 0))) ? (Float.parseFloat(relatedMedicine.getDiscount_percent())) :  0.00f);
        if (!AllSettingsManager.isNullOrEmpty(relatedMedicine.getDiscount_percent()) && discountPercent > 0) {
            tvStrikeAmount.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);

            try {
                float discountCost = (((!AllSettingsManager.isNullOrEmpty(relatedMedicine.getDiscount_percent()) && (Float.parseFloat(relatedMedicine.getDiscount_percent()) > 0))) ? (Float.parseFloat(relatedMedicine.getDiscount_percent())) : 0.00f);
                float originSellingPrice = (((!AllSettingsManager.isNullOrEmpty(relatedMedicine.getSelling_price()) && (Float.parseFloat(relatedMedicine.getSelling_price()) > 0))) ? (Float.parseFloat(relatedMedicine.getSelling_price())) : 0.00f);
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
            tvAmount.setText(getContext().getString(R.string.title_tk)+" " +AppUtil.optStringNullCheckValue(relatedMedicine.getSelling_price()));
            tvStrikeAmount.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);

        }

        AppUtil.loadImage(getContext(), ivProduct, relatedMedicine.getProduct_image(), false, false, true);

        itemView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                StaggeredMedicineByItem medicineByItem = new StaggeredMedicineByItem(relatedMedicine.getId(),relatedMedicine.getName(),relatedMedicine.getDiscount_percent(),relatedMedicine.getTrade_price(),relatedMedicine.getSelling_price(),relatedMedicine.getExpire_date(),relatedMedicine.getsImage(),relatedMedicine.getMaster_supplier_id(),relatedMedicine.getSupplier_name(), relatedMedicine.getMaster_supplier_code(),
                                                                          relatedMedicine.getMaster_form_id(),relatedMedicine.getForm_name(),relatedMedicine.getGeneric_name(),relatedMedicine.getBox_size(),relatedMedicine.getBox_price(),relatedMedicine.getThumb_photo(),relatedMedicine.getFavourite(),0,relatedMedicine.getProduct_image(),0,false);

                Intent intentDetails = new Intent(getContext(), ProductDetailsActivity.class);
                intentDetails.putExtra(INTENT_KEY_PRODUCT_ITEM, Parcels.wrap(medicineByItem));
                getContext().startActivity(intentDetails);
            }
        });
    }
}