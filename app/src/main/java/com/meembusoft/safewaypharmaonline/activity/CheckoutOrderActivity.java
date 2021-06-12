package com.meembusoft.safewaypharmaonline.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developers.imagezipper.ImageZipper;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.adapter.CheckoutOrderAdapter;
import com.meembusoft.safewaypharmaonline.adapter.ShippingChargeListAdapter;
import com.meembusoft.safewaypharmaonline.base.BaseLocationActivity;
import com.meembusoft.safewaypharmaonline.enumeration.FlavorType;
import com.meembusoft.safewaypharmaonline.enumeration.OrderCheckoutType;
import com.meembusoft.safewaypharmaonline.enumeration.ShippingType;
import com.meembusoft.safewaypharmaonline.geocoding.LocationAddressListener;
import com.meembusoft.safewaypharmaonline.geocoding.ReverseGeocoderTask;
import com.meembusoft.safewaypharmaonline.geocoding.UserLocationAddress;
import com.meembusoft.safewaypharmaonline.model.AppSupplier;
import com.meembusoft.safewaypharmaonline.model.AppUser;
import com.meembusoft.safewaypharmaonline.model.ParamsDoOrder;
import com.meembusoft.safewaypharmaonline.model.ParamsItems;
import com.meembusoft.safewaypharmaonline.model.ResponseOrderItem;
import com.meembusoft.safewaypharmaonline.model.ShippingAddress;
import com.meembusoft.safewaypharmaonline.model.ShippingCharge;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.retrofit.APIClient;
import com.meembusoft.safewaypharmaonline.retrofit.APIInterface;
import com.meembusoft.safewaypharmaonline.retrofit.APIResponse;
import com.meembusoft.safewaypharmaonline.util.AllConstants;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.util.SessionUtil;
import com.meembusoft.safewaypharmaonline.view.CanaroTextView;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.parceler.Parcels;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_ORDER_SHIPPING_TYPE;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_SHIPPING_ID;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_REQUEST_CODE_CAMERA;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_REQUEST_CODE_SHIPPING_ADDRESS;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class CheckoutOrderActivity extends BaseLocationActivity {


    //Toolbar
    private ImageView ivBack, ivCart, ivPicture, ivSelectedOk;
    private LinearLayout llLeftBack;
    private RelativeLayout rlCart;
    private CanaroTextView tvTitle;
    private TextView tvCart, tvTotalsAmount, tvSubtotalsAmount, tvDeliveryFeeAmount, tvAddAddress, tvAddNewAddressTitle;
    private Button btnOrderCheckout;
    private LinearLayout linLayAddNewAddress,linLayUploadPic;
    private EditText etAddNewAddress;
 //   private CardView cvPictureView;
    private RecyclerView rvMedicineCheckoutList, rvServiceChargeList;
    private CheckoutOrderAdapter checkoutListAdapter;
    private ShippingChargeListAdapter shippingChargeListAdapter;
    private List<StaggeredMedicineByItem> staggeredMedicineByItemList;
    private boolean isAdd;
    private String mImagePath = "", mBase64 = "";
    private String address = "", shippingChargeId = "",shippingOrderNote = "", shippingPrice = "", ShippingAddressId = "";
    ParamsDoOrder mParamsDoOrder = new ParamsDoOrder();
    private AppUser mAppUser;
    private float subTotalAmount = 0.0f;
    private float deliveryAmount = 0.0f;
    private float netTotalAmount = 0.0f;
    private int totalItem = 0;
    private double latitude = 0.0, longitude = 0.0;
    private OrderCheckoutType mOrderCheckoutType;
    private ShippingAddress mShippingAddress;

    //Background task
    private ReverseGeocoderTask currentLocationTask;

    private APIInterface mApiInterface;
    private DoUserParamsOrderTask doUserParamsOrderTask;
    private GetShippingChargeTask getShippingChargeTask;
    StaggeredMedicineByItem mOrderByProduct;
    //  Supplier
    private AppSupplier mAppSupplier;
    FlavorType mFlavorType;
    private String customerOrSupplierId = "";
    private String selectUserType = AllConstants.USER_TYPE_CUSTOMER;


    @Override
    public LOCATION_UPDATE_FREQUENCY initLocationUpdateFrequency() {
        return LOCATION_UPDATE_FREQUENCY.ONCE;
    }

    @Override
    public String[] initActivityPermissions() {
        return new String[]{Manifest.permission.CAMERA};
    }

    @Override
    public void onLocationFound(Location location) {
        if (location != null) {
            if ((currentLocationTask != null) && (currentLocationTask.getStatus() == AsyncTask.Status.RUNNING)) {
                currentLocationTask.cancel(true);
            }
            currentLocationTask = new ReverseGeocoderTask(getActivity(), new LocationAddressListener() {
                @Override
                public void getLocationAddress(UserLocationAddress userLocationAddress) {
                    if (userLocationAddress != null) {
                        Logger.d(TAG, "UserLocationAddress: " + userLocationAddress.toString());
                        latitude = userLocationAddress.getLatitude();
                        longitude = userLocationAddress.getLongitude();

                    }
                }
            });
            currentLocationTask.execute(location);
        }
    }


    @Override
    public int initActivityLayout() {
        return R.layout.activity_checkout_order_screen;
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
            String mParcelableOrderCheckoutType = intent.getStringExtra(AllConstants.INTENT_KEY_ORDER_CHECKOUT_TYPE);
            if (!AllSettingsManager.isNullOrEmpty(mParcelableOrderCheckoutType)) {
                mOrderCheckoutType = OrderCheckoutType.valueOf(mParcelableOrderCheckoutType);
                Logger.d(TAG, TAG + " >>> " + "mOrderCheckoutType: " + mParcelableOrderCheckoutType);
            }

            String mParcelableShippingAddressId = intent.getStringExtra(AllConstants.INTENT_KEY_SHIPPING_ID);
            if (!AllSettingsManager.isNullOrEmpty(mParcelableShippingAddressId)) {
                ShippingAddressId = mParcelableShippingAddressId;
                Logger.d(TAG, TAG + " >>> " + "ShippingAddressId: " + ShippingAddressId);
            }



            Parcelable mParcelable = intent.getParcelableExtra(AllConstants.INTENT_KEY_PRODUCT_ITEM);
            if (mParcelable != null) {
                mOrderByProduct = Parcels.unwrap(mParcelable);
                Logger.d(TAG, TAG + " >>> " + "mOrderByProduct: " + mOrderByProduct.toString());
            }
        }
    }

    @Override
    public void initActivityViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivCart = (ImageView) findViewById(R.id.iv_cart);
        tvCart = (TextView) findViewById(R.id.tv_cart);
        tvTitle = (CanaroTextView) findViewById(R.id.text_title);
        tvTitle.setText(getActivity().getResources().getString(R.string.title_checkout));
        rlCart = (RelativeLayout) findViewById(R.id.rl_cart);
        rlCart.setVisibility(View.GONE);
        llLeftBack = (LinearLayout) findViewById(R.id.ll_left_back);
        ivSelectedOk = (ImageView) findViewById(R.id.iv_selected_ok);
        ivPicture = (ImageView) findViewById(R.id.iv_picture);
        etAddNewAddress = (EditText) findViewById(R.id.et_add_new_address);
      //  cvPictureView = (CardView) findViewById(R.id.cv_picture_view);
        linLayAddNewAddress = (LinearLayout) findViewById(R.id.lin_lay_add_new_address);
        linLayUploadPic = (LinearLayout) findViewById(R.id.lin_lay_upload_pic);
        tvSubtotalsAmount = (TextView) findViewById(R.id.tv_subtotals_amt);
        tvDeliveryFeeAmount = (TextView) findViewById(R.id.tv_delivery_fee);
        tvTotalsAmount = (TextView) findViewById(R.id.tv_totals_amount);
        tvAddAddress = (TextView) findViewById(R.id.tv_add_address);
        tvAddNewAddressTitle = (TextView) findViewById(R.id.tv_add_new_address_title);
