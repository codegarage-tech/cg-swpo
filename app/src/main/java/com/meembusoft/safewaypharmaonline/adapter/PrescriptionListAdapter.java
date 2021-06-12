package com.meembusoft.safewaypharmaonline.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.meembusoft.safewaypharmaonline.model.PlaceOrderByItem;
import com.meembusoft.safewaypharmaonline.viewholder.PlaceOrderListViewHolder;
import com.meembusoft.safewaypharmaonline.viewholder.PrescriptionListViewHolder;

import java.security.InvalidParameterException;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class PrescriptionListAdapter extends RecyclerArrayAdapter<PlaceOrderByItem> {

    private static final int VIEW_TYPE_REGULAR = 1;

    public PrescriptionListAdapter(Context context) {
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
                return new PrescriptionListViewHolder(parent);
            default:
                throw new InvalidParameterException();
        }
    }


}