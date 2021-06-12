package com.meembusoft.safewaypharmaonline.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.viewholder.MedicineByCategoryViewHolder;
import com.meembusoft.safewaypharmaonline.viewholder.MyOrderListViewHolder;

import java.security.InvalidParameterException;
import java.util.List;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class MyOrderListAdapter extends RecyclerArrayAdapter<StaggeredMedicineByItem> {

    private static final int VIEW_TYPE_REGULAR = 1;

    public MyOrderListAdapter(Context context) {
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
                return new MyOrderListViewHolder(parent);
            default:
                throw new InvalidParameterException();
        }
    }

    public  boolean isCheckoutItemStored() {
        List<StaggeredMedicineByItem> medicineItems = getAllData();
        for (StaggeredMedicineByItem staggeredMedicineByItem : medicineItems) {
            if (staggeredMedicineByItem.getIsCheckedItem()) {
                return true;
            }
        }
        return false;
    }


    public int getItemPosition(StaggeredMedicineByItem medicineItem) {
        List<StaggeredMedicineByItem> medicineItems = getAllData();
        for (int i = 0; i < medicineItems.size(); i++) {
            if (medicineItems.get(i).getName().equalsIgnoreCase(medicineItem.getName())) {
                return i;
            }
        }
        return -1;
    }

    public void removeItem(StaggeredMedicineByItem medicineItemsItem) {
        int position = getItemPosition(medicineItemsItem);
        if (position != -1) {
            remove(position);
            notifyDataSetChanged();
        }
    }

}