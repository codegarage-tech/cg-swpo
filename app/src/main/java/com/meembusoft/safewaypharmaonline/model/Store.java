package com.meembusoft.safewaypharmaonline.model;

import org.parceler.Parcel;

@Parcel
public class Store {
    private String user_id = "";
    private String email = "";
    private String full_name = "";
    private String logo = "";
    private String phone = "";
    private String shop_name = "";
    private String address = "";

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Store{" +
                "user_id='" + user_id + '\'' +
                ", email='" + email + '\'' +
                ", full_name='" + full_name + '\'' +
                ", logo='" + logo + '\'' +
                ", phone='" + phone + '\'' +
                ", shop_name='" + shop_name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
