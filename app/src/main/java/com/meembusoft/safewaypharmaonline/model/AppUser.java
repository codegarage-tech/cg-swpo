package com.meembusoft.safewaypharmaonline.model;

public class AppUser {
    private String id = "";
    private String name = "";
    private String slug = "";
    private String customer_code = "";
    private String pharmacy_name = "";
    private String customer_type_id = "";
    private String barcode = "";
    private String email = "";
    private String drug_or_tread_licence = "";
    private String phone = "";
    private String address = "";
    private String username = "";
    private String deviceid = "";
    private String user_type = "";
    private String due_active = "";
    private String dateofbirth = "";
    private String gender = "";
    private String password = "";
    private String token = "";
    private String CamPhoto = "";
    private String CanvasPhoto = "";
    private String thumb_photo = "";
    private String target_amount = "";
    private String regular_discount = "";
    private String target_discount = "";
    private String remarks = "";
    private String credits = "";
    private String isActive = "";
    private String user_id = "";
    private String created = "";
    private String modified = "";
    private ShippingAddress shiping_address = null;
    public AppUser(String id, String name, String phone, String password) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.password = password;
    }

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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(String customer_code) {
        this.customer_code = customer_code;
    }

    public String getPharmacy_name() {
        return pharmacy_name;
    }

    public void setPharmacy_name(String pharmacy_name) {
        this.pharmacy_name = pharmacy_name;
    }

    public String getCustomer_type_id() {
        return customer_type_id;
    }

    public void setCustomer_type_id(String customer_type_id) {
        this.customer_type_id = customer_type_id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDrug_or_tread_licence() {
        return drug_or_tread_licence;
    }

    public void setDrug_or_tread_licence(String drug_or_tread_licence) {
        this.drug_or_tread_licence = drug_or_tread_licence;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getDue_active() {
        return due_active;
    }

    public void setDue_active(String due_active) {
        this.due_active = due_active;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCamPhoto() {
        return CamPhoto;
    }

    public void setCamPhoto(String camPhoto) {
        CamPhoto = camPhoto;
    }

    public String getCanvasPhoto() {
        return CanvasPhoto;
    }

    public void setCanvasPhoto(String canvasPhoto) {
        CanvasPhoto = canvasPhoto;
    }

    public String getThumb_photo() {
        return thumb_photo;
    }

    public void setThumb_photo(String thumb_photo) {
        this.thumb_photo = thumb_photo;
    }

    public String getTarget_amount() {
        return target_amount;
    }

    public void setTarget_amount(String target_amount) {
        this.target_amount = target_amount;
    }

    public String getRegular_discount() {
        return regular_discount;
    }

    public void setRegular_discount(String regular_discount) {
        this.regular_discount = regular_discount;
    }

    public String getTarget_discount() {
        return target_discount;
    }

    public void setTarget_discount(String target_discount) {
        this.target_discount = target_discount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public ShippingAddress getShiping_address() {
        return shiping_address;
    }

    public void setShiping_address(ShippingAddress shiping_address) {
        this.shiping_address = shiping_address;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", customer_code='" + customer_code + '\'' +
                ", pharmacy_name='" + pharmacy_name + '\'' +
                ", customer_type_id='" + customer_type_id + '\'' +
                ", barcode='" + barcode + '\'' +
                ", email='" + email + '\'' +
                ", drug_or_tread_licence='" + drug_or_tread_licence + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", username='" + username + '\'' +
                ", deviceid='" + deviceid + '\'' +
                ", user_type='" + user_type + '\'' +
                ", due_active='" + due_active + '\'' +
                ", dateofbirth='" + dateofbirth + '\'' +
                ", gender='" + gender + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                ", CamPhoto='" + CamPhoto + '\'' +
                ", CanvasPhoto='" + CanvasPhoto + '\'' +
                ", thumb_photo='" + thumb_photo + '\'' +
                ", target_amount='" + target_amount + '\'' +
                ", regular_discount='" + regular_discount + '\'' +
                ", target_discount='" + target_discount + '\'' +
                ", remarks='" + remarks + '\'' +
                ", credits='" + credits + '\'' +
                ", isActive='" + isActive + '\'' +
                ", user_id='" + user_id + '\'' +
                ", created='" + created + '\'' +
                ", modified='" + modified + '\'' +
                ", shiping_address=" + shiping_address +
                '}';
    }
}
