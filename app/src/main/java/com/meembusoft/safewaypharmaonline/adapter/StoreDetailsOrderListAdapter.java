package com.meembusoft.safewaypharmaonline.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.meembusoft.safewaypharmaonline.model.OrderItems;
import com.meembusoft.safewaypharmaonline.model.PlaceOrderByItem;
import com.meembusoft.safewaypharmaonline.viewholder.StoreDetailsOrderListViewHolder;
import com.meembusoft.safewaypharmaonline.viewholder.StoreOrderListViewHolder;

import java.security.InvalidParameterException;
import java.util.List;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class StoreDetailsOrderListAdapter extends RecyclerArrayAdapter<OrderItems> {

    private static final int VIEW_TYPE_REGULAR = 1;

    public StoreDetailsOrderListAdapter(Context context) {
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
                return new StoreDetailsOrderListViewHolder(parent);
            default:
                throw new InvalidParameterException();
        }
    }



    }