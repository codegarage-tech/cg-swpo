package com.meembusoft.safewaypharmaonline.activity;

import android.animation.Animator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.adapter.LatestProductAdapter;
import com.meembusoft.safewaypharmaonline.adapter.RelatedProductAdapter;
import com.meembusoft.safewaypharmaonline.base.BaseActivity;
import com.meembusoft.safewaypharmaonline.base.BaseLocationActivity;
import com.meembusoft.safewaypharmaonline.enumeration.FlavorType;
import com.meembusoft.safewaypharmaonline.enumeration.LoginSettingType;
import com.meembusoft.safewaypharmaonline.enumeration.OrderCheckoutType;
import com.meembusoft.safewaypharmaonline.fcm.UpdateTokenTask;
import com.meembusoft.safewaypharmaonline.fcm.fcmutils.OnTokenUpdateListener;
import com.meembusoft.safewaypharmaonline.fcm.fcmutils.TokenFetcher;
import com.meembusoft.safewaypharmaonline.model.AppSupplier;
import com.meembusoft.safewaypharmaonline.model.AppUser;
import com.meembusoft.safewaypharmaonline.model.CommonMedicinesByItem;
import com.meembusoft.safewaypharmaonline.model.ParamsFavourite;
import com.meembusoft.safewaypharmaonline.model.ProductDetails;
import com.meembusoft.safewaypharmaonline.model.RelatedMedicine;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.retrofit.APIClient;
import com.meembusoft.safewaypharmaonline.retrofit.APIInterface;
import com.meembusoft.safewaypharmaonline.retrofit.APIResponse;
import com.meembusoft.safewaypharmaonline.util.AllConstants;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.util.SessionUtil;
import com.meembusoft.safewaypharmaonline.view.CanaroTextView;
import com.meembusoft.safewaypharmaonline.view.CustomBulletSpan;
import com.meembusoft.safewaypharmaonline.view.CustomClickableSpan;
import com.meembusoft.safewaypharmaonline.view.CustomTagHandler;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;

import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.util.List;

import cn.ymex.popup.controller.AlertController;
import cn.ymex.popup.controller.DialogControllable;
import cn.ymex.popup.dialog.PopupDialog;
import retrofit2.Call;
import retrofit2.Response;

