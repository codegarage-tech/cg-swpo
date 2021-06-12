package com.meembusoft.safewaypharmaonline.adapter;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.meembusoft.safewaypharmaonline.model.StaggeredMedicineByItem;
import com.meembusoft.safewaypharmaonline.viewholder.MedicineByCategoryViewHolder;

import java.security.InvalidParameterException;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class MedicineByCategoryListAdapter extends RecyclerArrayAdapter<StaggeredMedicineByItem> {
    private String TAG = MedicineByCategoryListAdapter.class.getSimpleName();

    private static final int VIEW_TYPE_REGULAR = 1;
    private String mCategoryName = "";
    public MedicineByCategoryListAdapter(Context context,String categoryName) {
        super(context);
        mCategoryName = categoryName;
//        Log.d(TAG, "mCategoryName: " + mCategoryName.toString());
    }

    @Override
    public int getViewType(int position) {
        return VIEW_TYPE_REGULAR;
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_REGULAR:
                return new MedicineByCategoryViewHolder(parent,mCategoryName);
            default:
                throw new InvalidParameterException();
        }
    }


}