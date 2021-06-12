package com.meembusoft.safewaypharmaonline.model;

import org.parceler.Parcel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

@Parcel
public class StaggeredMedicineByItem extends RealmObject {

    @PrimaryKey
    private String id = "";
    private String name = "";
    private String discount_percent = "";
    private String origin = "";
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
    private int is_favourite = 0;
    private String product_image = "";
    private int item_quantity = 0;
    private String discount_percent_price = "";

    private Boolean IsCheckedItem = false;
    private int user_type = 0;
    public StaggeredMedicineByItem() {
    }

    public StaggeredMedicineByItem(String id, String name, String sImage) {
        this.id = id;
        this.name = name;
        this.sImage = sImage;
    }

    public StaggeredMedicineByItem(String id, String name,String discount_percent, String trade_price, String selling_price, String expire_date, String sImage, String master_supplier_id, String supplier_name, String master_supplier_code, String master_form_id, String form_name, String generic_name, String box_size, String box_price, String thumb_photo, String favourite, int is_favourite, String product_image, int item_quantity, Boolean isCheckedItem) {
        this.id = id;
        this.name = name;
        this.discount_percent = discount_percent;
        this.trade_price = trade_price;
        this.selling_price = selling_price;
        this.expire_date = expire_date;
        this.sImage = sImage;
        this.master_supplier_id = master_supplier_id;
        this.supplier_name = supplier_name;
        this.master_supplier_code = master_supplier_code;
        this.master_form_id = master_form_id;
        this.form_name = form_name;
        this.generic_name = generic_name;
        this.box_size = box_size;
        this.box_price = box_price;
        this.thumb_photo = thumb_photo;
        this.favourite = favourite;
        this.is_favourite = is_favourite;
        this.product_image = product_image;
        this.item_quantity = item_quantity;
        this.IsCheckedItem = isCheckedItem;
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

    public String getDiscount_percent() {
        return discount_percent;
    }

    public void setDiscount_percent(String discount_percent) {
        this.discount_percent = discount_percent;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Boolean getCheckedItem() {
        return IsCheckedItem;
    }

    public void setCheckedItem(Boolean checkedItem) {
        IsCheckedItem = checkedItem;
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

    public int getIs_favourite() {
        return is_favourite;
    }

    public void setIs_favourite(int is_favourite) {
        this.is_favourite = is_favourite;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public int getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(int item_quantity) {
        this.item_quantity = item_quantity;
    }

    public String getDiscount_percent_price() {
        return discount_percent_price;
    }

    public void setDiscount_percent_price(String discount_percent_price) {
        this.discount_percent_price = discount_percent_price;
    }

    public Boolean getIsCheckedItem() {
        return IsCheckedItem;
    }

    public void setIsCheckedItem(Boolean checkedItem) {
        IsCheckedItem = checkedItem;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    @Override
    public String toString() {
        return "StaggeredMedicineByItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", discount_percent='" + discount_percent + '\'' +
                ", origin='" + origin + '\'' +
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
                ", is_favourite=" + is_favourite +
                ", product_image='" + product_image + '\'' +
                ", item_quantity=" + item_quantity +
                ", discount_percent_price=" + discount_percent_price +
                ", IsCheckedItem=" + IsCheckedItem +
                ", user_type=" + user_type +
                '}';
    }
}
