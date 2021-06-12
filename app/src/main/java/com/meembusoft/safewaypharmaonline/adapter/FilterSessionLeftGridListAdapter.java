package com.meembusoft.safewaypharmaonline.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.meembusoft.safewaypharmaonline.model.FilterSessionLeft;
import com.meembusoft.safewaypharmaonline.viewholder.FilterSessionLeftGridViewHolder;
import com.meembusoft.safewaypharmaonline.viewholder.FilterSessionLeftViewHolder;

import java.security.InvalidParameterException;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class FilterSessionLeftGridListAdapter extends RecyclerArrayAdapter<String> {

    private static final int VIEW_TYPE_REGULAR = 1;

    public FilterSessionLeftGridListAdapter(Context context) {
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
                return new FilterSessionLeftGridViewHolder(parent);
            default:
                throw new InvalidParameterException();
        }
    }


}