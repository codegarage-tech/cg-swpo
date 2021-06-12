package com.meembusoft.safewaypharmaonline.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.MedicineRemainderActivity;
import com.meembusoft.safewaypharmaonline.model.Remainder;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.reversecoder.library.util.AllSettingsManager;

import java.util.ArrayList;
import java.util.List;

public class SwipeRemainderAdapter extends RecyclerView.Adapter<SwipeRemainderAdapter.SwipeViewHolder> {

    private Context context;
    private List<Remainder> remainderList;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public SwipeRemainderAdapter(Context context, List<Remainder> mRemainderList) {
        this.context = context;
        this.remainderList = mRemainderList;
    }

    public void setReminder(List<Remainder> employees) {
        this.remainderList = new ArrayList<>();
        this.remainderList = employees;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SwipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_pillbox_view, viewGroup, false);
        return new SwipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SwipeViewHolder swipeViewHolder, int i) {
        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(swipeViewHolder.swipelayout, String.valueOf(remainderList.get(i).getId()));
        viewBinderHelper.closeLayout(String.valueOf(remainderList.get(i).getId()));
        swipeViewHolder.bindData(remainderList.get(i));


        swipeViewHolder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MedicineRemainderActivity)  context).updateRemainder(remainderList.get(i));
            }
        });

        swipeViewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MedicineRemainderActivity)  context).deleteRemainder(remainderList.get(i),i);
//                Toast.makeText(context, "delete"+i, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return remainderList.size();
    }

    class SwipeViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName,tvQuantity,tvPerday;
        private TextView tvEdit,tvDelete;
        private ImageView ivMedicine;
        private SwipeRevealLayout swipelayout;
        private RecyclerView rvGridTime;

        SwipeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_medicine_name);
            tvQuantity = itemView.findViewById(R.id.tv_medicine_quantity);
            tvPerday = itemView.findViewById(R.id.tv_medicine_per_day);
            tvEdit = itemView.findViewById(R.id.tv_edit);
            tvDelete = itemView.findViewById(R.id.tv_delete);
            swipelayout = itemView.findViewById(R.id.swipelayout);
            ivMedicine = itemView.findViewById(R.id.iv_medicine);
            rvGridTime = itemView.findViewById(R.id.rv_grid_time);


        }

        void bindData(Remainder remainder) {
            if (!AllSettingsManager.isNullOrEmpty(remainder.getName())){
                tvName.setText(remainder.getName());
                tvName.setVisibility(View.VISIBLE);
            } else {
                tvName.setVisibility(View.GONE);
            }

            tvQuantity.setText(context.getString(R.string.title_quantity)+" :"+remainder.getQuantity());
            if (remainder.getHow_many_per_day().equalsIgnoreCase("1")){
                tvPerday.setText(context.getString(R.string.txt_medicine_remainder_day_one));
            } else if (remainder.getHow_many_per_day().equalsIgnoreCase("2")){
                tvPerday.setText(context.getString(R.string.txt_medicine_remainder_day_two));
            } else {
                tvPerday.setText(""+remainder.getHow_many_per_day()+" "+context.getString(R.string.title_medicine_remainder_times_per_day));
            }
            if (!AllSettingsManager.isNullOrEmpty( remainder.getProduct_image())) {
                AppUtil.loadImage(context, ivMedicine,  remainder.getProduct_image(), false, false, true);
            } else {
                AppUtil.loadImage(context, ivMedicine, R.mipmap.capsule, false, false, true);

            }
            RemainderTimeGridAdapter mRemainderTimeGridAdapter = new RemainderTimeGridAdapter(context);
            rvGridTime.setLayoutManager(new GridLayoutManager(context,2));
            rvGridTime.setAdapter(mRemainderTimeGridAdapter);
            mRemainderTimeGridAdapter.addAll(remainder.getTimeLists());
        }
    }
}
