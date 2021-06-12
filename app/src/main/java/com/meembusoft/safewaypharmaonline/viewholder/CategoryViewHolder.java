package com.meembusoft.safewaypharmaonline.viewholder;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.CategoryActivity;
import com.meembusoft.safewaypharmaonline.activity.ProductDetailsActivity;
import com.meembusoft.safewaypharmaonline.adapter.CategoryListAdapter;
import com.meembusoft.safewaypharmaonline.model.Category;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.reversecoder.library.event.OnSingleClickListener;

import java.util.List;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class CategoryViewHolder extends BaseViewHolder<Category> {

    private static String TAG = CategoryViewHolder.class.getSimpleName();
    private TextView titleView;
    private ImageView ivCategory;

    public CategoryViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_select_category_item);

        titleView = (TextView) $(R.id.tv_title);
    }

    @Override
    public void setData(final Category category) {
        //Set data
        titleView.setText(category.getName());
       // AppUtil.loadImage(getContext(), ivCategory, category.getThumb_image(), false, false, true);

        if (category.isSelected())
        {
            titleView.setBackgroundResource(R.drawable.selected_category_item);
            titleView.setTextColor(Color.parseColor("#7EBF2F"));
            ((CategoryActivity)getContext()).categoryByMedicine(category.getId());
        } else
        {
            titleView.setBackgroundResource(R.drawable.unselect_category_item);
            titleView.setTextColor(Color.parseColor("#FFFFFF"));
        }

        itemView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
//                Intent intentDetails = new Intent(getContext(), ProductDetailsActivity.class);
//                getContext().startActivity(intentDetails);
                setSelection(category);

            }
        });
    }

    private void setSelection(Category data) {
        CategoryListAdapter categoryAdapter = ((CategoryListAdapter) getOwnerAdapter());
        List<Category> categoryList = categoryAdapter.getAllData();

        if (categoryList != null && categoryList.size() > 0) {
            for (Category category : categoryList) {
                if (category.getId() == data.getId()) {
                    category.setSelected(true);
                    Logger.d("medicineByCategoryId", category.getId() + "");
                    ((CategoryActivity)getContext()).categoryByMedicine(category.getId());

                } else {
                    category.setSelected(false);
                }
            }
            categoryAdapter.notifyDataSetChanged();
        }
    }

}