//
        rvMedicineCheckoutList = (RecyclerView) findViewById(R.id.rv_medicine_checkout_list);
        rvServiceChargeList = (RecyclerView) findViewById(R.id.rv_service_charge_list);
        btnOrderCheckout = (Button) findViewById(R.id.btn_order_complete);
      //  mAppUser = SessionUtil.getUser(getActivity());

    }


    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        checkoutListAdapter = new CheckoutOrderAdapter(getActivity());
        rvMedicineCheckoutList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMedicineCheckoutList.setAdapter(checkoutListAdapter);
        // Get User Tag
        initGetTagValue();
        setAddressData();
        setData();
        //Network Check
        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
            // loadOfflineTimeData();
        } else {
            getShippingChargeTask = new GetShippingChargeTask(getActivity());
            getShippingChargeTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
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
    // Get Shipping Charge
    public void shippingCharge(ShippingCharge shippingCharge) {
        if (shippingCharge != null) {
            shippingChargeId = shippingCharge.getId();
            shippingPrice = shippingCharge.getAmount();
            Logger.d("shippingChargeId", shippingChargeId + "");
            Logger.d("shippingPrice", shippingPrice + "");
            checkoutTotalAmount();
        }
    }

    private void shippingChargeSet(List<ShippingCharge> shippingChargeList) {
        if (shippingChargeList.size() > 0 && shippingChargeList != null) {
            rvServiceChargeList.setVisibility(View.VISIBLE);
            shippingChargeListAdapter = new ShippingChargeListAdapter(getActivity());
            rvServiceChargeList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            rvServiceChargeList.setAdapter(shippingChargeListAdapter);
            if (shippingChargeListAdapter != null) {
                shippingChargeListAdapter.setAdapterData(shippingChargeList);
            }
        } else {
            rvServiceChargeList.setVisibility(View.GONE);
        }
    }


    private void setAddressData() {
        mShippingAddress = SessionUtil.getUserShippingAddress(getActivity());

        if (mShippingAddress != null && (!AllSettingsManager.isNullOrEmpty(mShippingAddress.getId()))) {
            Logger.d(TAG, "mShippingAddress: " + mShippingAddress.toString());
            List<String> strAddress = new ArrayList<String>();
            if (!AllSettingsManager.isNullOrEmpty(mShippingAddress.getAddress())){
                strAddress.add(AppUtil.optStringNullCheckValue(mShippingAddress.getAddress()));
            }

            if (!AllSettingsManager.isNullOrEmpty(mShippingAddress.getState())){
                strAddress.add(AppUtil.optStringNullCheckValue(mShippingAddress.getState()));
            }

            if (!AllSettingsManager.isNullOrEmpty(mShippingAddress.getCity())){
                strAddress.add(AppUtil.optStringNullCheckValue(mShippingAddress.getCity()));
            }

            if (strAddress.size()>0){
                String address = TextUtils.join(",", strAddress);
                tvAddAddress.setText(address);
            } else {
                tvAddAddress.setText("");
            }
        } else {
            tvAddAddress.setText("");
        }
    }

    private void setData() {
        if (mOrderCheckoutType != null) {
            switch (mOrderCheckoutType) {
                case CART_TO_CHECKOUT:
                    staggeredMedicineByItemList = AppUtil.getIsCheckoutItemList(getActivity());

                    break;
                case BUY_TO_CHECKOUT:
                    staggeredMedicineByItemList = new ArrayList<>();
                    staggeredMedicineByItemList.add(mOrderByProduct);
                    break;
            }

            if (staggeredMedicineByItemList != null && staggeredMedicineByItemList.size() > 0) {
                checkoutListAdapter.addAll(staggeredMedicineByItemList);
            }
        }
    }


    public void checkoutTotalAmount() {
        try {
            //  staggeredMedicineByItemList = AppUtil.getIsCheckoutItemList(getActivity());

            if (staggeredMedicineByItemList != null && staggeredMedicineByItemList.size() > 0) {
                totalItem = staggeredMedicineByItemList.size();
                deliveryAmount = (((!AllSettingsManager.isNullOrEmpty(shippingPrice) && (Float.parseFloat(shippingPrice) > 0))) ? (Float.parseFloat(shippingPrice)) : 0.0f);
                subTotalAmount = AppUtil.formatFloat(AppUtil.getCheckoutFromSubtotalPrice(staggeredMedicineByItemList));
                Logger.d(TAG, TAG + " >>> " + "totalAmount: " + subTotalAmount);
                Logger.d(TAG, TAG + " >>> " + "deliveryAmount: " + deliveryAmount);
                netTotalAmount = AppUtil.getTotalPrice(subTotalAmount, 0.0f, deliveryAmount);
                tvSubtotalsAmount.setText(subTotalAmount + " " + getResources().getString(R.string.title_tk));
                tvDeliveryFeeAmount.setText(deliveryAmount + " " + getResources().getString(R.string.title_tk));
                tvTotalsAmount.setText(netTotalAmount + " /-");
            } else {
                tvSubtotalsAmount.setText("0.0 " + getResources().getString(R.string.title_tk));
                tvTotalsAmount.setText("0.0" + " /-");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {

        llLeftBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

//        ivBack.setOnClickListener(new OnSingleClickListener() {
//            @Override
//            public void onSingleClick(View view) {
//                Intent intent = new Intent();
//                setResult(RESULT_OK, intent);
//                finish();
//            }
//        });

        linLayAddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iAddShippingAddress = new Intent(getActivity(), AddEditShippingAddressActivity.class);
                iAddShippingAddress.putExtra(INTENT_KEY_ORDER_SHIPPING_TYPE, ShippingType.ADD_TO_SHIPPING.name());
                startActivityForResult(iAddShippingAddress, INTENT_REQUEST_CODE_SHIPPING_ADDRESS);
                // if (!isAdd){
//
//                    tvAddNewAddressTitle.setVisibility(View.GONE);
//                    ivSelectedOk.setVisibility(View.VISIBLE);
//                    etAddNewAddress.setVisibility(View.VISIBLE);
//                    etAddNewAddress.requestFocus();
//                    isAdd = true;
//                    linLayAddNewAddress.setBackgroundResource(R.drawable.edittext_border_grey_background);
//                }
            }
        });

        btnOrderCheckout.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                if (!NetworkManager.isConnected(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                } else {
                    try {
                    if (customerOrSupplierId != null && customerOrSupplierId != "") {
                        if (staggeredMedicineByItemList != null && staggeredMedicineByItemList.size() > 0) {

                            mParamsDoOrder.setCustomer_id(customerOrSupplierId);
                            mParamsDoOrder.setUser_type(selectUserType);
                            mParamsDoOrder.setTotal_price(netTotalAmount + "");
                            mParamsDoOrder.setNet_total(netTotalAmount + "");
                            mParamsDoOrder.setDiscount("0");
                            mParamsDoOrder.setOrder_note(shippingOrderNote);
                            mParamsDoOrder.setShiping_charge(deliveryAmount + "");
                            mParamsDoOrder.setShiping_charge_id(shippingChargeId);
                            mParamsDoOrder.setTotal_item(totalItem + "");
                            mParamsDoOrder.setLatitude(latitude + "");
                            mParamsDoOrder.setLongitude(longitude + "");
                            mParamsDoOrder.setPayment_type("Cash");
                            mParamsDoOrder.setImage(mBase64);
                            ShippingAddress mShippingAddress = new ShippingAddress(ShippingAddressId, "");
                            mParamsDoOrder.setShiping_address(mShippingAddress);

                            List<ParamsItems> items = new ArrayList<>();
                            for (StaggeredMedicineByItem item : staggeredMedicineByItemList) {
                                ParamsItems paramDoItem = new ParamsItems();
                                float itemSubTotalsPrice = AppUtil.getItemTotalPrice(item);
                                paramDoItem.setProduct_ids(item.getId());
                                float discountPercent = (((!AllSettingsManager.isNullOrEmpty(item.getDiscount_percent()) && (Float.parseFloat(item.getDiscount_percent()) > 0))) ? (Float.parseFloat(item.getDiscount_percent())) :  0.00f);

                                if (!AllSettingsManager.isNullOrEmpty(item.getDiscount_percent()) && discountPercent >0) {
                                    paramDoItem.setPrices(item.getDiscount_percent_price());
                                } else {
                                    paramDoItem.setPrices(item.getSelling_price());
                                }

                                paramDoItem.setQuantitys(item.getItem_quantity() + "");
                                paramDoItem.setSub_total_prices(itemSubTotalsPrice + "");
                                items.add(paramDoItem);
                                Logger.d(TAG, TAG + "<<< >>> " + "item>>>: " + item.toString());
                                Logger.d(TAG, TAG + "<<< >>> " + "paramDoItem: " + paramDoItem.toString());
                            }
                            Logger.d(TAG, TAG + " >>> " + "items: " + items.toString());

                            mParamsDoOrder.setItems(items);
                            Logger.d(TAG, TAG + " >>> " + "DomParamsDoOrder: " + mParamsDoOrder.toString());


                            if (IsValidationShippingInfo()) {
                                doUserParamsOrderTask = new DoUserParamsOrderTask(getActivity(), mParamsDoOrder);
                                doUserParamsOrderTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            }
                        }


                    }
                    } catch (NumberFormatException nfe) {
                        System.out.println("NumberFormatException: " + nfe.getMessage());
                    }
                }

                //  Toast.makeText(getActivity(), getString(R.string.toast_order_api_under_development), Toast.LENGTH_SHORT).show();
            }
        });