import static androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_FULL_SCREEN_IMAGE_URL;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_LOGIN_SETTING_TYPE;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_ORDER_CHECKOUT_TYPE;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_PRODUCT_ITEM;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_REQUEST_CODE_CART_UPDATE;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_REQUEST_CODE_USER_LOGIN;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ProductDetailsActivity extends BaseActivity {

    //Toolbar
    private ImageView ivBack,ivCart;
    private CanaroTextView tvTitle;
    private RelativeLayout rlCart;

    private TextView tvCart, tvProductName, tvProductDetailsAmt,tvProductStrikeAmt, tvProductFormName, tvProductGenericName, tvProductStrength,tvSpecifications,tvSpecificationsTitle,tvService,tvServiceTitle,
            tvHighlight,tvHighlightTitle,tvDescriptionOthers,tvDescriptionOthersTitle,tvDescription,tvDescriptionTitle,tvOthersReadMore,tvProductAddToCart, tvProductDetailsBuyNow;
    private ImageView ivProduct, ivFavourite, ivShare,ivAddToCartCopy,ivLeftLatest,ivRightLatest;
    private FrameLayout latestFramelayoutLeft,latestFramelayoutRight,relatedFramelayoutLeft,relatedFramelayoutRight;
    private CardView cvProductDetails;
    // Linear Layout
    private LinearLayout llLeftBack,llSourceView, linProductTypeZero,linRelatedProduct,linLatestProduct,linProductDetailsOthersForm,linProducPrescribingtDetails,linProductDetailsSpecificationsDialog,linBottomAddToCart;
    // Recycler view
    private RecyclerView rvRelatedProduct,rvLatestProduct;
    private NestedScrollView nsProductDetails;
    private WebView webViewService,webViewHighlight,webViewDetailsOthers;
    AppUser mAppUser = null;
    private LinearLayoutManager latestLinLayoutManager;
    //Adapter
    private RelatedProductAdapter relatedProductAdapter;
    private LatestProductAdapter latestProductAdapter;
    //Background task
    private GetMedicineDetailsTask getMedicineDetailsTask;
    private GetRelatedMedicineTask getRelatedMedicineTask;
    private GetLatestMedicineTask getLatestMedicineTask;
    private DoUserFavouriteAddTask doUserFavouriteAddTask;
    private GetRemoveFavouriteListTask getRemoveFavouriteListTask;
    private TokenFetcher tokenFetcher;
    private UpdateTokenTask updateTokenTask;

    private APIInterface mApiInterface;
    StaggeredMedicineByItem medicineByProduct;

    ProductDetails productDetails;
    int lFirst = 0, lLast = 0,rFirst = 0, rLast = 0, latestTotalCount = 0, relatedTotalCount = 0;
    View viewSpecifications,viewService,viewHighlight;
    private String customerOrSupplierId = "";
    private String selectUserType = AllConstants.USER_TYPE_CUSTOMER;

    //  Supplier
    private AppSupplier mAppSupplier;
    FlavorType mFlavorType;

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_product_details;
    }

    @Override
    public void initStatusBarView() {
        // StatusBarUtil.setTransparent(getActivity());
    }

    @Override
    public void initNavigationBarView() {

    }

    @Override
    public void initIntentData(Bundle savedInstanceState, Intent intent) {
        if (intent != null) {
//            String mParcelableCategoryType = intent.getStringExtra(AllConstants.INTENT_KEY_MEDICINE_BY_CATEGORY_TYPE);
//            if (!AllSettingsManager.isNullOrEmpty(mParcelableCategoryType)) {
//                categoryType = CategoryType.valueOf(mParcelableCategoryType);
//                Logger.d(TAG, TAG + " >>> " + "mCategoryType: " + categoryType);
//            }
            Parcelable mParcelable = intent.getParcelableExtra(AllConstants.INTENT_KEY_PRODUCT_ITEM);
            if (mParcelable != null) {
                medicineByProduct = Parcels.unwrap(mParcelable);
                Logger.d(TAG, TAG + " >>> " + "medicineByProduct: " + medicineByProduct.toString());
            }
        }
    }

    @Override
    public void initActivityViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivCart = (ImageView) findViewById(R.id.iv_cart);
        tvCart = (TextView) findViewById(R.id.tv_cart);
        tvTitle = (CanaroTextView) findViewById(R.id.text_title);
        tvTitle.setText(getActivity().getResources().getString(R.string.title_product_details));
        rlCart = (RelativeLayout) findViewById(R.id.rl_cart);
        cvProductDetails = (CardView) findViewById(R.id.cv_product_details);
        ivProduct = (ImageView) findViewById(R.id.iv_product);
        ivFavourite = (ImageView) findViewById(R.id.iv_favourite);
        ivShare = (ImageView) findViewById(R.id.iv_share);
        ivAddToCartCopy = (ImageView) findViewById(R.id.iv_add_to_cart_copy);
        ivLeftLatest = (ImageView) findViewById(R.id.product_details_latest_iv_left);
        ivRightLatest = (ImageView) findViewById(R.id.product_details_latest_iv_right);
        latestFramelayoutLeft = (FrameLayout) findViewById(R.id.product_details_latest_framelayout_left);
        latestFramelayoutRight = (FrameLayout) findViewById(R.id.product_details_latest_framelayout_right);
        relatedFramelayoutLeft = (FrameLayout) findViewById(R.id.product_details_related_framelayout_left);
        relatedFramelayoutRight = (FrameLayout) findViewById(R.id.product_details_related_framelayout_right);
        tvProductName = (TextView) findViewById(R.id.tv_product_details_product_name);
        tvProductDetailsAmt = (TextView) findViewById(R.id.tv_product_details_amt);
        tvProductStrikeAmt = (TextView) findViewById(R.id.tv_product_strike_amt);
        tvProductFormName = (TextView) findViewById(R.id.tv_product_details_product_form);
        tvProductGenericName = (TextView) findViewById(R.id.tv_product_details_generic_name);
        tvProductStrength = (TextView) findViewById(R.id.tv_product_details_strength);
        tvProductAddToCart = (TextView) findViewById(R.id.tv_product_add_to_cart);
        tvProductDetailsBuyNow = (TextView) findViewById(R.id.tv_product_buy_now);
        llLeftBack = (LinearLayout) findViewById(R.id.ll_left_back);
        llSourceView = (LinearLayout) findViewById(R.id.ll_source_view);
        linRelatedProduct = (LinearLayout) findViewById(R.id.lin_related_product);
        linLatestProduct = (LinearLayout) findViewById(R.id.lin_latest_product);
        linProductTypeZero = (LinearLayout) findViewById(R.id.lin_product_details_product_type_zero);

        linProductDetailsOthersForm = (LinearLayout) findViewById(R.id.lin_product_details_others_form);
        linProductDetailsSpecificationsDialog = (LinearLayout) findViewById(R.id.lin_product_details_specifications_dialog);
        linProducPrescribingtDetails = (LinearLayout) findViewById(R.id.lin_product_details_prescribe_description);
        linBottomAddToCart = (LinearLayout) findViewById(R.id.lin_bottom_add_to_cart);

        tvSpecificationsTitle = (TextView) findViewById(R.id.tv_product_details_specifications_title);
        tvSpecifications = (TextView) findViewById(R.id.tv_product_details_specifications);
        tvServiceTitle = (TextView) findViewById(R.id.tv_product_details_service_title);
        tvService = (TextView) findViewById(R.id.tv_product_details_service);
        tvHighlightTitle = (TextView) findViewById(R.id.tv_product_details_highlight_title);
        tvHighlight = (TextView) findViewById(R.id.tv_product_details_highlight);
        tvDescriptionOthersTitle = (TextView) findViewById(R.id.tv_product_details_others_form_title);
        tvDescriptionOthers = (TextView) findViewById(R.id.tv_product_details_others_form);
        tvDescriptionTitle = (TextView) findViewById(R.id.tv_product_details_description_title);
        tvDescription = (TextView) findViewById(R.id.tv_product_details_description);
        tvOthersReadMore = (TextView) findViewById(R.id.tv_product_details_others_read_more);
        viewSpecifications = (View) findViewById(R.id.view_specifications);
        viewService = (View) findViewById(R.id.view_service);
        viewHighlight = (View) findViewById(R.id.view_highlight);

        nsProductDetails = (NestedScrollView) findViewById(R.id.ns_product_details);
        rvRelatedProduct = (RecyclerView) findViewById(R.id.rv_related_product);
        rvLatestProduct = (RecyclerView) findViewById(R.id.rv_latest_product);
        rvRelatedProduct.setNestedScrollingEnabled(false);
        rvLatestProduct.setNestedScrollingEnabled(false);


    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        // Get User Tag
        initGetTagValue();
