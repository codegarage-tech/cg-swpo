package com.meembusoft.safewaypharmaonline.model;

public class ParamsUserRegistration {
    private String full_name = "";
    private String phone = "";
    private String password = "";

    public ParamsUserRegistration(String full_name, String phone, String password) {
        this.full_name = full_name;
        this.phone = phone;
        this.password = password;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
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
        return "ParamsUserRegistration{" +
                "full_name='" + full_name + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
