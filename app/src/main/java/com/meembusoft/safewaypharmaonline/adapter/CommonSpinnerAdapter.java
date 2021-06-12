package com.meembusoft.safewaypharmaonline.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.model.DayScheduling;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Md. Hozrot Belal
 *         Email: belal.cse.brur@gmail.com
 */
public class CommonSpinnerAdapter<T> extends BaseAdapter {

    private Activity mActivity;
    private List<T> mData;
    private static LayoutInflater inflater = null;
    private ADAPTER_TYPE mAdapterType;

    public enum ADAPTER_TYPE {SELECT_PER_DAY,SELECT_SCHEDULING_TYPE}

    public CommonSpinnerAdapter(Activity activity, ADAPTER_TYPE adapterType) {
        mActivity = activity;
        mAdapterType = adapterType;
        mData = new ArrayList<T>();
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public int getItemPosition(String name) {
        for (int i = 0; i < mData.size(); i++) {

            if (mAdapterType == ADAPTER_TYPE.SELECT_PER_DAY) {
                DayScheduling spModel = (DayScheduling) mData.get(i);
                if (spModel.getTitle().equalsIgnoreCase(name)) {
                    return i;
                }

            } else if (mAdapterType == ADAPTER_TYPE.SELECT_SCHEDULING_TYPE){
                DayScheduling spModel = (DayScheduling) mData.get(i);
                if (spModel.getTitle().equalsIgnoreCase(name)) {
                    return i;
                }
            }




        }
        return -1;
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate( R.layout.spinner_row_user, null);

        TextView names = (TextView) vi.findViewById(R.id.tv_item_name);

        if (mAdapterType == ADAPTER_TYPE.SELECT_PER_DAY) {
            DayScheduling perDay= (DayScheduling) getItem(position);
            names.setText(perDay.getTitle());
        } else if (mAdapterType == ADAPTER_TYPE.SELECT_SCHEDULING_TYPE) {
            DayScheduling scheduling= (DayScheduling) getItem(position);
            names.setText(scheduling.getTitle());
        }




        return vi;
    }
}