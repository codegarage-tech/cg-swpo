package com.meembusoft.safewaypharmaonline.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.AddEditShippingAddressActivity;
import com.meembusoft.safewaypharmaonline.activity.CheckoutOrderActivity;
import com.meembusoft.safewaypharmaonline.activity.LoginActivity;
import com.meembusoft.safewaypharmaonline.activity.ShippingAddressActivity;
import com.meembusoft.safewaypharmaonline.adapter.EssentialProductListAdapter;
import com.meembusoft.safewaypharmaonline.adapter.MyOrderListAdapter;
import com.meembusoft.safewaypharmaonline.base.BaseFragment;
import com.meembusoft.safewaypharmaonline.enumeration.FlavorType;
import com.meembusoft.safewaypharmaonline.enumeration.LoginSettingType;
import com.meembusoft.safewaypharmaonline.enumeration.OrderCheckoutType;
import com.meembusoft.safewaypharmaonline.enumeration.ShippingType;
import com.meembusoft.safewaypharmaonline.model.AppSupplier;
import com.meembusoft.safewaypharmaonline.model.AppUser;
import com.meembusoft.safewaypharmaonline.model.ShippingAddress;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.retrofit.APIClient;
import com.meembusoft.safewaypharmaonline.retrofit.APIInterface;
import com.meembusoft.safewaypharmaonline.retrofit.APIResponse;
import com.meembusoft.safewaypharmaonline.util.AllConstants;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.util.SessionUtil;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_LOGIN_SETTING_TYPE;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_ORDER_CHECKOUT_TYPE;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_ORDER_SHIPPING;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_ORDER_SHIPPING_TYPE;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_SHIPPING_ID;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_REQUEST_CODE_CART_UPDATE;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_REQUEST_CODE_EDIT_SHIPPING_ADDRESS;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_REQUEST_CODE_SHIPPING_ADDRESS;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_REQUEST_CODE_USER_LOGIN;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class OrderListFragment extends BaseFragment {

    private ImageView ivSelectedOk;
    private TextView tvTotalsAmount, tvAddressTitle, tvDeliveryAddress, tvEssestialTitle;
    private EditText etEditDeliveryAddress;
    private Button btnOrderCheckout;
    private RelativeLayout nsMain;
    private FrameLayout myLayout;
    private RelativeLayout relBottomView;
    private LinearLayout linEditDeliveryAddress, linAddAddress, linEditAddress, linAllAddress, linLoginLayout, linNoDataFound, llContainerEssentialProduct;
    private RecyclerView rvMedicineOrderList, rvEssentialProductLis;
    private MyOrderListAdapter myOrderListAdapter;
    private EssentialProductListAdapter mEssentialProductListAdapter;
    private boolean isAdd;
    private AppUser mAppUser;
    private ShippingAddress mShippingAddress;
    private List<StaggeredMedicineByItem> checkItemList;
    private String address = "";
    //Background task
    private GetEssentialProductTask getEssentialProductTask;
    private APIInterface mApiInterface;
    private ShippingType mShippingType;
    private View mParentView = null;
    String mActivity;
    //  Supplier
    private AppSupplier mAppSupplier;
    FlavorType mFlavorType;
    private String customerOrSupplierId = "";
    private String selectUserType = "customer";

    public static OrderListFragment newInstance() {
        OrderListFragment fragment = new OrderListFragment();
        return fragment;
    }

    @Override
    public int initFragmentLayout() {
        return R.layout.fragment_card_order_screen;
    }

    @Override
    public void initFragmentBundleData(Bundle bundle) {

    }

    @Override
    public void initFragmentViews(View parentView) {
        myLayout = (FrameLayout) parentView.findViewById(R.id.frameLayout);
        relBottomView = (RelativeLayout) parentView.findViewById(R.id.rel_bottom_view);
        llContainerEssentialProduct = (LinearLayout)parentView.findViewById(R.id.ll_container_essential_product);
        btnOrderCheckout = (Button) parentView.findViewById(R.id.btn_order_checkout);
        nsMain = (RelativeLayout) parentView.findViewById(R.id.ns_main);
        linEditDeliveryAddress = (LinearLayout) parentView.findViewById(R.id.lin_lay_edit_delivery_address);
        linAddAddress = (LinearLayout) parentView.findViewById(R.id.lin_add_address);
        linEditAddress = (LinearLayout) parentView.findViewById(R.id.lin_edit_address);
        linAllAddress = (LinearLayout) parentView.findViewById(R.id.lin_all_address);
        linLoginLayout = (LinearLayout) parentView.findViewById(R.id.lin_login_layout);
        tvTotalsAmount = (TextView) parentView.findViewById(R.id.tv_totals_amount);
        tvDeliveryAddress = (TextView) parentView.findViewById(R.id.tv_delivery_address);
        tvEssestialTitle = (TextView) parentView.findViewById(R.id.tv_essestial_title);
        linNoDataFound = (LinearLayout) parentView.findViewById(R.id.lin_no_data_found_layout);
        rvMedicineOrderList = (RecyclerView) parentView.findViewById(R.id.rv_medicine_order_list);
        rvEssentialProductLis = (RecyclerView) parentView.findViewById(R.id.rv_essential_product);
        ivSelectedOk = (ImageView) parentView.findViewById(R.id.iv_selected_ok);
        // mAppUser = SessionUtil.getUser(getActivity());

        //checkUserLogin();
        updateTotalAmount();
        setFrameLayoutMargin();

    }

    private void checkUserLogin() {
        if (SessionUtil.getUser(getActivity()) != null) {
            nsMain.setVisibility(View.VISIBLE);
            linNoDataFound.setVisibility(View.GONE);
        } else {
            nsMain.setVisibility(View.GONE);
            linNoDataFound.setVisibility(View.GONE);
        }
    }

    @Override
    public void initFragmentViewsData() {
            mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
            // Get User Tag
            initGetTagValue();

            nsMain.setVisibility(View.VISIBLE);
            // Calling Shipping Address
            setDataShippingAddress();

            myOrderListAdapter = new MyOrderListAdapter(getActivity());
            rvMedicineOrderList.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvMedicineOrderList.setAdapter(myOrderListAdapter);
            Logger.d(TAG, "getAllStoredMedicineByItems: " + AppUtil.getAllStoredMedicineByItems(getActivity()).size());
            if (AppUtil.getAllStoredMedicineByItems(getActivity(),mFlavorType).size() > 0) {
                nsMain.setVisibility(View.VISIBLE);
                linNoDataFound.setVisibility(View.GONE);
                myOrderListAdapter.addAll(AppUtil.getAllStoredMedicineByItems(getActivity(),mFlavorType));
            } else {
                nsMain.setVisibility(View.GONE);
                linNoDataFound.setVisibility(View.VISIBLE);
            }

            if (!NetworkManager.isConnected(getActivity())) {
                Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
            } else {
                getEssentialProductTask = new GetEssentialProductTask(getActivity());
                getEssentialProductTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
                    selectUserType = "customer";
                    if (mAppUser != null) {
                        customerOrSupplierId = mAppUser.getId();
                    }
                    break;
                case SUPPLIER:
                    mAppSupplier = SessionUtil.getSupplier(getActivity());
                    selectUserType = "supplier";
                    if (mAppSupplier != null) {
                        customerOrSupplierId = mAppSupplier.getUser_id();
                    }
                    break;
            }

            Logger.d(TAG, TAG + " >>> " + "selectUserType " + selectUserType);
            Logger.d(TAG, TAG + " >>> " + "customerOrSupplierId " + customerOrSupplierId);
        }
    }
    @Override
    public void initFragmentActions() {
        btnOrderCheckout.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (!NetworkManager.isConnected(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                } else {
                    if (customerOrSupplierId != null && !TextUtils.isEmpty(customerOrSupplierId)) {
                        List<StaggeredMedicineByItem> staggeredMedicineByItemList = myOrderListAdapter.getAllData();
                        Logger.d(TAG, "staggeredMedicineByItemList: " + staggeredMedicineByItemList.toString());
                        if (staggeredMedicineByItemList != null && staggeredMedicineByItemList.size() > 0) {
                            for (StaggeredMedicineByItem itemResult : staggeredMedicineByItemList) {
                                onOrderNowClick(itemResult);
                            }
                            //updateTotalAmount();
                            // checkout the item
                            List<StaggeredMedicineByItem> checkoutItemList = AppUtil.getIsCheckoutItemList(getActivity());
                            if (checkoutItemList.size() > 0 && checkoutItemList != null) {
//                                mShippingAddress = SessionUtil.getUserShippingAddress(getActivity());
//                                Logger.d(TAG, "<<<<mShippingAddress): onResponse-object = " + mShippingAddress.toString());

                                if (mShippingAddress != null && (!AllSettingsManager.isNullOrEmpty(mShippingAddress.getId()))) {
                                    Intent iCheckoutOrder = new Intent(getActivity(), CheckoutOrderActivity.class);
                                    iCheckoutOrder.putExtra(INTENT_KEY_ORDER_CHECKOUT_TYPE, OrderCheckoutType.CART_TO_CHECKOUT.name());
                                    iCheckoutOrder.putExtra(INTENT_KEY_SHIPPING_ID, mShippingAddress.getId());
                                    getActivity().startActivityForResult(iCheckoutOrder, INTENT_REQUEST_CODE_CART_UPDATE);
                                } else {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_please_select_shipping_address), Toast.LENGTH_SHORT).show();
                                }
                                //getActivity().startActivity(iCheckoutOrder);
                            } else {
                                Toast.makeText(getActivity(), getResources().getString(R.string.toast_check_the_cart_one_item), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.txt_item_is_not_added_into_cart), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
                        intentLogin.putExtra(INTENT_KEY_LOGIN_SETTING_TYPE, LoginSettingType.ORDER_CART_TO_LOGIN.name());
                        getActivity().startActivityForResult(intentLogin, INTENT_REQUEST_CODE_USER_LOGIN);
                    }
                }
            }
        });

