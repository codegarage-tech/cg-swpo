package com.meembusoft.safewaypharmaonline.model;

import org.parceler.Parcel;

@Parcel
public class Category {
    private String id = "";
    private String name = "";
    private String thumb_image = "";
    private String icon_image = "";
     boolean isSelected = false;

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

    public String getIcon_image() {
        return icon_image;
    }

    public void setIcon_image(String icon_image) {
        this.icon_image = icon_image;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", thumb_image='" + thumb_image + '\'' +
                ", icon_image='" + icon_image + '\'' +
                '}';
    }


}
