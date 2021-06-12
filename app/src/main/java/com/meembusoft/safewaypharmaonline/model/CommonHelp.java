package com.meembusoft.safewaypharmaonline.model;

import org.parceler.Parcel;

@Parcel
public class CommonHelp {

    private String id = "";
    private String name = "";
    private String category = "";
    private String phone = "";
    private String email = "";
    private String hotlinenumber ="" ;
    private String  address = "";
    private String hospital = "";
    private String chamber = "";
    private String specialized = "";
    private String latitude = "";
    private String longitude = "";
    private String remarks = "";
    private String isActive = "";
    private String created = "";
    private String  modified = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getHotlinenumber() {
        return hotlinenumber;
    }

    public void setHotlinenumber(String hotlinenumber) {
        this.hotlinenumber = hotlinenumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getChamber() {
        return chamber;
    }

    public void setChamber(String chamber) {
        this.chamber = chamber;
    }

    public String getSpecialized() {
        return specialized;
    }

    public void setSpecialized(String specialized) {
        this.specialized = specialized;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", hotlinenumber='" + hotlinenumber + '\'' +
                ", address='" + address + '\'' +
                ", hospital='" + hospital + '\'' +
                ", chamber='" + chamber + '\'' +
                ", specialized='" + specialized + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", remarks='" + remarks + '\'' +
                ", isActive='" + isActive + '\'' +
                ", created='" + created + '\'' +
                ", modified='" + modified + '\'' +
                '}';
    }
}
