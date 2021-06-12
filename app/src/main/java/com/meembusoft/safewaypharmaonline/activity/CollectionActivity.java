package com.meembusoft.safewaypharmaonline.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jude.easyrecyclerview.waveswiperefresh.WaveSwipeRefreshLayout;
import com.jude.easyrecyclerview.waveswiperefresh.WaveSwipeRefreshRecyclerView;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.adapter.AllCollectionListAdapter;
import com.meembusoft.safewaypharmaonline.base.BaseActivity;
import com.meembusoft.safewaypharmaonline.model.CommonData;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.retrofit.APIClient;
import com.meembusoft.safewaypharmaonline.retrofit.APIInterface;
import com.meembusoft.safewaypharmaonline.retrofit.APIResponse;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.view.CanaroTextView;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.network.NetworkManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class CollectionActivity extends BaseActivity {

    //Toolbar
    private ImageView ivBack;
    private CanaroTextView tvTitle;
    private RelativeLayout rlCart;
    private TextView tvCount;

    private LinearLayout linNoDataFound;

    private WaveSwipeRefreshRecyclerView rvCollection;
    private AllCollectionListAdapter collectionListAdapter;
    // ArrayList
    List<CommonData> collectionList;
    //Background task
    private GetAllCollectionTask getAllCollectionTask;
    private APIInterface mApiInterface;
    private boolean isSwipeRefreshTask = false;

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }


    @Override
    public int initActivityLayout() {
        return R.layout.activity_collection_screen;
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
//        if (intent != null) {
//            Parcelable mParcelable = intent.getParcelableExtra(AllConstants.INTENT_KEY_COLLECTION);
//            if (mParcelable != null) {
//                collectionList = Parcels.unwrap(mParcelable);
//                Logger.d(TAG, TAG + " >>> " + "collectionList: " + collectionList.toString());
//            }
//        }
    }

    @Override
    public void initActivityViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (CanaroTextView) findViewById(R.id.text_title);
        tvTitle.setText(getActivity().getResources().getString(R.string.title_collection));
        tvCount = (TextView) findViewById(R.id.tv_cart);
        rlCart = (RelativeLayout) findViewById(R.id.rl_cart);
        rlCart.setVisibility(View.GONE);
        rvCollection = (WaveSwipeRefreshRecyclerView) findViewById(R.id.rv_collection);
        linNoDataFound = (LinearLayout) findViewById(R.id.lin_no_data_found_layout);

    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {

        mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
        } else {
            getAllCollectionTask = new GetAllCollectionTask(getActivity());
            getAllCollectionTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        resetCounterView();
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {
        ivBack.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });

//        rvCollection.setRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (!NetworkManager.isConnected(getActivity())) {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
//                    rvCollection.setRefreshing(false);
//                    return;
//                }
//
//                isSwipeRefreshTask = true;
//                initCollectionByCategoryRv(collectionList);
//
//            }
//        });

        rvCollection.setRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!NetworkManager.isConnected(getActivity())) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                    rvCollection.setRefreshing(false);
                    return;
                }

                if (getAllCollectionTask != null && getAllCollectionTask.getStatus() == AsyncTask.Status.RUNNING) {
                    getAllCollectionTask.cancel(true);
                }

                isSwipeRefreshTask = true;
                getAllCollectionTask = new GetAllCollectionTask(getActivity());
                getAllCollectionTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
    }

    private void initCollectionByCategoryRv(List<CommonData> collectionLists) {
        isSwipeRefreshTask = false;
        rvCollection.setRefreshing(false);
        if (collectionLists != null && collectionLists.size() > 0) {
            rvCollection.setVisibility(View.VISIBLE);
            linNoDataFound.setVisibility(View.GONE);
            collectionListAdapter = new AllCollectionListAdapter(getActivity());
            rvCollection.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            rvCollection.setAdapter(collectionListAdapter);
            collectionListAdapter.addAll(collectionLists);
        } else {
            rvCollection.setVisibility(View.GONE);
            linNoDataFound.setVisibility(View.VISIBLE);
        }
    }

    public void resetCounterView() {
        List<StaggeredMedicineByItem> data = AppUtil.getAllStoredMedicineByItems(getActivity());
        if (data.size() > 0) {
            tvCount.setText(data.size() + "");
            tvCount.setVisibility(View.VISIBLE);
            rlCart.setVisibility(View.GONE);
        } else {
            rlCart.setVisibility(View.GONE);
            tvCount.setVisibility(View.GONE);
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

        if (getAllCollectionTask != null && getAllCollectionTask.getStatus() == AsyncTask.Status.RUNNING) {
            getAllCollectionTask.cancel(true);
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

    private class GetAllCollectionTask extends AsyncTask<String, Integer, Response> {

        Context mContext;

        private GetAllCollectionTask(Context context) {
            mContext = context;

        }

        @Override
        protected void onPreExecute() {

            if (!isSwipeRefreshTask) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showPopupDialog();
                    }
                }, 50);
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
                Call<APIResponse<List<CommonData>>> call = mApiInterface.apiGetCollectionsList();
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
                if (!isSwipeRefreshTask) {
                    dismissPopupDialog();
                }

                isSwipeRefreshTask = false;
                rvCollection.setRefreshing(false);

                if (result != null && result.isSuccessful()) {
                    Logger.d(TAG, "APIResponse(GetAllCollectionTask): onResponse-server = " + result.toString());
                    APIResponse<List<CommonData>> data = (APIResponse<List<CommonData>>) result.body();
                    Logger.d("GetAllCollectionTask", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(GetAllCollectionTask()): onResponse-object = " + data.toString());
                        initCollectionByCategoryRv(data.getData());
                    } else {
                        Toast.makeText(getActivity(), data.getMsg(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_could_not_retrieve_info), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }


}