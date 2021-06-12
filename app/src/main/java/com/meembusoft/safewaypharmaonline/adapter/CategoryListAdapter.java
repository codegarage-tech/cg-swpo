package com.meembusoft.safewaypharmaonline.adapter;

import android.content.Context;
import android.icu.text.Transliterator;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.util.Log;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.meembusoft.safewaypharmaonline.model.Category;
import com.meembusoft.safewaypharmaonline.viewholder.CategoryViewHolder;
import com.meembusoft.safewaypharmaonline.viewholder.HomeCategoryViewHolder;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class CategoryListAdapter extends RecyclerArrayAdapter<Category> {

    private static final int VIEW_TYPE_REGULAR = 1;

    public CategoryListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getViewType(int position) {
        return VIEW_TYPE_REGULAR;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_REGULAR:
                return new CategoryViewHolder(parent);
            default:
                throw new InvalidParameterException();
        }
    }

    public void setAdapterData(List<Category> categoryList) {
        List<Category> categoryResults = new ArrayList<>();
        for (int i = 0; i < categoryList.size(); i++) {
            if (i==0){
                categoryList.get(0).setSelected(true);
                categoryResults.add(0, categoryList.get(0));

            } else {
                categoryList.get(i).setSelected(false);
                categoryResults.add(categoryList.get(i));
            }
        }
        removeAll();
        addAll(categoryResults);
        notifyDataSetChanged();
    }

    }