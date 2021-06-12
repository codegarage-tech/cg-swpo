package com.meembusoft.safewaypharmaonline.model;

import org.parceler.Parcel;

@Parcel
public class Promos {
    private String id = "";
    private String img_url = "";
    private String title = "";
    private String link = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", img_url='" + img_url + '\'' +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