//        initSetData();
        //Network Check
        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
        } else {


            if (medicineByProduct != null) {

                getMedicineDetailsTask = new GetMedicineDetailsTask(getActivity(), medicineByProduct.getId());
                getMedicineDetailsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                getRelatedMedicineTask = new GetRelatedMedicineTask(getActivity(), "");
                getRelatedMedicineTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

            getLatestMedicineTask = new GetLatestMedicineTask(getActivity());
            getLatestMedicineTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }

//
//        switch (flavorType) {
//            case SUPPLIER:
//                linBottomAddToCart.setVisibility(View.GONE);
//                rlCart.setVisibility(View.GONE);
//                ivCart.setVisibility(View.GONE);
//                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) nsProductDetails
//                        .getLayoutParams();
//                layoutParams.setMargins(0, 0, 0, 0);
//                nsProductDetails.setLayoutParams(layoutParams);
//                break;
//        }

    }

    private void initGetTagValue() {
        String userType = SessionManager.getStringSetting(getActivity(), SessionUtil.SESSION_KEY_USER_TAG);
        if (!AllSettingsManager.isNullOrEmpty(userType)) {
            mFlavorType = FlavorType.getFlavor(SessionManager.getStringSetting(getActivity(), SessionUtil.SESSION_KEY_USER_TAG));
        } else {
            mFlavorType = FlavorType.CUSTOMER;
        }

        if (mFlavorType != null) {
            switch (mFlavorType) {
                case CUSTOMER:
                    mAppUser = SessionUtil.getUser(getActivity());
                    selectUserType = AllConstants.USER_TYPE_CUSTOMER;
                    if (mAppUser != null) {
                        customerOrSupplierId = mAppUser.getId();
                    }
                    break;
                case SUPPLIER:
                    mAppSupplier = SessionUtil.getSupplier(getActivity());
                    selectUserType = AllConstants.USER_TYPE_SUPPLIER;
                    if (mAppSupplier != null) {
                        customerOrSupplierId = mAppSupplier.getUser_id();
                    }
                    break;
            }
            Logger.d(TAG, TAG + " >>> " + "selectUserType " + selectUserType);
            Logger.d(TAG, TAG + " >>> " + "customerOrSupplierId " + customerOrSupplierId);
        }
    }

    private void initSetData(ProductDetails productDetails) {

        if (productDetails != null) {
            cvProductDetails.setVisibility(View.VISIBLE);

            tvProductName.setText(AppUtil.optStringNullCheckValue(productDetails.getName()));
            AppUtil.loadImage(getActivity(), ivProduct, productDetails.getProduct_image(), false, false, true);
            AppUtil.loadImage(getActivity(), ivAddToCartCopy, productDetails.getProduct_image(), false, false, true);
            String boxSize = AppUtil.optStringNullCheckValue(productDetails.getBox_size());

            float discountPercent = (((!AllSettingsManager.isNullOrEmpty(productDetails.getDiscount_percent()) && (Float.parseFloat(productDetails.getDiscount_percent()) > 0))) ? (Float.parseFloat(productDetails.getDiscount_percent())) :  0.00f);

            if (!AllSettingsManager.isNullOrEmpty(productDetails.getDiscount_percent())&& discountPercent>0) {
                tvProductStrikeAmt.setVisibility(View.VISIBLE);
                try {
                    float discountCost = (((!AllSettingsManager.isNullOrEmpty(productDetails.getDiscount_percent()) && (Float.parseFloat(productDetails.getDiscount_percent()) > 0))) ? (Float.parseFloat(productDetails.getDiscount_percent())) : 0.00f);
                    float originSellingPrice = (((!AllSettingsManager.isNullOrEmpty(productDetails.getSelling_price()) && (Float.parseFloat(productDetails.getSelling_price()) > 0))) ? (Float.parseFloat(productDetails.getSelling_price())) : 0.00f);
                    float minusSubtotalToDiscountCost = AppUtil.getTotalPromotionalDiscountPrice(originSellingPrice, discountCost);
                    Log.e("discountCost", discountCost + "discountCost");
                    Log.e("originSellingPrice", originSellingPrice + "originSellingPrice");
                    Log.e("<<minusToDiscountCost", minusSubtotalToDiscountCost + "<<llMinus");
                    String finalSubtotalToDiscountCost = AppUtil.formatDoubleString(minusSubtotalToDiscountCost);
                    tvProductDetailsAmt.setText(getResources().getString(R.string.title_tk)+" " + finalSubtotalToDiscountCost);
                    tvProductStrikeAmt.setText(getString(R.string.title_tk)+" " + AppUtil.formatDoubleString(originSellingPrice));
                    tvProductStrikeAmt.setPaintFlags(tvProductStrikeAmt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } catch (NumberFormatException nfe) {
                    System.out.println("NumberFormatException: " + nfe.getMessage());
                }
            } else {
                tvProductStrikeAmt.setVisibility(View.GONE);
                tvProductDetailsAmt.setText(getResources().getString(R.string.title_tk)+" "+AppUtil.optStringNullCheckValue(productDetails.getSelling_price()));
            }

            if (productDetails.getFavourite() .equalsIgnoreCase("1")){
                ivFavourite.setImageResource(R.drawable.vector_favourite_fill_primary);
            } else {
                ivFavourite.setImageResource(R.drawable.vector_favourite_empty_primary_color);
            }

            if (productDetails.getProduct_type().equalsIgnoreCase("1")){
                linProductDetailsOthersForm.setVisibility(View.VISIBLE);
                linProductTypeZero.setVisibility(View.GONE);
                linProducPrescribingtDetails.setVisibility(View.GONE);
                if(AppUtil.optStringNullCheckValue(productDetails.getSpecification()) != "") {
                    linProductDetailsSpecificationsDialog.setVisibility(View.VISIBLE);
                    tvSpecificationsTitle.setVisibility(View.VISIBLE);
                    tvSpecifications.setVisibility(View.VISIBLE);
                    viewSpecifications.setVisibility(View.VISIBLE);
                    tvSpecifications.setText(productDetails.getSpecification());
                } else {
                    linProductDetailsSpecificationsDialog.setVisibility(View.GONE);
                    viewSpecifications.setVisibility(View.GONE);
                    tvSpecificationsTitle.setVisibility(View.GONE);
                    tvSpecifications.setVisibility(View.GONE);
                }
               // tvService.setText(buildBulletText(getActivity(),"<bullet>", AppUtil.optStringNullCheckValue(productDetails.getService()));
                String bulletText = "100% Original Product<click><bold><bullet>Warranty not available<bullet>Color text<color>";
                if(AppUtil.optStringNullCheckValue(productDetails.getService()) != ""){
                    tvServiceTitle.setVisibility(View.VISIBLE);
                    tvService.setVisibility(View.VISIBLE);
                    viewService.setVisibility(View.VISIBLE);
                    CharSequence service = buildBulletText(getActivity(),"<bullet>", AppUtil.optStringNullCheckValue(productDetails.getService()));
                    tvService.setText(service+"");
                    tvService.setText(buildBulletText(getActivity(),"<bullet>", productDetails.getService()));
                } else {
                    tvServiceTitle.setVisibility(View.GONE);
                    tvService.setVisibility(View.GONE);
                    viewService.setVisibility(View.GONE);
                }

                if(AppUtil.optStringNullCheckValue(productDetails.getHighlights()) != "") {
                    tvHighlightTitle.setVisibility(View.VISIBLE);
                    tvHighlight.setVisibility(View.VISIBLE);
                    tvHighlight.setText(buildBulletText(getActivity(),"<bullet>", productDetails.getHighlights()));
                } else {
                    tvHighlightTitle.setVisibility(View.GONE);
                    tvHighlight.setVisibility(View.GONE);
                }

               // tvHighlight.setText(Html.fromHtml(AppUtil.optStringNullCheckValue(productDetails.getHighlights()), null, new CustomTagHandler()));


                if(AppUtil.optStringNullCheckValue(productDetails.getDescriptions()) != "") {
                    tvDescriptionOthersTitle.setVisibility(View.VISIBLE);
                    tvDescriptionOthers.setVisibility(View.VISIBLE);
                    viewHighlight.setVisibility(View.VISIBLE);
                    String sDescription = String.valueOf(Html.fromHtml(productDetails.getDescriptions()));
                    tvDescriptionOthers.setText( sDescription);
                } else {
                    tvDescriptionOthersTitle.setVisibility(View.GONE);
                    tvDescriptionOthers.setVisibility(View.GONE);
                    viewHighlight.setVisibility(View.GONE);
                }


            } else {
                linProductTypeZero.setVisibility(View.VISIBLE);
                linProducPrescribingtDetails.setVisibility(View.VISIBLE);
                tvProductFormName.setText(AppUtil.optStringNullCheckValue(productDetails.getForm_name()));
                tvProductGenericName.setText(AppUtil.optStringNullCheckValue(productDetails.getGeneric_name()));
                tvProductStrength.setText(AppUtil.optStringNullCheckValue(productDetails.getStrength()));
                linProductDetailsOthersForm.setVisibility(View.GONE);
                tvDescription.setText(Html.fromHtml(AppUtil.optStringNullCheckValue(productDetails.getDescriptions())));

            }

        } else {
            cvProductDetails.setVisibility(View.GONE);
        }
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {

        llLeftBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        llSourceView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (mFlavorType == FlavorType.SUPPLIER){
                    medicineByProduct.setUser_type(1);
                } else {
                    medicineByProduct.setUser_type(0);
                }
                medicineByProduct.setItem_quantity(1);
                onOrderNowClick(medicineByProduct,tvProductAddToCart);
            }
        });

        rlCart.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                    Intent intentMyOrder = new Intent(getActivity(), MyOrderActivity.class);
                    startActivityForResult(intentMyOrder, INTENT_REQUEST_CODE_CART_UPDATE);

            }
        });
        resetCounterView();

        ivShare.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                if (productDetails != null){
                    shareUrl(AppUtil.optStringNullCheckValue(productDetails.getSlug()),productDetails.getName());

                }
            }
        });

        ivFavourite.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                //Favourite AppUser
                if (mAppUser != null) {
                    if (medicineByProduct != null) {
                        if (medicineByProduct.getIs_favourite() == 0) {
                            ParamsFavourite paramsFavourite = new ParamsFavourite(medicineByProduct.getId(), mAppUser.getId());
                            doUserFavouriteAddTask = new DoUserFavouriteAddTask(getActivity(), paramsFavourite);
                            doUserFavouriteAddTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            ParamsFavourite paramsFavourite = new ParamsFavourite(medicineByProduct.getId(),  mAppUser.getId());
                            getRemoveFavouriteListTask = new GetRemoveFavouriteListTask(getActivity(), paramsFavourite);
                            getRemoveFavouriteListTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.txt_favourite_login_mgs), Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivProduct.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent iFullImageActivity = new Intent(getActivity(), FullScreenActivity.class);
                iFullImageActivity.putExtra(INTENT_KEY_FULL_SCREEN_IMAGE_URL,medicineByProduct.getProduct_image());
                startActivity(iFullImageActivity);
            }
        });

        tvProductDetailsBuyNow.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
