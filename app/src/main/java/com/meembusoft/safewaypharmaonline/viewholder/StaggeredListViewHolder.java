package com.meembusoft.safewaypharmaonline.viewholder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.HomeActivity;
import com.meembusoft.safewaypharmaonline.activity.MedicineRemainderActivity;
import com.meembusoft.safewaypharmaonline.activity.ProductDetailsActivity;
import com.meembusoft.safewaypharmaonline.enumeration.CategoryType;
import com.meembusoft.safewaypharmaonline.enumeration.FlavorType;
import com.meembusoft.safewaypharmaonline.model.ParamsFavourite;
import com.meembusoft.safewaypharmaonline.model.StaggeredListItem;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.retrofit.APIClient;
import com.meembusoft.safewaypharmaonline.retrofit.APIInterface;
import com.meembusoft.safewaypharmaonline.retrofit.APIResponse;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.util.AllSettingsManager;

import org.parceler.Parcels;

import retrofit2.Call;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_PRODUCT_ITEM;


public class StaggeredListViewHolder extends BaseViewHolder<StaggeredListItem> {

    private TextView tv1FormName, tv1GenericName, tv1Amount, tv2FormName, tv2GenericName, tv2Amount, tv3DiscountPercent, tv3FormName, tv3GenericName, tv3Amount, tv3StrikeAmount, tv4DiscountPercent, tv4FormName, tv4GenericName, tv4Amount, tv4StrikeAmount, tv5FormName, tv5GenericName, tv5Amount,
            tv6FormName, tv6GenericName, tv6Amount, tv1Empty, tv2Empty, tv3Empty, tv4Empty, tv5Empty, tv6Empty;
    private LinearLayout ll_1, ll_2, ll_3, ll_4, ll_5, ll_6, lin_2_view;
    private ImageView iv1MedicineByCategory, iv2MedicineByCategory, iv3MedicineByCategory, iv4MedicineByCategory, iv5MedicineByCategory, iv6MedicineByCategory;
    private ImageView iv3Favourite, iv4Favourite;
    private DoRemoveFavouriteTask doRemoveFavouriteTask;
    private DoFavouriteAddTask doFavouriteAddTask;
    CategoryType mMedicineType;
    FlavorType mFlavorType;


    public StaggeredListViewHolder(ViewGroup parent, Context context, CategoryType medicineType,FlavorType flavorType) {
        super(parent, R.layout.row_staggered_item);
        mMedicineType = medicineType;
        mFlavorType = flavorType;
        Log.d(TAG, "mMedicineType:>>> " + mMedicineType.name());
        Log.d(TAG, ">>>mFlavorType: " + mFlavorType.toString());

        iv1MedicineByCategory = $(R.id.iv_1_medicine_by_category);
        iv2MedicineByCategory = $(R.id.iv_2_medicine_by_category);
        iv3MedicineByCategory = $(R.id.iv_3_medicine_by_category);
        iv4MedicineByCategory = $(R.id.iv_4_medicine_by_category);
        iv5MedicineByCategory = $(R.id.iv_5_medicine_by_category);
        iv6MedicineByCategory = $(R.id.iv_6_medicine_by_category);
        iv3Favourite = $(R.id.iv_3_favourite);
        iv4Favourite = $(R.id.iv_4_favourite);

        tv1Empty = $(R.id.tv_1_empty);
        tv1FormName = $(R.id.tv_1_form_name);
        tv1GenericName = $(R.id.tv_1_generic_name);
        tv1Amount = $(R.id.tv_1_amount);
        tv2Empty = $(R.id.tv_2_empty);
        tv2FormName = $(R.id.tv_2_form_name);
        tv2GenericName = $(R.id.tv_2_generic_name);
        tv2Amount = $(R.id.tv_2_amount);
        tv3Empty = $(R.id.tv_3_empty);
        tv3FormName = $(R.id.tv_3_form_name);
        tv3GenericName = $(R.id.tv_3_generic_name);
        tv3Amount = $(R.id.tv_3_amount);
        tv4Empty = $(R.id.tv_4_empty);
        tv4FormName = $(R.id.tv_4_form_name);
        tv4GenericName = $(R.id.tv_4_generic_name);
        tv4Amount = $(R.id.tv_4_amount);
        tv5Empty = $(R.id.tv_5_empty);
        tv5FormName = $(R.id.tv_5_form_name);
        tv5GenericName = $(R.id.tv_5_generic_name);
        tv5Amount = $(R.id.tv_5_amount);
        tv6Empty = $(R.id.tv_6_empty);
        tv6FormName = $(R.id.tv_6_form_name);
        tv6GenericName = $(R.id.tv_6_generic_name);
        tv6Amount = $(R.id.tv_6_amount);
        tv3DiscountPercent = $(R.id.tv_3_medicine_discount_percent);
        tv3StrikeAmount = $(R.id.tv_3_strike_amount);
        tv4StrikeAmount = $(R.id.tv_4_strike_amount);
        tv4DiscountPercent = $(R.id.tv_4_medicine_discount_percent);

        ll_1 = $(R.id.ll_1);
        ll_2 = $(R.id.ll_2);
        ll_3 = $(R.id.ll_3);
        ll_4 = $(R.id.ll_4);
        ll_5 = $(R.id.ll_5);
        ll_6 = $(R.id.ll_6);
        lin_2_view = $(R.id.lin_2_view);

        ll_1.setVisibility(View.GONE);
        ll_2.setVisibility(View.GONE);
        ll_3.setVisibility(View.GONE);
        ll_4.setVisibility(View.GONE);
        ll_5.setVisibility(View.GONE);
        ll_6.setVisibility(View.GONE);
        tv4Empty.setVisibility(View.GONE);
        lin_2_view.setVisibility(View.GONE);
    }

