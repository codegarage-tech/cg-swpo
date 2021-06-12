package com.meembusoft.safewaypharmaonline.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.meembusoft.safewaypharmaonline.model.Category;
import com.meembusoft.safewaypharmaonline.model.ShippingCharge;
import com.meembusoft.safewaypharmaonline.viewholder.CategoryViewHolder;
import com.meembusoft.safewaypharmaonline.viewholder.ShippingChargeViewHolder;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ShippingChargeListAdapter extends RecyclerArrayAdapter<ShippingCharge> {

    private static final int VIEW_TYPE_REGULAR = 1;

    public ShippingChargeListAdapter(Context context) {
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
                return new ShippingChargeViewHolder(parent);
            default:
                throw new InvalidParameterException();
        }
    }

    public void setAdapterData(List<ShippingCharge> chargeList) {

        List<ShippingCharge> chargeResults = new ArrayList<>();
        for (int i = 0; i < chargeList.size(); i++) {
            if (i==0){
                chargeList.get(0).setSelected(true);
                chargeResults.add(0, chargeList.get(0));
            } else {
                chargeList.get(i).setSelected(false);
                chargeResults.add(chargeList.get(i));
            }
        }
        removeAll();
        addAll(chargeResults);
        notifyDataSetChanged();
    }

    }