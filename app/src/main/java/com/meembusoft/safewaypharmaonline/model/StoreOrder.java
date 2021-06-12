package com.meembusoft.safewaypharmaonline.model;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;
@Parcel
public class StoreOrder {
    private int totals_count ;
    private String our_sale = "";
    private String totals_done_amount = "";
    private List<PlaceOrderByItem> orders = new ArrayList<>();

    public int getTotals_count() {
        return totals_count;
    }

    public void setTotals_count(int totals_count) {
        this.totals_count = totals_count;
    }

    public String getOur_sale() {
        return our_sale;
    }

    public void setOur_sale(String our_sale) {
        this.our_sale = our_sale;
    }

    public String getTotals_done_amount() {
        return totals_done_amount;
    }

    public void setTotals_done_amount(String totals_done_amount) {
        this.totals_done_amount = totals_done_amount;
    }

    public List<PlaceOrderByItem> getOrders() {
        return orders;
    }

    public void setOrders(List<PlaceOrderByItem> orders) {
        this.orders = orders;
    }


    @Override
    public String toString() {
        return "StoreOrder{" +
                "totals_count=" + totals_count +
                ", our_sale='" + our_sale + '\'' +
                ", totals_done_amount='" + totals_done_amount + '\'' +
                ", orders=" + orders +
                '}';
    }
}
