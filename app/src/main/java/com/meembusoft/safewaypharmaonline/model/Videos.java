package com.meembusoft.safewaypharmaonline.model;

import org.parceler.Parcel;

@Parcel
public class Videos {
    private String id = "";
    private String title = "";
    private String video_link = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo_link() {
        return video_link;
    }

    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", video_link='" + video_link + '\'' +
                '}';
    }
}
