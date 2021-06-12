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
public class MedicineByCategoryViewHolder extends BaseViewHolder<StaggeredMedicineByItem> {

    private static String TAG = MedicineByCategoryViewHolder.class.getSimpleName();
    private TextView tvEmpty,tvFormName,tvGenericName,tvAmount,tvStrikeAmount;
    private ImageView ivMedicineByCategory;
    private String mCategoryName = "";

    public MedicineByCategoryViewHolder(ViewGroup parent,String categoryName) {
        super(parent, R.layout.row_straggred_sub_category_view);
        mCategoryName = categoryName;
        Log.d(TAG, "mCategoryName:>>> " + mCategoryName.toString());
        tvEmpty = (TextView) $(R.id.tv_empty);
        tvFormName = (TextView) $(R.id.tv_form_name);
        tvGenericName = (TextView) $(R.id.tv_generic_name);
        tvAmount = (TextView) $(R.id.tv_amount);
        tvStrikeAmount = (TextView) $(R.id.tv_strike_amount);
        ivMedicineByCategory = (ImageView) $(R.id.iv_medicine_by_category);
    }

    @Override
    public void setData(final StaggeredMedicineByItem medicineByItem) {
        //Set data
        if (mCategoryName.equalsIgnoreCase(getContext().getResources().getString(R.string.title_general_medicine)) || mCategoryName.equalsIgnoreCase(getContext().getResources().getString(R.string.title_herbal_medicine))){
            tvFormName.setText(AppUtil.optStringNullCheckValue(medicineByItem.getName())+" "+AppUtil.optStringNullCheckValue(medicineByItem.getForm_name()));
            tvGenericName.setText(AppUtil.optStringNullCheckValue(medicineByItem.getGeneric_name()));
        } else if (mCategoryName.equalsIgnoreCase(getContext().getResources().getString(R.string.title_medical_instrument))){
            tvFormName.setText(AppUtil.optStringNullCheckValue(medicineByItem.getName()));
            tvGenericName.setText(AppUtil.optStringNullCheckValue(medicineByItem.getOrigin()));
        } else if (mCategoryName.equalsIgnoreCase(getContext().getResources().getString(R.string.title_baby_food_statioary2)) || mCategoryName.equalsIgnoreCase(getContext().getResources().getString(R.string.title_medical_cosmetics))){
            tvFormName.setText(AppUtil.optStringNullCheckValue(medicineByItem.getName()+" "+AppUtil.optStringNullCheckValue(medicineByItem.getForm_name())));
            tvGenericName.setVisibility(View.GONE);
            Log.d(TAG, "tvgetName:>>> " + medicineByItem.getName());

        } else {
            tvFormName.setText(AppUtil.optStringNullCheckValue(medicineByItem.getName()));
            tvGenericName.setText(AppUtil.optStringNullCheckValue(medicineByItem.getGeneric_name()));
        }
        // Discount Calculation
        float discountPercent = (((!AllSettingsManager.isNullOrEmpty(medicineByItem.getDiscount_percent()) && (Float.parseFloat(medicineByItem.getDiscount_percent()) > 0))) ? (Float.parseFloat(medicineByItem.getDiscount_percent())) :  0.00f);
        if (!AllSettingsManager.isNullOrEmpty(medicineByItem.getDiscount_percent()) && discountPercent > 0) {
            tvStrikeAmount.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
            try {
                float discountCost = (((!AllSettingsManager.isNullOrEmpty(medicineByItem.getDiscount_percent()) && (Float.parseFloat(medicineByItem.getDiscount_percent()) > 0))) ? (Float.parseFloat(medicineByItem.getDiscount_percent())) : 0.00f);
                float originSellingPrice = (((!AllSettingsManager.isNullOrEmpty(medicineByItem.getSelling_price()) && (Float.parseFloat(medicineByItem.getSelling_price()) > 0))) ? (Float.parseFloat(medicineByItem.getSelling_price())) : 0.00f);
                float minusSubtotalToDiscountCost = AppUtil.getTotalPromotionalDiscountPrice(originSellingPrice, discountCost);
                Log.e("minusToDiscountCost", minusSubtotalToDiscountCost + "llMinus");
                String finalSubtotalToDiscountCost = AppUtil.formatDoubleString(minusSubtotalToDiscountCost);
                tvAmount.setText("" + finalSubtotalToDiscountCost);
                tvStrikeAmount.setText(getContext().getString(R.string.title_tk) + " " + AppUtil.formatDoubleString(originSellingPrice));
                tvStrikeAmount.setPaintFlags(tvStrikeAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } catch (NumberFormatException nfe) {
                System.out.println("NumberFormatException: " + nfe.getMessage());
            }
        } else {
            tvAmount.setText(AppUtil.optStringNullCheckValue(medicineByItem.getSelling_price()));
            tvEmpty.setVisibility(View.VISIBLE);
            tvStrikeAmount.setVisibility(View.GONE);

        }
        AppUtil.loadImage(getContext(), ivMedicineByCategory, medicineByItem.getProduct_image(), false, false, true);

        itemView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intentProductDetails = new Intent(getContext(), ProductDetailsActivity.class);
                intentProductDetails.putExtra(INTENT_KEY_PRODUCT_ITEM, Parcels.wrap(medicineByItem));
                getContext().startActivity(intentProductDetails);
            }
        });
    }
}