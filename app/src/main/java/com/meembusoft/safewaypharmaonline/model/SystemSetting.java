package com.meembusoft.safewaypharmaonline.model;


public class SystemSetting {
    private String id = "";
    private String sitename = "";
    private String logo = "";
    private String favicon = "";
    private String print_logo = "";
    private String topslogan = "";
    private String address = "";
    private String hotline = "";
    private String phone = "";
    private String email = "";
    private String keywords = "";
    private String descriptions = "";
    private String copyright = "";
    private String GA_ID = "";
    private String created = "";
    private String modified = "";


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSitename() {
        return sitename;
    }

    public void setSitename(String sitename) {
        this.sitename = sitename;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }

    public String getPrint_logo() {
        return print_logo;
    }

    public void setPrint_logo(String print_logo) {
        this.print_logo = print_logo;
    }

    public String getTopslogan() {
        return topslogan;
    }

    public void setTopslogan(String topslogan) {
        this.topslogan = topslogan;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHotline() {
        return hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
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

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getGA_ID() {
        return GA_ID;
    }

    public void setGA_ID(String GA_ID) {
        this.GA_ID = GA_ID;
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

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", sitename='" + sitename + '\'' +
                ", logo='" + logo + '\'' +
                ", favicon='" + favicon + '\'' +
                ", print_logo='" + print_logo + '\'' +
                ", topslogan='" + topslogan + '\'' +
                ", address='" + address + '\'' +
                ", hotline='" + hotline + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", keywords='" + keywords + '\'' +
                ", descriptions='" + descriptions + '\'' +
                ", copyright='" + copyright + '\'' +
                ", GA_ID='" + GA_ID + '\'' +
                ", created='" + created + '\'' +
                ", modified='" + modified + '\'' +
                '}';
    }
}
