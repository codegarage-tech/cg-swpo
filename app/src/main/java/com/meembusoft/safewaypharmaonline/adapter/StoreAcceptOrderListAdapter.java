package com.meembusoft.safewaypharmaonline.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.meembusoft.safewaypharmaonline.model.PlaceOrderByItem;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.meembusoft.safewaypharmaonline.viewholder.StoreAcceptOrderViewHolder;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class StoreAcceptOrderListAdapter extends RecyclerArrayAdapter<PlaceOrderByItem> {

    private static final int VIEW_TYPE_REGULAR = 1;
    private static String TAG = PlaceOrderListAdapter.class.getSimpleName();

    public StoreAcceptOrderListAdapter(Context context) {
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
                return new StoreAcceptOrderViewHolder(parent);
            default:
                throw new InvalidParameterException();
        }
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

    public void addOrUpdateItem(PlaceOrderByItem order) {
        if (order != null) {
            int position = getItemPosition(order);
            Logger.d(TAG, TAG + " >>> " + "mposition: " + position + "");

            List<PlaceOrderByItem> orders = new ArrayList<>();
            if (position != -1) {
                orders.addAll(getAllData());
                orders.remove(position);
                orders.add(position, order);

                removeAll();
                addAll(orders);
            } else {
                orders.add(0, order);
                orders.addAll(getAllData());
                clear();
                addAll(orders);
            }
            notifyDataSetChanged();
        }
    }

    public void updateItem(PlaceOrderByItem order) {
        int position = getItemPosition(order);
        if (position != -1) {
            List<PlaceOrderByItem> orders = new ArrayList<>();
            orders.addAll(getAllData());
            orders.remove(position);
            orders.add(position, order);

            removeAll();
            addAll(orders);
            notifyDataSetChanged();
        }
    }

    private int getItemPosition(PlaceOrderByItem order) {
        List<PlaceOrderByItem> orders = getAllData();
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getId().equalsIgnoreCase(order.getId())) {
                return i;
            }
        }
        return -1;
    }
}