package com.meembusoft.safewaypharmaonline.model;

import org.parceler.Parcel;

@Parcel
public class OrderItems {
    private String product_ids = "";
    private String name = "";
    private String supplier_name = "";
    private String form_name = "";
    private String generic_name = "";
    private String product_image = "";
    private String prices = "";
    private String quantitys = "";
    private String sub_total_prices = "";

    public String getProduct_ids() {
        return product_ids;
    }

    public void setProduct_ids(String product_ids) {
        this.product_ids = product_ids;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getForm_name() {
        return form_name;
    }

    public void setForm_name(String form_name) {
        this.form_name = form_name;
    }

    public String getGeneric_name() {
        return generic_name;
    }

    public void setGeneric_name(String generic_name) {
        this.generic_name = generic_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public String getQuantitys() {
        return quantitys;
    }

    public void setQuantitys(String quantitys) {
        this.quantitys = quantitys;
    }

    public String getSub_total_prices() {
        return sub_total_prices;
    }

    public void setSub_total_prices(String sub_total_prices) {
        this.sub_total_prices = sub_total_prices;
    }

    @Override
    public String toString() {
        return "{" +
                "product_ids='" + product_ids + '\'' +
                ", name='" + name + '\'' +
                ", supplier_name='" + supplier_name + '\'' +
                ", form_name='" + form_name + '\'' +
                ", generic_name='" + generic_name + '\'' +
                ", product_image='" + product_image + '\'' +
                ", prices='" + prices + '\'' +
                ", quantitys='" + quantitys + '\'' +
                ", sub_total_prices='" + sub_total_prices + '\'' +
                '}';
    }
}