//        etAddNewAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(!hasFocus){
//                    Log.e("hasFocus",hasFocus+"");
//                    ivSelectedOk.setVisibility(View.GONE);
//                    Toast.makeText(getApplicationContext(), "on focus", Toast.LENGTH_LONG).show();
//                   // etAddNewAddress.setFocusable(false);
//                    etAddNewAddress.setFocusable(true);
//                    etAddNewAddress.setFocusableInTouchMode(true);
//                    etAddNewAddress.setClickable(true);
//                    etAddNewAddress.setEnabled(true);
//
//                }else {
//                    linLayAddNewAddress.setBackgroundResource(R.drawable.button_grey_background);
////                    if(etAddNewAddress.requestFocus()) {
////                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
////                    }
//
////                    etAddNewAddress.requestFocus();
//                    etAddNewAddress.setText("");
//                  //  etAddNewAddress.setFocusable(false);
//                    ivSelectedOk.setVisibility(View.VISIBLE);
//                    Toast.makeText(getApplicationContext(), "lost focus", Toast.LENGTH_LONG).show();
//
//                }
//            } });
//
//        etAddNewAddress.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                v.setFocusable(true);
//                v.setFocusableInTouchMode(true);
//                return false;
//            }
//        });
        ivSelectedOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                linLayAddNewAddress.setBackgroundResource(R.drawable.shape_dashed_line);
