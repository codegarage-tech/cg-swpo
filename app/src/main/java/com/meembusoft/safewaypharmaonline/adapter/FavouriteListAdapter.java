package com.meembusoft.safewaypharmaonline.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.viewholder.BestSellerProductViewHolder;
import com.meembusoft.safewaypharmaonline.viewholder.FavouriteProductViewHolder;

import java.security.InvalidParameterException;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class FavouriteListAdapter extends RecyclerArrayAdapter<StaggeredMedicineByItem> {

    private static final int VIEW_TYPE_REGULAR = 1;

    public FavouriteListAdapter(Context context) {
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
                return new FavouriteProductViewHolder(parent);
            default:
                throw new InvalidParameterException();
        }
    }


}