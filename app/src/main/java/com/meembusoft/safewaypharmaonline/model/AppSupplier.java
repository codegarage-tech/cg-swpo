package com.meembusoft.safewaypharmaonline.model;


public class AppSupplier {
    private String user_id = "";
    private String username = "";
    private String email = "";
    private String full_name = "";
    private String logo = "";
    private String slug = "";
    private String doc = "";
    private String terms = "";
    private String user_type = "";
    private String employee_store_id = "";
    private String phone = "";
    private String shop_name = "";
    private String counter_total = "";
    private String address = "";
    private String country = "";
    private String post_code = "";
    private String password = "";
    private String token = "";
    private String isSocial = "";
    private String isActive = "";
    private String modified = "";
    private String created = "";
    private ShippingAddress shiping_address = null;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getEmployee_store_id() {
        return employee_store_id;
    }

    public void setEmployee_store_id(String employee_store_id) {
        this.employee_store_id = employee_store_id;
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

    public String getCounter_total() {
        return counter_total;
    }

    public void setCounter_total(String counter_total) {
        this.counter_total = counter_total;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPost_code() {
        return post_code;
    }

    public void setPost_code(String post_code) {
        this.post_code = post_code;
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

    public String getIsSocial() {
        return isSocial;
    }

    public void setIsSocial(String isSocial) {
        this.isSocial = isSocial;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
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
                "user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", full_name='" + full_name + '\'' +
                ", logo='" + logo + '\'' +
                ", slug='" + slug + '\'' +
                ", doc='" + doc + '\'' +
                ", terms='" + terms + '\'' +
                ", user_type='" + user_type + '\'' +
                ", employee_store_id='" + employee_store_id + '\'' +
                ", phone='" + phone + '\'' +
                ", shop_name='" + shop_name + '\'' +
                ", counter_total='" + counter_total + '\'' +
                ", address='" + address + '\'' +
                ", country='" + country + '\'' +
                ", post_code='" + post_code + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                ", isSocial='" + isSocial + '\'' +
                ", isActive='" + isActive + '\'' +
                ", modified='" + modified + '\'' +
                ", created='" + created + '\'' +
                ", shiping_address=" + shiping_address +
                '}';
    }
}
