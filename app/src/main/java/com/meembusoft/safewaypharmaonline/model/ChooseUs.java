package com.meembusoft.safewaypharmaonline.model;

import org.parceler.Parcel;

@Parcel
public class ChooseUs {
    private String name = "";
    private String icon = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "ChooseUs{" +
                "name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
