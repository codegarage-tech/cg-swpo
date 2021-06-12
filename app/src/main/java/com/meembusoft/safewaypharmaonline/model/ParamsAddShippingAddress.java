package com.meembusoft.safewaypharmaonline.model;

public class ParamsAddShippingAddress {
    private String customer_id = "";
    private String user_type = "";
    private String name = "";
    private String phone = "";
    private String street_address = "";
    private String city = "";
    private String state = "";
    private String zipcode = "";
    private String longitude = "";
    private String latitude = "";

    public ParamsAddShippingAddress(String customer_id, String user_type, String name, String phone, String street_address, String city, String state, String zipcode, String longitude, String latitude) {
        this.customer_id = customer_id;
        this.user_type = user_type;
        this.name = name;
        this.phone = phone;
        this.street_address = street_address;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.longitude = longitude;
        this.latitude = latitude;
    }

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

    public String getStreet_address() {
        return street_address;
    }

    public void setStreet_address(String street_address) {
        this.street_address = street_address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
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

    @Override
    public String toString() {
        return "{" +
                "customer_id='" + customer_id + '\'' +
                ", user_type='" + user_type + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", street_address='" + street_address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}
