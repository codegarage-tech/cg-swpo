package com.meembusoft.safewaypharmaonline.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.meembusoft.safewaypharmaonline.application.SwpoApp;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class CanaroTextView extends AppCompatTextView {

    public CanaroTextView(Context context) {
        this(context, null);
    }

    public CanaroTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CanaroTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(SwpoApp.canaroExtraBold);
    }
}
