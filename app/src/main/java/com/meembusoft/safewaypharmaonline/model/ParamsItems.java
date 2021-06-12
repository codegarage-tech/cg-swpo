package com.meembusoft.safewaypharmaonline.model;


public class ParamsItems {
    private String product_ids = "";
    private String prices = "";
    private String quantitys = "";
    private String sub_total_prices = "";



    public String getProduct_ids() {
        return product_ids;
    }

    public void setProduct_ids(String product_ids) {
        this.product_ids = product_ids;
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
                ", prices='" + prices + '\'' +
                ", quantitys='" + quantitys + '\'' +
                ", sub_total_prices='" + sub_total_prices + '\'' +
                '}';
    }
}
