package com.meembusoft.safewaypharmaonline.model;


public class ParamsUserLogin {
    private String phone = "";
    private String password = "";

    public ParamsUserLogin(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }



    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "ParamsUserLogin{" +
                "phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
