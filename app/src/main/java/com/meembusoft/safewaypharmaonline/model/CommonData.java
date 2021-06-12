package com.meembusoft.safewaypharmaonline.model;

import org.parceler.Parcel;

@Parcel
public class CommonData {
    private String id = "";
    private String name = "";
    private String thumb_image = "";
    private String icon = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", thumb_image='" + thumb_image + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
