package com.meembusoft.safewaypharmaonline.viewholder;

import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.CheckoutOrderActivity;
import com.meembusoft.safewaypharmaonline.activity.MedicineRemainderActivity;
import com.meembusoft.safewaypharmaonline.adapter.CheckoutOrderAdapter;
import com.meembusoft.safewaypharmaonline.adapter.FilterSessionLeftGridListAdapter;
import com.meembusoft.safewaypharmaonline.dialog.ReminderStatusDialog;
import com.meembusoft.safewaypharmaonline.dialog.RemoveMedicineFromCartDialog;
import com.meembusoft.safewaypharmaonline.model.FilterSessionLeft;
import com.meembusoft.safewaypharmaonline.model.Promos;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.DateManager;
import com.melih.strokedtextview.StrokedTextView;
import com.reversecoder.library.event.OnSingleClickListener;
import com.reversecoder.library.util.AllSettingsManager;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class FilterSessionLeftViewHolder extends BaseViewHolder<FilterSessionLeft> {

    private static String TAG = FilterSessionLeftViewHolder.class.getSimpleName();
    private ImageView ivPromos;
    private StrokedTextView tvStrokedTitle;
    private TextView tvMedicine,tvTime,tvStatus;
    private RecyclerView rvGridImg;


    public FilterSessionLeftViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_filter_session_left_view);

        tvTime = (TextView) $(R.id.tv_time);
        tvMedicine = (TextView) $(R.id.tv_medicine);
        tvStatus = (TextView) $(R.id.tv_status);
        rvGridImg = (RecyclerView) $(R.id.rv_grid_img);
    }

    @Override
    public void setData(final FilterSessionLeft data) {

        //String getTime = DateManager.millsToTimeFormat(data.getTime());
        final MedicineRemainderActivity mActivity = ((MedicineRemainderActivity) getContext());

        tvTime.setText(AppUtil.optStringNullCheckValue(data.getFormattedTime()));
        if (AppUtil.optStringNullCheckValue(data.getStatus()).equalsIgnoreCase("MISSED")){
            tvStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.red_500));
        }  else {
            tvStatus.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));

        }
        tvStatus.setText(AppUtil.optStringNullCheckValue(data.getStatus()));
        //Set data
        FilterSessionLeftGridListAdapter mFilterSessionLeftGridListAdapter = new FilterSessionLeftGridListAdapter(getContext());
        rvGridImg.setLayoutManager(new GridLayoutManager(getContext(),3));
        rvGridImg.setAdapter(mFilterSessionLeftGridListAdapter);
        mFilterSessionLeftGridListAdapter.addAll(data.getImages());

        String getMedicine = "";
        for(int i=0;i<data.getDescriptions().size();i++){
            if(i==data.getDescriptions().size()-1){
                getMedicine = getMedicine +data.getDescriptions().get(i)+"";
            }
            else{
                getMedicine = getMedicine+data.getDescriptions().get(i)+",";
            }

        }

        if (!AllSettingsManager.isNullOrEmpty(getMedicine)){
            tvMedicine.setText(getMedicine);
            tvMedicine.setVisibility(View.VISIBLE);
        } else {
            tvMedicine.setText("");
            tvMedicine.setVisibility(View.GONE);
        }
        if (data.getTime() >0) {
            mActivity.startReceiver(data.getTime());
        }

        tvStatus.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (AppUtil.optStringNullCheckValue(data.getStatus()).equalsIgnoreCase("MISSED")) {

                    ReminderStatusDialog reminderStatusDialog = new ReminderStatusDialog(((MedicineRemainderActivity) getContext()), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Update Status Medicine reminder
                                    mActivity.updateStatusReminder(data);
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                                case DialogInterface.BUTTON_NEUTRAL:
                                    break;
                            }
                        }
                    });
                    reminderStatusDialog.initView().show();
                }
            }
        });

    }
}