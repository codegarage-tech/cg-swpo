package com.meembusoft.safewaypharmaonline.model;

public class ResponseOrderItem {
    private String order_id = "";

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    @Override
    public String toString() {
        return "ResponseOrderItem{" +
                "order_id='" + order_id + '\'' +
                '}';
    }
}
