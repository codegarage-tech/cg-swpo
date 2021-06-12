package com.meembusoft.safewaypharmaonline.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.meembusoft.safewaypharmaonline.model.CommonData;
import com.meembusoft.safewaypharmaonline.model.Promos;
import com.meembusoft.safewaypharmaonline.viewholder.CollectionProductViewHolder;
import com.meembusoft.safewaypharmaonline.viewholder.PromosViewHolder;

import java.security.InvalidParameterException;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class PromosListAdapter extends RecyclerArrayAdapter<Promos> {

    private static final int VIEW_TYPE_REGULAR = 1;

    public PromosListAdapter(Context context) {
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
                return new PromosViewHolder(parent);
            default:
                throw new InvalidParameterException();
        }
    }


}