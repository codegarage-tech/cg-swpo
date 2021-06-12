package com.meembusoft.safewaypharmaonline.adapter;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.meembusoft.safewaypharmaonline.enumeration.CategoryType;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.viewholder.SeeMoreStaggeredViewHolder;

import java.security.InvalidParameterException;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class SeeMoreStaggeredListAdapter extends RecyclerArrayAdapter<StaggeredMedicineByItem> {
    private String TAG = SeeMoreStaggeredListAdapter.class.getSimpleName();

    private static final int VIEW_TYPE_REGULAR = 1;
    CategoryType mMedicineType;

    public SeeMoreStaggeredListAdapter(Context context,CategoryType medicineType) {
        super(context);
        mMedicineType = medicineType;
        Log.d(TAG, "mMedicineType: " + mMedicineType.toString());
    }

    @Override
    public int getViewType(int position) {
        return VIEW_TYPE_REGULAR;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_REGULAR:
                return new SeeMoreStaggeredViewHolder(parent,mMedicineType);
            default:
                throw new InvalidParameterException();
        }
    }


}