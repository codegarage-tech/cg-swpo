package com.meembusoft.safewaypharmaonline.model;

public class ParamsSupplierProfileUpdate {
    private String user_id = "";
    private String name = "";
    private String phone = "";
    private String email = "";
    private String shop_name = "";
    private String address = "";
    private String longitude = "";
    private String latitude = "";
    private String password = "";
    private String logo = "";

    public ParamsSupplierProfileUpdate(String user_id, String name, String phone, String email, String shop_name, String address, String longitude, String latitude, String password, String logo) {
        this.user_id = user_id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.shop_name = shop_name;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.password = password;
        this.logo = logo;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public String
    toString() {
        return "{" +
                "user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", shop_name='" + shop_name + '\'' +
                ", address='" + address + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", password='" + password + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }
}
