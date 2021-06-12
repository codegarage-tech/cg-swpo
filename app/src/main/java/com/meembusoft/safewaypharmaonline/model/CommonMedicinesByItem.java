package com.meembusoft.safewaypharmaonline.model;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class CommonMedicinesByItem {
    private int totals_count ;
    private List<StaggeredMedicineByItem> medicines = new ArrayList<>();
    private List<Emergency> ermergency = new ArrayList<>();


    public int getTotals_count() {
        return totals_count;
    }

    public void setTotals_count(int totals_count) {
        this.totals_count = totals_count;
    }

    public List<StaggeredMedicineByItem> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<StaggeredMedicineByItem> medicines) {
        this.medicines = medicines;
    }

    public List<Emergency> getErmergency() {
        return ermergency;
    }

    public void setErmergency(List<Emergency> ermergency) {
        this.ermergency = ermergency;
    }

    @Override
    public String toString() {
        return "{" +
                "totals_count=" + totals_count +
                ", medicines=" + medicines +
                ", ermergency=" + ermergency +
                '}';
    }
}