    @Override
    public void setData(final StaggeredListItem data) {
        if (data.getItems().size() > 0) {
            for (int i = 0; i < data.getItems().size(); i++) {
                if (data.getItems().size() > 3) {
                    lin_2_view.setVisibility(View.VISIBLE);
                }
                if (i == 0) {
                    if (mMedicineType.name().equalsIgnoreCase(CategoryType.GENERAL_MEDICINE.name()) || mMedicineType.name().equalsIgnoreCase(CategoryType.HERBAL_MEDICINE.name())) {
                        tv1FormName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getName()) + " " + AppUtil.optStringNullCheckValue(data.getItems().get(i).getForm_name()));
                        tv1GenericName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getGeneric_name()));
                    } else if (mMedicineType.name().equalsIgnoreCase(CategoryType.MEDICAL_INSTRUMENT.name())) {
                        tv1FormName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getName()));
                        tv1GenericName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getOrigin()));
                    } else if (mMedicineType.name().equalsIgnoreCase(CategoryType.BABY_FOOD_STATIONARY.name()) || mMedicineType.name().equalsIgnoreCase(CategoryType.COSMETICS.name())
                            || mMedicineType.name().equalsIgnoreCase(CategoryType.MEDICAL_SUPPORT.name()) || mMedicineType.name().equalsIgnoreCase(CategoryType.OPTICS_LENS.name())) {
                        tv1FormName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getName()));
                        tv1Empty.setVisibility(View.VISIBLE);
                        tv1GenericName.setVisibility(View.GONE);
                    } else {
                        tv1FormName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getName()));
                        tv1GenericName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getGeneric_name()));
                    }
                    tv1Amount.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getSelling_price()));
                    AppUtil.loadImage(getContext(), iv1MedicineByCategory, data.getItems().get(i).getProduct_image(), false, false, true);
                    ll_1.setVisibility(View.VISIBLE);
                    Logger.d(TAG, TAG + " >>> " + "getItems:0 " + data.getItems().toString());
                    final int finalI = i;
                    ll_1.setOnClickListener(new OnSingleClickListener() {
                        @Override
                        public void onSingleClick(View view) {
                            Intent intentProductDetails = new Intent(getContext(), ProductDetailsActivity.class);
                            intentProductDetails.putExtra(INTENT_KEY_PRODUCT_ITEM, Parcels.wrap(data.getItems().get(finalI)));
                            getContext().startActivity(intentProductDetails);
                        }
                    });
                    ll_1.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            Intent intentMedicineRemainder = new Intent(getContext(), MedicineRemainderActivity.class);
                            intentMedicineRemainder.putExtra(INTENT_KEY_PRODUCT_ITEM, Parcels.wrap(data.getItems().get(finalI)));
                            getContext().startActivity(intentMedicineRemainder);
                            return true;
                        }
                    });
                } else if (i == 1) {
                    if (mMedicineType.name().equalsIgnoreCase(CategoryType.GENERAL_MEDICINE.name()) || mMedicineType.name().equalsIgnoreCase(CategoryType.HERBAL_MEDICINE.name())) {
                        tv2FormName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getName()) + " " + AppUtil.optStringNullCheckValue(data.getItems().get(i).getForm_name()));
                        tv2GenericName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getGeneric_name()));
                    } else if (mMedicineType.name().equalsIgnoreCase(CategoryType.MEDICAL_INSTRUMENT.name())) {
                        tv2FormName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getName()));
                        tv2GenericName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getOrigin()));
                    } else if (mMedicineType.name().equalsIgnoreCase(CategoryType.BABY_FOOD_STATIONARY.name()) || mMedicineType.name().equalsIgnoreCase(CategoryType.COSMETICS.name())
                            || mMedicineType.name().equalsIgnoreCase(CategoryType.MEDICAL_SUPPORT.name()) || mMedicineType.name().equalsIgnoreCase(CategoryType.OPTICS_LENS.name())) {
                        tv2FormName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getName()));
                        tv2Empty.setVisibility(View.VISIBLE);
                        tv2GenericName.setVisibility(View.GONE);
                    } else {
                        tv2FormName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getName()));
                        tv2GenericName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getGeneric_name()));
                    }
                    tv2Amount.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getSelling_price()));
                    AppUtil.loadImage(getContext(), iv2MedicineByCategory, data.getItems().get(i).getProduct_image(), false, false, true);
                    ll_2.setVisibility(View.VISIBLE);
                    final int finalI = i;
                    ll_2.setOnClickListener(new OnSingleClickListener() {
                        @Override
                        public void onSingleClick(View view) {
                            Intent intentProductDetails = new Intent(getContext(), ProductDetailsActivity.class);
                            intentProductDetails.putExtra(INTENT_KEY_PRODUCT_ITEM, Parcels.wrap(data.getItems().get(finalI)));
                            getContext().startActivity(intentProductDetails);
                        }
                    });

                    ll_2.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            Intent intentMedicineRemainder = new Intent(getContext(), MedicineRemainderActivity.class);
                            intentMedicineRemainder.putExtra(INTENT_KEY_PRODUCT_ITEM, Parcels.wrap(data.getItems().get(finalI)));
                            getContext().startActivity(intentMedicineRemainder);
                            return true;
                        }
                    });
                } else if (i == 2) {
                    if (mMedicineType.name().equalsIgnoreCase(CategoryType.GENERAL_MEDICINE.name()) || mMedicineType.name().equalsIgnoreCase(CategoryType.HERBAL_MEDICINE.name())) {
                        tv3FormName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getName()) + " " + AppUtil.optStringNullCheckValue(data.getItems().get(i).getForm_name()));
                        tv3GenericName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getGeneric_name()));
                    } else if (mMedicineType.name().equalsIgnoreCase(CategoryType.MEDICAL_INSTRUMENT.name())) {
                        tv3FormName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getName()));
                        tv3GenericName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getOrigin()));
                    } else if (mMedicineType.name().equalsIgnoreCase(CategoryType.BABY_FOOD_STATIONARY.name()) || mMedicineType.name().equalsIgnoreCase(CategoryType.COSMETICS.name())
                            || mMedicineType.name().equalsIgnoreCase(CategoryType.MEDICAL_SUPPORT.name()) || mMedicineType.name().equalsIgnoreCase(CategoryType.OPTICS_LENS.name())) {
                        tv3FormName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getName()));
                        tv3Empty.setVisibility(View.VISIBLE);
                        tv3GenericName.setVisibility(View.GONE);
                    } else {
                        tv3FormName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getName()));
                        tv3GenericName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getGeneric_name()));
                    }

                    AppUtil.loadImage(getContext(), iv3MedicineByCategory, data.getItems().get(i).getProduct_image(), false, false, true);
                    if (data.getItems().get(i).getFavourite().equalsIgnoreCase("1")) {
                        iv3Favourite.setImageResource(R.drawable.vector_favourite_fill_primary);
                    } else {
                        iv3Favourite.setImageResource(R.drawable.vector_favourite_empty_primary_color);
                    }

                    float discountPercent = (((!AllSettingsManager.isNullOrEmpty(data.getItems().get(i).getDiscount_percent()) && (Float.parseFloat(data.getItems().get(i).getDiscount_percent()) > 0))) ? (Float.parseFloat(data.getItems().get(i).getDiscount_percent())) :  0.00f);

                    if (!AllSettingsManager.isNullOrEmpty(data.getItems().get(i).getDiscount_percent()) && discountPercent > 0 ) {
                        if (mFlavorType == FlavorType.CUSTOMER){
                            tv3DiscountPercent.setVisibility(View.VISIBLE);
                        }else {
                            tv3DiscountPercent.setVisibility(View.GONE);
                        }
                        tv3StrikeAmount.setVisibility(View.VISIBLE);
                        tv3DiscountPercent.setText("-" + data.getItems().get(i).getDiscount_percent() + "%");
                        try {
                            float discountCost = (((!AllSettingsManager.isNullOrEmpty(data.getItems().get(i).getDiscount_percent()) && (Float.parseFloat(data.getItems().get(i).getDiscount_percent()) > 0))) ? (Float.parseFloat(data.getItems().get(i).getDiscount_percent())) : 0.00f);
                            float originSellingPrice = (((!AllSettingsManager.isNullOrEmpty(data.getItems().get(i).getSelling_price()) && (Float.parseFloat(data.getItems().get(i).getSelling_price()) > 0))) ? (Float.parseFloat(data.getItems().get(i).getSelling_price())) : 0.00f);
                            float minusSubtotalToDiscountCost = AppUtil.getTotalPromotionalDiscountPrice(originSellingPrice, discountCost);
                            Log.e("minusToDiscountCost", minusSubtotalToDiscountCost + "llMinus");
                            String finalSubtotalToDiscountCost = AppUtil.formatDoubleString(minusSubtotalToDiscountCost);
                            tv3Amount.setText("" + finalSubtotalToDiscountCost);
                            tv3StrikeAmount.setText(getContext().getString(R.string.title_tk)+"  " + AppUtil.formatDoubleString(originSellingPrice));
                            tv3StrikeAmount.setPaintFlags(tv3StrikeAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        } catch (NumberFormatException nfe) {
                            System.out.println("NumberFormatException: " + nfe.getMessage());
                        }
                    } else {
                        tv3DiscountPercent.setVisibility(View.GONE);
                        tv3StrikeAmount.setVisibility(View.GONE);
                        tv3Amount.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getSelling_price()));
                    }
                    Logger.d(TAG, TAG + " >>> " + "getFavourite3 " + data.getItems().get(i).getFavourite().toString());

                    ll_3.setVisibility(View.VISIBLE);
                    final int finalI = i;
                    ll_3.setOnClickListener(new OnSingleClickListener() {
                        @Override
                        public void onSingleClick(View view) {
                            Intent intentProductDetails = new Intent(getContext(), ProductDetailsActivity.class);
                            intentProductDetails.putExtra(INTENT_KEY_PRODUCT_ITEM, Parcels.wrap(data.getItems().get(finalI)));
                            getContext().startActivity(intentProductDetails);
                        }
                    });
                    ll_3.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            Intent intentMedicineRemainder = new Intent(getContext(), MedicineRemainderActivity.class);
                            intentMedicineRemainder.putExtra(INTENT_KEY_PRODUCT_ITEM, Parcels.wrap(data.getItems().get(finalI)));
                            getContext().startActivity(intentMedicineRemainder);
                            return true;
                        }
                    });
                    int iFavourite = i;
