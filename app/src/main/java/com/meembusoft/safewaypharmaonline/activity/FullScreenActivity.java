package com.meembusoft.safewaypharmaonline.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.base.BaseActivity;
import com.meembusoft.safewaypharmaonline.util.AllConstants;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.view.CanaroTextView;
import com.reversecoder.library.util.AllSettingsManager;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class FullScreenActivity extends BaseActivity {

    //Toolbar
    private ImageView ivBack;
    private CanaroTextView tvTitle;
    private RelativeLayout rlCart;

    private String imageUrl = "";
    private ImageView ivFullImage;

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }


    @Override
    public int initActivityLayout() {
        return R.layout.activity_fullscreen_image;
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
            String mParcelableCategoryId = intent.getStringExtra(AllConstants.INTENT_KEY_FULL_SCREEN_IMAGE_URL);

            if (!AllSettingsManager.isNullOrEmpty(mParcelableCategoryId)) {
                imageUrl = mParcelableCategoryId;
                Logger.d(TAG, TAG + " >>> " + "imageUrl: " + imageUrl);
            }

        }
    }

    @Override
    public void initActivityViews() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivFullImage = (ImageView) findViewById(R.id.iv_full_image);
        tvTitle = (CanaroTextView) findViewById(R.id.text_title);
        tvTitle.setText(getActivity().getResources().getString(R.string.title_full_screen));
        rlCart = (RelativeLayout) findViewById(R.id.rl_cart);
        rlCart.setVisibility(View.GONE);
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        AppUtil.loadImage(getActivity(), ivFullImage, imageUrl, false, false, true);

    }

    @Override
    public void initActivityActions(Bundle savedInstanceState) {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
    }


    @Override
    public void initActivityOnResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void initActivityBackPress() {
        finish();
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