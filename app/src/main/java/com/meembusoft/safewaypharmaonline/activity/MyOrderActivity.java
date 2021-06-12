package com.meembusoft.safewaypharmaonline.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.adapter.MyOrderListAdapter;
import com.meembusoft.safewaypharmaonline.base.BaseActivity;
import com.meembusoft.safewaypharmaonline.fragment.OrderListFragment;
import com.meembusoft.safewaypharmaonline.util.AllConstants;
import com.meembusoft.safewaypharmaonline.util.FragmentUtilsManager;
import com.meembusoft.safewaypharmaonline.view.CanaroTextView;

import java.util.List;

import static com.meembusoft.safewaypharmaonline.util.AllConstants.INTENT_REQUEST_CODE_SHIPPING_ADDRESS;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class MyOrderActivity extends BaseActivity {


    //Toolbar
    private ImageView ivBack, ivCart;
    private CanaroTextView tvTitle;
    private RelativeLayout rlCart;
    LinearLayout llLeftBack;
    private TextView tvCart, tvTotalsAmount;
    private RecyclerView rvMedicineOrderList;
    private MyOrderListAdapter myOrderListAdapter;
    int itemCountNo = 0;
    //Background task

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }


    @Override
    public int initActivityLayout() {
        return R.layout.activity_myorder_list_screen;
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
        ivCart = (ImageView) findViewById(R.id.iv_cart);
        tvCart = (TextView) findViewById(R.id.tv_cart);
        tvTitle = (CanaroTextView) findViewById(R.id.text_title);
        tvTitle.setText(getActivity().getResources().getString(R.string.title_order));
        rlCart = (RelativeLayout) findViewById(R.id.rl_cart);
        rlCart.setVisibility(View.GONE);
        llLeftBack = (LinearLayout) findViewById(R.id.ll_left_back);

        FragmentUtilsManager.changeSupportFragment(MyOrderActivity.this, R.id.order_container, new OrderListFragment());

    }


    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        //  resetCounterView();
//        myOrderListAdapter = new MyOrderListAdapter(getActivity());
//        rvMedicineOrderList.setLayoutManager(new LinearLayoutManager(getActivity()));
//        rvMedicineOrderList.setAdapter(myOrderListAdapter);
//        myOrderListAdapter.addAll(AppUtil.getAllStoredMedicineByItems(getActivity()));
    }

    // Cart counter view
//    public void resetCounterView() {
//        List<StaggeredMedicineByItem> data = AppUtil.getAllStoredMedicineByItems(getActivity());
//        if (data.size() > 0) {
//            itemCountNo = data.size();
//            tvCart.setText(data.size() + "");
//            tvCart.setVisibility(View.VISIBLE);
//        } else {
//            tvCart.setVisibility(View.GONE);
//        }
//    }


    //    public void updateTotalAmount(){
//        try {
//            float subtotal = AppUtil.formatFloat(AppUtil.getSubtotalPrice(getActivity()));
//            Logger.d(TAG, TAG + " >>> " + "subtotal: " + subtotal);
//
//            tvTotalsAmount.setText(subtotal + " /-" );
//        }catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
    @Override
    public void initActivityActions(Bundle savedInstanceState) {
//        ivBack.setOnClickListener(new OnSingleClickListener() {
//            @Override
//            public void onSingleClick(View view) {
//                Intent intent = new Intent();
//                setResult(RESULT_OK, intent);
//                finish();
//            }
//        });


        llLeftBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
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
                //  resetCounterView();
                FragmentUtilsManager.changeSupportFragment(MyOrderActivity.this, R.id.order_container, new OrderListFragment());

                break;

            case INTENT_REQUEST_CODE_SHIPPING_ADDRESS:
                openFragmentOnActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    public void subTotals() {
        //send back press event to the fragment
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null && fragmentList.size() > 0) {
            for (Fragment fragment : fragmentList) {
                if (fragment instanceof OrderListFragment) {
                    // ((OrderListFragment) fragment).initFragmentViewsData();
                    ((OrderListFragment) fragment).updateTotalAmount();
                }
            }
        }
    }


    private void openFragmentOnActivityResult(int requestCode, int resultCode, Intent data) {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null) {
            for (Fragment fragment : fragmentList) {
                if (fragment instanceof OrderListFragment) {
                    ((OrderListFragment) fragment).initFragmentOnResult(requestCode, resultCode, data);
                }
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


}