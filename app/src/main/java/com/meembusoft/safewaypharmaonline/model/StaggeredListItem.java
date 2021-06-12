package com.meembusoft.safewaypharmaonline.model;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class StaggeredListItem {

    private List<StaggeredMedicineByItem> items = new ArrayList<>();

    public StaggeredListItem(List<StaggeredMedicineByItem> items) {
        this.items = items;
    }

    public List<StaggeredMedicineByItem> getItems() {
        return items;
    }

    public void setItems(List<StaggeredMedicineByItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "{" +
                "items=" + items +
                '}';
    }
}