package com.meembusoft.safewaypharmaonline.viewholder;

import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.ProductDetailsActivity;
import com.meembusoft.safewaypharmaonline.enumeration.CategoryType;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.util.AllSettingsManager;

import org.parceler.Parcels;

import java.util.Random;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_PRODUCT_ITEM;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SeeMoreStaggeredViewHolder extends BaseViewHolder<StaggeredMedicineByItem> {

    private static String TAG = SeeMoreStaggeredViewHolder.class.getSimpleName();
    private TextView tvName,tvGenericName,tvAmount,tvQty,tvStrikeAmount;
    private ImageView ivProduct;
    private CardView cvShopStaggered;
    private int largeCardHeight, smallCardHeight;
    CategoryType mMedicineType;

    public SeeMoreStaggeredViewHolder(ViewGroup parent,CategoryType medicineType) {
        super(parent, R.layout.row_shop_staggered_view);
        mMedicineType = medicineType;
        Log.d(TAG, "mMedicineType:>>> " + mMedicineType.name());
        largeCardHeight = AppUtil.dip2px(getContext(), 300);
        smallCardHeight = AppUtil.dip2px(getContext(), 260);
        tvName = (TextView) $(R.id.tv_name);
        tvGenericName = (TextView) $(R.id.tv_generic_name);
        tvAmount = (TextView) $(R.id.tv_amount);
        tvStrikeAmount = (TextView) $(R.id.tv_strike_amount);

        //   tvQty = (TextView) $(R.id.tv_qty);
        ivProduct = (ImageView) $(R.id.iv_product);
        cvShopStaggered = (CardView) $(R.id.cv_shop_staggered);

        Random random = new Random();
        int result = random.nextInt(300-200) + 220;
        ivProduct.getLayoutParams().height = result;
        ivProduct.requestLayout();


//        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
//        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
//            cvShopStaggered.getLayoutParams().height = position % 2 != 0 ? largeCardHeight : smallCardHeight;
//        }
//       position ++ ;
//        Log.e("position>>",position+"");
    }

    @Override
    public void setData(final StaggeredMedicineByItem data) {
        //Set data

      //  tvQty.setText(data.getBox_size()+"");
        if (mMedicineType.name().equalsIgnoreCase(CategoryType.GENERAL_MEDICINE.name()) || mMedicineType.name().equalsIgnoreCase(CategoryType.HERBAL_MEDICINE.name())){
            tvName.setText(AppUtil.optStringNullCheckValue(data.getName()) +" "+AppUtil.optStringNullCheckValue(data.getForm_name()));
            tvGenericName.setText(AppUtil.optStringNullCheckValue(data.getGeneric_name()));
        } else if (mMedicineType.name().equalsIgnoreCase(CategoryType.MEDICAL_INSTRUMENT.name())){
            tvName.setText(AppUtil.optStringNullCheckValue(data.getName()));
            tvGenericName.setText(AppUtil.optStringNullCheckValue(data.getOrigin()));
        }  else if (mMedicineType.name().equalsIgnoreCase(CategoryType.BABY_FOOD_STATIONARY.name()) || mMedicineType.name().equalsIgnoreCase(CategoryType.COSMETICS.name())){
            tvName.setText(AppUtil.optStringNullCheckValue(data.getName()));
            tvGenericName.setVisibility(View.GONE);
        } else {
            tvName.setText(AppUtil.optStringNullCheckValue(data.getName()));
            tvGenericName.setText(AppUtil.optStringNullCheckValue(data.getGeneric_name()));
        }


        // Discount Calculation
        float discountPercent = (((!AllSettingsManager.isNullOrEmpty(data.getDiscount_percent()) && (Float.parseFloat(data.getDiscount_percent()) > 0))) ? (Float.parseFloat(data.getDiscount_percent())) :  0.00f);

        if (!AllSettingsManager.isNullOrEmpty(data.getDiscount_percent()) && discountPercent > 0) {
            tvStrikeAmount.setVisibility(View.VISIBLE);
            try {
                float discountCost = (((!AllSettingsManager.isNullOrEmpty(data.getDiscount_percent()) && (Float.parseFloat(data.getDiscount_percent()) > 0))) ? (Float.parseFloat(data.getDiscount_percent())) : 0.00f);
                float originSellingPrice = (((!AllSettingsManager.isNullOrEmpty(data.getSelling_price()) && (Float.parseFloat(data.getSelling_price()) > 0))) ? (Float.parseFloat(data.getSelling_price())) : 0.00f);
                float minusSubtotalToDiscountCost = AppUtil.getTotalPromotionalDiscountPrice(originSellingPrice, discountCost);
                Log.e("minusToDiscountCost", minusSubtotalToDiscountCost + "llMinus");
                String finalSubtotalToDiscountCost = AppUtil.formatDoubleString(minusSubtotalToDiscountCost);
                tvAmount.setText("" + finalSubtotalToDiscountCost);
                tvStrikeAmount.setText(getContext().getString(R.string.title_tk)+"  " + AppUtil.formatDoubleString(originSellingPrice));
                tvStrikeAmount.setPaintFlags(tvStrikeAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } catch (NumberFormatException nfe) {
                System.out.println("NumberFormatException: " + nfe.getMessage());
            }
        } else {
            tvStrikeAmount.setVisibility(View.GONE);
            tvAmount.setText(AppUtil.optStringNullCheckValue(data.getSelling_price()));
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