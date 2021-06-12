package com.meembusoft.safewaypharmaonline.model;

import java.util.ArrayList;
import java.util.List;

public class ParamsDoOrder {

    private String customer_id = "";
    private String user_type = "";
    private String total_price ="" ;
    private String net_total = "";
    private String  discount = "";
    private String order_note = "";
    private String shiping_charge = "";
    private String shiping_charge_id = "";
    private String  total_item = "";
    private String  longitude = "";
    private String  latitude = "";
    private String payment_type = "";
    private String image = "";
    ShippingAddress shiping_address = null;
    List<ParamsItems> items = new ArrayList<>();


    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getNet_total() {
        return net_total;
    }

    public void setNet_total(String net_total) {
        this.net_total = net_total;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getOrder_note() {
        return order_note;
    }

    public void setOrder_note(String order_note) {
        this.order_note = order_note;
    }

    public String getShiping_charge() {
        return shiping_charge;
    }

    public void setShiping_charge(String shiping_charge) {
        this.shiping_charge = shiping_charge;
    }

    public String getShiping_charge_id() {
        return shiping_charge_id;
    }

    public void setShiping_charge_id(String shiping_charge_id) {
        this.shiping_charge_id = shiping_charge_id;
    }

    public String getTotal_item() {
        return total_item;
    }

    public void setTotal_item(String total_item) {
        this.total_item = total_item;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ShippingAddress getShiping_address() {
        return shiping_address;
    }

    public void setShiping_address(ShippingAddress shiping_address) {
        this.shiping_address = shiping_address;
    }

    public List<ParamsItems> getItems() {
        return items;
    }

    public void setItems(List<ParamsItems> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "ParamsDoOrder{" +
                "customer_id='" + customer_id + '\'' +
                ", user_type='" + user_type + '\'' +
                ", total_price='" + total_price + '\'' +
                ", net_total='" + net_total + '\'' +
                ", discount='" + discount + '\'' +
                ", order_note='" + order_note + '\'' +
                ", shiping_charge='" + shiping_charge + '\'' +
                ", shiping_charge_id='" + shiping_charge_id + '\'' +
                ", total_item='" + total_item + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", payment_type='" + payment_type + '\'' +
                ", image='" + image + '\'' +
                ", shiping_address=" + shiping_address +
                ", items=" + items +
                '}';
    }
}
