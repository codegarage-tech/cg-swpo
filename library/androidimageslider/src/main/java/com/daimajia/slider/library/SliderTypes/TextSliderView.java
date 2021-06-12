package com.daimajia.slider.library.SliderTypes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.slider.library.R;

/**
 * This is a slider with a description TextView.
 */
public class TextSliderView extends BaseSliderView{
    ImageView targetImageView;

    public TextSliderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.render_type_text,null);

        TextView description = (TextView)v.findViewById(R.id.description);
        description.setText(getDescription());

        LinearLayout descriptionLayout = v.findViewById(R.id.description_layout);
        descriptionLayout.setVisibility(getDescriptionVisibility());

        targetImageView = (ImageView)v.findViewById(R.id.daimajia_slider_image);
        bindEventAndShow(v, targetImageView);
        return v;
    }

    public ImageView getTargetImageView(){
        return targetImageView;
    }
}
