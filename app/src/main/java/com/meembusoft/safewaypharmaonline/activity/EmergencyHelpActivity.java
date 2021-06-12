package com.meembusoft.safewaypharmaonline.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.adapter.BaseSellerListAdapter;
import com.meembusoft.safewaypharmaonline.adapter.EmergencyHelpListAdapter;
import com.meembusoft.safewaypharmaonline.base.BaseActivity;
import com.meembusoft.safewaypharmaonline.enumeration.CategoryType;
import com.meembusoft.safewaypharmaonline.googlemap.OnMapAndViewReadyListener;
import com.meembusoft.safewaypharmaonline.model.CommonHelp;
import com.meembusoft.safewaypharmaonline.model.CommonMedicinesByItem;
import com.meembusoft.safewaypharmaonline.model.Emergency;
import com.meembusoft.safewaypharmaonline.model.Home;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.retrofit.APIClient;
import com.meembusoft.safewaypharmaonline.retrofit.APIInterface;
import com.meembusoft.safewaypharmaonline.retrofit.APIResponse;
import com.meembusoft.safewaypharmaonline.util.AllConstants;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.view.CanaroTextView;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_MEDICINE_BY_CATEGORY_TYPE;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class EmergencyHelpActivity extends BaseActivity implements GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener,
        OnMapAndViewReadyListener.OnGlobalLayoutAndMapReadyListener {

    // Toolbar
    private ImageView ivBack;
    private CanaroTextView tvTitle;
    private RelativeLayout rlCart;
    private TextView tvCount, tvSeeMore;
    private LinearLayout llLeftBack;
    // Recycler view
    private RecyclerView rvEmergencyHelp, rvBaseSellerProduct;
    private EmergencyHelpListAdapter mEmergencyHelpListAdapter;

    private BaseSellerListAdapter baseSellerListAdapter;
    //Background task
    private GetEmergencyHelpTask getEmergencyHelpTask;
    private APIInterface mApiInterface;
    Emergency mEmergency;

    private static final LatLng DHANMONDI = new LatLng(23.746466, 90.376015);
    private static final LatLng GULSHAN = new LatLng(23.797911, 90.414391);
    private static final LatLng MIRPUR = new LatLng(23.822350, 90.365417);
    private static final LatLng UTTORA = new LatLng(23.777176, 90.399452);
    private static final LatLng TEJGAON = new LatLng(23.759739, 90.392418);

    private GoogleMap mMap = null;

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
        return R.layout.activity_emergency_help_screen;
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

    }

    @Override
    public void initActivityViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        // rvShowAll = (RecyclerView) findViewById(R.id.rv_show_all_video);
        tvTitle = (CanaroTextView) findViewById(R.id.text_title);
        tvTitle.setText(getResources().getString(R.string.title_emergency_help));
        tvCount = (TextView) findViewById(R.id.tv_cart);
        tvSeeMore = (TextView) findViewById(R.id.tv_base_seller_see_more);
        rlCart = (RelativeLayout) findViewById(R.id.rl_cart);
        llLeftBack = (LinearLayout) findViewById(R.id.ll_left_back);
        rvEmergencyHelp = (RecyclerView) findViewById(R.id.rv_emergency_help);
        rvBaseSellerProduct = (RecyclerView) findViewById(R.id.rv_base_seller_product);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.emergency_map);
        new OnMapAndViewReadyListener(mapFragment, this);
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
            // loadOfflineTimeData();
        } else {
            String offlineHomeData = "";
            if (!AllSettingsManager.isNullOrEmpty(SessionManager.getStringSetting(getActivity(), AllConstants.SESSION_KEY_HOME))) {
                offlineHomeData = SessionManager.getStringSetting(getActivity(), AllConstants.SESSION_KEY_HOME);
                Logger.d(TAG, "HomeData(Session): " + offlineHomeData);
                //Load home data
                // ResponseOfflineHome responseOfflineHome = APIResponse.getResponseObject(offlineHomeData, ResponseOfflineHome.class);
                Home responseHOme = APIResponse.getResponseObject(offlineHomeData, Home.class);
                Logger.d("Best_seller", responseHOme.getBest_seller());

                initBestSellerProduct(responseHOme.getBest_seller());
            }
            getEmergencyHelpTask = new GetEmergencyHelpTask(getActivity());
            getEmergencyHelpTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

    }


    // Base Seller List
    private void initBestSellerProduct(List<StaggeredMedicineByItem> baseSellerList) {
        if (baseSellerList.size() > 0 && baseSellerList != null) {
            baseSellerListAdapter = new BaseSellerListAdapter(getActivity());
            rvBaseSellerProduct.setLayoutManager(new GridLayoutManager(getActivity(), 4));
            rvBaseSellerProduct.setAdapter(baseSellerListAdapter);
            baseSellerListAdapter.addAll(baseSellerList);
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

        tvSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iBaseSellerSeeAll = new Intent(getActivity(), SeeMoreActivity.class);
                iBaseSellerSeeAll.putExtra(INTENT_KEY_MEDICINE_BY_CATEGORY_TYPE, CategoryType.BASE_SELLER.name());
                getActivity().startActivity(iBaseSellerSeeAll);
            }
        });

    }


    private void initEmergencyHelpList(List<Emergency> emergencyList) {
        if (emergencyList != null && emergencyList.size() > 0) {
            mEmergencyHelpListAdapter = new EmergencyHelpListAdapter(getActivity());
            rvEmergencyHelp.setLayoutManager(new GridLayoutManager(getActivity(), 5));
            rvEmergencyHelp.setAdapter(mEmergencyHelpListAdapter);
            mEmergencyHelpListAdapter.addAll(emergencyList);
        }
    }


    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {

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

        if (getEmergencyHelpTask != null && getEmergencyHelpTask.getStatus() == AsyncTask.Status.RUNNING) {
            getEmergencyHelpTask.cancel(true);
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
        //  addMarkersToMap();
        if (mEmergency != null) {
            emergencyHelpList(mEmergency);
        }
        // Set listener for marker click event.  See the bottom of this class for its behavior.
        mMap.setOnMarkerClickListener(this);

        // Set listener for map click event.  See the bottom of this class for its behavior.
        mMap.setOnMapClickListener(this);

        // Override the default content description on the view, for accessibility mode.
        // Ideally this string would be localized.
        map.setContentDescription("Demo showing how to close the info window when the currently"
                + " selected marker is re-tapped.");

//        LatLngBounds bounds = new LatLngBounds.Builder()
//                .include(DHANMONDI)
//                .include(GULSHAN)
//                .include(MIRPUR)
//                .include(UTTORA)
//                .include(TEJGAON)
//                .build();
//        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));

        // Enable user current location
        mMap.setMyLocationEnabled(true);
        // Enable my location Button
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        // Re-position my location button on right bottom
        @SuppressLint("ResourceType") View myLocationButton = mapFragment.getView().findViewById(0x2);
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

    public void emergencyHelpList(Emergency emergency) {
        Logger.d("emergency", emergency.toString() + "");

        if (emergency != null) {
            if (emergency.getId().equalsIgnoreCase("1") || emergency.getTitle().equalsIgnoreCase("Doctors")) {
                initMapList(emergency.getDoctors(),emergency.getTitle(),emergency.getId());
            } else if (emergency.getId().equalsIgnoreCase("2") || emergency.getTitle().equalsIgnoreCase("Hospitals")) {
                initMapList(emergency.getHospitals(),emergency.getTitle(),emergency.getId());
            } else if (emergency.getId().equalsIgnoreCase("3") || emergency.getTitle().equalsIgnoreCase("Ambulances")) {
                initMapList(emergency.getAmbulances(),emergency.getTitle(),emergency.getId());
            } else if (emergency.getId().equalsIgnoreCase("4") || emergency.getTitle().equalsIgnoreCase("Fire Services")) {
                initMapList(emergency.getFire_services(),emergency.getTitle(),emergency.getId());
            } else if (emergency.getId().equalsIgnoreCase("5") || emergency.getTitle().equalsIgnoreCase("Police Stations")) {
                initMapList(emergency.getPolice_stations(),emergency.getTitle(),emergency.getId());
            }
        }
    }

    private void initMapList(List<CommonHelp> commonHelpList, String title, String id) {
        Logger.d("commonHelpList>>", commonHelpList.toString() + "");
        Logger.e("<<<commonHelpList>>",commonHelpList.toString());
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        mMap.clear();
        if (mSelectedMarker != null) {
            mSelectedMarker.remove();
        }
        String commonValue ="";
        for (CommonHelp commonHelp : commonHelpList) {

            if (id.equalsIgnoreCase("1") || title.equalsIgnoreCase("Doctors")) {
                commonValue = AppUtil.optStringNullCheckValue(commonHelp.getSpecialized())+ "\n"+ AppUtil.optStringNullCheckValue(commonHelp.getChamber()) + "\n"+ AppUtil.optStringNullCheckValue(commonHelp.getPhone());
            } else if (id.equalsIgnoreCase("2") || title.equalsIgnoreCase("Hospitals")) {
                commonValue = AppUtil.optStringNullCheckValue(commonHelp.getAddress())+ "\n"+ AppUtil.optStringNullCheckValue(commonHelp.getEmail()) + "\n"+ AppUtil.optStringNullCheckValue(commonHelp.getPhone());
            } else if (id.equalsIgnoreCase("3") || title.equalsIgnoreCase("Ambulances")) {
                commonValue = AppUtil.optStringNullCheckValue(commonHelp.getAddress())+"\n"+ AppUtil.optStringNullCheckValue(commonHelp.getPhone());
            } else if (id.equalsIgnoreCase("4") || title.equalsIgnoreCase("Fire Services")) {
                commonValue = AppUtil.optStringNullCheckValue(commonHelp.getPhone())+"\n"+ AppUtil.optStringNullCheckValue(commonHelp.getAddress());
            } else if (id.equalsIgnoreCase("5") || title.equalsIgnoreCase("Police Stations")) {
                commonValue = AppUtil.optStringNullCheckValue(commonHelp.getAddress())+"\n"+ AppUtil.optStringNullCheckValue(commonHelp.getPhone());
            }
//            mMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(Float.parseFloat(commonHelp.getLatitude()), Float.parseFloat(commonHelp.getLongitude())))
//                    .title(commonHelp.getName())
//                    .snippet("My Snippet"+"\n"+"1st Line Text"+"\n"+"2nd Line Text"+"\n"+"3rd Line Text"));

            MarkerOptions markerOptions = new MarkerOptions();

            // Setting latitude and longitude for the marker
            markerOptions.position(new LatLng(Float.parseFloat(commonHelp.getLatitude()), Float.parseFloat(commonHelp.getLongitude())));
            markerOptions.title(commonHelp.getName());
            markerOptions.snippet(commonValue);

            // Adding the marker to the map
            mMap.addMarker(markerOptions).showInfoWindow();

            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    LinearLayout info = new LinearLayout(getActivity());
                    info.setOrientation(LinearLayout.VERTICAL);

                    TextView title = new TextView(getActivity());
                    title.setTextColor(Color.BLACK);
                    title.setGravity(Gravity.CENTER);
                    title.setTypeface(null, Typeface.BOLD);
                    title.setText(marker.getTitle());

                    TextView snippet = new TextView(getActivity());
                    snippet.setTextColor(ContextCompat.getColor(getActivity(), R.color.color_grey_700));
                    title.setGravity(Gravity.CENTER);
                    title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    snippet.setText(marker.getSnippet());

                    info.addView(title);
                    info.addView(snippet);

                    return info;
                }
            });
            mMap.addMarker(markerOptions);
            builder.include(new LatLng(Float.parseFloat(commonHelp.getLatitude()), Float.parseFloat(commonHelp.getLongitude())));


        }
        LatLngBounds bounds = builder.build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
    }

    /************************
     * Server communication *
     ************************/
    private class GetEmergencyHelpTask extends AsyncTask<String, Integer, Response> {

        Context mContext;

        private GetEmergencyHelpTask(Context context) {
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
                Call<APIResponse<CommonMedicinesByItem>> call = mApiInterface.apiGetEmergencyHelpList();
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
                    Logger.d(TAG, "APIResponse(GetEmergencyHelpTask): onResponse-server = " + result.toString());
                    APIResponse<CommonMedicinesByItem> data = (APIResponse<CommonMedicinesByItem>) result.body();
                    Logger.d("GetEmergencyHelpTask", data.toString() + "");

                    if (data != null) {
                        Logger.d(TAG, "APIResponse(GetEmergencyHelpTask()): onResponse-object = " + data.toString());
                        initEmergencyHelpList(data.getData().getErmergency());
                        mEmergency = data.getData().getErmergency().get(0);
                        emergencyHelpList(mEmergency);
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