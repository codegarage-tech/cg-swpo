package com.meembusoft.safewaypharmaonline.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jude.easyrecyclerview.waveswiperefresh.WaveSwipeRefreshLayout;
import com.jude.easyrecyclerview.waveswiperefresh.WaveSwipeRefreshRecyclerView;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.adapter.FavouriteListAdapter;
import com.meembusoft.safewaypharmaonline.base.BaseActivity;
import com.meembusoft.safewaypharmaonline.enumeration.LoginSettingType;
import com.meembusoft.safewaypharmaonline.model.AppUser;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.retrofit.APIClient;
import com.meembusoft.safewaypharmaonline.retrofit.APIInterface;
import com.meembusoft.safewaypharmaonline.retrofit.APIResponse;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.util.SessionUtil;
import com.meembusoft.safewaypharmaonline.view.CanaroTextView;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.network.NetworkManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_KEY_LOGIN_SETTING_TYPE;
import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_REQUEST_CODE_USER_LOGIN;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class FavouriteListActivity extends BaseActivity {

    //Toolbar
    private ImageView ivBack;
    private CanaroTextView tvTitle;
    private RelativeLayout rlCart;

    private WaveSwipeRefreshRecyclerView rvFavourite;
    private FavouriteListAdapter favouriteListAdapter;
    private LinearLayout linLoginLayout, linNoDataFound;
    private Button btnLogin;
    private AppUser mAppUser;

    //Background task
    private GetAllFavouriteTask getAllFavouriteTask;
    private APIInterface mApiInterface;
    private boolean isSwipeRefreshTask = false;

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }



    @Override
    public int initActivityLayout() {
        return R.layout.activity_favourite_screen;
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
        ivBack = (ImageView)findViewById(R.id.iv_back);
        tvTitle = (CanaroTextView) findViewById(R.id.text_title);
        tvTitle.setText(getActivity().getResources().getString(R.string.title_my_favourites));
        rlCart = (RelativeLayout) findViewById(R.id.rl_cart);
        rlCart.setVisibility(View.GONE);
        rvFavourite = (WaveSwipeRefreshRecyclerView ) findViewById(R.id.rv_favourite);
        btnLogin = (Button) findViewById(R.id.btn_login);
        linLoginLayout = (LinearLayout) findViewById(R.id.lin_login_layout);
        linNoDataFound = (LinearLayout) findViewById(R.id.lin_no_data_found_layout);


    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        mApiInterface = APIClient.getClient(getActivity()).create(APIInterface.class);

        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
            // loadOfflineTimeData();
        } else {

            checkUserLogin();
        }
    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {
        ivBack.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });
        rvFavourite.setRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!NetworkManager.isConnected(getActivity())) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                    rvFavourite.setRefreshing(false);
                    return;
                }

                if (getAllFavouriteTask != null && getAllFavouriteTask.getStatus() == AsyncTask.Status.RUNNING) {
                    getAllFavouriteTask.cancel(true);
                }

                isSwipeRefreshTask = true;
                getAllFavouriteTask = new GetAllFavouriteTask(getActivity(), "");
                getAllFavouriteTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            }
        });

        btnLogin.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (SessionUtil.getUser(getActivity()) != null) {
                } else {
                    Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
                    intentLogin.putExtra(INTENT_KEY_LOGIN_SETTING_TYPE, LoginSettingType.MENU_TO_LOGIN.name());
                    getActivity().startActivityForResult(intentLogin, INTENT_REQUEST_CODE_USER_LOGIN);
                }
            }
        });

    }

    private void checkUserLogin() {
        if (SessionUtil.getUser(getActivity()) != null) {
            mAppUser = SessionUtil.getUser(getActivity());
            rvFavourite.setVisibility(View.VISIBLE);
            linLoginLayout.setVisibility(View.GONE);
            linNoDataFound.setVisibility(View.GONE);
            getAllFavouriteTask = new GetAllFavouriteTask(getActivity(), "");
            getAllFavouriteTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            rvFavourite.setVisibility(View.GONE);
            linLoginLayout.setVisibility(View.VISIBLE);
            linNoDataFound.setVisibility(View.GONE);

        }
    }

    private void initFavouriteListRv(List<StaggeredMedicineByItem> favouriteList) {

        if (favouriteList!=null && favouriteList.size()>0) {
            rvFavourite.setVisibility(View.VISIBLE);
            linNoDataFound.setVisibility(View.GONE);
            favouriteListAdapter = new FavouriteListAdapter(getActivity());
            rvFavourite.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            rvFavourite.setAdapter(favouriteListAdapter);
            favouriteListAdapter.addAll(favouriteList);
        } else {
            rvFavourite.setVisibility(View.GONE);
            linNoDataFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case INTENT_REQUEST_CODE_USER_LOGIN:
                Logger.d(TAG, TAG + " >>> " + "INTENT_REQUEST_CODE_LOGIN: " + requestCode);
                if (data != null && resultCode == RESULT_OK) {
                    if (!NetworkManager.isConnected(getActivity())) {
                        Toast.makeText(getActivity(), getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
                        // loadOfflineTimeData();
                    } else {
                        linLoginLayout.setVisibility(View.GONE);
                        linNoDataFound.setVisibility(View.GONE);
                        if (getAllFavouriteTask != null && getAllFavouriteTask.getStatus() == AsyncTask.Status.RUNNING) {
                            getAllFavouriteTask.cancel(true);
                        }
                        getAllFavouriteTask = new GetAllFavouriteTask(getActivity(), "");
                        getAllFavouriteTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    }
                }

                break;
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

        if (getAllFavouriteTask != null && getAllFavouriteTask.getStatus() == AsyncTask.Status.RUNNING) {
            getAllFavouriteTask.cancel(true);
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
    private class GetAllFavouriteTask extends AsyncTask<String, Integer, Response> {

        Context mContext;
        String favouriteID;

        private GetAllFavouriteTask(Context context, String favouriteId) {
            mContext = context;
            favouriteID = favouriteId;

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
                Call<APIResponse<List<StaggeredMedicineByItem>>> call = mApiInterface.apiGetAllFavouriteList(mAppUser.getId());
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
                rvFavourite.setRefreshing(false);

                if (result != null && result.isSuccessful()) {
                    Logger.d(TAG, "APIResponse(GetAllFavouriteTask): onResponse-server = " + result.toString());
                    APIResponse<List<StaggeredMedicineByItem>> data = (APIResponse<List<StaggeredMedicineByItem>>) result.body();
                    Logger.d("GetSupplierTask", data.toString() + "");

                    if (data != null && data.getStatus().equalsIgnoreCase("1")) {
                        Logger.d(TAG, "APIResponse(GetAllFavouriteTask()): onResponse-object = " + data.toString());
                        initFavouriteListRv(data.getData());
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