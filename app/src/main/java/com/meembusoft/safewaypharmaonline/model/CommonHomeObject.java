package com.meembusoft.safewaypharmaonline.model;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class CommonHomeObject {
    private String id = "";
    private String title = "";
    private List<StaggeredMedicineByItem> item = new ArrayList<>();

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

    public List<StaggeredMedicineByItem> getItem() {
        return item;
    }

    public void setItem(List<StaggeredMedicineByItem> item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", item=" + item +
                '}';
    }
}
