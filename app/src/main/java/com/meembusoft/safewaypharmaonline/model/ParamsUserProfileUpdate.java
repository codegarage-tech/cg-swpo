package com.meembusoft.safewaypharmaonline.model;

public class ParamsUserProfileUpdate {
    private String customer_id = "";
    private String name = "";
    private String phone = "";
    private String email = "";
    private String address = "";
    private String age = "";
    private String gender = "";
    private String dateofbirth = "";
    private String blood = "";
    private String password = "";
    private String thumb_photo = "";

    public ParamsUserProfileUpdate(String customer_id, String name, String phone, String email, String address, String age, String gender, String dateofbirth,String blood, String password, String thumb_photo) {
        this.customer_id = customer_id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.age = age;
        this.gender = gender;
        this.dateofbirth = dateofbirth;
        this.blood = blood;
        this.password = password;
        this.thumb_photo = thumb_photo;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }


    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getThumb_photo() {
        return thumb_photo;
    }

    public void setThumb_photo(String thumb_photo) {
        this.thumb_photo = thumb_photo;
    }

    @Override
    public String toString() {
        return "ParamsUserProfileUpdate{" +
                "customer_id='" + customer_id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                ", dateofbirth='" + dateofbirth + '\'' +
                ", blood='" + blood + '\'' +
                ", password='" + password + '\'' +
                ", thumb_photo='" + thumb_photo + '\'' +
                '}';
    }
}
