package com.meembusoft.safewaypharmaonline.model;

import org.parceler.Parcel;

import java.util.List;

public class Remainder {
    private String id = "";
    private String medicines_id = "";
    private String name = "";
    private String quantity = "";
    private String schedule_time = "";
    private String schudule_type = "";
    private String start_from_day = "";
    private String start_from = "";
    private String remind_me = "";
    private String customer_id = "";
    private String how_many_per_day = "";
    private String is_deleted = "";
    private String medicine_name = "";
    private String sImage = "";
    private String thumb_photo = "";
    private String product_image = "";
    private List<String> timeLists = null;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMedicines_id() {
        return medicines_id;
    }

    public void setMedicines_id(String medicines_id) {
        this.medicines_id = medicines_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSchedule_time() {
        return schedule_time;
    }

    public void setSchedule_time(String schedule_time) {
        this.schedule_time = schedule_time;
    }

    public String getSchudule_type() {
        return schudule_type;
    }

    public void setSchudule_type(String schudule_type) {
        this.schudule_type = schudule_type;
    }

    public String getStart_from_day() {
        return start_from_day;
    }

    public void setStart_from_day(String start_from_day) {
        this.start_from_day = start_from_day;
    }

    public String getStart_from() {
        return start_from;
    }

    public void setStart_from(String start_from) {
        this.start_from = start_from;
    }

    public String getRemind_me() {
        return remind_me;
    }

    public void setRemind_me(String remind_me) {
        this.remind_me = remind_me;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getHow_many_per_day() {
        return how_many_per_day;
    }

    public void setHow_many_per_day(String how_many_per_day) {
        this.how_many_per_day = how_many_per_day;
    }

    public String getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(String is_deleted) {
        this.is_deleted = is_deleted;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public String getsImage() {
        return sImage;
    }

    public void setsImage(String sImage) {
        this.sImage = sImage;
    }

    public String getThumb_photo() {
        return thumb_photo;
    }

    public void setThumb_photo(String thumb_photo) {
        this.thumb_photo = thumb_photo;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public List<String> getTimeLists() {
        return timeLists;
    }

    public void setTimeLists(List<String> timeLists) {
        this.timeLists = timeLists;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", medicines_id='" + medicines_id + '\'' +
                ", name='" + name + '\'' +
                ", quantity='" + quantity + '\'' +
                ", schedule_time='" + schedule_time + '\'' +
                ", schudule_type='" + schudule_type + '\'' +
                ", start_from_day='" + start_from_day + '\'' +
                ", start_from='" + start_from + '\'' +
                ", remind_me='" + remind_me + '\'' +
                ", customer_id='" + customer_id + '\'' +
                ", how_many_per_day='" + how_many_per_day + '\'' +
                ", is_deleted='" + is_deleted + '\'' +
                ", medicine_name='" + medicine_name + '\'' +
                ", sImage='" + sImage + '\'' +
                ", thumb_photo='" + thumb_photo + '\'' +
                ", product_image='" + product_image + '\'' +
                ", timeLists=" + timeLists +
                '}';
    }
}
