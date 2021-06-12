package com.meembusoft.safewaypharmaonline.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.daimajia.slider.library.Animations.NoDescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.jude.easyrecyclerview.waveswiperefresh.WaveSwipeRefreshLayout;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.AllVideoListActivity;
import com.meembusoft.safewaypharmaonline.activity.CategoryActivity2;
import com.meembusoft.safewaypharmaonline.activity.CollectionActivity;
import com.meembusoft.safewaypharmaonline.activity.HomeActivity;
import com.meembusoft.safewaypharmaonline.activity.SeeMoreActivity;
import com.meembusoft.safewaypharmaonline.adapter.BaseSellerListAdapter;
import com.meembusoft.safewaypharmaonline.adapter.CollectionListAdapter;
import com.meembusoft.safewaypharmaonline.adapter.HomeCategoryListAdapter;
import com.meembusoft.safewaypharmaonline.adapter.ShopStaggeredListAdapter;
import com.meembusoft.safewaypharmaonline.adapter.StaggeredListAdapter;
import com.meembusoft.safewaypharmaonline.adapter.VideoListAdapter;
import com.meembusoft.safewaypharmaonline.adapter.WhyChooseUsListAdapter;
import com.meembusoft.safewaypharmaonline.base.BaseFragment;
import com.meembusoft.safewaypharmaonline.enumeration.CategoryType;
import com.meembusoft.safewaypharmaonline.enumeration.FlavorType;
import com.meembusoft.safewaypharmaonline.enumeration.LoginSettingType;
import com.meembusoft.safewaypharmaonline.enumeration.SearchType;
import com.meembusoft.safewaypharmaonline.model.AppSupplier;
import com.meembusoft.safewaypharmaonline.model.AppUser;
import com.meembusoft.safewaypharmaonline.model.Category;
import com.meembusoft.safewaypharmaonline.model.CommonData;
import com.meembusoft.safewaypharmaonline.model.CommonHomeObject;
import com.meembusoft.safewaypharmaonline.model.CommonMedicinesByItem;
import com.meembusoft.safewaypharmaonline.model.Home;
import com.meembusoft.safewaypharmaonline.model.Slider;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.model.Videos;
import com.meembusoft.safewaypharmaonline.retrofit.APIClient;
import com.meembusoft.safewaypharmaonline.retrofit.APIInterface;
import com.meembusoft.safewaypharmaonline.retrofit.APIResponse;
import com.meembusoft.safewaypharmaonline.util.AllConstants;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.util.SessionUtil;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_CATEGORY_ID;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_MEDICINE_BY_CATEGORY_TYPE;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_REQUEST_CODE_USER_LOGIN;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class HomeFragment extends BaseFragment {

    // TextView
    private TextView tvShopNow, tvBaseSellerSeeAll, tvGeneralMedicineSeeMore, tvMedicalInstrumentSeeMore, tvMedicalCosmeticSeeMore, tvHerbalMedicineSeeMore, tvVideoHeadingTitle, tvVideoSeeMore, tvBabyFoodStationarySeeMore, tvCategorySeeMore,
            tvCollectionShopMore, tvMedicalSupportSeeMore, tvOpticsLensSeeMore, tvShopSeeAll;
    // Recycler view
    private RecyclerView rvBaseSellerProduct, rvCategory, rvMedicineByCategory, rvCollection, rvMedicalInstrumentByCategory, rvHerbalMedicineByCategory, rvMedicalCosmeticsCategory, rvWhyChooseUs, rvBabyFoodStationaryCategory, rvMedicalSupport, rvOpticsLens;
    private RecyclerView rvShopStaggered;
    private NestedScrollView nestedScrollView;
    private HomeCategoryListAdapter categoryListAdapter;
    private VideoListAdapter videoListAdapter;
    private StaggeredListAdapter staggeredListAdapter;
    private BaseSellerListAdapter baseSellerListAdapter;
    private CollectionListAdapter collectionListAdapter;
    private WhyChooseUsListAdapter whyChooseUsListAdapter;
    private ShopStaggeredListAdapter mShopStaggeredListAdapter;

    private YouTubePlayerView mYouTubePlayerView;
    private YouTubePlayer mYouTubePlayer;
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    //Image slider
    private SliderLayout mDemoSlider;
    private TextSliderView mCurrentSliderView;
    //More loading
    private int mOffset = 0, mItemLimit = AllConstants.PER_PAGE_ITEM, mTotalcount = 0;
    private Handler mHandler = new Handler();
    //Background task
    private GetAllAPIHome getAllAPIHome;
    private GetAllProductMedicinesTask getShopAllProductMedicinesTask;

    private APIInterface mApiInterface;
    private String generalMedicineId = "", medicalInstrumentId = "", herbalMedicineId = "", medicalCosmeticId = "", babyFoodStatioaryId = "", medicalSupportId = "", opticsLensId = "";
    // ArrayList
    List<StaggeredMedicineByItem> staggeredGeneralMedicine;
    List<StaggeredMedicineByItem> staggeredMedicalInstrument;
    List<StaggeredMedicineByItem> staggeredHerbalMedicine;
    List<StaggeredMedicineByItem> staggeredMedicalCosmetics;
    List<StaggeredMedicineByItem> staggeredBabyFoodStatioary;
    List<StaggeredMedicineByItem> bestSellerLists;
    List<StaggeredMedicineByItem> staggeredMedicalSupport;
    List<StaggeredMedicineByItem> staggeredOpticsLens;

    List<Videos> videosList;
    List<CommonData> collectionList;
    AppUser mAppUser;
    private String customerId = "";
    private String selectUserType = "customer";

    //  Supplier
    private AppSupplier mAppSupplier;
    FlavorType flavorType;
    private boolean isRefreshLoader = true;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public int initFragmentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void initFragmentBundleData(Bundle bundle) {

    }

    @Override
    public void initFragmentViews(View parentView) {
        nestedScrollView = (NestedScrollView) parentView.findViewById(R.id.nested_scrollview);
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) parentView.findViewById(R.id.main_swipe);
        mDemoSlider = (SliderLayout) parentView.findViewById(R.id.home_img_slider);
        rvBaseSellerProduct = (RecyclerView) parentView.findViewById(R.id.rv_base_seller_product);
        rvCategory = (RecyclerView) parentView.findViewById(R.id.rv_category);
        rvCollection = (RecyclerView) parentView.findViewById(R.id.rv_collection);
        rvMedicineByCategory = (RecyclerView) parentView.findViewById(R.id.rv_medicine_by_category);
        rvMedicalInstrumentByCategory = (RecyclerView) parentView.findViewById(R.id.rv_medical_instrument_by_category);
        rvHerbalMedicineByCategory = (RecyclerView) parentView.findViewById(R.id.rv_herbal_medicine_by_category);
        rvMedicalCosmeticsCategory = (RecyclerView) parentView.findViewById(R.id.rv_medical_cosmetic_by_category);
        rvBabyFoodStationaryCategory = (RecyclerView) parentView.findViewById(R.id.rv_baby_food_stationary_category);
        rvMedicalSupport = (RecyclerView) parentView.findViewById(R.id.rv_medical_support);
        rvOpticsLens = (RecyclerView) parentView.findViewById(R.id.rv_optics_lens);
        rvWhyChooseUs = (RecyclerView) parentView.findViewById(R.id.rv_why_choose_us);
        rvShopStaggered = (RecyclerView) parentView.findViewById(R.id.rv_shop_staggered);
        // rvShopStaggered = (EasyRecyclerView) parentView.findViewById(R.id.rv_shop_staggered);
        tvShopNow = (TextView) parentView.findViewById(R.id.tv_shop_now);
        tvBaseSellerSeeAll = (TextView) parentView.findViewById(R.id.tv_base_seller_see_all);
        tvGeneralMedicineSeeMore = (TextView) parentView.findViewById(R.id.tv_general_medicine_see_more);
        tvMedicalInstrumentSeeMore = (TextView) parentView.findViewById(R.id.tv_medical_instrument_see_more);
        tvCategorySeeMore = (TextView) parentView.findViewById(R.id.tv_category_see_more);
        tvHerbalMedicineSeeMore = (TextView) parentView.findViewById(R.id.tv_herbal_medicine_see_more);
        tvVideoHeadingTitle = (TextView) parentView.findViewById(R.id.tv_video_heading_title);
        tvVideoSeeMore = (TextView) parentView.findViewById(R.id.tv_video_see_more);
        tvBabyFoodStationarySeeMore = (TextView) parentView.findViewById(R.id.tv_baby_food_stationary_see_more);
        tvMedicalCosmeticSeeMore = (TextView) parentView.findViewById(R.id.tv_medical_cosmetic_see_more);
        tvCollectionShopMore = (TextView) parentView.findViewById(R.id.tv_collection_shop_more);
        tvMedicalSupportSeeMore = (TextView) parentView.findViewById(R.id.tv_medical_support_see_more);
        tvOpticsLensSeeMore = (TextView) parentView.findViewById(R.id.tv_optics_lens_see_more);
//        tvShopSeeAll = (TextView) parentView.findViewById(R.id.tv_shop_see_all);
        mYouTubePlayerView = parentView.findViewById(R.id.youtube_player_view);

    }

    @Override
    public void initFragmentViewsData() {
        mAppUser = SessionUtil.getUser(getActivity());
        mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        // Get User Tag
        String userType = SessionManager.getStringSetting(getActivity(), SessionUtil.SESSION_KEY_USER_TAG);
        if (!AllSettingsManager.isNullOrEmpty(userType)) {
            flavorType = FlavorType.getFlavor(SessionManager.getStringSetting(getActivity(), SessionUtil.SESSION_KEY_USER_TAG));
        } else {
            flavorType = FlavorType.CUSTOMER;
        }
        //shop recycler view
        initShopList();

        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
            loadOfflineHomeData();
        } else {
//            // Process home api
//            String offlineHomeData = "";
//            if (!AllSettingsManager.isNullOrEmpty(SessionManager.getStringSetting(getActivity(), AllConstants.SESSION_KEY_HOME))) {
//                offlineHomeData = SessionManager.getStringSetting(getActivity(), AllConstants.SESSION_KEY_HOME);
//                Logger.d(TAG, "HomeData(Session): " + offlineHomeData);
//                //Load home data
//                // ResponseOfflineHome responseOfflineHome = APIResponse.getResponseObject(offlineHomeData, ResponseOfflineHome.class);
//                Home responseHOme = APIResponse.getResponseObject(offlineHomeData, Home.class);
//                initAllHomeListShow(responseHOme);
//            } else {
//                if (mAppUser != null) {
//                    customerId = mAppUser.getId();
//                }
//                Logger.d(TAG, TAG + " >>> " + "customerId " + customerId);
//
//                getAllAPIHome = new GetAllAPIHome(getActivity(), customerId);
//                getAllAPIHome.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            }

            if (mAppUser != null) {
                customerId = mAppUser.getId();
            }
                if (getAllAPIHome != null && getAllAPIHome.getStatus() == AsyncTask.Status.RUNNING) {
                    getAllAPIHome.cancel(true);
                }

            if (flavorType != null) {
                switch (flavorType) {
                    case CUSTOMER:
                        selectUserType = AllConstants.USER_TYPE_CUSTOMER;
                        break;
                    case SUPPLIER:
                        mAppSupplier = SessionUtil.getSupplier(getActivity());
                        selectUserType = AllConstants.USER_TYPE_SUPPLIER;
                        if (mAppSupplier != null) {
                            customerId = mAppSupplier.getUser_id();
                        }
                        break;
                }
                Logger.d(TAG, TAG + " >>> " + "userType " + userType);
                Logger.d(TAG, TAG + " >>> " + "customerId " + customerId);
            }
            getAllAPIHome = new GetAllAPIHome(getActivity(), customerId);
            getAllAPIHome.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            // Process shop api
            if (getShopAllProductMedicinesTask != null && getShopAllProductMedicinesTask.getStatus() == AsyncTask.Status.RUNNING) {
                Logger.d(TAG, TAG + " >>> " + "setData: canceling asynctask");
                getShopAllProductMedicinesTask.cancel(true);
            }
            getShopAllProductMedicinesTask = new GetAllProductMedicinesTask(getActivity(),customerId);
            getShopAllProductMedicinesTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        initWaveSwipeRefreshLayout();
    }

    @Override
    public void initFragmentActions() {
        tvBaseSellerSeeAll.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent iBaseSellerMedicineByCategoryShowAll = new Intent(getActivity(), SeeMoreActivity.class);
                iBaseSellerMedicineByCategoryShowAll.putExtra(INTENT_KEY_MEDICINE_BY_CATEGORY_TYPE, CategoryType.BASE_SELLER.name());
                getActivity().startActivity(iBaseSellerMedicineByCategoryShowAll);
            }
        });

        tvCategorySeeMore.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intentCategory = new Intent(getActivity(), CategoryActivity2.class);
                getActivity().startActivity(intentCategory);
            }
        });

        tvCollectionShopMore.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intentCollection = new Intent(getActivity(), CollectionActivity.class);
