package com.meembusoft.safewaypharmaonline.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.adapter.ShopStaggeredListAdapter;
import com.meembusoft.safewaypharmaonline.base.BaseActivity;
import com.meembusoft.safewaypharmaonline.enumeration.FlavorType;
import com.meembusoft.safewaypharmaonline.enumeration.SearchType;
import com.meembusoft.safewaypharmaonline.model.AppSupplier;
import com.meembusoft.safewaypharmaonline.model.AppUser;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.persistentsearch.AnimationUtils;
import com.meembusoft.safewaypharmaonline.retrofit.APIClient;
import com.meembusoft.safewaypharmaonline.retrofit.APIInterface;
import com.meembusoft.safewaypharmaonline.retrofit.APIResponse;
import com.meembusoft.safewaypharmaonline.util.AllConstants;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.util.SearchDataProvider;
import com.meembusoft.safewaypharmaonline.util.SessionUtil;
import com.paulrybitskyi.persistentsearchview.PersistentSearchView;
import com.paulrybitskyi.persistentsearchview.adapters.model.SuggestionItem;
import com.paulrybitskyi.persistentsearchview.listeners.OnSearchConfirmedListener;
import com.paulrybitskyi.persistentsearchview.listeners.OnSearchQueryChangeListener;
import com.paulrybitskyi.persistentsearchview.listeners.OnSuggestionChangeListener;
import com.paulrybitskyi.persistentsearchview.utils.SuggestionCreationUtil;
import com.paulrybitskyi.persistentsearchview.utils.VoiceRecognitionDelegate;
import com.paulrybitskyi.sample.utils.HeaderedRecyclerViewListener;
import com.reversecoder.library.network.NetworkManager;
import com.reversecoder.library.storage.SessionManager;
import com.reversecoder.library.util.AllSettingsManager;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SearchActivity extends BaseActivity {

    private String TAG = "SearchActivity";
    private Toolbar toolbar;
    private ActionBar actionBar;
    private EditText etSearch;
    private ImageButton ibClear;
    //Background task
    private APIInterface mApiInterface;
    private GetMedicineSearchTask getMedicineSearchTask;
    private PersistentSearchView persistentSearchView;
    private ProgressBar progressBar;
    private LinearLayout emptyViewLl;
    private RecyclerView recyclerView;
    private ShopStaggeredListAdapter mShopStaggeredListAdapter;
    private String mBarcode = "";
    private SearchType mSearchType = SearchType.REGULAR;
    AppUser mAppUser = null;
    //  Supplier
    private AppSupplier mAppSupplier;
    FlavorType mFlavorType;
    private String customerOrSupplierId = "";
    private String selectUserType = AllConstants.USER_TYPE_CUSTOMER;
    ;

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }

    @Override
    public int initActivityLayout() {
        return R.layout.activity_search;
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
            String barcode = intent.getStringExtra(AllConstants.INTENT_KEY_SEARCH_KEYWORD);
            if (!AllSettingsManager.isNullOrEmpty(barcode)) {
                mBarcode = barcode;
            }

            String searchType = intent.getStringExtra(AllConstants.INTENT_KEY_SEARCH_TYPE);
            if (!AllSettingsManager.isNullOrEmpty(searchType)) {
                mSearchType = SearchType.valueOf(searchType);
            }
        }
    }

    @Override
    public void initActivityViews() {
//        setupToolbar();
        persistentSearchView = (PersistentSearchView) findViewById(R.id.persistent_searchview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        emptyViewLl = (LinearLayout) findViewById(R.id.emptyViewLl);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        // Get User Tag
        initGetTagValue();
        initProgressBar();
        initSearchView();
        initRecyclerView();
//        initEmptyView();

        if (mSearchType == SearchType.BARCODE) {
            searchData(mBarcode);
        }
    }

    private void initGetTagValue() {
        // Get User Tag
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
        }

    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {
        persistentSearchView.setOnLeftBtnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Handle the left button click
                initActivityBackPress();
            }

        });

        persistentSearchView.setOnClearInputBtnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Handle the clear input button click
            }

        });

        // Setting a delegate for the voice recognition input
        persistentSearchView.setVoiceRecognitionDelegate(new VoiceRecognitionDelegate(this));

        persistentSearchView.setOnSearchConfirmedListener(new OnSearchConfirmedListener() {

            @Override
            public void onSearchConfirmed(PersistentSearchView searchView, String query) {
                // Handle a search confirmation. This is the place where you'd
                // want to save a new query and perform a search against your
                // data provider.

                if (!TextUtils.isEmpty(query)) {
                    searchData(query);
                }
            }
        });

        persistentSearchView.setOnSearchQueryChangeListener(new OnSearchQueryChangeListener() {

            @Override
            public void onSearchQueryChanged(PersistentSearchView searchView, String oldQuery, String newQuery) {
                // Handle a search query change. This is the place where you'd
                // want load new suggestions based on the newQuery parameter.
                Logger.d(TAG, TAG + ">>persistentSearchView>>onSearchQueryChanged>>oldQuery: " + oldQuery + " newQuery: " + newQuery);
                List<String> queries;
                if (TextUtils.isEmpty(newQuery)) {
                    queries = SearchDataProvider.getInitialSearchQueries();
                } else {
                    queries = SearchDataProvider.getSuggestionsForQuery(newQuery);
                    performSearch(newQuery);
                }
                setSuggestions(queries, true);
            }
        });

        persistentSearchView.setOnSuggestionChangeListener(new OnSuggestionChangeListener() {

            @Override
            public void onSuggestionPicked(SuggestionItem suggestion) {
                // Handle a suggestion pick event. This is the place where you'd
                // want to perform a search against your data provider.

                if (suggestion != null) {
                    String query = suggestion.getItemModel().getText();
                    Logger.d(TAG, TAG + ">>persistentSearchView>>onSuggestionPicked>>suggestion: " + query);
                    saveSearchQueryIfNecessary(query);
                    setSuggestions(SearchDataProvider.getSuggestionsForQuery(query), false);
                    performSearch(query);
                }
            }

            @Override
            public void onSuggestionRemoved(SuggestionItem suggestion) {
                // Handle a suggestion remove event. This is the place where
                // you'd want to remove the suggestion from your data provider.

                if (suggestion != null) {
                    String query = suggestion.getItemModel().getText();
                    Logger.d(TAG, TAG + ">>persistentSearchView>>onSuggestionRemoved>>suggestion: " + query);
                    SearchDataProvider.removeSearchQuery(query);
                }
            }
        });
    }

    private void setupToolbar() {
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {
        // Calling the voice recognition delegate to properly handle voice input results
        VoiceRecognitionDelegate.handleResult(persistentSearchView, requestCode, resultCode, data);
    }

    @Override
    public void initActivityBackPress() {
        if (persistentSearchView.isExpanded()) {
            persistentSearchView.collapse();
            return;
        }

        finish();
    }

    @Override
    public void initActivityDestroyTasks() {
        dismissPopupDialog();

        if (getMedicineSearchTask != null && getMedicineSearchTask.getStatus() == AsyncTask.Status.RUNNING) {
            getMedicineSearchTask.cancel(true);
        }
    }

    @Override
    public void initActivityPermissionResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    @Override
    public void onResume() {
        super.onResume();

        loadInitialDataIfNecessary();

        if (mSearchType == SearchType.REGULAR || mSearchType == SearchType.REMINDER) {
            if ((persistentSearchView.isInputQueryEmpty() && (mShopStaggeredListAdapter.getCount() == 0)) || persistentSearchView.isExpanded()) {
                persistentSearchView.expand(false);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            } else {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /******************
     * Search methods *
     ******************/
    private void searchData(String query) {
        if (!TextUtils.isEmpty(query)) {
            Logger.d(TAG, TAG + ">>persistentSearchView>>searchData>>query: " + query);
            saveSearchQueryIfNecessary(query);
            persistentSearchView.collapse();
            performSearch(query);
        }
    }

    private void initProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void initSearchView() {
//        with(persistentSearchView) {
//            setOnLeftBtnClickListener(this@DemoActivity)
//            setOnClearInputBtnClickListener(this@DemoActivity)
//            setOnRightBtnClickListener(this@DemoActivity)
//            showRightButton()
//            setVoiceRecognitionDelegate(VoiceRecognitionDelegate(this@DemoActivity))
//            setOnSearchConfirmedListener(mOnSearchConfirmedListener)
//            setOnSearchQueryChangeListener(mOnSearchQueryChangeListener)
//            setOnSuggestionChangeListener(mOnSuggestionChangeListener)
//            setDismissOnTouchOutside(true)
//            setDimBackground(true)
//            setProgressBarEnabled(true)
//            setVoiceInputButtonEnabled(true)
//            setClearInputButtonEnabled(true)
//            setSuggestionsDisabled(mMode == DemoModes.WITHOUT_SUGGESTIONS)
//            setQueryInputGravity(Gravity.START or Gravity.CENTER)
//        }
    }

    private void initEmptyView() {
        if (mShopStaggeredListAdapter.getCount() > 0) {
            emptyViewLl.setVisibility(View.GONE);
        } else {
            emptyViewLl.setVisibility(View.VISIBLE);
        }
    }

    private void initRecyclerView() {
        mShopStaggeredListAdapter = new ShopStaggeredListAdapter(getActivity(),mSearchType);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        //rvShopStaggered.addItemDecoration(new SpacesItemDecoration(4));
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(mShopStaggeredListAdapter);

        recyclerView.addOnScrollListener(new HeaderedRecyclerViewListener(getActivity()) {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void showHeader() {
                super.showHeader();
                AnimationUtils.Factory.showHeader(persistentSearchView);
            }

            @Override
            public void hideHeader() {
                super.hideHeader();
                AnimationUtils.Factory.hideHeader(persistentSearchView);
            }
        });
    }

    private void loadInitialDataIfNecessary() {
        List<String> searchQueries = null;

        // Fetching the search queries from the data provider
        if (persistentSearchView.isInputQueryEmpty()) {
            searchQueries = SearchDataProvider.getInitialSearchQueries();
        } else {
            searchQueries = SearchDataProvider.getSuggestionsForQuery(persistentSearchView.getInputQuery());
        }

        // Converting them to recent suggestions and setting them to the widget
        setSuggestions(searchQueries, false);
    }

    private void performSearch(String query) {
        emptyViewLl.setVisibility(View.GONE);
        recyclerView.setAlpha(0f);
        progressBar.setVisibility(View.VISIBLE);
        mShopStaggeredListAdapter.clear();

        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
        } else {
            if (getMedicineSearchTask != null && getMedicineSearchTask.getStatus() == AsyncTask.Status.RUNNING) {
                getMedicineSearchTask.cancel(true);
            }

            getMedicineSearchTask = new GetMedicineSearchTask(getActivity(), query);
            getMedicineSearchTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        persistentSearchView.hideLeftButton(false);
        persistentSearchView.showProgressBar();
    }

    private void saveSearchQueryIfNecessary(String query) {
        SearchDataProvider.saveSearchQuery(query);
    }

    private void setSuggestions(List<String> queries, boolean expandIfNecessary) {
        List<SuggestionItem> suggestions = SuggestionCreationUtil.asRecentSearchSuggestions(queries);
        persistentSearchView.setSuggestions(suggestions, expandIfNecessary);
    }

    /************************
     * Server communication *
     ************************/
    private class GetMedicineSearchTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        String categoryID;

        private GetMedicineSearchTask(Context context, String categoryId) {
            mContext = context;
            categoryID = categoryId;
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
                Call<APIResponse<List<StaggeredMedicineByItem>>> call = mApiInterface.apiGetMedicineSearchList(categoryID,customerOrSupplierId,selectUserType);
                Response response = call.execute();
                if (response.isSuccessful()) {
                    Logger.d(TAG, TAG + ">> response found ");
                    return response;
                }
            } catch (Exception ex) {
                Logger.d(TAG, TAG + ">> exception: " + ex.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Response result) {
            try {
                persistentSearchView.hideProgressBar(false);
                persistentSearchView.showLeftButton();
                progressBar.setVisibility(View.GONE);

                if (result != null && result.isSuccessful()) {
                    Logger.d(TAG, "APIResponse(GetMedicineSearchTask): onResponse-server = " + result.toString());
                    APIResponse<List<StaggeredMedicineByItem>> data = (APIResponse<List<StaggeredMedicineByItem>>) result.body();
                    Logger.d(TAG, "APIResponse(data): " + data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(GetMedicineSearchTask()): onResponse-object = " + data.toString());

                        if (data.getData().size() > 0) {
                            mShopStaggeredListAdapter.clear();
                            mShopStaggeredListAdapter.addAll(data.getData());
                            recyclerView.animate()
                                    .alpha(1f)
                                    .setInterpolator(new LinearInterpolator())
                                    .setDuration(300L)
                                    .start();
                        } else {
//                            initEmptyView();
                            Toast.makeText(getActivity(), getString(R.string.txt_no_items_are_found), Toast.LENGTH_LONG).show();
                        }
                    } else {
//                        initEmptyView();
                        Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_LONG).show();
                    }
                } else {
//                    initEmptyView();
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}