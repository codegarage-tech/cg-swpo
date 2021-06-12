package com.meembusoft.safewaypharmaonline.viewholder;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.meembusoft.safewaypharmaonline.R;
import com.meembusoft.safewaypharmaonline.activity.AllVideoListActivity;
import com.meembusoft.safewaypharmaonline.activity.CategoryActivity;
import com.meembusoft.safewaypharmaonline.activity.HomeActivity;
import com.meembusoft.safewaypharmaonline.adapter.CategoryListAdapter;
import com.meembusoft.safewaypharmaonline.model.Category;
import com.meembusoft.safewaypharmaonline.model.Videos;
import com.meembusoft.safewaypharmaonline.util.AppUtil;
import com.meembusoft.safewaypharmaonline.util.Logger;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.reversecoder.library.event.OnSingleClickListener;

import java.util.List;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class VideoViewHolder extends BaseViewHolder<Videos> {

    private static String TAG = VideoViewHolder.class.getSimpleName();
    YouTubePlayerView youTubePlayerView;
    public VideoViewHolder(ViewGroup parent) {
        super(parent, R.layout.row_video_feature);
        youTubePlayerView = (YouTubePlayerView) $(R.id.youtube_player_view);

    }

    @Override
    public void setData(final Videos videos) {
        //Set data
       ((AllVideoListActivity) getContext()).getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = "r8kE7rSzfQs";
                youTubePlayer.loadVideo(videos.getVideo_link(), 0);
                youTubePlayer.pause();

            }
        });
    }

}