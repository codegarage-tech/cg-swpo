package com.meembusoft.safewaypharmaonline.medicinereminder.service;

import com.meembusoft.safewaypharmaonline.model.FilterSessionLeft;

import java.util.ArrayList;
import java.util.List;

public class MedicineReminders {

    List<FilterSessionLeft> data = new ArrayList<>();

    public MedicineReminders(List<FilterSessionLeft> data) {
        this.data = data;
    }

    public List<FilterSessionLeft> getData() {
        return data;
    }

    public void setData(List<FilterSessionLeft> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{" +
                "data=" + data +
                '}';
    }
}