//        btnUpdateCart.setOnClickListener(new OnSingleClickListener() {
//            @Override
//            public void onSingleClick(View view) {
//                List<StaggeredMedicineByItem> staggeredMedicineByItemList = myOrderListAdapter.getAllData();
//                Logger.d(TAG, "staggeredMedicineByItemList: " + staggeredMedicineByItemList.toString());
//                if (staggeredMedicineByItemList!=null && staggeredMedicineByItemList.size()>0) {
//                    for (StaggeredMedicineByItem itemResult : staggeredMedicineByItemList) {
//                        onOrderNowClick(itemResult);
//                    }
//                    updateTotalAmount();
//                } else {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.txt_item_is_not_added_into_cart), Toast.LENGTH_SHORT).show();
//
//                }
//
//            }
//        });

        linAddAddress.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (customerOrSupplierId != null && customerOrSupplierId != "") {
                    Intent iAddShippingAddress = new Intent(getActivity(), AddEditShippingAddressActivity.class);
                    iAddShippingAddress.putExtra(INTENT_KEY_ORDER_SHIPPING_TYPE, ShippingType.ADD_TO_SHIPPING.name());
                    getActivity().startActivityForResult(iAddShippingAddress, INTENT_REQUEST_CODE_SHIPPING_ADDRESS);
                } else {
                    Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
                    intentLogin.putExtra(INTENT_KEY_LOGIN_SETTING_TYPE, LoginSettingType.ORDER_CART_TO_LOGIN.name());
                    getActivity().startActivityForResult(intentLogin, INTENT_REQUEST_CODE_USER_LOGIN);
                }
            }
        });
        linEditAddress.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                if (customerOrSupplierId != null && customerOrSupplierId != "") {
                    if (mShippingAddress != null && (!AllSettingsManager.isNullOrEmpty(mShippingAddress.getId()))) {
                        Intent iEditShippingAddress = new Intent(getActivity(), AddEditShippingAddressActivity.class);
                        iEditShippingAddress.putExtra(INTENT_KEY_ORDER_SHIPPING, Parcels.wrap(mShippingAddress));
                        iEditShippingAddress.putExtra(INTENT_KEY_ORDER_SHIPPING_TYPE, ShippingType.EDIT_TO_SHIPPING.name());
                        getActivity().startActivityForResult(iEditShippingAddress, INTENT_REQUEST_CODE_EDIT_SHIPPING_ADDRESS);
                    } else {
                        Intent iShippingAddress = new Intent(getActivity(), AddEditShippingAddressActivity.class);
                        iShippingAddress.putExtra(INTENT_KEY_ORDER_SHIPPING_TYPE, ShippingType.ADD_TO_SHIPPING.name());
                        getActivity().startActivityForResult(iShippingAddress, INTENT_REQUEST_CODE_SHIPPING_ADDRESS);
                    }
                } else {
                    Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
                    intentLogin.putExtra(INTENT_KEY_LOGIN_SETTING_TYPE, LoginSettingType.ORDER_CART_TO_LOGIN.name());
                    getActivity().startActivityForResult(intentLogin, INTENT_REQUEST_CODE_USER_LOGIN);

                }
            }
        });

        linAllAddress.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (customerOrSupplierId != null && customerOrSupplierId != "") {
                    Intent iShippingAddress = new Intent(getActivity(), ShippingAddressActivity.class);
                    iShippingAddress.putExtra(INTENT_KEY_ORDER_SHIPPING, Parcels.wrap(mShippingAddress));
                    getActivity().startActivityForResult(iShippingAddress, INTENT_REQUEST_CODE_SHIPPING_ADDRESS);
                } else {
                    Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
                    intentLogin.putExtra(INTENT_KEY_LOGIN_SETTING_TYPE, LoginSettingType.ORDER_CART_TO_LOGIN.name());
                    getActivity().startActivityForResult(intentLogin, INTENT_REQUEST_CODE_USER_LOGIN);

                }
            }
        });

