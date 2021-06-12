package com.meembusoft.safewaypharmaonline.model;

public class AllProductMedicines {
   
    private String id = "";
    private String name = "";
    private String trade_price = "";
    private String selling_price = "";
    private String expire_date = "";
    private String sImage = "";
    private String master_supplier_id = "";
    private String supplier_name = "";
    private String master_supplier_code = "";
    private String master_form_id = "";
    private String form_name = "";
    private String generic_name = "";
    private String box_size = "";
    private String box_price = "";
    private String thumb_photo = "";
    private String favourite = "";
    private String product_image = "";

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

    public String getTrade_price() {
        return trade_price;
    }

    public void setTrade_price(String trade_price) {
        this.trade_price = trade_price;
    }

    public String getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(String selling_price) {
        this.selling_price = selling_price;
    }

    public String getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(String expire_date) {
        this.expire_date = expire_date;
    }

    public String getsImage() {
        return sImage;
    }

    public void setsImage(String sImage) {
        this.sImage = sImage;
    }

    public String getMaster_supplier_id() {
        return master_supplier_id;
    }

    public void setMaster_supplier_id(String master_supplier_id) {
        this.master_supplier_id = master_supplier_id;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getMaster_supplier_code() {
        return master_supplier_code;
    }

    public void setMaster_supplier_code(String master_supplier_code) {
        this.master_supplier_code = master_supplier_code;
    }

    public String getMaster_form_id() {
        return master_form_id;
    }

    public void setMaster_form_id(String master_form_id) {
        this.master_form_id = master_form_id;
    }

    public String getForm_name() {
        return form_name;
    }

    public void setForm_name(String form_name) {
        this.form_name = form_name;
    }

    public String getGeneric_name() {
        return generic_name;
    }

    public void setGeneric_name(String generic_name) {
        this.generic_name = generic_name;
    }

    public String getBox_size() {
        return box_size;
    }

    public void setBox_size(String box_size) {
        this.box_size = box_size;
    }

    public String getBox_price() {
        return box_price;
    }

    public void setBox_price(String box_price) {
        this.box_price = box_price;
    }

    public String getThumb_photo() {
        return thumb_photo;
    }

    public void setThumb_photo(String thumb_photo) {
        this.thumb_photo = thumb_photo;
    }

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    @Override
    public String toString() {
        return "AllProductMedicines{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", trade_price='" + trade_price + '\'' +
                ", selling_price='" + selling_price + '\'' +
                ", expire_date='" + expire_date + '\'' +
                ", sImage='" + sImage + '\'' +
                ", master_supplier_id='" + master_supplier_id + '\'' +
                ", supplier_name='" + supplier_name + '\'' +
                ", master_supplier_code='" + master_supplier_code + '\'' +
                ", master_form_id='" + master_form_id + '\'' +
                ", form_name='" + form_name + '\'' +
                ", generic_name='" + generic_name + '\'' +
                ", box_size='" + box_size + '\'' +
                ", box_price='" + box_price + '\'' +
                ", thumb_photo='" + thumb_photo + '\'' +
                ", favourite='" + favourite + '\'' +
                ", product_image='" + product_image + '\'' +
                '}';
    }
}
