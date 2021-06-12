package com.meembusoft.safewaypharmaonline.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;
import com.meembusoft.safewaypharmaonline.R;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ReminderActivity extends AppCompatActivity {

    private static String TAG = ReminderActivity.class.getSimpleName();
    private LinearLayout llTakeMedicine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        initStatusBar();
        initActivityViews();
        initActivityViewsData();
    }

    private void initStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white));
        StatusBarUtil.setTransparent(ReminderActivity.this);
    }

    public void initActivityViews() {
        llTakeMedicine = (LinearLayout) findViewById(R.id.ll_take_medicine);
    }

    public void initActivityViewsData() {
        llTakeMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}