//                    iv3Favourite.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            AppUser mAppUser = SessionUtil.getUser(getContext());
//                            if (mAppUser!=null) {
//                                if (doRemoveFavouriteTask != null && doRemoveFavouriteTask.getStatus() == AsyncTask.Status.RUNNING) {
//                                    doRemoveFavouriteTask.cancel(true);
//                                }
//                                if (doFavouriteAddTask != null && doFavouriteAddTask.getStatus() == AsyncTask.Status.RUNNING) {
//                                    doFavouriteAddTask.cancel(true);
//                                }
//
//                                //send favorite request to the server
//                                if (data.getItems().get(iFavourite).getFavourite().equalsIgnoreCase("1")) {
//
//                                    ParamsFavourite paramsFavourite = new ParamsFavourite(data.getItems().get(iFavourite).getId(), mAppUser.getId());
//                                    doRemoveFavouriteTask = new DoRemoveFavouriteTask(getContext(), paramsFavourite, data.getItems().get(iFavourite), iv3Favourite);
//                                    doRemoveFavouriteTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                                } else {
//                                    ParamsFavourite paramsFavourite = new ParamsFavourite(data.getItems().get(iFavourite).getId(), mAppUser.getId());
//                                    doFavouriteAddTask = new DoFavouriteAddTask(getContext(),  paramsFavourite, data.getItems().get(iFavourite), iv3Favourite);
//                                    doFavouriteAddTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//                                }
//                            } else {
//                                Intent intentLogin = new Intent(view.getContext(), LoginActivity.class);
//                                intentLogin.putExtra(INTENT_KEY_LOGIN_SETTING_TYPE, LoginSettingType.HOME_TO_LOGIN.name());
//                                ((HomeActivity) getContext()).startActivityForResult(intentLogin, INTENT_REQUEST_CODE_USER_LOGIN);
//                            }
//                        }
//                    });

                } else if (i == 3) {

                    if (mMedicineType.name().equalsIgnoreCase(CategoryType.GENERAL_MEDICINE.name()) || mMedicineType.name().equalsIgnoreCase(CategoryType.HERBAL_MEDICINE.name())) {
                        tv4FormName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getName()) + " " + AppUtil.optStringNullCheckValue(data.getItems().get(i).getForm_name()));
                        tv4GenericName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getGeneric_name()));
                    } else if (mMedicineType.name().equalsIgnoreCase(CategoryType.MEDICAL_INSTRUMENT.name())) {
                        tv4FormName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getName()));
                        tv4GenericName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getOrigin()));
                    } else if (mMedicineType.name().equalsIgnoreCase(CategoryType.BABY_FOOD_STATIONARY.name()) || mMedicineType.name().equalsIgnoreCase(CategoryType.COSMETICS.name())
                            || mMedicineType.name().equalsIgnoreCase(CategoryType.MEDICAL_SUPPORT.name()) || mMedicineType.name().equalsIgnoreCase(CategoryType.OPTICS_LENS.name())) {
                        tv4Empty.setVisibility(View.VISIBLE);
                        tv4FormName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getName()));
                        tv4GenericName.setVisibility(View.GONE);

                    } else {
                        tv4FormName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getName()));
                        tv4GenericName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getGeneric_name()));
                    }
                    AppUtil.loadImage(getContext(), iv4MedicineByCategory, data.getItems().get(i).getProduct_image(), false, false, true);

                    Logger.d(TAG, TAG + " >>> " + "getFavourite4" + data.getItems().get(i).getFavourite());

                    if (data.getItems().get(i).getFavourite().equalsIgnoreCase("1")) {
                        iv4Favourite.setImageResource(R.drawable.vector_favourite_fill_primary);
                    } else {
                        iv4Favourite.setImageResource(R.drawable.vector_favourite_empty_primary_color);
                    }
                    // Discount Calculation
                    float discountPercent = (((!AllSettingsManager.isNullOrEmpty(data.getItems().get(i).getDiscount_percent()) && (Float.parseFloat(data.getItems().get(i).getDiscount_percent()) > 0))) ? (Float.parseFloat(data.getItems().get(i).getDiscount_percent())) :  0.00f);
                    if (!AllSettingsManager.isNullOrEmpty(data.getItems().get(i).getDiscount_percent()) && discountPercent > 0) {
                        if (mFlavorType == FlavorType.CUSTOMER){
                            tv4DiscountPercent.setVisibility(View.VISIBLE);
                        }else {
                            tv4DiscountPercent.setVisibility(View.GONE);
                        }
                        tv4StrikeAmount.setVisibility(View.VISIBLE);

                        tv4DiscountPercent.setText("-" + data.getItems().get(i).getDiscount_percent() + "%");
                        try {
                            float discountCost = (((!AllSettingsManager.isNullOrEmpty(data.getItems().get(i).getDiscount_percent()) && (Float.parseFloat(data.getItems().get(i).getDiscount_percent()) > 0))) ? (Float.parseFloat(data.getItems().get(i).getDiscount_percent())) : 0.00f);
                            float originSellingPrice = (((!AllSettingsManager.isNullOrEmpty(data.getItems().get(i).getSelling_price()) && (Float.parseFloat(data.getItems().get(i).getSelling_price()) > 0))) ? (Float.parseFloat(data.getItems().get(i).getSelling_price())) : 0.00f);
                            float minusSubtotalToDiscountCost = AppUtil.getTotalPromotionalDiscountPrice(originSellingPrice, discountCost);
                            Log.e("minusToDiscountCost", minusSubtotalToDiscountCost + "llMinus");
                            String finalSubtotalToDiscountCost = AppUtil.formatDoubleString(minusSubtotalToDiscountCost);
                            tv4Amount.setText("" + finalSubtotalToDiscountCost);
                            tv4StrikeAmount.setText(getContext().getString(R.string.title_tk)+"  " + AppUtil.formatDoubleString(originSellingPrice));
                            tv4StrikeAmount.setPaintFlags(tv4StrikeAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        } catch (NumberFormatException nfe) {
                            System.out.println("NumberFormatException: " + nfe.getMessage());
                        }
                    } else {
                        tv4DiscountPercent.setVisibility(View.GONE);
                        tv4StrikeAmount.setVisibility(View.GONE);
                        tv4Amount.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getSelling_price()));
                    }

                    ll_4.setVisibility(View.VISIBLE);
                    final int finalI = i;
                    ll_4.setOnClickListener(new OnSingleClickListener() {
                        @Override
                        public void onSingleClick(View view) {
                            Intent intentProductDetails = new Intent(getContext(), ProductDetailsActivity.class);
                            intentProductDetails.putExtra(INTENT_KEY_PRODUCT_ITEM, Parcels.wrap(data.getItems().get(finalI)));
                            getContext().startActivity(intentProductDetails);
                        }
                    });

                    ll_4.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            Intent intentMedicineRemainder = new Intent(getContext(), MedicineRemainderActivity.class);
                            intentMedicineRemainder.putExtra(INTENT_KEY_PRODUCT_ITEM, Parcels.wrap(data.getItems().get(finalI)));
                            getContext().startActivity(intentMedicineRemainder);
                            return true;
                        }
                    });

