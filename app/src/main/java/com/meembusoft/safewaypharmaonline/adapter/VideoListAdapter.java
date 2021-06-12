package com.meembusoft.safewaypharmaonline.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.meembusoft.safewaypharmaonline.model.Category;
import com.meembusoft.safewaypharmaonline.model.Videos;
import com.meembusoft.safewaypharmaonline.viewholder.CategoryViewHolder;
import com.meembusoft.safewaypharmaonline.viewholder.VideoViewHolder;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class VideoListAdapter extends RecyclerArrayAdapter<Videos> {

    private static final int VIEW_TYPE_REGULAR = 1;

    public VideoListAdapter(Context context) {
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
                return new VideoViewHolder(parent);
            default:
                throw new InvalidParameterException();
        }
    }


    }