package com.meembusoft.safewaypharmaonline.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.adapter.BaseSellerListAdapter;
import com.meembusoft.safewaypharmaonline.adapter.MedicalCosmetisListAdapter;
import com.meembusoft.safewaypharmaonline.base.BaseActivity;
import com.meembusoft.safewaypharmaonline.enumeration.CategoryType;
import com.meembusoft.safewaypharmaonline.enumeration.DetailType;
import com.meembusoft.safewaypharmaonline.enumeration.OrderStatusType;
import com.meembusoft.safewaypharmaonline.googlemap.OnMapAndViewReadyListener;
import com.meembusoft.safewaypharmaonline.model.CommonHomeObject;
import com.meembusoft.safewaypharmaonline.model.Home;
import com.meembusoft.safewaypharmaonline.model.NearestStore;
import com.meembusoft.safewaypharmaonline.model.PlaceOrderByItem;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.retrofit.APIClient;
import com.meembusoft.safewaypharmaonline.retrofit.APIInterface;
import com.meembusoft.safewaypharmaonline.retrofit.APIResponse;
import com.meembusoft.safewaypharmaonline.util.AllConstants;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.BroadcastManager;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.view.CanaroTextView;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;

import org.parceler.Parcels;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_CATEGORY_ID;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_MEDICINE_BY_CATEGORY_TYPE;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class MapActivity extends BaseActivity implements
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener,
        OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener {

    //Toolbar
    private ImageView ivUser, ivBack;
    private CanaroTextView tvTitle;
    private TextView tvCount;
    private TextView tvName, tvShopname, tvAddress, tvEmail, tvShopMore;
    private LinearLayout llUserContact;
    private RelativeLayout rlCart, rlPharmacyInfo;
    // Recycler view
    private RecyclerView rvMedicalCosmeticsProduct;
    private MedicalCosmetisListAdapter medicalCosmetisListAdapter;
    private GetNearestStoreListTask getNearestStoreListTask;
    //Background task
    private APIInterface mApiInterface;

    private static final LatLng DHANMONDI = new LatLng(23.746466, 90.376015);
    private static final LatLng GULSHAN = new LatLng(23.797911, 90.414391);
    private static final LatLng MIRPUR = new LatLng(23.822350, 90.365417);
    private static final LatLng UTTORA = new LatLng(23.777176, 90.399452);
    private static final LatLng TEJGAON = new LatLng(23.759739, 90.392418);

    private GoogleMap mMap = null;
    private PlaceOrderByItem placeOrderByItem;
    private String OrderId = "", medicalCosmeticsID = "";
    private List<NearestStore> nearestStoreList;
    /**
     * Keeps track of the selected marker.
     */
    private Marker mSelectedMarker;
    SupportMapFragment mapFragment;

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }


    @Override
    public int initActivityLayout() {
        return R.layout.activity_map_screen;
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
            String mParcelableOrderId = intent.getStringExtra(AllConstants.INTENT_KEY_ORDER_ID);
            String mParcelableDetailType = intent.getStringExtra(AllConstants.INTENT_KEY_DETAIL_TYPE);
            if (!AllSettingsManager.isNullOrEmpty(mParcelableDetailType)) {
                DetailType mDetailType = DetailType.valueOf(mParcelableDetailType);
                Logger.d(TAG, TAG + " >>> " + "mDetailType: " + mDetailType);

                switch (mDetailType) {
                    case FCM:
                        Parcelable mParcelable = intent.getParcelableExtra(AllConstants.INTENT_KEY_FCM_ORDER_ITEM);
                        if (mParcelable != null) {
                            placeOrderByItem = Parcels.unwrap(mParcelable);
                            Logger.d(TAG, TAG + " >>>MapActivity " + "placeOrderByItem: " + placeOrderByItem.toString());
                            OrderId = placeOrderByItem.getId();
                        }
                        break;
                }
            }

            if (!AllSettingsManager.isNullOrEmpty(mParcelableOrderId)) {
                OrderId = mParcelableOrderId;
            }
        }
    }

    @Override
    public void initActivityViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (CanaroTextView) findViewById(R.id.text_title);
        tvTitle.setText(getActivity().getResources().getString(R.string.title_pharmacy_map));
        tvCount = (TextView) findViewById(R.id.tv_cart);
        ivUser = (ImageView) findViewById(R.id.iv_user);

        tvName = (TextView) findViewById(R.id.tv_name);
        tvShopname = (TextView) findViewById(R.id.tv_shopname);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvShopMore = (TextView) findViewById(R.id.tv_medical_cosmetic_shop_more);

        rlCart = (RelativeLayout) findViewById(R.id.rl_cart);
        rlPharmacyInfo = (RelativeLayout) findViewById(R.id.rl_pharmacy_info);
        rvMedicalCosmeticsProduct = (RecyclerView) findViewById(R.id.rv_medicated_cosmetic_product);
        llUserContact = (LinearLayout) findViewById(R.id.lin_user_contact);

        // Map Fragment
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        new OnMapAndViewReadyListener(mapFragment, this);
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        // Register BroadCast
        BroadcastManager.registerBroadcastUpdate(getActivity(), notificationUpdate);

        mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
        } else {
            if (OrderId != null) {
                getNearestStoreListTask = new GetNearestStoreListTask(getActivity(), OrderId);
                getNearestStoreListTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
        if (!AllSettingsManager.isNullOrEmpty(SessionManager.getStringSetting(getActivity(), AllConstants.SESSION_KEY_HOME))) {
            String offlineHomeData = SessionManager.getStringSetting(getActivity(), AllConstants.SESSION_KEY_HOME);
            Logger.d(TAG, "HomeData(Session): " + offlineHomeData);
            Home responseHOme = APIResponse.getResponseObject(offlineHomeData, Home.class);
            medicalCosmeticsID = responseHOme.getMedicated_cosmetics().getId();
            initMedicalCosmeticsProduct(responseHOme.getMedicated_cosmetics());
        }

        setSettingDataInfo(placeOrderByItem);
    }

    private void setSettingDataInfo(PlaceOrderByItem mPlaceOrderByItem) {

        if (mPlaceOrderByItem != null && mPlaceOrderByItem.getSupplier_information() != null && mPlaceOrderByItem.getStatus().equalsIgnoreCase(OrderStatusType.ACCEPTED.toString())) {
            rlPharmacyInfo.setVisibility(View.VISIBLE);
            tvName.setText(mPlaceOrderByItem.getSupplier_information().getFull_name());
            tvShopname.setText(mPlaceOrderByItem.getSupplier_information().getShop_name());
            tvAddress.setText(mPlaceOrderByItem.getSupplier_information().getAddress());
            tvEmail.setText(mPlaceOrderByItem.getSupplier_information().getEmail());
            //  AppUtil.loadImage(getActivity(), ivUser, mPlaceOrderByItem.getSupplier_information().getLogo(), false, true, true);

            tvEmail.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/html");
                        intent.putExtra(Intent.EXTRA_EMAIL, AppUtil.optStringNullCheckValue(mPlaceOrderByItem.getSupplier_information().getEmail()));
                        intent.putExtra(Intent.EXTRA_SUBJECT, AppUtil.optStringNullCheckValue(mPlaceOrderByItem.getCustomer_name()));
                        intent.putExtra(Intent.EXTRA_TEXT, "");

                        startActivity(Intent.createChooser(intent, "Send Email"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            llUserContact.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + AppUtil.optStringNullCheckValue(mPlaceOrderByItem.getSupplier_information().getPhone())));
                        startActivity(intent);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } else {
            rlPharmacyInfo.setVisibility(View.GONE);
        }
    }

    // Base Seller List
    private void initMedicalCosmeticsProduct(CommonHomeObject objMedicalCosmetics) {
        if (objMedicalCosmetics != null) {
            List<StaggeredMedicineByItem> medicalCosmeticsByItemList = AppUtil.getFourMedicineByItemsList(getActivity(), objMedicalCosmetics.getItem());
            if (medicalCosmeticsByItemList.size() > 0 && medicalCosmeticsByItemList != null) {
                medicalCosmetisListAdapter = new MedicalCosmetisListAdapter(getActivity());
                rvMedicalCosmeticsProduct.setLayoutManager(new GridLayoutManager(getActivity(), 4));
                rvMedicalCosmeticsProduct.setAdapter(medicalCosmetisListAdapter);
                medicalCosmetisListAdapter.addAll(medicalCosmeticsByItemList);
            }
        }
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iMap = new Intent(getActivity(), HomeActivity.class);
                iMap.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                iMap.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                iMap.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(iMap);
                finish();
            }
        });

        tvShopMore.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (medicalCosmeticsID != null) {
                    Intent iMedicineByCategoryShowAll = new Intent(getActivity(), SeeMoreActivity.class);
                    iMedicineByCategoryShowAll.putExtra(INTENT_KEY_CATEGORY_ID, medicalCosmeticsID);
                    iMedicineByCategoryShowAll.putExtra(INTENT_KEY_MEDICINE_BY_CATEGORY_TYPE, CategoryType.COSMETICS.name());
                    //  iMedicineByCategoryShowAll.putExtra(INTENT_KEY_GENERAL_MEDICINE_BY_CATEGORY, Parcels.wrap(staggeredMedicalCosmetics));
                    getActivity().startActivity(iMedicineByCategoryShowAll);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_no_info_found), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        if (intent != null) {
//            Parcelable mParcelable = intent.getParcelableExtra(AllConstants.INTENT_KEY_ORDER_ITEM);
//            if (mParcelable != null) {
//                placeOrderByItem = Parcels.unwrap(mParcelable);
//                Logger.d(TAG, TAG + " >>>MapActivity " + "placeOrderByItem: " + placeOrderByItem.toString());
//                setSettingDataInfo(placeOrderByItem);
//            }
//        }
//    }

    /*****************************
     * Order notification update *
     *****************************/
    BroadcastReceiver notificationUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            PlaceOrderByItem order = BroadcastManager.getBroadcastData(intent);
            if (order != null) {
                placeOrderByItem = order;
                setSettingDataInfo(placeOrderByItem);
                Logger.d(TAG, TAG + ">>notificationUpdate>> FCM: Updated order detail");
            }
        }
    };

    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void initActivityBackPress() {
        Intent iMap = new Intent(getActivity(), HomeActivity.class);
        iMap.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        iMap.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        iMap.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(iMap);
        finish();
        ;
    }

    @Override
    public void initActivityDestroyTasks() {
        dismissPopupDialog();
        // Unregister BroadCast
        BroadcastManager.unregisterBroadcastUpdate(getActivity(), notificationUpdate);
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

    @Override
    public void onMapClick(LatLng latLng) {
// Any showing info window closes when the map is clicked.
        // Clear the currently selected marker.
        mSelectedMarker = null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // The user has re-tapped on the marker which was already showing an info window.
        if (marker.equals(mSelectedMarker)) {
            // The showing info window has already been closed - that's the first thing to happen
            // when any marker is clicked.
            // Return true to indicate we have consumed the event and that we do not want the
            // the default behavior to occur (which is for the camera to move such that the
            // marker is centered and for the marker's info window to open, if it has one).
            mSelectedMarker = null;
            return true;
        }

        mSelectedMarker = marker;
        return false;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Hide the zoom controls.
        mMap.getUiSettings().setZoomControlsEnabled(false);

        // Add lots of markers to the map.
        if (nearestStoreList != null) {
            initNearestStore(nearestStoreList);
        }

        // Set listener for marker click event.  See the bottom of this class for its behavior.
        mMap.setOnMarkerClickListener(this);

        // Set listener for map click event.  See the bottom of this class for its behavior.
        mMap.setOnMapClickListener(this);

        // Override the default content description on the view, for accessibility mode.
        // Ideally this string would be localized.
        map.setContentDescription("Demo showing how to close the info window when the currently"
                + " selected marker is re-tapped.");
//
//        LatLngBounds bounds = new LatLngBounds.Builder()
//                .include(DHANMONDI)
//                .include(GULSHAN)
//                .include(MIRPUR)
//                .include(UTTORA)
//                .include(TEJGAON)
//                .build();
        //       mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));

        // Enable user current location
        mMap.setMyLocationEnabled(true);
        // Enable my location Button
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        // Re-position my location button on right bottom
        View myLocationButton = mapFragment.getView().findViewById(0x2);
        if (myLocationButton != null && myLocationButton.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            // location button is inside of RelativeLayout
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myLocationButton.getLayoutParams();

            // Align it to - parent BOTTOM|RIGHT
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);

            // Update margins, set to 20dp
            final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
            params.setMargins(margin, margin, margin, margin);

            myLocationButton.setLayoutParams(params);
        }
    }

    private void addMarkersToMap() {
        mMap.addMarker(new MarkerOptions()
                .position(DHANMONDI)
                .title("DHANMONDI"));
//                .snippet("Population: 2,074,200"));

        mMap.addMarker(new MarkerOptions()
                .position(GULSHAN)
                .title("GULSHAN"));
//                .snippet("Population: 4,627,300"));

        mMap.addMarker(new MarkerOptions()
                .position(MIRPUR)
                .title("Melbourne"));
//                .snippet("Population: 4,137,400"));

        mMap.addMarker(new MarkerOptions()
                .position(UTTORA)
                .title("UTTORA"));
//                .snippet("Population: 1,738,800"));

        mMap.addMarker(new MarkerOptions()
                .position(TEJGAON)
                .title("TEJGAON"));
//                .snippet("Population: 1,213,000"));
    }

