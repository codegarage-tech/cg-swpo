package com.meembusoft.safewaypharmaonline.viewholder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.MedicineRemainderActivity;
import com.meembusoft.safewaypharmaonline.activity.ProductDetailsActivity;
import com.meembusoft.safewaypharmaonline.enumeration.SearchType;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.util.AllSettingsManager;

import org.parceler.Parcels;

import java.util.Random;

import static android.app.Activity.RESULT_OK;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_PRODUCT_ITEM;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ShopStaggeredViewHolder extends BaseViewHolder<StaggeredMedicineByItem> {

    private static String TAG = ShopStaggeredViewHolder.class.getSimpleName();
    private TextView tvName, tvGenericName, tvAmount, tvQty, tvStrikeAmount;
    private ImageView ivProduct;
    private CardView cvShopStaggered;
    private int largeCardHeight, smallCardHeight;
    Activity mActivity;
    private SearchType mSearchType;

    public ShopStaggeredViewHolder(ViewGroup parent,  SearchType  searchType) {
        super(parent, R.layout.row_shop_staggered_view);
        mActivity = ((Activity) getContext());
        mSearchType = searchType;

        largeCardHeight = AppUtil.dip2px(getContext(), 320);
        smallCardHeight = AppUtil.dip2px(getContext(), 250);
        tvName = (TextView) $(R.id.tv_name);
        tvGenericName = (TextView) $(R.id.tv_generic_name);
        tvAmount = (TextView) $(R.id.tv_amount);
        tvStrikeAmount = (TextView) $(R.id.tv_strike_amount);
        //   tvQty = (TextView) $(R.id.tv_qty);
        ivProduct = (ImageView) $(R.id.iv_product);
        cvShopStaggered = (CardView) $(R.id.cv_shop_staggered);

        Random random = new Random();
        int result = random.nextInt(300 - 160) + 200;
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

        tvName.setText(AppUtil.optStringNullCheckValue(data.getName()) + " " + AppUtil.optStringNullCheckValue(data.getForm_name()));
        tvGenericName.setText(AppUtil.optStringNullCheckValue(data.getGeneric_name()));
        //  tvQty.setText(data.getBox_size()+"");
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
                tvStrikeAmount.setText(getContext().getString(R.string.title_tk) + "  " + AppUtil.formatDoubleString(originSellingPrice));
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

                Logger.d(TAG, "mActivity: " + mActivity);

                if (mSearchType == SearchType.REMINDER) {
                    Intent intentProductDetails = new Intent();
                    intentProductDetails.putExtra(INTENT_KEY_PRODUCT_ITEM, Parcels.wrap(data));
                    ((AppCompatActivity)getContext()).setResult(RESULT_OK, intentProductDetails);
                    ((AppCompatActivity)getContext()).finish();
                } else  {
                    Intent intentProductDetails = new Intent(getContext(), ProductDetailsActivity.class);
                    intentProductDetails.putExtra(INTENT_KEY_PRODUCT_ITEM, Parcels.wrap(data));
                    getContext().startActivity(intentProductDetails);
                }


            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intentMedicineRemainder = new Intent(getContext(), MedicineRemainderActivity.class);
                intentMedicineRemainder.putExtra(INTENT_KEY_PRODUCT_ITEM, Parcels.wrap(data));
                getContext().startActivity(intentMedicineRemainder);
                return true;
            }
        });
    }
}