//                if (mAppUser != null) {
//                    Intent intentCheckout = new Intent(getActivity(), CheckoutOrderActivity.class);
//                    intentCheckout.putExtra(INTENT_KEY_PRODUCT_ITEM, Parcels.wrap(medicineByProduct));
//                    intentCheckout.putExtra(INTENT_KEY_ORDER_CHECKOUT_TYPE, OrderCheckoutType.BUY_TO_CHECKOUT.name());
//                    startActivity(intentCheckout);
//                }else {
//                    Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
//                    intentLogin.putExtra(INTENT_KEY_LOGIN_SETTING_TYPE, LoginSettingType.DETAILS_TO_LOGIN.name());
//                    getActivity().startActivityForResult(intentLogin, INTENT_REQUEST_CODE_USER_LOGIN);
//                }
            }
        });


        tvOthersReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppUtil.optStringNullCheckValue(productDetails.getDescriptions()) !="") {
                    onReadMoreClick( getResources().getString(R.string.title_read_more),productDetails.getDescriptions());
                }
            }
        });

        linProductDetailsSpecificationsDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppUtil.optStringNullCheckValue(productDetails.getSpecification()) !="") {
                    onReadMoreClick( getResources().getString(R.string.title_specifications),productDetails.getSpecification());
                }
            }
        });

        latestFramelayoutLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager llm = (LinearLayoutManager) rvLatestProduct.getLayoutManager();
                llm.scrollToPositionWithOffset(lFirst - 1, latestTotalCount);
            }
        });

        latestFramelayoutRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager llm = (LinearLayoutManager) rvLatestProduct.getLayoutManager();
                llm.scrollToPositionWithOffset(lLast + 1, latestTotalCount);
            }
        });

        relatedFramelayoutLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager llm = (LinearLayoutManager) rvRelatedProduct.getLayoutManager();
                llm.scrollToPositionWithOffset(rFirst - 1, relatedTotalCount);
            }
        });

        relatedFramelayoutRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager llm = (LinearLayoutManager) rvRelatedProduct.getLayoutManager();
                llm.scrollToPositionWithOffset(rLast + 1, relatedTotalCount);
            }
        });


        rvLatestProduct.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llm = (LinearLayoutManager) rvLatestProduct.getLayoutManager();
                lLast = llm.findLastCompletelyVisibleItemPosition();
                lFirst = llm.findFirstCompletelyVisibleItemPosition();
            }
        });

        rvRelatedProduct.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llm = (LinearLayoutManager) rvRelatedProduct.getLayoutManager();
                rLast = llm.findLastCompletelyVisibleItemPosition();
                rFirst = llm.findFirstCompletelyVisibleItemPosition();
            }
        });
    }

    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AllConstants.INTENT_REQUEST_CODE_CART_UPDATE:
                if (resultCode == Activity.RESULT_OK) {
                    //Order successfully placed and refresh the selected data
                   // Toast.makeText(getActivity(), getString(R.string.toast_order_is_placed_successfully), Toast.LENGTH_SHORT).show();
                }

                //Update counter view
                resetCounterView();
                break;
            case INTENT_REQUEST_CODE_USER_LOGIN:
                Logger.d(TAG, TAG + " >>> " + "INTENT_REQUEST_CODE_LOGIN: " + requestCode);
                LoginSettingType loginSettingType;
                if (data != null && resultCode == RESULT_OK) {

                    if (mFlavorType == FlavorType.SUPPLIER) {
                        if (SessionUtil.getSupplier(getActivity()) != null) {
                            mAppSupplier = SessionUtil.getSupplier(getActivity());
                            if (customerOrSupplierId != null && !TextUtils.isEmpty(customerOrSupplierId)) {
                                initTokenForFCM();
                                if (medicineByProduct != null) {
                                    getMedicineDetailsTask = new GetMedicineDetailsTask(getActivity(), medicineByProduct.getId());
                                    getMedicineDetailsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                }
                            }
                        }
                    } else{
                        if (SessionUtil.getUser(getActivity()) != null) {
                            mAppUser = SessionUtil.getUser(getActivity());
                            if (customerOrSupplierId != null && !TextUtils.isEmpty(customerOrSupplierId)) {
                                initTokenForFCM();
                                if (medicineByProduct != null) {
                                    getMedicineDetailsTask = new GetMedicineDetailsTask(getActivity(), medicineByProduct.getId());
                                    getMedicineDetailsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                }
                            }
                        }
                    }
                }
        }
    }

    @Override
    public void initActivityBackPress() {
        finish();
    }

    @Override
    public void initActivityDestroyTasks() {
        dismissPopupDialog();

        if (getMedicineDetailsTask != null && getMedicineDetailsTask.getStatus() == AsyncTask.Status.RUNNING) {
            getMedicineDetailsTask.cancel(true);
        }

        if (getRelatedMedicineTask != null && getRelatedMedicineTask.getStatus() == AsyncTask.Status.RUNNING) {
            getRelatedMedicineTask.cancel(true);
        }

        if (getLatestMedicineTask != null && getLatestMedicineTask.getStatus() == AsyncTask.Status.RUNNING) {
            getLatestMedicineTask.cancel(true);
        }

        if (doUserFavouriteAddTask != null && doUserFavouriteAddTask.getStatus() == AsyncTask.Status.RUNNING) {
            doUserFavouriteAddTask.cancel(true);
        }

        if (getRemoveFavouriteListTask != null && getRemoveFavouriteListTask.getStatus() == AsyncTask.Status.RUNNING) {
            getRemoveFavouriteListTask.cancel(true);
        }
        if (updateTokenTask != null && updateTokenTask.getStatus() == AsyncTask.Status.RUNNING) {
            updateTokenTask.cancel(true);
        }
    }

    @Override
    public void initActivityPermissionResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    @Override
    public void onResume() {
        super.onResume();
        resetCounterView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    /***********************
     * Add to cart methods *
     ***********************/
    public void onOrderNowClick(final StaggeredMedicineByItem item, TextView llSourceView) {
        Logger.d(TAG, "onOrderNowClick: " + "count: " + item.getItem_quantity());
        Logger.d(TAG, "onOrderNowClick: " + "count: " + item.getItem_quantity());

        if (item.getItem_quantity() == 0) {
            if (AppUtil.isMedicineByItemStored(getActivity(), item,mFlavorType)) {
                //Delete the food from database
                AppUtil.deleteSelectedMedicineByItem(getActivity(), item);
            }
        } else if (item.getItem_quantity() == 1) {
            if (AppUtil.isMedicineByItemStored(getActivity(), item,mFlavorType)) {
                //Update data into database
                AppUtil.storeSelectedMedicineByItem(getActivity(), item);
                Toast.makeText(getActivity(), getResources().getString(R.string.toast_item_have_placed_cart), Toast.LENGTH_SHORT).show();

            } else {
                //Add item into database
                AppUtil.storeSelectedMedicineByItem(getActivity(), item);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                Bitmap bitmap = BitmapFactory.decodeFile(item.getProduct_image());
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                //make fly animation for adding item
                AppUtil.makeFlyAnimation(getActivity(), llSourceView, ivAddToCartCopy, ivCart, 1000, new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        resetCounterView();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
        } else {
            //Update data into database
            AppUtil.storeSelectedMedicineByItem(getActivity(), item);
        }

        //Reset counter view into toolbar
       // resetCounterView();
    }

    public void resetCounterView() {
        List<StaggeredMedicineByItem> data = AppUtil.getAllStoredMedicineByItems(getActivity(),mFlavorType);
        Logger.d(TAG, "data: " + "count: " + data.size());

        if (data.size() > 0) {
            tvCart.setText(data.size() + "");
            tvCart.setVisibility(View.VISIBLE);
        } else {
            tvCart.setVisibility(View.GONE);
        }
    }

    // Latest Product
    private void initLatestProductRv(List<StaggeredMedicineByItem> latestProductList) {
        if (latestProductList != null && latestProductList.size() >0) {
            linLatestProduct.setVisibility(View.VISIBLE);
            latestProductAdapter = new LatestProductAdapter(getActivity());
            rvLatestProduct.setLayoutManager(new LinearLayoutManager(getActivity(), HORIZONTAL, false));
            rvLatestProduct.setAdapter(latestProductAdapter);
            latestProductAdapter.addAll(latestProductList);
          } else {
            linLatestProduct.setVisibility(View.GONE);
        }
        }

    // Related Product
    private void initRelatedProductRv(List<RelatedMedicine> relatedMedicineList) {

        if (relatedMedicineList != null && relatedMedicineList.size() >0) {
            linRelatedProduct.setVisibility(View.VISIBLE);
            relatedProductAdapter = new RelatedProductAdapter(getActivity());
            latestLinLayoutManager = new LinearLayoutManager(getActivity(), HORIZONTAL, false);
            rvRelatedProduct.setLayoutManager(latestLinLayoutManager);
            rvRelatedProduct.setAdapter(relatedProductAdapter);
            relatedProductAdapter.addAll(relatedMedicineList);
        } else {
            linRelatedProduct.setVisibility(View.GONE);
        }
    }
    /************************
     * Server communication *
     ************************/
    private class GetMedicineDetailsTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        String productID;

        private GetMedicineDetailsTask(Context context, String  productId) {
            mContext = context;
            productID = productId;

        }

        @Override
        protected void onPreExecute() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showPopupDialog();
                    }
                }, 100);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Logger.d(TAG, TAG + ">> Background task is cancelled");
        }

        @Override
        protected Response doInBackground(String... params) {
            try {

                Call<APIResponse<ProductDetails>> call = mApiInterface.apiGetProductDetails(productID,customerOrSupplierId,selectUserType);
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
                dismissPopupDialog();

                if (result != null && result.isSuccessful()) {
                    Logger.d(TAG, "APIResponse(GetMedicineDetailsTask): onResponse-server = " + result.toString());
                    APIResponse<ProductDetails> data = (APIResponse<ProductDetails>) result.body();
                    Logger.d("GetMedicineDetailsTask", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(GetMedicineDetailsTask()): onResponse-object = " + data.toString());
                        productDetails = data.getData();
                        initSetData(data.getData());
                    } else {
                        Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();

                    // loadOfflineTimeData();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }


    private class DoUserFavouriteAddTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        ParamsFavourite mParamsFavourite;

        public DoUserFavouriteAddTask(Context context, ParamsFavourite paramsFavourite ) {
            mContext = context;
            mParamsFavourite = paramsFavourite ;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Logger.d(TAG, TAG + ">> Background task is cancelled");
        }

        @Override
        protected void onPreExecute() {
            showPopupDialog();
        }

        @Override
        protected Response doInBackground(String... params) {
            try {
                Logger.d(TAG, TAG + " >>> " + "mParamsFavourite: " + mParamsFavourite.toString());
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
                dismissPopupDialog();

                if (result != null ) {
                    Logger.d(TAG, "APIResponse(DoUserFavouriteAddTask): onResponse-server = " + result.toString());
                    APIResponse  data = (APIResponse) result.body();
                    Logger.d("UserFavouriteAdd>>", data.toString());

                    if (data != null) {
                        Logger.d(TAG, "APIResponse(DoUserFavouriteAddTask()): onResponse-object = " + data.toString());
                        ivFavourite.setImageResource(R.drawable.vector_favourite_fill_primary);
                        //Update food item data for favorite info
                        medicineByProduct.setIs_favourite(1);
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private class GetRemoveFavouriteListTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        ParamsFavourite mParamsFavourite;

        private GetRemoveFavouriteListTask(Context context,ParamsFavourite paramsFavourite) {
            mContext = context;
            mParamsFavourite = paramsFavourite ;
        }

        @Override
        protected void onPreExecute() {
            showPopupDialog();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Logger.d(TAG, TAG + ">> Background task is cancelled");
        }

        @Override
        protected Response doInBackground(String... params) {
            try {
                Call<APIResponse> call = mApiInterface.apiGetAllFavouriteRemoveList(mParamsFavourite.getCustomer_id(),mParamsFavourite.getProduct_id());
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
                dismissPopupDialog();

                if (result != null && result.isSuccessful()) {
                    Logger.d(TAG, "APIResponse(GetRemoveFavouriteListTask): onResponse-server = " + result.toString());
                    APIResponse data = (APIResponse) result.body();
                    Logger.d("GetRemoveFavouriteListTask", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(GetRemoveFavouriteListTask()): onResponse-object = " + data.toString());
                        ivFavourite.setImageResource(R.drawable.vector_favourite_empty_primary_color);
                        //Update item data for favorite info
                        medicineByProduct.setIs_favourite(0);
                    } else {
                        Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();

                    // loadOfflineTimeData();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }


    private class GetRelatedMedicineTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        String categoryID;

        private GetRelatedMedicineTask(Context context, String categoryId) {
            mContext = context;
            categoryID = categoryId;

        }

        @Override
        protected void onPreExecute() {
            showPopupDialog();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Logger.d(TAG, TAG + ">> Background task is cancelled");
        }

        @Override
        protected Response doInBackground(String... params) {
            try {
                Call<APIResponse<List<RelatedMedicine>>> call = mApiInterface.apiGetRelatedMedicineList(medicineByProduct.getId(),customerOrSupplierId,selectUserType);
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
                dismissPopupDialog();

                if (result != null && result.isSuccessful()) {
                    Logger.d(TAG, "APIResponse(GetRelatedMedicineTask): onResponse-server = " + result.toString());
                    APIResponse<List<RelatedMedicine>> data = (APIResponse<List<RelatedMedicine>>) result.body();
                    Logger.d("GetRelatedMedicineTask", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(GetRelatedMedicineTask()): onResponse-object = " + data.toString());
                        relatedTotalCount = data.getData().size();
                        if (relatedTotalCount >= 3){
                            relatedFramelayoutLeft.setVisibility(View.VISIBLE);
                            relatedFramelayoutRight.setVisibility(View.VISIBLE);
                        }
                        initRelatedProductRv(data.getData());

                    } else {
                        Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Logger.d("result", result.toString() + "");
                    //  Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private class GetLatestMedicineTask extends AsyncTask<String, Integer, Response> {

        Context mContext;

        private GetLatestMedicineTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            showPopupDialog();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Logger.d(TAG, TAG + ">> Background task is cancelled");
        }

        @Override
        protected Response doInBackground(String... params) {
            try {
                Call<APIResponse<CommonMedicinesByItem>> call = mApiInterface.apiGetLatestProductList(customerOrSupplierId,selectUserType);

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
                dismissPopupDialog();

                if (result != null && result.isSuccessful()) {
                    Logger.d(TAG, "APIResponse(GetLatestMedicineTask): onResponse-server = " + result.toString());
                    APIResponse<CommonMedicinesByItem> data = (APIResponse<CommonMedicinesByItem>) result.body();
                    Logger.d("GetLatestMedicineTask", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(GetLatestMedicineTask()): onResponse-object = " + data.toString());
                        latestTotalCount = data.getData().getTotals_count();
                        if (latestTotalCount >= 3){
                            latestFramelayoutLeft.setVisibility(View.VISIBLE);
                            latestFramelayoutRight.setVisibility(View.VISIBLE);
                        }
                        initLatestProductRv(data.getData().getMedicines());
                    } else {
                        Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Logger.d("result", result.toString() + "");

                    //  Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }


    /****************************************
     * Methods for firebase cloud Messaging *
     ****************************************/
    private void initTokenForFCM() {
        if (NetworkManager.isConnected(getActivity())) {
            if (tokenFetcher != null && tokenFetcher.getStatus() == AsyncTask.Status.RUNNING) {
                tokenFetcher.cancel(true);
            }
            tokenFetcher = new TokenFetcher(getActivity(), new OnTokenUpdateListener() {
                @Override
                public void onTokenUpdate(String update) {
                    if (!AllSettingsManager.isNullOrEmpty(update)) {
                        Logger.d(TAG, TAG + ">>updateToken " +update+ "update" );
                        //  Send server request for updating token
                        if (updateTokenTask != null && updateTokenTask.getStatus() == AsyncTask.Status.RUNNING) {
                            updateTokenTask.cancel(true);
                        }
                        if (customerOrSupplierId != null && !TextUtils.isEmpty(customerOrSupplierId)) {
                            updateTokenTask = new UpdateTokenTask(getActivity(),mFlavorType,customerOrSupplierId, update);
                            updateTokenTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                    }
                }
            });
            tokenFetcher.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }


    // Method to share either text or URL.
    private void shareUrl(String slugValue,String title) {
        try {
            Intent share = new Intent( android.content.Intent.ACTION_SEND );
            //type of things
            share.setType("text/plain");
            // Add data to the intent, the receiving app will decide
            share.putExtra( Intent.EXTRA_SUBJECT, title );
            share.putExtra( Intent.EXTRA_TEXT, slugValue );
            startActivity( Intent.createChooser( share, "Share SafeWayPharma!" ) );
    } catch (Exception ex) {
        Logger.d(TAG, "Exception: " + ex.getMessage());
    }

    }

    public void readMoreDialog(String title, String message) {
        PopupDialog.create(getActivity())
                .outsideTouchHide(false)
                .controller(AlertController.build()
                        .title(title + "\n")
                        .message(message)
                        .negativeButton(getString(R.string.dialog_cancel), null)
                        .positiveButton(getString(R.string.dialog_ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }))
                .show();
    }
    public void onReadMoreClick(String title,String description) {
        PopupDialog.create(getActivity())
                .animationIn(R.anim.push_in)
                .animationOut(R.anim.push_out)
                .controller(new CustomDialogController(title,description)).show();
    }

    class CustomDialogController implements DialogControllable {
        String mTitle = "", mDescription = "";

        public CustomDialogController(String title,String description) {
            this.mTitle = title;
            this.mDescription = description;
        }
        @Override
        public View createView(Context context, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.dialog_view_details_descriptions, parent, false);
        }

        @Override
        public PopupDialog.OnBindViewListener bindView() {


            return new PopupDialog.OnBindViewListener() {
                @Override
                public void onCreated(final PopupDialog dialog, final View layout) {

                    dialog.backPressedHide(true);
                    dialog.outsideTouchHide(false);

                    dialog.backgroundDrawable(new ColorDrawable(Color.parseColor("#66000000")));
                    TextView tvTitles = (TextView)layout.findViewById(R.id.tv_dialog_title);
                    TextView tvDescriptions = (TextView)layout.findViewById(R.id.tv_dialog_descriptions);
                    tvTitles.setText(mTitle);
                    String sDescription = String.valueOf(Html.fromHtml(AppUtil.optStringNullCheckValue(mDescription)));
                    tvDescriptions.setText( sDescription);

                    layout.findViewById(R.id.tv_dialog_cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                }
            };
        }
    }

    //Bullet Text method
    public static CharSequence buildBulletText(Context context, String bulletExpression, String totalText) {
        SpannableStringBuilder ssb = new SpannableStringBuilder("");
        int gapWidth = 30;
        int bulletRadius = 5;
        int bulletColor = ContextCompat.getColor(context, R.color.color_pink);
        if (!TextUtils.isEmpty(totalText) && !TextUtils.isEmpty(bulletExpression)) {
            String[] bulletTexts = totalText.split(bulletExpression);
            if (bulletTexts.length > 0) {
                for (int i = 0; i < bulletTexts.length; i++) {
                    SpannableString txtSpannable;
                    String updatedText = bulletTexts[i];
                    StyleSpan boldSpan ;
                    ClickableSpan clickSpan;
                    ForegroundColorSpan colorSpan;
                    // Set bold span
                    if(bulletTexts[i].contains("<bold>")){
                        updatedText = updatedText.replaceAll("<bold>","");
                        boldSpan = new StyleSpan(Typeface.BOLD);
                    } else {
                        boldSpan= new StyleSpan(Typeface.NORMAL);
                    }

                    // Set color span
                    if(bulletTexts[i].contains("<color>")){
                        updatedText = updatedText.replaceAll("<color>","");
                        colorSpan = new ForegroundColorSpan(bulletColor);
                    } else {
                        colorSpan = new ForegroundColorSpan(Color.BLACK);
                    }

                    // Set click span
                    if(updatedText.contains("<click>")){
                        updatedText = updatedText.replaceAll("<click>","");
                        clickSpan = new CustomClickableSpan(updatedText) {
                            @Override
                            public void onClick(View textView) {
                                // do some thing
                                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                            }
                        };
                    } else {
                        clickSpan = new CustomClickableSpan(updatedText) {
                            @Override
                            public void onClick(View textView) {
                                // Do nothing
                            }
                        };
                    }

                    // Set all span
                    txtSpannable =  new SpannableString(updatedText);
                    txtSpannable.setSpan(boldSpan, 0, updatedText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    txtSpannable.setSpan(colorSpan, 0, updatedText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    txtSpannable.setSpan(clickSpan, 0, updatedText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    // Add one bullet
                    ssb.append(txtSpannable, new CustomBulletSpan(gapWidth, bulletColor,bulletRadius), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ssb.append("\n");
                }
            }
        }
        return ssb;
    }

}