//                if (etAddNewAddress.getText().toString().length()>0) {
//                    tvAddAddress.setText(etAddNewAddress.getText().toString());
//                }
//                etAddNewAddress.setText("");
//                etAddNewAddress.setVisibility(View.GONE);
//                ivSelectedOk.setVisibility(View.GONE);
//                tvAddNewAddressTitle.setVisibility(View.VISIBLE);
//                isAdd = false;
            }
        });

        linLayUploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPicture();
            }
        });

    }

    private void showPicture() {
        Matisse.from(getActivity())
                .choose(MimeType.ofImage())
                .theme(R.style.Matisse_Dracula)
                .capture(true)

//             .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
//                .captureStrategy(
//                        new CaptureStrategy(true, "com.zhihu.matisse.sample.fileprovider", "test"))
                .setDefaultCaptureStrategy()
                .countable(false)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .maxSelectable(1)
                .imageEngine(new GlideEngine())
                .forResult(AllConstants.INTENT_REQUEST_CODE_IMAGE_PICKER);


    }

    private boolean IsValidationShippingInfo() {
        if (AllSettingsManager.isNullOrEmpty(shippingChargeId)) {
            Toast.makeText(getActivity(), getString(R.string.toast_please_select_shipping_area), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showPictureDialog() {
        View.OnClickListener onClickListenerCamera = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhotoFromCamera();
            }
        };

        View.OnClickListener onClickListenerGallery = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
        AppUtil.showPictureDialog(this, onClickListenerCamera, onClickListenerGallery);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, INTENT_REQUEST_CODE_CAMERA);
    }

    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AllConstants.INTENT_REQUEST_CODE_IMAGE_PICKER && resultCode == RESULT_OK) {
            List<String> mData = Matisse.obtainPathResult(data);

            if (mData.size() == 1) {
                mImagePath = mData.get(0);
                Logger.d(TAG, "MatisseImage: " + mImagePath);

                AppUtil.loadImage(getActivity(), ivPicture, mImagePath, false, false, false);

                try {
                    File imageZipperFile = new ImageZipper(getActivity())
                            .setQuality(100)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .compressToFile(new File(mImagePath));
                    mBase64 = ImageZipper.getBase64forImage(imageZipperFile);
                    Logger.d(TAG, "MatisseImage(mBase64): " + mBase64);
                } catch (Exception ex) {
                    Logger.d(TAG, "ex>>>>>: " + ex.toString());
                    ex.printStackTrace();
                }
            }
        } else if (requestCode == AllConstants.INTENT_REQUEST_CODE_SHIPPING_ADDRESS && resultCode == RESULT_OK) {
            Logger.d(TAG, TAG + " >>> " + "INTENT_REQUEST_CODE_SHIPPING_ADDRESS: " + requestCode);
            if (data != null) {
                setAddressData();
                checkoutTotalAmount();
            }
        }
    }


    @Override
    public void initActivityBackPress() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    @Override
    public void initActivityDestroyTasks() {
        dismissPopupDialog();

        if (doUserParamsOrderTask != null && doUserParamsOrderTask.getStatus() == AsyncTask.Status.RUNNING) {
            doUserParamsOrderTask.cancel(true);
        }

        if (getShippingChargeTask != null && getShippingChargeTask.getStatus() == AsyncTask.Status.RUNNING) {
            getShippingChargeTask.cancel(true);
        }


    }

    @Override
    public void initActivityPermissionResult(int requestCode, String[] permissions, int[] grantResults) {

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /************************
     * Server communication *
     ************************/

    private class DoUserParamsOrderTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        ParamsDoOrder mParamsDoOrder;

        public DoUserParamsOrderTask(Context context, ParamsDoOrder paramsDoOrder) {
            mContext = context;
            mParamsDoOrder = paramsDoOrder;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Logger.d(TAG, TAG + ">> Background task is cancelled");
        }

        @Override
        protected void onPreExecute() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showPopupDialog();
                }
            }, 50);
        }

        @Override
        protected Response doInBackground(String... params) {
            try {
                Logger.d(TAG, TAG + " >>> " + "mParamsDoOrder: " + mParamsDoOrder.toString());
                Call<APIResponse<ResponseOrderItem>> call = mApiInterface.apiUserOrderPlace(mParamsDoOrder);

                Response response = call.execute();
                Logger.d(TAG, TAG + " >>> " + "response: " + response);
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

                if (result != null) {
                    Logger.d(TAG, "APIResponse(DoUserParamsOrderTask): onResponse-server = " + result.toString());
                    APIResponse<ResponseOrderItem> data = (APIResponse<ResponseOrderItem>) result.body();
                    Logger.d("AppUserParamsOrderTask", data.toString());
                    if (data != null) {
                        Logger.d(TAG, "APIResponse(DoUserParamsOrderTask()): onResponse-object = " + data.toString());
                        Toast.makeText(getActivity(), getString(R.string.toast_order_is_placed_successfully), Toast.LENGTH_SHORT).show();
                        AppUtil.deleteAllStoredMedicineByItems(getActivity(),mFlavorType);
                        Intent iMap = new Intent(getActivity(), MapActivity.class);
                        Logger.d("getOrder_id", data.getData().getOrder_id());
                        iMap.putExtra(AllConstants.INTENT_KEY_ORDER_ID, data.getData().getOrder_id());
                        startActivity(iMap);
                        finish();

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

    private class GetShippingChargeTask extends AsyncTask<String, Integer, Response> {

        Context mContext;

        private GetShippingChargeTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showPopupDialog();
                }
            }, 50);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Logger.d(TAG, TAG + ">> Background task is cancelled");
        }

        @Override
        protected Response doInBackground(String... params) {
            try {
                Call<APIResponse<List<ShippingCharge>>> call = mApiInterface.apiGetShippingChargeList();
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
                    Logger.d(TAG, "APIResponse(GetShippingChargeTask): onResponse-server = " + result.toString());
                    APIResponse<List<ShippingCharge>> data = (APIResponse<List<ShippingCharge>>) result.body();
                    Logger.d("GetShippingChargeTask", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(GetShippingChargeTask()): onResponse-object = " + data.toString());
                        shippingChargeId = data.getData().get(0).getId();
                        shippingOrderNote = data.getData().get(0).getNote();
                        shippingPrice = data.getData().get(0).getAmount();
                        Logger.d("shippingChargeId", shippingChargeId + "");
                        checkoutTotalAmount();
                        shippingChargeSet(data.getData());
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


}