//                    int iFavourite = i;
//                    iv4Favourite.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            AppUser mAppUser = SessionUtil.getUser(getContext());
//                            if (mAppUser!=null) {
//                                if (doRemoveFavouriteTask != null && doRemoveFavouriteTask.getStatus() == AsyncTask.Status.RUNNING) {
//                                    doRemoveFavouriteTask.cancel(true);
//                                }
//
//                                if (doFavouriteAddTask != null && doFavouriteAddTask.getStatus() == AsyncTask.Status.RUNNING) {
//                                    doFavouriteAddTask.cancel(true);
//                                }
//                                Logger.d(TAG, TAG + " >>> " + "getFavourite:0 " +  data.getItems().get(iFavourite).getFavourite().toString());
//                                Logger.d(TAG, TAG + " >>> " + "Favourite:getItems " +  data.getItems().get(iFavourite).toString());
//
//                                if (data.getItems().get(iFavourite).getFavourite().equalsIgnoreCase("1")) {
//
//                                    ParamsFavourite paramsFavourite = new ParamsFavourite(data.getItems().get(iFavourite).getId(), mAppUser.getId());
//                                    doRemoveFavouriteTask = new DoRemoveFavouriteTask(getContext(), paramsFavourite, data.getItems().get(iFavourite), iv4Favourite);
//                                    doRemoveFavouriteTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//
//                                } else {
//                                    ParamsFavourite paramsFavourite = new ParamsFavourite(data.getItems().get(iFavourite).getId(), mAppUser.getId());
//                                    doFavouriteAddTask = new DoFavouriteAddTask(getContext(),  paramsFavourite, data.getItems().get(iFavourite), iv4Favourite);
//                                    doFavouriteAddTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                                }
//                            } else {
//                                Intent intentLogin = new Intent(view.getContext(), LoginActivity.class);
//                                intentLogin.putExtra(INTENT_KEY_LOGIN_SETTING_TYPE, LoginSettingType.HOME_TO_LOGIN.name());
//                                ((HomeActivity) getContext()).startActivityForResult(intentLogin, INTENT_REQUEST_CODE_USER_LOGIN);
//                            }
//                        }
//                    });
                } else if (i == 4) {
                    if (mMedicineType.name().equalsIgnoreCase(CategoryType.GENERAL_MEDICINE.name()) || mMedicineType.name().equalsIgnoreCase(CategoryType.HERBAL_MEDICINE.name())) {
                        tv5FormName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getName()) + " " + AppUtil.optStringNullCheckValue(data.getItems().get(i).getForm_name()));
                        tv5GenericName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getGeneric_name()));
                    } else if (mMedicineType.name().equalsIgnoreCase(CategoryType.MEDICAL_INSTRUMENT.name())) {
                        tv5FormName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getName()));
                        tv5GenericName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getOrigin()));
                    } else if (mMedicineType.name().equalsIgnoreCase(CategoryType.BABY_FOOD_STATIONARY.name()) || mMedicineType.name().equalsIgnoreCase(CategoryType.COSMETICS.name())
                            || mMedicineType.name().equalsIgnoreCase(CategoryType.MEDICAL_SUPPORT.name()) || mMedicineType.name().equalsIgnoreCase(CategoryType.OPTICS_LENS.name())) {
                        tv5FormName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getName()));
                        tv5Empty.setVisibility(View.VISIBLE);
                        tv5GenericName.setVisibility(View.GONE);
                    } else {
                        tv5FormName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getName()));
                        tv5GenericName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getGeneric_name()));
                    }

                    tv5Amount.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getSelling_price()));
                    AppUtil.loadImage(getContext(), iv5MedicineByCategory, data.getItems().get(i).getProduct_image(), false, false, true);
                    ll_5.setVisibility(View.VISIBLE);
                    final int finalI = i;
                    ll_5.setOnClickListener(new OnSingleClickListener() {
                        @Override
                        public void onSingleClick(View view) {
                            Intent intentProductDetails = new Intent(getContext(), ProductDetailsActivity.class);
                            intentProductDetails.putExtra(INTENT_KEY_PRODUCT_ITEM, Parcels.wrap(data.getItems().get(finalI)));
                            getContext().startActivity(intentProductDetails);
                        }
                    });

                    ll_5.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            Intent intentMedicineRemainder = new Intent(getContext(), MedicineRemainderActivity.class);
                            intentMedicineRemainder.putExtra(INTENT_KEY_PRODUCT_ITEM, Parcels.wrap(data.getItems().get(finalI)));
                            getContext().startActivity(intentMedicineRemainder);
                            return true;
                        }
                    });
                } else if (i == 5) {
                    if (mMedicineType.name().equalsIgnoreCase(CategoryType.GENERAL_MEDICINE.name()) || mMedicineType.name().equalsIgnoreCase(CategoryType.HERBAL_MEDICINE.name())) {
                        tv6FormName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getName()) + " " + AppUtil.optStringNullCheckValue(data.getItems().get(i).getForm_name()));
                        tv6GenericName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getGeneric_name()));
                    } else if (mMedicineType.name().equalsIgnoreCase(CategoryType.MEDICAL_INSTRUMENT.name())) {
                        tv6FormName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getName()));
                        tv6GenericName.setText(data.getItems().get(i).getOrigin());
                    } else if (mMedicineType.name().equalsIgnoreCase(CategoryType.BABY_FOOD_STATIONARY.name()) || mMedicineType.name().equalsIgnoreCase(CategoryType.COSMETICS.name())
                            || mMedicineType.name().equalsIgnoreCase(CategoryType.MEDICAL_SUPPORT.name()) || mMedicineType.name().equalsIgnoreCase(CategoryType.OPTICS_LENS.name())) {
                        tv6FormName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getName()));
                        tv6Empty.setVisibility(View.VISIBLE);
                        tv6GenericName.setVisibility(View.GONE);
                    } else {
                        tv6FormName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getName()));
                        tv6GenericName.setText(AppUtil.optStringNullCheckValue(data.getItems().get(i).getGeneric_name()));
                    }
                    tv6Amount.setText(""+AppUtil.optStringNullCheckValue(data.getItems().get(i).getSelling_price()));
                    AppUtil.loadImage(getContext(), iv6MedicineByCategory, data.getItems().get(i).getProduct_image(), false, false, true);
                    ll_6.setVisibility(View.VISIBLE);
                    final int finalI = i;
                    ll_6.setOnClickListener(new OnSingleClickListener() {
                        @Override
                        public void onSingleClick(View view) {
                            Intent intentProductDetails = new Intent(getContext(), ProductDetailsActivity.class);
                            intentProductDetails.putExtra(INTENT_KEY_PRODUCT_ITEM, Parcels.wrap(data.getItems().get(finalI)));
                            getContext().startActivity(intentProductDetails);
                        }
                    });

                    ll_6.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            Intent intentMedicineRemainder = new Intent(getContext(), MedicineRemainderActivity.class);
                            intentMedicineRemainder.putExtra(INTENT_KEY_PRODUCT_ITEM, Parcels.wrap(data.getItems().get(finalI)));
                            getContext().startActivity(intentMedicineRemainder);
                            return true;
                        }
                    });
                }
            }
        }
    }


    /************************
     * Server communication *
     ************************/
    private class DoRemoveFavouriteTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        ParamsFavourite mParamsFavourite;
        StaggeredMedicineByItem mMedicineItem;
        ImageView mImageView;

        private DoRemoveFavouriteTask(Context context, ParamsFavourite paramsFavourite, StaggeredMedicineByItem medicineItem, ImageView imageView) {
            mContext = context;
            mParamsFavourite = paramsFavourite;
            mMedicineItem = medicineItem;
            mImageView = imageView;

        }

        @Override
        protected void onPreExecute() {
            ((HomeActivity) getContext()).showPopupDialog();

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Logger.d(TAG, TAG + ">> Background task is cancelled");
        }

        @Override
        protected Response doInBackground(String... params) {
            try {
                APIInterface mApiInterface = APIClient.getClient(getContext()).create(APIInterface.class);

                Call<APIResponse> call = mApiInterface.apiGetAllFavouriteRemoveList(mParamsFavourite.getCustomer_id(), mParamsFavourite.getProduct_id());
                Response response = call.execute();
                if (response.isSuccessful()) {
                    return response;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Response result) {
            try {
                ((HomeActivity) getContext()).dismissPopupDialog();

                if (result != null && result.isSuccessful()) {
                    Logger.d(TAG, "APIResponse(DoRemoveFavouriteTask): onResponse-server = " + result.toString());
                    APIResponse data = (APIResponse) result.body();
                    Logger.d("DoRemoveFavouriteTask", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(GetRemoveFavouriteListTask()): onResponse-object = " + data.toString());
                        //Remove from list
                        iv4Favourite.setImageResource(R.drawable.vector_favourite_empty_primary_color);

                        Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_your_remove_favourite_items), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getContext(), data.getMsg(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    // Do Add Favourite
    private class DoFavouriteAddTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        ParamsFavourite mParamsFavourite;
        StaggeredMedicineByItem mMedicineItem;
        ImageView mImageView;

        public DoFavouriteAddTask(Context context, ParamsFavourite paramsFavourite, StaggeredMedicineByItem medicineItem, ImageView imageView) {
            mContext = context;
            mParamsFavourite = paramsFavourite;
            mMedicineItem = medicineItem;
            mImageView = imageView;

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Logger.d(TAG, TAG + ">> Background task is cancelled");
        }

        @Override
        protected void onPreExecute() {
            ((HomeActivity) getContext()).showPopupDialog();
        }

        @Override
        protected Response doInBackground(String... params) {
            try {
                Logger.d(TAG, TAG + " >>> " + "mParamsFavourite: " + mParamsFavourite.toString());
                APIInterface mApiInterface = APIClient.getClient(getContext()).create(APIInterface.class);

                Call<APIResponse> call = mApiInterface.apiUserFavouriteAdd(mParamsFavourite);
                // Call<LoginResponse> call = mApiInterface.apiUserLogin2(mParamUserLogin);

                Response response = call.execute();
                Logger.d(TAG, TAG + " >>> " + "LoginResponse: " + response);
                if (response.isSuccessful()) {
                    return response;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Response result) {
            try {
                ((HomeActivity) getContext()).dismissPopupDialog();

                if (result != null) {
                    Logger.d(TAG, "APIResponse(DoFavouriteAddTask): onResponse-server = " + result.toString());
                    APIResponse data = (APIResponse) result.body();
                    Logger.d("DoFavouriteAddTask>>", data.toString());

                    if (data != null) {
                        Logger.d(TAG, "APIResponse(DoFavouriteAddTask()): onResponse-object = " + data.toString());
                        mImageView.setImageResource(R.drawable.vector_favourite_fill_primary);
                        //Update food item data for favorite info
                        mMedicineItem.setIs_favourite(1);
                    } else {
                        Toast.makeText(getContext(), data.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

}