package com.meembusoft.safewaypharmaonline.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BulletSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.base.BaseActivity;
import com.meembusoft.safewaypharmaonline.view.CanaroTextView;
import com.meembusoft.safewaypharmaonline.view.CustomBulletSpan;
import com.meembusoft.safewaypharmaonline.view.CustomClickableSpan;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class BulletTestActivity extends BaseActivity {

    //Toolbar
    private ImageView ivBack;
    private CanaroTextView tvTitle;

    private TextView tvBullet;
    private String bulletText = "100% Original Product<click><bold><bullet>Warranty not available<bullet>Color text<color>";

    @Override
    public String[] initActivityPermissions() {
        return new String[]{};
    }


    @Override
    public int initActivityLayout() {
        return R.layout.activity_bullet_test;
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
        tvTitle = (CanaroTextView) findViewById(R.id.text_title);
        tvBullet = (TextView) findViewById(R.id.tv_bullet);
        tvTitle.setText("Bullet Test");
    }

    @Override
    public void initActivityViewsData(Bundle savedInstanceState) {
        tvBullet.setText(buildBulletText(getActivity(),"<bullet>", bulletText));
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

    public static CharSequence buildBulletText(Context context, String bulletExpression, String totalText) {
        SpannableStringBuilder ssb = new SpannableStringBuilder("");
        int gapWidth = 60;
        int bulletRadius = 5;
        int bulletColor = ContextCompat.getColor(context, R.color.colorPrimaryDark);
        if (!TextUtils.isEmpty(totalText) && !TextUtils.isEmpty(bulletExpression)) {
            String[] bulletTexts = totalText.split(bulletExpression);
            if (bulletTexts.length > 0) {
                for (int i = 0; i < bulletTexts.length; i++) {
                    SpannableString txtSpannable;
                    String updatedText = bulletTexts[i];
                    StyleSpan boldSpan ;
                    ClickableSpan clickSpan;
                    ForegroundColorSpan colorSpan;
                    // Set bold span
                    if(bulletTexts[i].contains("<bold>")){
                        updatedText = updatedText.replaceAll("<bold>","");
                        boldSpan = new StyleSpan(Typeface.BOLD);
                    } else {
                        boldSpan= new StyleSpan(Typeface.NORMAL);
                    }

                    // Set color span
                    if(bulletTexts[i].contains("<color>")){
                        updatedText = updatedText.replaceAll("<color>","");
                        colorSpan = new ForegroundColorSpan(bulletColor);
                    } else {
                        colorSpan = new ForegroundColorSpan(Color.BLACK);
                    }

                    // Set click span
                    if(updatedText.contains("<click>")){
                        updatedText = updatedText.replaceAll("<click>","");
                        clickSpan = new CustomClickableSpan(updatedText) {
                            @Override
                            public void onClick(View textView) {
                                // do some thing
                                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                            }
                        };
                    } else {
                        clickSpan = new CustomClickableSpan(updatedText) {
                            @Override
                            public void onClick(View textView) {
                                // Do nothing
                            }
                        };
                    }

                    // Set all span
                    txtSpannable =  new SpannableString(updatedText);
                    txtSpannable.setSpan(boldSpan, 0, updatedText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    txtSpannable.setSpan(colorSpan, 0, updatedText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    txtSpannable.setSpan(clickSpan, 0, updatedText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    // Add one bullet
                    ssb.append(txtSpannable, new CustomBulletSpan(gapWidth, bulletColor,bulletRadius), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ssb.append("\n\n");
                }
            }
        }
        return ssb;
    }
}