//                intentCollection.putExtra(INTENT_KEY_COLLECTION, Parcels.wrap(collectionList));
                getActivity().startActivity(intentCollection);
            }
        });

        tvGeneralMedicineSeeMore.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (staggeredGeneralMedicine != null && staggeredGeneralMedicine.size() > 0) {
                    Intent iMedicineByCategoryShowAll = new Intent(getActivity(), SeeMoreActivity.class);
                    iMedicineByCategoryShowAll.putExtra(INTENT_KEY_CATEGORY_ID, generalMedicineId);
                    iMedicineByCategoryShowAll.putExtra(INTENT_KEY_MEDICINE_BY_CATEGORY_TYPE, CategoryType.GENERAL_MEDICINE.name());
                    //    iMedicineByCategoryShowAll.putExtra(INTENT_KEY_GENERAL_MEDICINE_BY_CATEGORY, Parcels.wrap(staggeredGeneralMedicine));
                    getActivity().startActivity(iMedicineByCategoryShowAll);
                }
            }
        });

        tvMedicalInstrumentSeeMore.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (staggeredMedicalInstrument != null && staggeredMedicalInstrument.size() > 0) {
                    Intent iMedicineByCategoryShowAll = new Intent(getActivity(), SeeMoreActivity.class);
                    iMedicineByCategoryShowAll.putExtra(INTENT_KEY_CATEGORY_ID, medicalInstrumentId);
                    iMedicineByCategoryShowAll.putExtra(INTENT_KEY_MEDICINE_BY_CATEGORY_TYPE, CategoryType.MEDICAL_INSTRUMENT.name());
                    //   iMedicineByCategoryShowAll.putExtra(INTENT_KEY_GENERAL_MEDICINE_BY_CATEGORY, Parcels.wrap(staggeredMedicalInstrument));
                    getActivity().startActivity(iMedicineByCategoryShowAll);
                }
            }
        });


        tvHerbalMedicineSeeMore.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (staggeredHerbalMedicine != null && staggeredHerbalMedicine.size() > 0) {
                    Intent iMedicineByCategoryShowAll = new Intent(getActivity(), SeeMoreActivity.class);
                    iMedicineByCategoryShowAll.putExtra(INTENT_KEY_CATEGORY_ID, herbalMedicineId);
                    iMedicineByCategoryShowAll.putExtra(INTENT_KEY_MEDICINE_BY_CATEGORY_TYPE, CategoryType.HERBAL_MEDICINE.name());
                    //  iMedicineByCategoryShowAll.putExtra(INTENT_KEY_GENERAL_MEDICINE_BY_CATEGORY, Parcels.wrap(staggeredHerbalMedicine));
                    getActivity().startActivity(iMedicineByCategoryShowAll);
                }
            }
        });

        tvMedicalCosmeticSeeMore.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (staggeredMedicalCosmetics != null && staggeredMedicalCosmetics.size() > 0) {
                    Intent iMedicineByCategoryShowAll = new Intent(getActivity(), SeeMoreActivity.class);
                    iMedicineByCategoryShowAll.putExtra(INTENT_KEY_CATEGORY_ID, medicalCosmeticId);
                    iMedicineByCategoryShowAll.putExtra(INTENT_KEY_MEDICINE_BY_CATEGORY_TYPE, CategoryType.COSMETICS.name());
                    //  iMedicineByCategoryShowAll.putExtra(INTENT_KEY_GENERAL_MEDICINE_BY_CATEGORY, Parcels.wrap(staggeredMedicalCosmetics));
                    getActivity().startActivity(iMedicineByCategoryShowAll);
                }
            }
        });

        tvBabyFoodStationarySeeMore.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (staggeredBabyFoodStatioary != null && staggeredBabyFoodStatioary.size() > 0) {
                    Intent iMedicineByCategoryShowAll = new Intent(getActivity(), SeeMoreActivity.class);
                    iMedicineByCategoryShowAll.putExtra(INTENT_KEY_CATEGORY_ID, babyFoodStatioaryId);
                    iMedicineByCategoryShowAll.putExtra(INTENT_KEY_MEDICINE_BY_CATEGORY_TYPE, CategoryType.BABY_FOOD_STATIONARY.name());
                    // iMedicineByCategoryShowAll.putExtra(INTENT_KEY_GENERAL_MEDICINE_BY_CATEGORY, Parcels.wrap(staggeredBabyFoodStatioary));
                    getActivity().startActivity(iMedicineByCategoryShowAll);
                }
            }
        });

        tvMedicalSupportSeeMore.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (staggeredMedicalSupport != null && staggeredMedicalSupport.size() > 0) {
                    Intent iMedicineByCategoryShowAll = new Intent(getActivity(), SeeMoreActivity.class);
                    iMedicineByCategoryShowAll.putExtra(INTENT_KEY_CATEGORY_ID, medicalSupportId);
                    iMedicineByCategoryShowAll.putExtra(INTENT_KEY_MEDICINE_BY_CATEGORY_TYPE, CategoryType.MEDICAL_SUPPORT.name());
                    // iMedicineByCategoryShowAll.putExtra(INTENT_KEY_GENERAL_MEDICINE_BY_CATEGORY, Parcels.wrap(staggeredBabyFoodStatioary));
                    getActivity().startActivity(iMedicineByCategoryShowAll);
                }
            }
        });


        tvOpticsLensSeeMore.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (staggeredOpticsLens != null && staggeredOpticsLens.size() > 0) {
                    Intent iMedicineByCategoryShowAll = new Intent(getActivity(), SeeMoreActivity.class);
                    iMedicineByCategoryShowAll.putExtra(INTENT_KEY_CATEGORY_ID, opticsLensId);
                    iMedicineByCategoryShowAll.putExtra(INTENT_KEY_MEDICINE_BY_CATEGORY_TYPE, CategoryType.OPTICS_LENS.name());
                    // iMedicineByCategoryShowAll.putExtra(INTENT_KEY_GENERAL_MEDICINE_BY_CATEGORY, Parcels.wrap(staggeredBabyFoodStatioary));
                    getActivity().startActivity(iMedicineByCategoryShowAll);
                }
            }
        });


        tvVideoSeeMore.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (mYouTubePlayer != null) {
                    mYouTubePlayer.pause();
                }
                Intent iVideoSeeAll = new Intent(getActivity(), AllVideoListActivity.class);
