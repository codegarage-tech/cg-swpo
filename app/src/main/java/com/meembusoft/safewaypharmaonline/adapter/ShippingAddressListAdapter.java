package com.meembusoft.safewaypharmaonline.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.meembusoft.safewaypharmaonline.model.CommonData;
import com.meembusoft.safewaypharmaonline.model.ShippingAddress;
import com.meembusoft.safewaypharmaonline.viewholder.ShippingAddressViewHolder;
import com.meembusoft.safewaypharmaonline.viewholder.WhyChooseUsViewHolder;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class ShippingAddressListAdapter extends RecyclerArrayAdapter<ShippingAddress> {

    private static final int VIEW_TYPE_REGULAR = 1;

    public ShippingAddressListAdapter(Context context) {
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
                return new ShippingAddressViewHolder(parent);
            default:
                throw new InvalidParameterException();
        }
    }

    public void setAdapterData(List<ShippingAddress> shippingAddressesList,ShippingAddress shippingAddress) {
        List<ShippingAddress> mShippingAddressesList = new ArrayList<>();
        for (int i = 0; i < shippingAddressesList.size(); i++) {
            if ( shippingAddress != null) {
                if (shippingAddressesList.get(i).getId().equalsIgnoreCase(shippingAddress.getId())) {
                    shippingAddressesList.get(i).setSelected(true);
                    mShippingAddressesList.add(0, shippingAddressesList.get(i));
                } else {
                    shippingAddressesList.get(i).setSelected(false);
                    mShippingAddressesList.add(shippingAddressesList.get(i));
                }
            } else  {
                if (i==0){
                    shippingAddressesList.get(0).setSelected(true);
                    mShippingAddressesList.add(0, shippingAddressesList.get(0));
                } else {
                    shippingAddressesList.get(i).setSelected(false);
                    mShippingAddressesList.add(shippingAddressesList.get(i));
                }
            }
        }
        removeAll();
        addAll(mShippingAddressesList);
        notifyDataSetChanged();
    }

}