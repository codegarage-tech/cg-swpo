package com.reversecoder.attributionpresenter.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.reversecoder.attributionpresenter.R;
import com.reversecoder.attributionpresenter.adapter.AttributionAdapter;
import com.reversecoder.attributionpresenter.model.Attribution;
import com.reversecoder.attributionpresenter.model.Library;
import com.reversecoder.attributionpresenter.view.AnimatedImageView;
import com.reversecoder.attributionpresenter.view.AnimatedTextView;
import com.reversecoder.attributionpresenter.view.ArcView;

import java.util.ArrayList;

public class LicenseActivity extends AppCompatActivity {

    //toolbar
    ArcView arcMenuView;
    AnimatedImageView arcMenuImage;
    AnimatedTextView toolbarTitle;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        initView();
    }

    private void initView() {
        initToolBar();

        ListView lvLicense = (ListView) findViewById(R.id.list);
        AttributionAdapter attributionAdapter = new AttributionAdapter(LicenseActivity.this);
        lvLicense.setAdapter(attributionAdapter);
        attributionAdapter.setData(getAllAttributions());
    }

    private void initToolBar() {
        toolbarTitle = (AnimatedTextView) findViewById(R.id.toolbarTitle);
        arcMenuImage = (AnimatedImageView) findViewById(R.id.arcImage);
        arcMenuView = (ArcView) findViewById(R.id.arcView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbarTitle.setAnimatedText(getString(R.string.title_activity_third_party_license), 0L);

        arcMenuImage.setAnimatedImage(R.drawable.arrow_left, 0L);
        arcMenuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private ArrayList<Attribution> getAllAttributions() {
        ArrayList<Attribution> attributions = new ArrayList<>();

        //Gradle projects
        attributions.add(Library.PICASSO.getAttribution());
        attributions.add(Library.NINE_OLD_ANDROIDS_JAKEWHARTON.getAttribution());
        attributions.add(Library.GLIDE.getAttribution());
        attributions.add(Library.IMAGEVIEW_ZOOM_SEPHIROTH74.getAttribution());
        attributions.add(Library.RETROFIT.getAttribution());
        attributions.add(Library.VOLLEY.getAttribution());
        attributions.add(Library.ZXING_ANDROID_EMBEDDED_JOURNEYAPPS.getAttribution());
        attributions.add(Library.PARCELER_JOHNCARL81.getAttribution());
        attributions.add(Library.ANDROID_SWITCH_ICON.getAttribution());

        //Library projects
        attributions.add(Library.ADAPSTER_ARTHUR3486.getAttribution());
        attributions.add(Library.ANDROID_IMAGE_SLIDER_DAIMAJIA.getAttribution());
        attributions.add(Library.ATTRIBUTE_PRESENTER_FRANMONTIEL.getAttribution());
        attributions.add(Library.BUBBLE_TABBAR_AKSHAY2211.getAttribution());
        attributions.add(Library.EASY_RECYCLERVIEW_JUDE95.getAttribution());
        attributions.add(Library.IMAGE_ZIPPER_AMANJEETSINGH150.getAttribution());
        attributions.add(Library.MATERIAL_RATINGBAR_ZHANGHAI.getAttribution());
        attributions.add(Library.MATISSE_ZHIHU.getAttribution());
        attributions.add(Library.MEOW_BOTTOM_NAVIGATION_SHETMOBILE.getAttribution());
        attributions.add(Library.POPUP_DIALOG_YMEX.getAttribution());
        attributions.add(Library.PERSISTENT_SARCHVIEW_MARS885.getAttribution());
        attributions.add(Library.STATUSBAR_UTIL_LAOBIE.getAttribution());

        return attributions;
    }
}