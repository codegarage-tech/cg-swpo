package com.meembusoft.safewaypharmaonline.adapter;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.meembusoft.safewaypharmaonline.enumeration.SearchType;
import com.meembusoft.safewaypharmaonline.model.AllProductMedicines;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.viewholder.BestSellerProductViewHolder;
import com.meembusoft.safewaypharmaonline.viewholder.ShopStaggeredViewHolder;

import java.security.InvalidParameterException;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ShopStaggeredListAdapter extends RecyclerArrayAdapter<StaggeredMedicineByItem> {
    private static String TAG = ShopStaggeredViewHolder.class.getSimpleName();

    private static final int VIEW_TYPE_REGULAR = 1;
    private SearchType mSearchType;
    public ShopStaggeredListAdapter(Context context,  SearchType  searchType) {
        super(context);
        mSearchType = searchType;
        Log.d(TAG, "mSearchType<<<: " + mSearchType.toString());
    }

    @Override
    public int getViewType(int position) {
        return VIEW_TYPE_REGULAR;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_REGULAR:
                return new ShopStaggeredViewHolder(parent,mSearchType);
            default:
                throw new InvalidParameterException();
        }
    }


}