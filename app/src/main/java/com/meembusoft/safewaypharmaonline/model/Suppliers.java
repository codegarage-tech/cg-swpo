package com.meembusoft.safewaypharmaonline.model;


public class Suppliers {
    private String id = "";
    private String supplier_code = "";
    private String name = "";
    private String slug = "";
    private String email = "";
    private String phone = "";
    private String address = "";
    private String thumb_photo = "";
    private String details = "";
    private String user_id = "";
    private String isActive = "";
    private String created = "";
    private String copyright = "";
    private String modified = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSupplier_code() {
        return supplier_code;
    }

    public void setSupplier_code(String supplier_code) {
        this.supplier_code = supplier_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getThumb_photo() {
        return thumb_photo;
    }

    public void setThumb_photo(String thumb_photo) {
        this.thumb_photo = thumb_photo;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    @Override
    public String toString() {
        return "Suppliers{" +
                "id='" + id + '\'' +
                ", supplier_code='" + supplier_code + '\'' +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", thumb_photo='" + thumb_photo + '\'' +
                ", details='" + details + '\'' +
                ", user_id='" + user_id + '\'' +
                ", isActive='" + isActive + '\'' +
                ", created='" + created + '\'' +
                ", copyright='" + copyright + '\'' +
                ", modified='" + modified + '\'' +
                '}';
    }
}