//                    iVideoSeeAll.putExtra(INTENT_KEY_VIDEO_LIST, Parcels.wrap(videosList));
                getActivity().startActivity(iVideoSeeAll);

            }
        });

        tvShopNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://safewaypharmaonline.com/"));
                startActivity(i);
            }
        });
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!NetworkManager.isConnected(getActivity())) {
                    Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                    // loadOfflineTimeData();
                } else {
                    // Stop slider
                    mDemoSlider.stopAutoCycle();
                    mDemoSlider.removeAllSliders();
                    isRefreshLoader = false;
                    // Call home api
                    if (getAllAPIHome != null && getAllAPIHome.getStatus() == AsyncTask.Status.RUNNING) {
                        getAllAPIHome.cancel(true);
                    }
                    getAllAPIHome = new GetAllAPIHome(getActivity(), customerId);
                    getAllAPIHome.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    // Call medicine api
                    if (getShopAllProductMedicinesTask != null && getShopAllProductMedicinesTask.getStatus() == AsyncTask.Status.RUNNING) {
                        getShopAllProductMedicinesTask.cancel(true);
                    }
                    mOffset = 0;
                    mItemLimit = AllConstants.PER_PAGE_ITEM;
                    mTotalcount = 0;
                    mShopStaggeredListAdapter.clear();
                    initShopList();
                    setShopListData();
                }
            }
        });

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    Logger.d(TAG, "Scroll DOWN");
                }
                if (scrollY < oldScrollY) {
                    Logger.d(TAG, "Scroll UP");
                }

                if (scrollY == 0) {
                    Logger.d(TAG, "TOP SCROLL");
                }

                // For youtube player playing
                Rect mRect = new Rect();
                nestedScrollView.getHitRect(mRect);
                if (mYouTubePlayerView != null) {
                    if (!mYouTubePlayerView.getLocalVisibleRect(mRect) || mRect.height() < mYouTubePlayerView.getHeight()) {
                        // invisible
                        Logger.d(TAG, "mYouTubePlayerView is Partially visible or invisible");
                        if (mYouTubePlayer != null) {
                            mYouTubePlayer.pause();
                        }
                    } else {
                        // visible
                        Logger.d(TAG, "mYouTubePlayerView is Completely visible");
                        if (mYouTubePlayer != null) {
                            mYouTubePlayer.play();
                        }
                    }
                }

                // For more loading
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    Logger.d(TAG, "BOTTOM SCROLL");
                    if (mOffset < mTotalcount) {
                        Logger.d(TAG, TAG + " >>> " + "onLoadMore: started loading");
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setShopListData();
                            }
                        }, 2000);
                    } else {
                        //No more item
                        mShopStaggeredListAdapter.addAll(new ArrayList<StaggeredMedicineByItem>());
                    }
                }
            }
        });
    }

    private void initWaveSwipeRefreshLayout() {
        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setWaveColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    private void initShopList() {
        mShopStaggeredListAdapter = new ShopStaggeredListAdapter(getActivity(), SearchType.REGULAR );
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        rvShopStaggered.setLayoutManager(staggeredGridLayoutManager);
        rvShopStaggered.setAdapter(mShopStaggeredListAdapter);
    }

    private void setShopListData() {
        Activity activity = getActivity();
        if (activity != null && isAdded()) {
            if (!NetworkManager.isConnected(getActivity())) {
                ((HomeActivity) getActivity()).dismissProgressDialog();
                //This is for more loading
                if (mShopStaggeredListAdapter != null) {
                    mShopStaggeredListAdapter.pauseMore();
                }
                //   tvSuggestion.setText(getString(R.string.view_please_connect_internet_and_retry));
                Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                return;
            } else {
                if (getShopAllProductMedicinesTask != null && getShopAllProductMedicinesTask.getStatus() == AsyncTask.Status.RUNNING) {
                    Logger.d(TAG, TAG + " >>> " + "setData: canceling asynctask");
                    getShopAllProductMedicinesTask.cancel(true);
                }

                getShopAllProductMedicinesTask = new GetAllProductMedicinesTask(getActivity(),customerId);
                getShopAllProductMedicinesTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
    }

    private void loadOfflineHomeData() {
        String offlineHomeData = "";
        if (!AllSettingsManager.isNullOrEmpty(SessionManager.getStringSetting(getActivity(), AllConstants.SESSION_KEY_HOME))) {
            offlineHomeData = SessionManager.getStringSetting(getActivity(), AllConstants.SESSION_KEY_HOME);
            Logger.d(TAG, "HomeData(Session): " + offlineHomeData);
        } else {
            offlineHomeData = AllConstants.DEFAULT_HOME_LIST;
            Logger.d(TAG, "HomeData(default): " + offlineHomeData);
        }

        //Load home data
        Home responseHOme = APIResponse.getResponseObject(offlineHomeData, Home.class);
        initAllHomeListShow(responseHOme);
    }

    // slider image data
    private void initImageSlider(List<Slider> sliderImagesData) {
        Logger.d(TAG, TAG + " >>> " + "sliderImagesData:" + sliderImagesData.toString());

        if (sliderImagesData.size() > 0 && sliderImagesData != null) {
            for (final Slider image : sliderImagesData) {
                TextSliderView textSliderView = new TextSliderView(getActivity());
                // initialize a SliderLayout
                textSliderView
                        .description(image.getSlider_id())
                        .descriptionVisibility(View.GONE)
                        .image(image.getSlider_img())
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                            @Override
                            public void onSliderClick(BaseSliderView slider) {
                                Logger.d(TAG, TAG + " >>> " + "getCategory_id:" + image.getCategory_id());
                                Intent intentCategory = new Intent(getContext(), CategoryActivity2.class);
                                intentCategory.putExtra(INTENT_KEY_CATEGORY_ID, image.getCategory_id());
                                getActivity().startActivity(intentCategory);
                            }
                        });

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra", image.getSlider_id());

                mDemoSlider.addSlider(textSliderView);
            }
            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            mDemoSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Visible);
            mDemoSlider.setCustomAnimation(new NoDescriptionAnimation());
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setDuration(5000);

            mDemoSlider.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    BaseSliderView baseSliderView = mDemoSlider.getCurrentSlider();
                    if (baseSliderView != null && baseSliderView instanceof TextSliderView) {
                        mCurrentSliderView = (TextSliderView) baseSliderView;

                        ((HomeActivity) getActivity()).setThemeColor(mCurrentSliderView.getTargetImageView());
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    public TextSliderView getCurrentSliderView() {
        return mCurrentSliderView;
    }

    @Override
    public void initFragmentBackPress() {

    }

    @Override
    public void initFragmentOnResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case INTENT_REQUEST_CODE_USER_LOGIN:
                Logger.d(TAG, TAG + " >>> " + "INTENT_REQUEST_CODE_LOGIN:FRAGMENT " + requestCode);
                LoginSettingType loginSettingType;
                if (data != null && resultCode == RESULT_OK) {
                    String mParcelableCategoryType = data.getStringExtra(AllConstants.INTENT_KEY_LOGIN_SETTING_TYPE);
                    if (!AllSettingsManager.isNullOrEmpty(mParcelableCategoryType)) {
                        loginSettingType = LoginSettingType.valueOf(mParcelableCategoryType);
                        Logger.d(TAG, TAG + " >>> " + "loginType: " + loginSettingType);
                    }
                }
        }
    }

    @Override
    public void initFragmentUpdate(Object object) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Image slider
        if (mDemoSlider != null) {
            mDemoSlider.stopAutoCycle();
            mDemoSlider.removeAllSliders();
        }
        if (mHandler != null) {
            mHandler.removeCallbacks(null);
        }
        if (getAllAPIHome != null && getAllAPIHome.getStatus() == AsyncTask.Status.RUNNING) {
            getAllAPIHome.cancel(true);
        }
        if (getShopAllProductMedicinesTask != null && getShopAllProductMedicinesTask.getStatus() == AsyncTask.Status.RUNNING) {
            getShopAllProductMedicinesTask.cancel(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).setThemeColor(null);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mYouTubePlayer != null) {
            mYouTubePlayer.pause();
        }
    }

    // All home list show
    private void initAllHomeListShow(Home homeDataObj) {
        if (homeDataObj != null) {
            initImageSlider(homeDataObj.getSlider());
            bestSellerLists = homeDataObj.getBest_seller();
            initBaseSellerProduct(bestSellerLists);
            initGeneralMedicineByCategoryRv(homeDataObj.getGeneral_medicine());
            collectionList = (homeDataObj.getCollection());
            initCollection(collectionList);
            initMedicineInstrumentRv(homeDataObj.getMedical_instrument());
            initCategoryRv(homeDataObj.getCategories());
            initHervalMedicineRv(homeDataObj.getHerbal_yunani_medicine());
            initAddVideoYoutubePlayer(homeDataObj.getVideo());
            initCosmeticsRv(homeDataObj.getMedicated_cosmetics());
            initWhyChooseUs(homeDataObj.getWhy_choose_us());
            initBabyFoodStationryRv(homeDataObj.getBaby_food_stationary());
            initMedicalSupportRv(homeDataObj.getMedical_support());
            initOpticsLensRv(homeDataObj.getOptics_lens());
        }

    }


    // General Medicine List
    private void initGeneralMedicineByCategoryRv(CommonHomeObject data) {
        if (data != null) {
            staggeredGeneralMedicine = data.getItem();
            generalMedicineId = data.getId();
            Logger.d("GeneralMedicineByCategory",  data.getItem().toString() + "");

            if (staggeredGeneralMedicine.size() > 0 && staggeredGeneralMedicine != null) {
                staggeredListAdapter = new StaggeredListAdapter(getActivity(), CategoryType.GENERAL_MEDICINE,flavorType);
                rvMedicineByCategory.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                rvMedicineByCategory.setAdapter(staggeredListAdapter);
                staggeredListAdapter.setAllData(staggeredGeneralMedicine, 6);
            }
        }
    }

    // Medical Instrument List
    private void initMedicineInstrumentRv(CommonHomeObject data) {
        if (data != null) {
            staggeredMedicalInstrument = data.getItem();
            medicalInstrumentId = data.getId();
            if (staggeredMedicalInstrument.size() > 0 && staggeredMedicalInstrument != null) {
                staggeredListAdapter = new StaggeredListAdapter(getActivity(), CategoryType.MEDICAL_INSTRUMENT,flavorType);
                rvMedicalInstrumentByCategory.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                rvMedicalInstrumentByCategory.setAdapter(staggeredListAdapter);
                staggeredListAdapter.setAllData(staggeredMedicalInstrument, 6);
            }
        }
    }

    // Category
    private void initCategoryRv(List<Category> categoryList) {
        List<Category> mCategoryList = AppUtil.getFourCategoryList(getActivity(), categoryList);
        if (mCategoryList.size() > 0 && mCategoryList != null) {
            categoryListAdapter = new HomeCategoryListAdapter(getActivity());
            rvCategory.setLayoutManager(new GridLayoutManager(getActivity(), 4));
            //  rvCategory.setLayoutManager(new LinearLayoutManager(getActivity(), HORIZONTAL, false));
            rvCategory.setAdapter(categoryListAdapter);
            categoryListAdapter.addAll(mCategoryList);
        }
    }

    // Herval Medicine List
    private void initHervalMedicineRv(CommonHomeObject data) {
        if (data != null) {
            staggeredHerbalMedicine = data.getItem();
            herbalMedicineId = data.getId();
            if (staggeredHerbalMedicine.size() > 0 && staggeredHerbalMedicine != null) {
                staggeredListAdapter = new StaggeredListAdapter(getActivity(), CategoryType.HERBAL_MEDICINE,flavorType);
                rvHerbalMedicineByCategory.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                rvHerbalMedicineByCategory.setAdapter(staggeredListAdapter);
                staggeredListAdapter.setAllData(staggeredHerbalMedicine, 6);
            }
        }
    }

    // Baby Food Stationry List
    private void initBabyFoodStationryRv(CommonHomeObject data) {
        if (data != null) {
            staggeredBabyFoodStatioary = data.getItem();
            babyFoodStatioaryId = data.getId();
            if (staggeredBabyFoodStatioary.size() > 0 && staggeredBabyFoodStatioary != null) {
                staggeredListAdapter = new StaggeredListAdapter(getActivity(), CategoryType.BABY_FOOD_STATIONARY,flavorType);
                rvBabyFoodStationaryCategory.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                rvBabyFoodStationaryCategory.setAdapter(staggeredListAdapter);
                staggeredListAdapter.setAllData(staggeredBabyFoodStatioary, 6);
            }
        }
    }

    // Medical Cosmetics List
    private void initCosmeticsRv(CommonHomeObject data) {
        if (data != null) {
            staggeredMedicalCosmetics = data.getItem();
            medicalCosmeticId = data.getId();
            if (staggeredMedicalCosmetics.size() > 0 && staggeredMedicalCosmetics != null) {
                staggeredListAdapter = new StaggeredListAdapter(getActivity(), CategoryType.COSMETICS,flavorType);
                rvMedicalCosmeticsCategory.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                rvMedicalCosmeticsCategory.setAdapter(staggeredListAdapter);
                staggeredListAdapter.setAllData(staggeredMedicalCosmetics, 6);
            }
        }
    }

    // Collection List
    private void initCollection(List<CommonData> collectionLists) {
        if (collectionLists.size() > 0 && collectionLists != null) {
            collectionListAdapter = new CollectionListAdapter(getActivity());
            rvCollection.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
            rvCollection.setAdapter(collectionListAdapter);
            collectionListAdapter.addAll(collectionLists);
        }
    }

    // Why Choose Us List
    private void initWhyChooseUs(List<CommonData> whyChooseUsList) {
        List<CommonData> whyChooseUsByItemList = AppUtil.getFourWhyChooseList(getActivity(), whyChooseUsList);

        if (whyChooseUsByItemList.size() > 0 && whyChooseUsByItemList != null) {
            whyChooseUsListAdapter = new WhyChooseUsListAdapter(getActivity());
            rvWhyChooseUs.setLayoutManager(new GridLayoutManager(getActivity(), 4));
            rvWhyChooseUs.setAdapter(whyChooseUsListAdapter);
            whyChooseUsListAdapter.addAll(whyChooseUsByItemList);
        }
    }

    // Base Seller List
    private void initBaseSellerProduct(List<StaggeredMedicineByItem> bestSellerList) {
        List<StaggeredMedicineByItem> bestSellerByItemList = AppUtil.getFourMedicineByItemsList(getActivity(), bestSellerList);
        if (bestSellerByItemList.size() > 0 && bestSellerByItemList != null) {
            baseSellerListAdapter = new BaseSellerListAdapter(getActivity());
            rvBaseSellerProduct.setLayoutManager(new GridLayoutManager(getActivity(), 4));
            rvBaseSellerProduct.setAdapter(baseSellerListAdapter);
            baseSellerListAdapter.addAll(bestSellerByItemList);
        }
    }

    // Medical Support List
    private void initMedicalSupportRv(CommonHomeObject data) {
        if (data != null) {
            staggeredMedicalSupport = data.getItem();
            medicalSupportId = data.getId();
            if (staggeredMedicalSupport.size() > 0 && staggeredMedicalSupport != null) {
                staggeredListAdapter = new StaggeredListAdapter(getActivity(), CategoryType.MEDICAL_SUPPORT,flavorType);
                rvMedicalSupport.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                rvMedicalSupport.setAdapter(staggeredListAdapter);
                staggeredListAdapter.setAllData(staggeredMedicalSupport, 6);
            }
        }
    }

    // Optics Lens List
    private void initOpticsLensRv(CommonHomeObject data) {
        if (data != null) {
            staggeredOpticsLens = data.getItem();
            opticsLensId = data.getId();
            if (staggeredOpticsLens.size() > 0 && staggeredOpticsLens != null) {
                staggeredListAdapter = new StaggeredListAdapter(getActivity(), CategoryType.OPTICS_LENS,flavorType);
                rvOpticsLens.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                rvOpticsLens.setAdapter(staggeredListAdapter);
                staggeredListAdapter.setAllData(staggeredOpticsLens, 6);
            }
        }
    }

    // Video Youtube Player
    private void initAddVideoYoutubePlayer(final Videos video) {
        tvVideoHeadingTitle.setText(video.getTitle());
        getLifecycle().addObserver(mYouTubePlayerView);
        Logger.d("getVideo_link", video.getVideo_link() + "");

        mYouTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                mYouTubePlayer = youTubePlayer;
                if (video != null) {
//                String videoId = "r8kE7rSzfQs";
                    String videoId = video.getVideo_link();
                    if (!TextUtils.isEmpty(videoId)) {
                        youTubePlayer.loadVideo(videoId, 0);
                        youTubePlayer.pause();
                    }
                }
            }
        });
    }

    /************************
     * Server communication *
     ************************/
    private class GetAllAPIHome extends AsyncTask<String, Integer, Response> {

        Context mContext;
        private String mCustomerId;

        private GetAllAPIHome(Context context, String customerID) {
            mContext = context;
            mCustomerId = customerID;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Logger.d(TAG, TAG + ">> Background task is cancelled");
        }

        @Override
        protected Response doInBackground(String... params) {
            try {
                Call<APIResponse<Home>> call = mApiInterface.apiAllGetHome(mCustomerId,selectUserType);
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
                mWaveSwipeRefreshLayout.setRefreshing(false);

                if (result != null && result.isSuccessful()) {
                    Logger.d(TAG, "APIResponse(GetAllAPIHome): onResponse-server = " + result.toString());
                    APIResponse<Home> data = (APIResponse<Home>) result.body();
                    Logger.d("GetAllAPIHome", data.toString() + "");

                    if (data != null) {
                        Logger.d(TAG, "APIResponse(GetAllAPIHome()): onResponse-object = " + data.toString());
                        Home homeDataObj = data.getData();
                        //Store home data into the session
                        SessionManager.setStringSetting(getActivity(), AllConstants.SESSION_KEY_HOME, APIResponse.getResponseString(homeDataObj));
                        initAllHomeListShow(homeDataObj);
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.toast_no_info_found) + ",\n" + getResources().getString(R.string.toast_loaded_offline_data), Toast.LENGTH_SHORT).show();
                        loadOfflineHomeData();
                    }

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info) + ",\n" + getResources().getString(R.string.toast_loaded_offline_data), Toast.LENGTH_SHORT).show();
                    loadOfflineHomeData();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private class GetAllProductMedicinesTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        private String mCustomerId;

        private GetAllProductMedicinesTask(Context context,  String customerID) {
            mContext = context;
            mCustomerId = customerID;
        }

        @Override
        protected void onPreExecute() {

            if (mOffset == 0) {
//                ProgressDialog progressDialog = ((HomeActivity) getActivity()).showProgressDialog();
//                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        cancel(true);
//                    }
//                });
                if (isRefreshLoader) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showPopupDialog();
                        }
                    }, 50);
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Logger.d(TAG, TAG + ">> Background task is cancelled");
        }

        @Override
        protected Response doInBackground(String... params) {
            try {
                Call<APIResponse<CommonMedicinesByItem>> call = mApiInterface.apiGetAllProductMedicinesList(mOffset, mItemLimit,selectUserType,mCustomerId);
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
//                ((HomeActivity) getActivity()).dismissProgressDialog();
                dismissPopupDialog();

                if (result != null && result.isSuccessful()) {
                    Logger.d(TAG, "APIResponse(GetAllProductMedicinesTask): onResponse-server = " + result.toString());
                    APIResponse<CommonMedicinesByItem> data = (APIResponse<CommonMedicinesByItem>) result.body();
                    Logger.d("GetAllProductMedicinesTask", data.toString() + "");

                    if (data != null && data.getData().getMedicines().size() > 0) {
                        Logger.d(TAG, "APIResponse(GetAllProductMedicinesTask()): onResponse-object = " + data.toString());
                        mShopStaggeredListAdapter.addAll(data.getData().getMedicines());
                        // initShopStaggeredList(data.getData().getMedicines());
                        //This is needed for more loading
                        mOffset = mOffset + AllConstants.PER_PAGE_ITEM;
                        mItemLimit = mItemLimit + AllConstants.PER_PAGE_ITEM;
                        mTotalcount = data.getData().getTotals_count();

                        Logger.d("mTotalcount", mTotalcount + "");
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