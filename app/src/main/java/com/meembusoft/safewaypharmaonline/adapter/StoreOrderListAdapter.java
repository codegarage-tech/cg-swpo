package com.meembusoft.safewaypharmaonline.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.meembusoft.safewaypharmaonline.model.PlaceOrderByItem;
import com.meembusoft.safewaypharmaonline.model.Store;
import com.meembusoft.safewaypharmaonline.viewholder.StoreOrderListViewHolder;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class StoreOrderListAdapter extends RecyclerArrayAdapter<PlaceOrderByItem> {

    private static final int VIEW_TYPE_REGULAR = 1;

    public StoreOrderListAdapter(Context context) {
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
                return new StoreOrderListViewHolder(parent);
            default:
                throw new InvalidParameterException();
        }
    }

    public int getItemPosition(PlaceOrderByItem storeItem) {
        List<PlaceOrderByItem> placeOrderByItems = getAllData();
        for (int i = 0; i < placeOrderByItems.size(); i++) {
            if (placeOrderByItems.get(i).getId().equalsIgnoreCase(storeItem.getId())) {
                return i;
            }
        }
        return -1;
    }

    public void addStore(PlaceOrderByItem store) {
        if (store != null) {
            List<PlaceOrderByItem> properties = new ArrayList<>();
            properties.add(0, store);
            properties.addAll(getAllData());
            clear();
            addAll(properties);
            notifyDataSetChanged();
        }
    }

    public void removeItem(PlaceOrderByItem placeOrderByItem) {
        int position = getItemPosition(placeOrderByItem);
        if (position != -1) {
            remove(position);
            notifyDataSetChanged();
        }
    }
}