//        ivSelectedOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                linEditDeliveryAddress.setBackgroundResource(R.drawable.shape_dashed_line);
//
//                if (etEditDeliveryAddress.getText().toString().length()>0) {
//                    tvEditDeliveryAddress.setText(etEditDeliveryAddress.getText().toString());
//                }
//                etEditDeliveryAddress.setText("");
//                etEditDeliveryAddress.setVisibility(View.GONE);
//                ivSelectedOk.setVisibility(View.GONE);
//                tvEditAddressTitle.setVisibility(View.VISIBLE);
//                isAdd = false;
//
//                if (mAppUser.getShiping_address() != null){
//
//                }
//            }
//        });
    }

    private void setDataShippingAddress() {
        mShippingAddress = SessionUtil.getUserShippingAddress(getActivity());

        if (mShippingAddress != null && (!AllSettingsManager.isNullOrEmpty(mShippingAddress.getId()))) {
            Logger.d(TAG, "mShippingAddress: " + mShippingAddress.toString());
            List<String> strAddress = new ArrayList<String>();
            if (!AllSettingsManager.isNullOrEmpty(mShippingAddress.getAddress())) {
                strAddress.add(AppUtil.optStringNullCheckValue(mShippingAddress.getAddress()));
            }

            if (!AllSettingsManager.isNullOrEmpty(mShippingAddress.getState())) {
                strAddress.add(AppUtil.optStringNullCheckValue(mShippingAddress.getState()));
            }

            if (!AllSettingsManager.isNullOrEmpty(mShippingAddress.getCity())) {
                strAddress.add(AppUtil.optStringNullCheckValue(mShippingAddress.getCity()));
            }

            if (strAddress.size() > 0) {
                String address = TextUtils.join(",", strAddress);
                tvDeliveryAddress.setText(address);
            } else {
                tvDeliveryAddress.setText("");
            }
        } else {
            tvDeliveryAddress.setText("");
        }
    }

    public StaggeredMedicineByItem onOrderNowClick(final StaggeredMedicineByItem item) {
        Logger.d(TAG, "onOrderNowClick: " + "count: " + item.getItem_quantity());

        if (item.getItem_quantity() == 0) {
            if (AppUtil.isMedicineByItemStored(getActivity(), item)) {
                //Delete the Item from database
                AppUtil.deleteSelectedMedicineByItem(getActivity(), item);
            }
        } else if (item.getItem_quantity() == 1) {
            if (AppUtil.isMedicineByItemStored(getActivity(), item)) {
                //Update data into database
                AppUtil.storeSelectedMedicineByItem(getActivity(), item);
            } else {
                //Add item into database
                AppUtil.storeSelectedMedicineByItem(getActivity(), item);
            }
        } else {
            //Update data into database
            AppUtil.storeSelectedMedicineByItem(getActivity(), item);
        }

        return item;
    }

    public void updateTotalAmount() {
        try {
            mAppUser = SessionUtil.getUser(getActivity());
            mAppSupplier = SessionUtil.getSupplier(getActivity());
            //  Logger.d(TAG, "mAppUser: " + mAppUser.toString());
            checkItemList = AppUtil.getIsCheckoutItemList(getActivity());
            Logger.d(TAG, TAG + " >>> " + "checkItemList: " + checkItemList.toString());

            if (checkItemList != null && checkItemList.size() > 0) {
                float subtotal = AppUtil.formatFloat(AppUtil.getCheckoutFromSubtotalPrice(checkItemList));
                Logger.d(TAG, TAG + " >>> " + "subtotal: " + subtotal);
                String subTotalFinal = AppUtil.formatDoubleString(subtotal);
                String textTotalsAmount = ""
                        + " <font color='" + getResources().getColor(R.color.black) + "'>"
                        + getResources().getString(R.string.title_totals_taka) + "</font>"
                        + " <font color='" + getResources().getColor(R.color.color_pink) + "'>"
                        + " " + subTotalFinal + " /-" + "</font>";
                tvTotalsAmount.setText(Html.fromHtml(textTotalsAmount));

            } else {
                List<StaggeredMedicineByItem> data = AppUtil.getAllStoredMedicineByItems(getActivity(),mFlavorType);
                Logger.d(TAG, TAG + " >>> " + "AllStoredDataList: " + data.toString());

                if (data != null && data.size() > 0) {
                    float subtotal = AppUtil.formatFloat(AppUtil.getCheckoutFromSubtotalPrice(data));
                    Logger.d(TAG, TAG + " >>> " + "subtotal: " + subtotal);
                    String subTotalFinal = AppUtil.formatDoubleString(subtotal);
                    String textTotalsAmount = ""
                            + " <font color='" + getResources().getColor(R.color.black) + "'>"
                            + getResources().getString(R.string.title_totals_taka) + "</font>"
                            + " <font color='" + getResources().getColor(R.color.color_pink) + "'>"
                            + " " + subTotalFinal + " /-" + "</font>";
                    tvTotalsAmount.setText(Html.fromHtml(textTotalsAmount));
                } else {
                    String textTotalsAmount = ""
                            + " <font color='" + getResources().getColor(R.color.black) + "'>"
                            + getResources().getString(R.string.title_totals_taka) + "</font>"
                            + " <font color='" + getResources().getColor(R.color.color_pink) + "'>"
                            + " 0.0 /-" + "</font>";
                    tvTotalsAmount.setText(Html.fromHtml(textTotalsAmount));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.d(TAG, TAG + " >>> " + "Exception: " + ex.toString());

        }
    }

    public void setFrameLayoutMargin() {
        try {
            mActivity = getActivity().getClass().getSimpleName();
            if (mActivity != null && isAdded()) {
                Logger.d(TAG, TAG + " >>> " + "mActivity>>: " + mActivity);
                if (mActivity.equalsIgnoreCase(AllConstants.HOME_ACTIVITY)) {
//                    if (Build.VERSION.SDK_INT >= 29) {
//                        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 300);
//                    } else if (Build.VERSION.SDK_INT >= 28) {
//                        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
//                                285);
//                    } else if (Build.VERSION.SDK_INT == 27) {
//                        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
//                                215);
//                    } else if ( Build.VERSION.SDK_INT >= 26) {
//                        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
//                                195);
//                    } else if ( Build.VERSION.SDK_INT >= 25) {
//                        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
//                                180);
//                    } else if ( Build.VERSION.SDK_INT >= 24) {
//                        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
//                                170);
//                    } else {
//                        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
//                                154);
//                    }
//                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//                    if (AppUtil.hasNavigationBar(getActivity())) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) relBottomView.getLayoutParams();
                    params.height = AppUtil.dpToPx(105, getActivity());
                    relBottomView.setLayoutParams(params);

                    LinearLayout.LayoutParams container = (LinearLayout.LayoutParams) llContainerEssentialProduct.getLayoutParams();
                    container.bottomMargin = AppUtil.dpToPx(50, getActivity());
                    llContainerEssentialProduct.setLayoutParams(container);
//                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initEssentialRv(List<StaggeredMedicineByItem> essentialProductList) {
        mEssentialProductListAdapter = new EssentialProductListAdapter(getActivity());
        rvEssentialProductLis.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvEssentialProductLis.setAdapter(mEssentialProductListAdapter);
        if (essentialProductList != null && essentialProductList.size() > 0) {
            mEssentialProductListAdapter.addAll(essentialProductList);
            tvEssestialTitle.setVisibility(View.VISIBLE);
            rvEssentialProductLis.setVisibility(View.VISIBLE);

        } else {
            tvEssestialTitle.setVisibility(View.GONE);
            rvEssentialProductLis.setVisibility(View.GONE);
        }
    }

    @Override
    public void initFragmentBackPress() {

        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getActivity().getSupportFragmentManager().popBackStack();
        } else {
            getActivity().finish();
        }
    }

    @Override
    public void initFragmentOnResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case INTENT_REQUEST_CODE_USER_LOGIN:
                Logger.d(TAG, TAG + " >>> " + "INTENT_REQUEST_CODE_LOGIN: " + requestCode);
                LoginSettingType loginSettingType;
                if (data != null && resultCode == RESULT_OK) {

                    if (SessionUtil.getUser(getActivity()) != null) {
                        initGetTagValue();
                        updateTotalAmount();
                        setDataShippingAddress();
                        setFrameLayoutMargin();
                    }

                    if (SessionUtil.getSupplier(getActivity()) != null) {
                        updateTotalAmount();
                        setDataShippingAddress();
                        setFrameLayoutMargin();
                    }
                    }
                break;

            case INTENT_REQUEST_CODE_SHIPPING_ADDRESS:
                Logger.d(TAG, TAG + " >>> " + "INTENT_REQUEST_CODE_SHIPPING_ADDRESS: " + requestCode);
                if (data != null) {
                    setDataShippingAddress();
                }
                break;

            case INTENT_REQUEST_CODE_EDIT_SHIPPING_ADDRESS:
                Logger.d(TAG, TAG + " >>> " + "INTENT_REQUEST_CODE_EDIT_SHIPPING_ADDRESS: " + requestCode);
                if (data != null) {
                    setDataShippingAddress();
                }
                break;

        }
    }

    @Override
    public void initFragmentUpdate(Object object) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Image slider
        if (getEssentialProductTask != null && getEssentialProductTask.getStatus() == AsyncTask.Status.RUNNING) {
            getEssentialProductTask.cancel(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /************************
     * Server communication *
     ************************/

    private class GetEssentialProductTask extends AsyncTask<String, Integer, Response> {

        Context mContext;

        private GetEssentialProductTask(Context context) {
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
                Call<APIResponse<List<StaggeredMedicineByItem>>> call = mApiInterface.apiGetEssentialProductList(customerOrSupplierId,selectUserType);
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
                    Logger.d(TAG, "APIResponse(GetEssentialProductTask): onResponse-server = " + result.toString());
                    APIResponse<List<StaggeredMedicineByItem>> data = (APIResponse<List<StaggeredMedicineByItem>>) result.body();
                    Logger.d("GetEssentialProductTask", data.toString() + "");
                    Log.e("EssentialProductTask>>", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(GetEssentialProductTask()): onResponse-object = " + data.toString());

                        initEssentialRv(data.getData());

                    }
                } else {
                    // loadOfflineTimeData();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }


}