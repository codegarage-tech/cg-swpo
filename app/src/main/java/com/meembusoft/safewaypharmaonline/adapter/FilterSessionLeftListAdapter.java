package com.meembusoft.safewaypharmaonline.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.meembusoft.safewaypharmaonline.model.FilterSessionLeft;
import com.meembusoft.safewaypharmaonline.model.PlaceOrderByItem;
import com.meembusoft.safewaypharmaonline.model.Promos;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.viewholder.FilterSessionLeftViewHolder;
import com.meembusoft.safewaypharmaonline.viewholder.PromosViewHolder;
import com.meembusoft.safewaypharmaonline.viewholder.ShopStaggeredViewHolder;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class FilterSessionLeftListAdapter extends RecyclerArrayAdapter<FilterSessionLeft> {
    private static String TAG = FilterSessionLeftListAdapter.class.getSimpleName();

    private static final int VIEW_TYPE_REGULAR = 1;

    public FilterSessionLeftListAdapter(Context context) {
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
                return new FilterSessionLeftViewHolder(parent);
            default:
                throw new InvalidParameterException();
        }
    }

    public void updateItem(FilterSessionLeft filterSessionLeft) {

        int position = getItemPosition(filterSessionLeft);

        if (position != -1) {
            List<FilterSessionLeft> filterSessionLeftsList = new ArrayList<>();
            filterSessionLeftsList.addAll(getAllData());
            filterSessionLeftsList.remove(position);
            filterSessionLeftsList.add(position, filterSessionLeft);

            removeAll();
            addAll(filterSessionLeftsList);
            notifyDataSetChanged();
        }
    }

    private int getItemPosition(FilterSessionLeft filterSessionLeft) {
        List<FilterSessionLeft> filterSessionLefts = getAllData();
        for (int i = 0; i < filterSessionLefts.size(); i++) {
            if (filterSessionLefts.get(i).getId().get(0).equalsIgnoreCase(filterSessionLeft.getId().get(0))) {
                return i;
            }
        }
        return -1;
    }

}