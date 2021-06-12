package com.meembusoft.safewaypharmaonline.model;


public class ParamsFavourite {
    private String product_id = "";
    private String customer_id = "";

    public ParamsFavourite(String product_id, String customer_id) {
        this.product_id = product_id;
        this.customer_id = customer_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    @Override
    public String toString() {
        return "ParamsFavourite{" +
                "product_id='" + product_id + '\'' +
                ", customer_id='" + customer_id + '\'' +
                '}';
    }
}