//    TextWatcher textWatcher = new TextWatcher() {
//        @Override
//        public void onTextChanged(CharSequence c, int i, int i1, int i2) {
//            if (c.toString().trim().length() == 0) {
//                ibClear.setVisibility(View.GONE);
//            } else {
//                ibClear.setVisibility(View.VISIBLE);
//            }
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence c, int i, int i1, int i2) {
//        }
//
//        @Override
//        public void afterTextChanged(Editable editable) {
//        }
//    };

    private void initNearestStore(List<NearestStore> nearestStoreList) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (NearestStore nearestStore : nearestStoreList) {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Float.parseFloat(nearestStore.getLatitude()), Float.parseFloat(nearestStore.getLongitude())))
                    .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.map_icon)));

            builder.include(new LatLng(Float.parseFloat(nearestStore.getLatitude()), Float.parseFloat(nearestStore.getLongitude())));

        }
        LatLngBounds bounds = builder.build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private class GetNearestStoreListTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        String mOrderId;

        private GetNearestStoreListTask(Context context, String orderId) {
            mContext = context;
            mOrderId = orderId;
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
                Call<APIResponse<List<NearestStore>>> call = mApiInterface.apiGetNearestStoreList(mOrderId);
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
                    Logger.d(TAG, "APIResponse(GetNearestStoreListTask): onResponse-server = " + result.toString());
                    APIResponse<List<NearestStore>> data = (APIResponse<List<NearestStore>>) result.body();
                    Logger.d("GetNearestStoreListTask", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(GetNearestStoreListTask()): onResponse-object = " + data.toString());
                        nearestStoreList = data.getData();

                        initNearestStore(nearestStoreList);
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