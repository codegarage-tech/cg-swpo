package com.meembusoft.safewaypharmaonline.model;

public class ParamsStoreAcceptOrder {
    private String store_id = "";
    private String order_id = "";

    public ParamsStoreAcceptOrder() {
    }

    public ParamsStoreAcceptOrder(String order_id) {
        this.order_id = order_id;
    }

    public ParamsStoreAcceptOrder(String store_id, String order_id) {
        this.store_id = store_id;
        this.order_id = order_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    @Override
    public String toString() {
        return "{" +
                "store_id='" + store_id + '\'' +
                ", order_id='" + order_id + '\'' +
                '}';
    }
}
