package com.meembusoft.safewaypharmaonline.model;


public class ParamsAddReminder {
    private String id = "";
    private String customer_id = "";
    private String medicines_id = "";
    private String name = "";
    private String quantity = "";
    private String schedule_time = "";
    private String schudule_type = "";
    private String start_from_day = "";
    private String remind_me = "";
    private String how_many_per_day = "";
    private String start_from = "";

    public ParamsAddReminder(String id, String customer_id, String medicines_id,String name, String quantity, String schedule_time, String schudule_type, String start_from_day, String remind_me, String how_many_per_day, String start_from) {
        this.id = id;
        this.customer_id = customer_id;
        this.medicines_id = medicines_id;
        this.name = name;
        this.quantity = quantity;
        this.schedule_time = schedule_time;
        this.schudule_type = schudule_type;
        this.start_from_day = start_from_day;
        this.remind_me = remind_me;
        this.how_many_per_day = how_many_per_day;
        this.start_from = start_from;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getMedicines_id() {
        return medicines_id;
    }

    public void setMedicines_id(String medicines_id) {
        this.medicines_id = medicines_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getRemind_me() {
        return remind_me;
    }

    public void setRemind_me(String remind_me) {
        this.remind_me = remind_me;
    }

    public String getHow_many_per_day() {
        return how_many_per_day;
    }

    public void setHow_many_per_day(String how_many_per_day) {
        this.how_many_per_day = how_many_per_day;
    }

    public String getStart_from() {
        return start_from;
    }

    public void setStart_from(String start_from) {
        this.start_from = start_from;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", customer_id='" + customer_id + '\'' +
                ", medicines_id='" + medicines_id + '\'' +
                ", quantity='" + quantity + '\'' +
                ", name='" + name + '\'' +
                ", schedule_time='" + schedule_time + '\'' +
                ", schudule_type='" + schudule_type + '\'' +
                ", start_from_day='" + start_from_day + '\'' +
                ", remind_me='" + remind_me + '\'' +
                ", how_many_per_day='" + how_many_per_day + '\'' +
                ", start_from='" + start_from + '\'' +
                '}';
    }
}
