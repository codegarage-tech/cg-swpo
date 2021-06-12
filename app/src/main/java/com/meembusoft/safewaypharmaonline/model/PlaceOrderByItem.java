package com.meembusoft.safewaypharmaonline.model;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class PlaceOrderByItem {

    private String id = "";
    private String customer_id = "";
    private String payment_type = "";
    private String review_rating = "";
    private String status = "";
    private String image = "";
    private String total_price ="" ;
    private String discount = "";
    private String net_total = "";
    private String order_note = "";
    private String order_date = "";
    private String shiping_address = "";
    private String shiping_charge = "";
    private String shiping_charge_id = "";
    private String transaction_status = "";
    private String  total_item = "";
    private String  longitude = "";
    private String  latitude = "";
    private String  customer_name = "";
    private String  gender = "";
    private int   totals_count = 0;
    private List<OrderItems>   items = new ArrayList<>();
    private SupplierInfo supplier_information = new SupplierInfo();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReview_rating() {
        return review_rating;
    }

    public void setReview_rating(String review_rating) {
        this.review_rating = review_rating;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getNet_total() {
        return net_total;
    }

    public void setNet_total(String net_total) {
        this.net_total = net_total;
    }

    public String getOrder_note() {
        return order_note;
    }

    public void setOrder_note(String order_note) {
        this.order_note = order_note;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getShiping_address() {
        return shiping_address;
    }

    public void setShiping_address(String shiping_address) {
        this.shiping_address = shiping_address;
    }

    public String getShiping_charge() {
        return shiping_charge;
    }

    public void setShiping_charge(String shiping_charge) {
        this.shiping_charge = shiping_charge;
    }

    public String getTransaction_status() {
        return transaction_status;
    }

    public void setTransaction_status(String transaction_status) {
        this.transaction_status = transaction_status;
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

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getTotals_count() {
        return totals_count;
    }

    public void setTotals_count(int totals_count) {
        this.totals_count = totals_count;
    }

    public List<OrderItems> getItems() {
        return items;
    }

    public void setItems(List<OrderItems> items) {
        this.items = items;
    }

    public SupplierInfo getSupplier_information() {
        return supplier_information;
    }

    public void setSupplier_information(SupplierInfo supplier_information) {
        this.supplier_information = supplier_information;
    }

    public String getShiping_charge_id() {
        return shiping_charge_id;
    }

    public void setShiping_charge_id(String shiping_charge_id) {
        this.shiping_charge_id = shiping_charge_id;
    }

    @Override
    public String toString() {
        return "PlaceOrderByItem{" +
                "id='" + id + '\'' +
                ", customer_id='" + customer_id + '\'' +
                ", payment_type='" + payment_type + '\'' +
                ", review_rating='" + review_rating + '\'' +
                ", status='" + status + '\'' +
                ", image='" + image + '\'' +
                ", total_price='" + total_price + '\'' +
                ", discount='" + discount + '\'' +
                ", net_total='" + net_total + '\'' +
                ", order_note='" + order_note + '\'' +
                ", order_date='" + order_date + '\'' +
                ", shiping_address='" + shiping_address + '\'' +
                ", shiping_charge='" + shiping_charge + '\'' +
                ", shiping_charge_id='" + shiping_charge_id + '\'' +
                ", transaction_status='" + transaction_status + '\'' +
                ", total_item='" + total_item + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", customer_name='" + customer_name + '\'' +
                ", gender='" + gender + '\'' +
                ", totals_count=" + totals_count +
                ", items=" + items +
                ", supplier_information=" + supplier_information +
                '}';
    }
}
