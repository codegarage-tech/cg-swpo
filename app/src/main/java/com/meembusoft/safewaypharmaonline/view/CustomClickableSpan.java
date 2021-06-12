package com.meembusoft.safewaypharmaonline.view;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class CustomClickableSpan extends ClickableSpan {

//    public abstract void onTextClick(View view);

    public CustomClickableSpan(String text) {
        super();
    }

    public void onClick(View tv) {
//        onTextClick(tv);
    }

    public void updateDrawState(TextPaint ds) {// override updateDrawState
        ds.setUnderlineText(false); // set to false to remove underline
    }
}