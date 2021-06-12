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
import com.meembusoft.safewaypharmaonline.adapter.VideoListAdapter;
import com.meembusoft.safewaypharmaonline.base.BaseActivity;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.model.Videos;
import com.meembusoft.safewaypharmaonline.retrofit.APIClient;
import com.meembusoft.safewaypharmaonline.retrofit.APIInterface;
import com.meembusoft.safewaypharmaonline.retrofit.APIResponse;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.view.CanaroTextView;
import com.reversecoder.library.network.NetworkManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AllVideoListActivity extends BaseActivity {
    // Toolbar
    private CanaroTextView tvTitle;
    private RelativeLayout rlCart;
    private TextView tvCount;
    private LinearLayout llLeftBack;

    // ImageView
    private ImageView ivBack;
    // Recycler view
    // private RecyclerView rvShowAll;
    private WaveSwipeRefreshRecyclerView rvVideoShowAll;
    private List<Videos> videosList;
    private VideoListAdapter videoListAdapter;
    private boolean isSwipeRefreshTask = false;

    //Background task
    private GetVideoTask getVideoTask;
    private APIInterface mApiInterface;

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }


    @Override
    public int initActivityLayout() {
        return R.layout.activity_all_video_list_screen;
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

//        Parcelable mParcelable = intent.getParcelableExtra(AllConstants.INTENT_KEY_VIDEO_LIST);
//        if (mParcelable != null) {
//            videosList = Parcels.unwrap(mParcelable);
//            Logger.d(TAG, TAG + " >>> " + "videosList: " + videosList.toString());
//        }
    }

    @Override
    public void initActivityViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        // rvShowAll = (RecyclerView) findViewById(R.id.rv_show_all_video);
        tvTitle = (CanaroTextView) findViewById(R.id.text_title);
        tvTitle.setText(getResources().getString(R.string.title_video));
        tvCount = (TextView) findViewById(R.id.tv_cart);
        rlCart = (RelativeLayout) findViewById(R.id.rl_cart);
        llLeftBack = (LinearLayout) findViewById(R.id.ll_left_back);
        rvVideoShowAll = (WaveSwipeRefreshRecyclerView) findViewById(R.id.rv_video_show_all);

        resetCounterView();

    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);
        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
            // loadOfflineTimeData();
        } else {
            getVideoTask = new GetVideoTask(getActivity());
            getVideoTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
        rvVideoShowAll.setRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!NetworkManager.isConnected(getActivity())) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                    rvVideoShowAll.setRefreshing(false);
                    return;
                }

                if (getVideoTask != null && getVideoTask.getStatus() == AsyncTask.Status.RUNNING) {
                    getVideoTask.cancel(true);
                }

                isSwipeRefreshTask = true;
                getVideoTask = new GetVideoTask(getActivity());
                getVideoTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
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

        if (getVideoTask != null && getVideoTask.getStatus() == AsyncTask.Status.RUNNING) {
            getVideoTask.cancel(true);
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

    private class GetVideoTask extends AsyncTask<String, Integer, Response> {

        Context mContext;

        private GetVideoTask(Context context) {
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
                Call<APIResponse<List<Videos>>> call = mApiInterface.apiGetVideosList();
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
                rvVideoShowAll.setRefreshing(false);

                if (result != null && result.isSuccessful()) {
                    Logger.d(TAG, "APIResponse(GetVideoTask): onResponse-server = " + result.toString());
                    APIResponse<List<Videos>> data = (APIResponse<List<Videos>>) result.body();
                    Logger.d("GetVideoTask", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(GetVideoTask()): onResponse-object = " + data.toString());
                        videosList = data.getData();
                        initVideoPlayerList(videosList);
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

    private void initVideoPlayerList(List<Videos> videosList) {
        if (videosList != null && videosList.size() > 0) {
            videoListAdapter = new VideoListAdapter(getActivity());
            rvVideoShowAll.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            rvVideoShowAll.setAdapter(videoListAdapter);
            videoListAdapter.addAll(videosList);
        }
    }


}